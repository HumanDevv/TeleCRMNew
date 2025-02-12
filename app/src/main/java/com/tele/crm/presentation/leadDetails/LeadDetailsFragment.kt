package com.tele.crm.presentation.leadDetails

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
import com.tele.crm.databinding.FragmentLeadDetailsBinding
import com.tele.crm.databinding.FragmentLoginBinding
import com.tele.crm.presentation.lead.LeadViewModel
import com.tele.crm.presentation.lead.LeadsAdapter
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LeadDetailsFragment : Fragment() {

    lateinit var binding:FragmentLeadDetailsBinding
    private val viewModel: LeadDetailsViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentLeadDetailsBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClickEvents()
        historyAdapter = HistoryAdapter{ history ->
        }
        binding.rvHistory.adapter = historyAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.leadEntries.collectLatest { calls ->
                historyAdapter.submitList(calls)
            }
        }

        viewModel.loadMockData()
    }
    private fun handleClickEvents() {
        binding.apply {
            sectionHeader.setOnClickListener {
                if (layoutMoreFields.visibility == View.VISIBLE) {
                    layoutMoreFields.visibility = View.GONE
                    showMoreText.text = "Show Fields"
                    arrowIcon.animate().rotation(180F).setDuration(300).start();

                } else {
                    layoutMoreFields.visibility = View.VISIBLE
                    showMoreText.text = "Hide Fields"
                    arrowIcon.animate().rotation(0F).setDuration(300).start();

                }
            }


        }
    }
}