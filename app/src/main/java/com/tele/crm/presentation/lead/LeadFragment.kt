package com.tele.crm.presentation.lead

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tele.crm.R
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.databinding.FragmentLeadBinding
import com.tele.crm.domain.entites.UserItem
import com.tele.crm.presentation.setting.SettingViewModel
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LeadFragment : Fragment() {

    private lateinit var binding: FragmentLeadBinding
    private val viewModel: LeadViewModel by viewModels()
    private val profileViewModel: SettingViewModel by viewModels()
    private lateinit var leadsAdapter: LeadsAdapter
    private var CALL_LOG_PERMISSION_REQUEST = 101
    private var recyclerViewState: Parcelable? = null

    @Inject
    lateinit var appDataStore: AppDataStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLeads()
        observerGetLeadResponse()
        binding.addLeadBtn.setDebouncedOnClickListener {
            val bundle=Bundle()
            bundle.putString("type","add")
            findNavController().navigate(R.id.action_leadFragment_to_addLeadsFragment,bundle)
        }

        profileViewModel.profileListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            lifecycleScope.launch {
                updateProfileData(metaItems)
            }
        })
        leadsAdapter = LeadsAdapter { leadEntry ->
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG), CALL_LOG_PERMISSION_REQUEST)
            } else {
                navigateToCallDetails(leadEntry._id)
            }
        }
        binding.rvLeads.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leadsAdapter
            setHasFixedSize(true)  // Helps with performance
        }
        profileViewModel.getProfile()



    }

    private fun navigateToCallDetails(leadId: String) {
        val bundle = Bundle().apply {
            putString("leadId", leadId)
        }
        findNavController().navigate(R.id.action_leadFragment_to_CallDetailsFragment, bundle)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_LOG_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showToast("Permission granted! Tap again to proceed.")
        } else {
            showToast("Permission denied. Cannot access call logs.")
        }
    }
    private suspend fun updateProfileData(list: List<UserItem>?) {
        appDataStore.updateData { results ->
            val userItem = list?.firstOrNull() // Assuming you want the first item from the list

            results.copy(
                isUserLoggedIn = true,
                userId = userItem?.id.toString(),
                email = userItem?.email ?:"",
                firstName = userItem?.firstName ?:  "",
                lastName = userItem?.lastName ?:"",
                mobile = userItem?.mobile ?:""
            )
        }
    }


    private fun observerGetLeadResponse() {
        viewModel.leadEnteries.onEach {
            when (it) {
                is ApiCallingState.Loading -> {
                    showProgress()
                }

                is ApiCallingState.Success -> {
                    hideProgress()

                    if (it.value.success) {
                        if (it.value.data.isNotEmpty()) {
                            leadsAdapter.submitList(it.value.data)
                        }
                        else{
                            binding.layoutNoitem.root.visibility=View.VISIBLE
                            binding.rvLeads.visibility=View.GONE
                        }
                    }

                    else {
                        binding.layoutNoitem.root.visibility=View.VISIBLE
                        binding.rvLeads.visibility=View.GONE
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
    override fun onPause() {
        super.onPause()
        recyclerViewState = binding.rvLeads.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        recyclerViewState?.let {
            binding.rvLeads.layoutManager?.onRestoreInstanceState(it)
        }
    }
}
