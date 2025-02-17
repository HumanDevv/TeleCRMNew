package com.tele.crm.presentation.addleads

import Lead
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.databinding.FragmentAddLeadsBinding
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.utils.MetaItemBottomSheet
import com.tele.crm.utils.extension.fragmentScope
import com.tele.crm.utils.extension.getCurrentDate
import com.tele.crm.utils.extension.getFormattedTimestamp
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.showDatePicker
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddLeadsFragment : Fragment() {

    private lateinit var binding: FragmentAddLeadsBinding
    private val viewModel: AddLeadViewModel by viewModels()
    private var metaItemsForInterest: List<MetaItem> = emptyList()
    private var metaItemsForStream: List<MetaItem> = emptyList()
    private var metaItemsForYear: List<MetaItem> = emptyList()
    private var type = ""
    private lateinit var lead: Lead
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentAddLeadsBinding.inflate(layoutInflater, container, false).apply {
        binding = this
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackPress()
        }
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAddLeadApi()
        observeUpdateLeadApi()
        observeSpinnerEvents()

        viewModel.getInterest()
        viewModel.getStream()
        viewModel.getYear()

        type = arguments?.getString("type").toString()
        Log.d("ASdfasfasdf",type)
        if (type == "detail" || type == "edit") {
            lead = arguments?.getParcelable("lead_data")!!
            populateLeadData(lead)
        }

        handleUIBasedOnType()
        handleClicks()
    }

    private fun handleUIBasedOnType() {
        binding.apply {
            when (type) {
                "add" -> {
                    enableFields(true)
                    btnAddLead.visibility = View.VISIBLE
                    edit.visibility = View.GONE
                }
                "edit" -> {
                    enableFields(true)
                    btnAddLead.visibility = View.VISIBLE
                    edit.visibility = View.GONE
                }
                "detail" -> {
                    enableFields(false)
                    btnAddLead.visibility = View.GONE
                    edit.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun handleBackPress() {
        if (type == "edit") {
            type = "detail"
            enableFields(false)
            binding.btnAddLead.visibility = View.GONE
            binding.leadsTitle.text="Lead Details"
            binding.edit.visibility = View.VISIBLE
        } else {
            findNavController().popBackStack()
        }
    }

    private fun enableFields(enable: Boolean) {
        binding.apply {
            etName.isEnabled = enable
            etMobile.isEnabled = enable
            etEmail.isEnabled = enable
            altMobile.isEnabled = enable
            tvStream.isEnabled = enable
            tvYear.isEnabled = enable
            address.isEnabled = enable
            collegeName.isEnabled = enable
            date.isEnabled = enable
            tvInterested.isEnabled = enable
        }
        isEditing = enable

    }

    private fun populateLeadData(lead: Lead) {
        binding.apply {
            etName.setText(lead.name)
            etMobile.setText(lead.mobile)
            etEmail.setText(lead.email_id)
            leadsTitle.text="Lead Details"
            altMobile.setText(lead.alternate_mobile)
            tvStream.text = lead.stream
            tvYear.text = lead.year
            address.setText(lead.address)
            collegeName.setText(lead.college_name)
            date.text = getFormattedTimestamp(lead.createdAt)
            tvInterested.text = lead.interested_in
        }
    }

    private fun handleClicks() {
        binding.apply {
            ivBack.setOnClickListener {
               handleBackPress()
            }
date.text= getCurrentDate()
            btnAddLead.setOnClickListener {
                if (type == "add") {
                    viewModel.addLeadApi(
                        AddLeadRequest(
                            name = etName.text.toString(),
                            mobile = etMobile.text.toString(),
                            emailId = etEmail.text.toString(),
                            alternateMobile = altMobile.text.toString(),
                            stream = tvStream.text.toString(),
                            year = tvYear.text.toString(),
                            address = address.text.toString(),
                            collegeName = collegeName.text.toString(),
                            counsellingDate = date.text.toString(),
                            interestedIn = tvInterested.text.toString(),
                            status = "Fresh"
                        )
                    )
                }

            else{
                    viewModel.updateLeadApi(
                        lead._id,
                        AddLeadRequest(
                            name = etName.text.toString(),
                            mobile = etMobile.text.toString(),
                            emailId = etEmail.text.toString(),
                            alternateMobile = altMobile.text.toString(),
                            stream = tvStream.text.toString(),
                            year = tvYear.text.toString(),
                            address = address.text.toString(),
                            collegeName = collegeName.text.toString(),
                            counsellingDate = date.text.toString(),
                            interestedIn = tvInterested.text.toString(),
                            status = lead.status
                        )
                    )
            }

        }

            edit.setOnClickListener {
                type="edit"
                enableFields(true)
                btnAddLead.text="Update Lead"
                leadsTitle.text="Edit Lead"
                btnAddLead.visibility = View.VISIBLE
                edit.visibility = View.GONE
            }


            tvInterested.setOnClickListener {
                showMetaItemBottomSheet(metaItemsForInterest) { selectedItem ->
                    tvInterested.text = selectedItem.name
                }
            }

            tvStream.setOnClickListener {
                showMetaItemBottomSheet(metaItemsForStream) { selectedItem ->
                    tvStream.text = selectedItem.name
                }
            }

            tvYear.setOnClickListener {
                showMetaItemBottomSheet(metaItemsForYear) { selectedItem ->
                    tvYear.text = selectedItem.name
                }
            }

            date.setOnClickListener {
                val selectedDate = if (date.text.isNotEmpty()) date.text.toString() else null
                showDatePicker(requireActivity(), selectedDate) { newDate ->
                    date.text = newDate
                }
            }

        }
    }

    private fun showMetaItemBottomSheet(metaItems: List<MetaItem>, onItemSelected: (MetaItem) -> Unit) {
        val bottomSheet = MetaItemBottomSheet(requireContext(), metaItems, onItemSelected)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    private fun observeAddLeadApi() {
        viewModel.addLeadFlow.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        showToast(it.value.message)
                        findNavController().popBackStack()
                    } else {
                        it.value.message?.let { message -> showToast(message) }
                    }
                }
                is ApiCallingState.Failure -> {
                    hideProgress()
                    showToast("Failed to update Lead")
                }
                else -> Unit
            }
        }.launchIn(fragmentScope)
    }
    private fun observeUpdateLeadApi() {
        viewModel.updateLeadFlow.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        showToast(it.value.message)
                        type = "detail"
                        enableFields(false)
                        binding.btnAddLead.visibility = View.GONE
                        binding.edit.visibility = View.VISIBLE
                    } else {
                        it.value.message?.let { message -> showToast(message) }
                    }
                }
                is ApiCallingState.Failure -> {
                    hideProgress()
                    showToast("Failed to update Lead")
                }
                else -> Unit
            }
        }.launchIn(fragmentScope)
    }

    private fun observeSpinnerEvents() {
        viewModel.interestListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            metaItemsForInterest = metaItems
        })

        viewModel.streamListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            metaItemsForStream = metaItems
        })

        viewModel.yearListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            metaItemsForYear = metaItems
        })
    }

}
