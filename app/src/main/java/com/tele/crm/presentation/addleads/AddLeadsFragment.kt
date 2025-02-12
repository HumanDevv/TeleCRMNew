package com.tele.crm.presentation.addleads

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.tele.crm.R
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.databinding.FragmentAddLeadsBinding
import com.tele.crm.databinding.FragmentLoginBinding
import com.tele.crm.databinding.TabItemBinding
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.presentation.auth.login.LoginViewModel
import com.tele.crm.utils.MetaItemBottomSheet
import com.tele.crm.utils.dialog.CommonDialogUtils
import com.tele.crm.utils.extension.fragmentScope
import com.tele.crm.utils.extension.getCurrentDate
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.showDatePicker
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddLeadsFragment : Fragment() {

    private lateinit var binding: FragmentAddLeadsBinding
    private val viewModel: AddLeadViewModel by viewModels()
    private var metaItemsForInterest: List<MetaItem> = emptyList()
    private var metaItemsForStream: List<MetaItem> = emptyList()
    private var metaItemsForYear: List<MetaItem> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentAddLeadsBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAddLeadApi()
        observeSpinnerEvents()

        viewModel.getInterest()
        viewModel.getStream()
        viewModel.getYear()

        handleClicks()

    }

    private fun handleClicks(){
        binding.apply {
            date.text= getCurrentDate()
            viewModel.name.observe(viewLifecycleOwner, Observer { name ->
                if (name.isNullOrEmpty()) {
                    binding.collegeName.error = "Name cannot be empty"
                } else {
                    binding.collegeName.error = null
                }
            })

            viewModel.mobile.observe(viewLifecycleOwner, Observer { mobile ->
                if (mobile.isNullOrEmpty() || mobile.length != 10) {
                    binding.collegeName.error = "Invalid mobile number"
                } else {
                    binding.collegeName.error = null
                }
            })

            viewModel.email.observe(viewLifecycleOwner, Observer { email ->
                if (email.isNullOrEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.collegeName.error = "Invalid email address"
                } else {
                    binding.collegeName.error = null
                }
            })
            ivBack.setOnClickListener {

                findNavController().popBackStack()
            }

            btnAddLead.setOnClickListener {
                viewModel.addLeadApi(AddLeadRequest(
                    name= etName.text.toString(),
                    mobile= etMobile.text.toString(),
                    emailId= etEmail.text.toString(),
                    alternateMobile = altMobile.text.toString(),
                    stream = tvStream.text.toString(),
                    year = tvYear.text.toString(),
                    address = address.text.toString(),
                    collegeName= collegeName.text.toString(),
                    counsellingDate = date.text.toString(),
                    interestedIn = date.text.toString(),
                    status = "Fresh"
                ))
            }
            tvInterested.setOnClickListener {
                showMetaItemBottomSheet(metaItemsForInterest) { selectedItem ->
                    binding.tvInterested.text = selectedItem.title
                }
            }

            tvStream.setOnClickListener {
                showMetaItemBottomSheet(metaItemsForStream) { selectedItem ->
                    binding.tvStream.text = selectedItem.title
                }
            }

            tvYear.setOnClickListener {
                showMetaItemBottomSheet(metaItemsForYear) { selectedItem ->
                    binding.tvYear.text = selectedItem.title
                }
            }

            date.setOnClickListener {
                showDatePicker(requireActivity()) { selectedDate ->
                    // Handle the selected date (e.g., set it to a TextView)
                    date.text = selectedDate
                }
            }
        }
    }
    private fun showMetaItemBottomSheet(metaItems: List<MetaItem>, onItemSelected: (MetaItem) -> Unit) {
        val bottomSheet = MetaItemBottomSheet(requireContext(), metaItems, onItemSelected)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
    private fun observeAddLeadApi() {
        viewModel.addLeadFlow.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {
                        showToast(it.value.message)
                        findNavController().popBackStack()
                    } else {
                        it.value.message?.let { it1 -> showToast(it1) }
                    }
                }

                is ApiCallingState.Failure -> {
                    hideProgress()


                }

                else -> Unit
            }
        }.launchIn(fragmentScope)
    }


    private fun observeSpinnerEvents() {
        // Observe the interest, stream, and year lists from the ViewModel
        viewModel.interestListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            metaItemsForInterest = metaItems // Store data in the local variable
        })

        viewModel.streamListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            metaItemsForStream = metaItems // Store data in the local variable
        })

        viewModel.yearListLiveData.observe(viewLifecycleOwner, Observer { metaItems ->
            metaItemsForYear = metaItems // Store data in the local variable
        })
    }


}
