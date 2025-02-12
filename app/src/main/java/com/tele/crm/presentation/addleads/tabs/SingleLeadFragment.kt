package com.tele.crm.presentation.addleads.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tele.crm.databinding.FragmentSingleLeadBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingleLeadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleLeadFragment : Fragment() {
    private lateinit var binding: FragmentSingleLeadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSingleLeadBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
    }

    private fun setUpClicks() {
        binding.apply {
            showMore.setOnClickListener {
                if (additionalFields.visibility == View.VISIBLE) {
                    additionalFields.visibility = View.GONE
                    showMore.text = "Show Maximized View"
                } else {
                    additionalFields.visibility = View.VISIBLE
                    showMore.text = "Show Minimized View"
                }
            }
        }
    }
    private fun setupSpinners() {
        val spinner1Data = listOf("Select Option", "Option 1", "Option 2", "Option 3")
        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinner1Data)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //binding.spinner1.adapter = adapter1

        val spinner2Data = listOf("Select Category", "Category A", "Category B", "Category C")
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinner2Data)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
       // binding.spinner2.adapter = adapter2
    }
}