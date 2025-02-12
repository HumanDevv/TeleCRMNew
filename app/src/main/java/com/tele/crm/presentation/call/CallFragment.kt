package com.tele.crm.presentation.call

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.databinding.FragmentCallBinding
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CallFragment : Fragment() {

    private lateinit var binding: FragmentCallBinding
    private val viewModel: CallViewModel by viewModels()
    private val callsAdapter = CallsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCalls.adapter = callsAdapter
        binding.rvCalls.layoutManager = LinearLayoutManager(context)

        observerRecentCallsResponse()
        viewModel.getRecentCalls()
    }

    private fun observerRecentCallsResponse() {
        viewModel.callEntries.onEach {
            when (it) {
                is ApiCallingState.Loading -> {
                    showProgress()
                }

                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value != null) {
                        callsAdapter.submitList(it.value.data?: emptyList())

                    } else {
                        showToast("Empty List")
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
