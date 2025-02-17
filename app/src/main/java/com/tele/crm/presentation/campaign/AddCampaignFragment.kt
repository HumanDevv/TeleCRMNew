package com.tele.crm.presentation.campaign

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tele.crm.R
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.databinding.FragmentAddCampaignBinding
import com.tele.crm.presentation.lead.LeadsAdapter
import com.tele.crm.utils.extension.fragmentScope
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class AddCampaignFragment : Fragment() {
    private lateinit var binding: FragmentAddCampaignBinding
    private val viewModel: CampaignViewModel by viewModels()

    @Inject
    lateinit var appDataStore: AppDataStore
    private lateinit var type: String
    private lateinit var id: String
    private var isEditMode = false // Track edit mode
    private lateinit var leadsAdapter: LeadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCampaignBinding.inflate(inflater, container, false)
        type = arguments?.getString("type") ?: ""
        id = arguments?.getString("id") ?: ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leadsAdapter = LeadsAdapter { leadEntry ->
            navigateToCallDetails(leadEntry._id)
        }

        binding.rvLeads.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leadsAdapter
            setHasFixedSize(true)
        }
        handleClicks()
        observeCampaignResponse()
        observeCampaignDetailsResponse()
        observeUpdateCampaignResponse()

        if (type == "detail") {
            setupDetailMode()
        } else {
            setupAddMode()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackPress()
        }
    }

    private fun setupDetailMode() {
        isEditMode = false
        binding.leadsTitle.text = "Campaign Details"
        binding.btnSave.visibility = View.GONE
        binding.edit.visibility = View.VISIBLE
        binding.layoutLeadDetails.visibility = View.VISIBLE
        setEditTextsEnabled(false)
        viewModel.getCampaignDetails(id)

    }

    private fun setupAddMode() {
        binding.leadsTitle.text = "Add Campaign"
        binding.btnSave.text = "Save"
        binding.edit.visibility = View.GONE
        binding.btnSave.visibility = View.VISIBLE
        binding.layoutLeadDetails.visibility = View.GONE

    }
    private fun navigateToCallDetails(leadId: String) {
        val bundle = Bundle().apply {
            putString("leadId", leadId)
        }
        findNavController().navigate(R.id.action_AddCampaignFragment_to_CallDetailsFragment, bundle)
    }
    private fun setEditTextsEnabled(isEnabled: Boolean) {
        binding.etCampaginName.isEnabled = isEnabled
        binding.etCampaignDescription.isEnabled = isEnabled
    }

    private fun observeCampaignResponse() {
        viewModel.campaignEntries.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        findNavController().popBackStack()
                    } else {
                        showToast(it.value.message)
                    }
                }
                is ApiCallingState.Failure -> {
                    hideProgress()
                    showToast(it.throwable.message ?: "An error occurred.")
                }

                else -> {}
            }
        }.launchIn(fragmentScope)
    }

    private fun observeCampaignDetailsResponse() {
        viewModel.getCampaignDetails.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        requireActivity().runOnUiThread {
                            leadsAdapter.submitList(it.value.data.leads)
                        }
                        binding.tvLeads.text=("Leads(${it.value.data.leads.size})")
                        binding.etCampaginName.setText(it.value.data.name)
                        binding.etCampaignDescription.setText(it.value.data.description)

                    } else {
                        showToast(it.value.message)
                    }
                }
                is ApiCallingState.Failure -> {
                    hideProgress()
                    showToast(it.throwable.message ?: "An error occurred.")
                }

                else -> {}
            }
        }.launchIn(fragmentScope)
    }
    private fun observeUpdateCampaignResponse() {
        viewModel.updateCampaignDetails.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        binding.etCampaginName.setText(it.value.data.name)
                        binding.etCampaignDescription.setText(it.value.data.description)
                    } else {
                        showToast(it.value.message)
                    }
                }
                is ApiCallingState.Failure -> {
                    hideProgress()
                    showToast(it.throwable.message ?: "An error occurred.")
                }

                else -> {}
            }
        }.launchIn(fragmentScope)
    }
    private fun handleClicks() {
        binding.apply {
            btnSave.setDebouncedOnClickListener {
                if (!validateInputs()) return@setDebouncedOnClickListener

                if (isEditMode) {
                    // Call update API
                    viewModel.updateCampaignDetails(
                        id,
                        CampaignRequest(
                            etCampaginName.text.toString().trim(),
                            etCampaignDescription.text.toString().trim(),
                        )
                    )
                    setupDetailMode()
                } else {
                    viewModel.addCampaignEntry(
                        CampaignRequest(
                            etCampaginName.text.toString().trim(),
                            etCampaignDescription.text.toString().trim(),
                        )
                    )
                }
            }

            ivBack.setDebouncedOnClickListener {
                handleBackPress()
            }

            edit.setDebouncedOnClickListener {
                isEditMode = true
                binding.leadsTitle.text = "Edit Campaign"
                binding.btnSave.visibility = View.VISIBLE
                binding.edit.visibility = View.GONE
                binding.btnSave.text = "Update"

                setEditTextsEnabled(true)
            }
        }
    }

    private fun validateInputs(): Boolean   {
        val name = binding.etCampaginName.text.toString().trim()
        val description = binding.etCampaignDescription.text.toString().trim()

        return when {
            name.isEmpty() -> {
                binding.etCampaginName.error = "Campaign name cannot be empty"
                false
            }
            description.isEmpty() -> {
                binding.etCampaignDescription.error = "Campaign description cannot be empty"
                false
            }
            else -> true
        }
    }

    private fun handleBackPress() {
        if (isEditMode) {
            // Revert to detail mode
            setupDetailMode()
        } else {
            // Exit fragment
            findNavController().popBackStack()
        }
    }
}

