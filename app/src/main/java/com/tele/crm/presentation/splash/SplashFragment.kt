package com.tele.crm.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tele.crm.utils.connectivity.NetworkConnectivityObserver
import com.tele.crm.R
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.datastore.getData
import com.tele.crm.databinding.FragmentSplashBinding
import com.tele.crm.utils.connectivity.ConnectivityObserver
import com.tele.crm.utils.dialog.CommonDialogUtils
import com.tele.crm.utils.extension.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    @Inject
    lateinit var appDataStore: AppDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        changeStatusBarColor(R.color.transparent) // change status bar color for splash fragment only


        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity().isNetworkConnected()) {
            openNextScreen()
        } else {
            checkNetworkConnectivity()
        }


    }

    private fun openNextScreen() {

        lifecycleScope.launch(Dispatchers.Main) {
            delay(timeMillis = 2000)
            if (appDataStore.getData().isUserLoggedIn) {
                        findNavController().navigate(R.id.action_splashFragment_to_leadFragment)

            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }

    }

    private fun checkNetworkConnectivity() {
        val dialog = CommonDialogUtils.networkDialog(requireActivity())
        dialog.show()
        val connectivityObserver = NetworkConnectivityObserver(requireActivity())
        connectivityObserver.observe().onEach {
            if (!requireActivity().isNetworkConnected()) {
                dialog.show()
            } else if (it == ConnectivityObserver.Status.Available) {
                dialog.dismiss()
                openNextScreen()
            }
        }.launchIn(lifecycleScope)
    }

}