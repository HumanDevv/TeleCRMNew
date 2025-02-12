package com.tele.crm.presentation.callDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.databinding.FragmentCallDetailsBinding
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.presentation.call.CallViewModel
import com.tele.crm.presentation.campaign.CampaignViewModel
import com.tele.crm.utils.MetaItemBottomSheet
import com.tele.crm.utils.extension.fragmentScope
import com.tele.crm.utils.extension.getCallLogsForNumber
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CallDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCallDetailsBinding
    private val viewModel: CallViewModel by viewModels()
    private val campaignViewModel: CampaignViewModel by viewModels()
    var leadId=""
    private val callsAdapter = CallsHistoryAdapter()
    private var getCampaign: MutableList<MetaItem> = mutableListOf()
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
        observeAddLeadToCampaignApi()
        observeUpdateLeadStatusApi()
        binding.rvCallHistory.adapter = callsAdapter
        binding.rvCallHistory.layoutManager = LinearLayoutManager(context)
        observerCallLogsResponse()
        viewModel.getCallLogs(leadId)
        campaignViewModel.getCampaigns()
        observerGetCampaignResponse()
        handleClicks()

    }

    private fun handleClicks() {
        binding.apply {
            ivBack.setDebouncedOnClickListener {
                findNavController().popBackStack()
            }
            tvCampaign.setOnClickListener {
                showMetaItemBottomSheet(getCampaign) { selectedItem ->
                    binding.tvCampaign.text = selectedItem.title
                    viewModel.addLeadToCampaignApi(AddToCampaignRequest(selectedItem.id,
                        listOf(leadId)
                    ))
                }
            }
            tvStatus.setOnClickListener {
                showStatusItemBottomSheet(statusList) { selectedItem ->
                    binding.tvStatus.text = selectedItem
                    viewModel.updateLeadStatus(UpdateStatusRequest(selectedItem,leadId))

                }
            }

            ivCall.setOnClickListener {
                val phoneNumber = binding.tvMobile.text.toString()
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(dialIntent)
            }

        }
    }


    private fun observerCallLogsResponse() {
        viewModel.callLogs.onEach {
            when (it) {
                is ApiCallingState.Loading -> {
                    showProgress()
                }

                is ApiCallingState.Success -> {
                    hideProgress()
                    binding.layoutMain.visibility=View.VISIBLE
                    binding.layoutNoitem.root.visibility=View.GONE
                    if (it.value != null) {
                        binding.tvName.text=it.value.data.lead.name
                        binding.tvMobile.text=it.value.data.lead.mobile
                        binding.tvStatus.text=it.value.data.lead.status
                        //binding.tvCampaign.text=it.value.lead.status
                        binding.initialsTextView.text=getInitials(it.value.data.lead.name)
                        val callLogs = getCallLogsForNumber(requireActivity(), it.value.data.lead.mobile)

                        Log.d("ASDfasdfsa",callLogs.toString())
                        callsAdapter.submitList(callLogs)

                    } else {
                        showToast("Empty List")
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()
                    binding.layoutMain.visibility=View.GONE
                    binding.layoutNoitem.root.visibility=View.VISIBLE
                    showToast(it.throwable.message.toString())
                }

                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }


    private fun getInitials(name: String): String {
        val firstName = name.split(" ").firstOrNull() ?: ""

        return firstName.take(2).uppercase()  // Ensure two characters or less and uppercase
    }

    private fun showMetaItemBottomSheet(metaItems: List<MetaItem>, onItemSelected: (MetaItem) -> Unit) {
        val bottomSheet = MetaItemBottomSheet(requireContext(), metaItems, onItemSelected)
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
                        showToast(it.value.message)
                        findNavController().popBackStack()
                    } else {
                        showToast(it.value.message)
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()


                }

                else -> Unit
            }
        }.launchIn(fragmentScope)
    }
    private fun observeUpdateLeadStatusApi() {
        viewModel.updateLeadStatus.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        showToast(it.value.message)
                        findNavController().popBackStack()
                    } else {
                        showToast(it.value.message)
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
                                title = item.name
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

}

