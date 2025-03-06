package com.tele.crm.presentation.callDetails

import Lead
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tele.crm.R
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.activityAdd.activtiyAddRequest
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.data.network.model.callLogs.CallLog
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkRequest

import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.databinding.FragmentCallDetailsBinding
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.presentation.call.CallViewModel
import com.tele.crm.presentation.campaign.CampaignViewModel
import com.tele.crm.utils.CampaignBottomSheet
import com.tele.crm.utils.MetaItemBottomSheet
import com.tele.crm.utils.extension.CallLogItem
import com.tele.crm.utils.extension.fragmentScope
import com.tele.crm.utils.extension.getCallLogsForNumber
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.openWhatsAppChat
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

@AndroidEntryPoint
class CallDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCallDetailsBinding
    private val viewModel: CallViewModel by viewModels()
    private val campaignViewModel: CampaignViewModel by viewModels()
    var leadId=""
    lateinit var callsAdapter :CallsHistoryAdapter
    private var getCampaign: MutableList<MetaItem> = mutableListOf()
    var campaignIdList: MutableList<String> = mutableListOf()
    val statusList = listOf(
        "Fresh",
        "RNR",
        "Call Later",
        "Just Curious",
        "Interested",
        "Counselling Scheduled",
        "Counselling Done",
        "Won",
        "Lost",
    )
    lateinit var lead:Lead
    private var isCallDetailsSent = false // Flag to track if call details have been sent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            leadId = it.getString("leadId")!!

        }
        val isFirstTime = SharedPrefs.getIsFirstTime(requireContext())

        observeAddLeadToCampaignApi()
        observeAddActivityApi()
        observeUpdateLeadStatusApi()

        callsAdapter = CallsHistoryAdapter { remark, callId,outcome ->
            sendRemarkToServer(remark, callId,outcome )
        }
        binding.rvCallHistory.adapter = callsAdapter
        binding.rvCallHistory.layoutManager = LinearLayoutManager(context)
        observerCallLogsResponse()
        viewModel.getCallLogs(leadId)
        campaignViewModel.getCampaigns()
        observerGetCampaignResponse()
        observeUpdateRemarkApi()
        handleClicks()
        if (isFirstTime) {
            SharedPrefs.saveIsFirstTime(requireContext(), false)
        }
    }

    private fun handleClicks() {
        binding.apply {
            ivBack.setDebouncedOnClickListener {
                findNavController().popBackStack()
            }
            edit.setOnClickListener {
                showMetaItemBottomSheet(getCampaign) { selectedItem ->
                    if (campaignIdList.contains(selectedItem.id)) {
                        // If the item is already selected, remove the lead from the campaign
                       /* viewModel.removeLeadToCampaignApi(AddToCampaignRequest(selectedItem.id, listOf(leadId)))
                        campaignIdList.remove(selectedItem.id)*/
                    } else {
                        // If the item is not selected, add the lead to the campaign
                        viewModel.addLeadToCampaignApi(AddToCampaignRequest(selectedItem.id, listOf(leadId)))
                        campaignIdList.add(selectedItem.id)
                    }

                    // Update UI
                    binding.tvCampaign.text = selectedItem.name
                }
            }

/*
            ivCall.setOnClickListener {
                val randomDuration = Random.nextInt(0, 121).toString()

                viewModel.addActivity(
                    activtiyAddRequest(leadId,randomDuration,"","connected","outgoing")
                )
            }*/
            tvStatus.setOnClickListener {
                showStatusItemBottomSheet(statusList) { selectedItem ->
                    binding.tvStatus.text = selectedItem
                    viewModel.updateLeadStatus(UpdateStatusRequest(selectedItem,leadId))

                }
            }

            ivCall.setOnClickListener {
                val phoneNumber = binding.tvMobile.text.toString()
                makeCallFromDialer(phoneNumber)
            }
            ivWhatsapp.setOnClickListener {
                val phoneNumber = binding.tvMobile.text.toString()
                requireActivity().openWhatsAppChat(phoneNumber,"This is the dummy message")
            }
            showLeadInfo.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("lead_data", lead)
                    putString("type","detail")
                }
                Log.d("leaddsdsd",lead.toString())
                findNavController().navigate(R.id.action_CallDetailsFragment_to_addLeadsFragment,bundle)
            }

        }
    }

    private fun sendRemarkToServer(remark: String, callId: String,outcome:String) {
        viewModel.updateRemark(UpdateRemarkRequest(leadId,callId,remark,outcome)) // Call API to send remark
    }
    private fun observerCallLogsResponse() {
        viewModel.callLogs.onEach {
            when (it) {
                is ApiCallingState.Loading -> {
                    showProgress()
                }

                is ApiCallingState.Success -> {
                    hideProgress()
                    binding.layoutMain.visibility = View.VISIBLE
                    binding.layoutNoitem.root.visibility = View.GONE
                    if (it.value != null) {
                        binding.tvName.text = it.value.data.lead.name
                        binding.tvMobile.text = it.value.data.lead.mobile
                        binding.tvStatus.text = it.value.data.lead.status
                        binding.initialsTextView.text = getInitials(it.value.data.lead.name?:"")
                        lead = it.value.data.lead
                        it.value?.data?.lead?.campaigns?.let { campaigns ->
                            campaignIdList.clear()
                            campaignIdList.addAll(campaigns.map { campaign -> campaign.id })
                        }
                        callsAdapter.submitList(it.value.data.callLogs)

                        val callLogs = getCallLogsForNumber(requireActivity(), it.value.data.lead.mobile?:"")

                        if (callLogs.isNotEmpty()) {
                            val mostRecentCall = callLogs[0]
                            // Check if the details have changed before sending to the API
                            if (hasCallDetailsChanged(mostRecentCall)) {
                                if (!SharedPrefs.getIsFirstTime(requireContext())) {

                                    sendCallDetailsToApi(mostRecentCall)
                                }
                            }
                        }
                    } else {
                        showToast("Empty List")
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()
                    binding.layoutMain.visibility = View.GONE
                    binding.layoutNoitem.root.visibility = View.VISIBLE
                    showToast(it.throwable.message.toString())
                }

                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCallLogs(leadId)
    }
    private fun sendCallDetailsToApi(call: CallLogItem) {
        viewModel.addActivity(
            activtiyAddRequest(
                leadId = leadId,
                duration = call.duration.toString(),
                remarks = "",
                status = call.type, // Assuming type is incoming, outgoing, etc.
                outcome = "connected" // You can add more logic here to determine the outcome
            )
        )

        // Save the latest call details to SharedPreferences to avoid redundant calls
        SharedPrefs.saveLastCallDetails(call.toString(), context = requireContext())
    }

    private fun hasCallDetailsChanged(call: CallLogItem): Boolean {
        // Check if the current call details are different from the last sent call details
        val lastSentData = SharedPrefs.getLastCallDetails(requireActivity())
        return lastSentData == null || lastSentData != call.toString()
    }
    private fun getInitials(name: String): String {
        val firstName = name.split(" ").firstOrNull() ?: ""

        return firstName.take(2).uppercase()  // Ensure two characters or less and uppercase
    }

    private fun showMetaItemBottomSheet(metaItems: List<MetaItem>, onItemSelected: (MetaItem) -> Unit) {
        val bottomSheet = CampaignBottomSheet(requireContext(), metaItems,campaignIdList, onItemSelected)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
    private fun showStatusItemBottomSheet(metaItems: List<String>, onItemSelected: (String) -> Unit) {
        val bottomSheet = MetaItemBottomSheet(requireContext(), metaItems, onItemSelected)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }


    private fun observeAddLeadToCampaignApi() {
        viewModel.addLeadToCampaign.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        requireContext().showToast(it.value.message)
                        //findNavController().popBackStack()
                        //viewModel.getCallLogs(leadId)
                    } else {
                        requireContext().showToast(it.value.message)
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()


                }

                else -> Unit
            }
        }.launchIn(fragmentScope)
    }
    private fun observeAddActivityApi() {
        viewModel.addActivity.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        requireContext().showToast(it.value.message)
                        //findNavController().popBackStack()
                        viewModel.getCallLogs(leadId)
                    } else {
                        requireContext().showToast(it.value.message)
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()


                }

                else -> Unit
            }
        }.launchIn(fragmentScope)
    }

    private fun makeCallFromDialer(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)

        // Save the dialed number to track only calls made from the app
        SharedPrefs.saveLastDialedNumber(phoneNumber, requireActivity())
    }

    private fun observeUpdateLeadStatusApi() {
        viewModel.updateLeadStatus.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        requireContext().showToast(it.value.message)
                       // Log.d("SDfgfdsg",it.value.lead.status )
                        //binding.tvStatus.text = it.value.lead.status ?: binding.tvStatus.text
                        //findNavController().popBackStack()
                    } else {
                        requireContext().showToast(it.value.message)
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()


                }

                else -> Unit
            }
        }.launchIn(fragmentScope)
    }
    private fun observerGetCampaignResponse() {
        campaignViewModel.getCampaigns.onEach {
            when (it) {
                is ApiCallingState.Loading -> {
                    showProgress()
                }

                is ApiCallingState.Success -> {
                    hideProgress()

                    if (it.value.success) {
                        val tempList = it.value.data.map { item ->
                            MetaItem(
                                id = item._id.orEmpty(),
                                name = item.name
                            )
                        }
                        getCampaign.addAll(tempList)
                    }
                    else {
                        showToast(it.value.message)
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()
                    showToast(it.throwable.message.toString())
                }

                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }
    private fun observeUpdateRemarkApi() {
        viewModel.updateRemark.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        //requireContext().showToast(it.value.message)
                        viewModel.getCallLogs(leadId)

                    } else {
                        requireContext().showToast(it.value.message)
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()
                }

                else -> Unit
            }
        }.launchIn(fragmentScope)
    }

}
object SharedPrefs {
    fun saveLastCallDetails(call: String, context: Context) {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_call_details", call).apply()
    }

    fun getLastCallDetails(context: Context): String? {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        return prefs.getString("last_call_details", null)
    }

    fun saveLastDialedNumber(number: String, context: Context) {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_dialed", number).apply()
    }

    fun getLastDialedNumber(context: Context): String? {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        return prefs.getString("last_dialed", null)
    }

    fun saveIsFirstTime(context: Context, isFirstTime: Boolean) {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_first_time", isFirstTime).apply()
    }

    // Retrieve whether it's the first time or not
    fun getIsFirstTime(context: Context): Boolean {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_first_time", true)
    }

    // Reset the flag after API call success
    fun resetIsFirstTime(context: Context) {
        val prefs = context.getSharedPreferences("CallPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_first_time", false).apply()
    }
}
