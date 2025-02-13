package com.tele.crm.presentation.campaign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tele.crm.R
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.databinding.FragmentCampaignBinding
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CampaignFragment : Fragment() {
    private lateinit var binding: FragmentCampaignBinding
    private val viewModel: CampaignViewModel by viewModels()
    private lateinit var campaignAdapter : CampaignAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCampaignBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campaignAdapter = CampaignAdapter {
            val bundle = Bundle()
            bundle.putString("type","detail")
            bundle.putString("id",it._id)
           findNavController().navigate(R.id.action_campaignFragment_to_AddCampaignFragment,bundle)
        }
        binding.rvCampaign.adapter = campaignAdapter
        binding.rvCampaign.layoutManager = LinearLayoutManager(context)

        observerGetCampaignResponse()
        viewModel.getCampaigns()
        handleClicks()
    }

    private fun handleClicks(){
        binding.apply {
            ivBack.setDebouncedOnClickListener {
                findNavController().popBackStack()
            }
            add.setDebouncedOnClickListener {
                val bundle = Bundle()
                bundle.putString("type","add")
                findNavController().navigate(R.id.action_campaignFragment_to_AddCampaignFragment,bundle)            }
        }
    }
    private fun observerGetCampaignResponse() {
        viewModel.getCampaigns.onEach {
            when (it) {
                is ApiCallingState.Loading -> {
                    showProgress()
                }

                is ApiCallingState.Success -> {
                    hideProgress()

                    if (it.value.success) {
                        campaignAdapter.submitList(it.value.data)

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