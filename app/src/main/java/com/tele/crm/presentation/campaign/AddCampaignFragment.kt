package com.tele.crm.presentation.campaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCampaignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClicks()
        observeCampaignResponse()

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
                        hideProgress()
                        it.value.message.let { it1 -> showToast(it1) }
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()
                    when (it) {
                        is ApiCallingState.Failure.Unknown -> {
                            hideProgress()
                            it.throwable.message?.let { errorMessage ->
                                showToast(errorMessage)
                            } ?: showToast("An unknown error occurred.")
                        }
                        else -> showToast("An error occurred.")
                    }
                }

                else -> {
                    // Debugging fallback
                    hideProgress()
                }
            }
        }.launchIn(fragmentScope)
    }

    private fun handleClicks() {
        binding.apply {
            btnSave.setDebouncedOnClickListener {
                viewModel.addCampaignEntry(CampaignRequest(
                    etCampaginName.text.toString().trim(),
                    etCampaignDescription.text.toString().trim(),
                ))
            }
            ivBack.setDebouncedOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}