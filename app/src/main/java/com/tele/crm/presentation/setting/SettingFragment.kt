package com.tele.crm.presentation.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tele.crm.R
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.datastore.clear
import com.tele.crm.data.datastore.getData
import com.tele.crm.databinding.FragmentSettingBinding
import com.tele.crm.domain.entites.UserItem
import com.tele.crm.presentation.addleads.AddLeadViewModel
import com.tele.crm.utils.dialog.CommonDialogUtils
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    @Inject
    lateinit var appDataStore: AppDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClicks()

        runBlocking {
            updateProfileData()
        }
    }



    @SuppressLint("SetTextI18n")
    private suspend fun updateProfileData() {
        binding.apply {
            tvName.text= appDataStore.getData().firstName
            tvEmail.text=appDataStore.getData().email
            tvMobile.text= appDataStore.getData().mobile
            initialsTextView.text=getInitials(appDataStore.getData().firstName)
        }

            }

    private fun handleClicks(){


        binding.layoutCampaign.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_campaignFragment)
        }


        binding.btnLogout.setDebouncedOnClickListener {
            CommonDialogUtils.showAppLogOutDialog(
                requireContext(),
                getString(R.string.logout), getString(R.string.do_you_want_to_logout_from_the),
                getString(R.string.confirm),
            ) {
                viewModel.logOut()
                lifecycleScope.launch {
                    viewModel.appDataStore.clear()
                    findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
                }
            }
        }

    }
    private fun getInitials(name: String): String {
        val firstName = name.split(" ").firstOrNull() ?: ""

        return firstName.take(2).uppercase()  // Ensure two characters or less and uppercase
    }
}