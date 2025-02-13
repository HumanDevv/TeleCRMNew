package com.tele.crm.presentation.campaign

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.databinding.FragmentAddCampaignBinding
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
        handleClicks()
        observeCampaignResponse()
        observeCampaignDetailsResponse()

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
        setEditTextsEnabled(false)
        viewModel.getCampaignDetails(id)
    }

    private fun setupAddMode() {
        binding.leadsTitle.text = "Add Campaign"
        binding.btnSave.text = "Save"
        binding.edit.visibility = View.GONE
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
                        binding.etCampaginName.setText(it.value.data.name)
                        binding.etCampaignDescription.setText(it.value.data.name)
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
                if (isEditMode) {
                    // Call update API
                  /*  viewModel.updateCampaign(
                        id,
                        CampaignRequest(
                            etCampaginName.text.toString().trim(),
                            etCampaignDescription.text.toString().trim(),
                        )
                    )*/
                    setupDetailMode() // Switch back to detail mode after saving
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
                setEditTextsEnabled(true)
            }
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

