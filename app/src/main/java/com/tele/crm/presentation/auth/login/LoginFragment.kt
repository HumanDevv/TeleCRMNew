package com.tele.crm.presentation.auth.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tele.crm.R
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.ApiResponse
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.databinding.FragmentLoginBinding
import com.tele.crm.presentation.lead.LeadViewModel
import com.tele.crm.utils.extension.fragmentScope
import com.tele.crm.utils.extension.hideProgress
import com.tele.crm.utils.extension.isValidEmail
import com.tele.crm.utils.extension.isValidMobile
import com.tele.crm.utils.extension.setDebouncedOnClickListener
import com.tele.crm.utils.extension.showProgress
import com.tele.crm.utils.extension.showToast
import com.tele.crm.utils.extension.value
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    var isPasswordVisible = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoginApi()

        handleClickEvents()
    }


    private fun handleClickEvents() {
        binding.apply {

            loginBtn.setDebouncedOnClickListener {
                validateFields()
               // findNavController().navigate(R.id.action_loginFragment_to_leadFragment)
            }
            togglePasswordVisibility.setOnClickListener {
                if (isPasswordVisible) {
                    // Hide the password
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    togglePasswordVisibility.setImageResource(R.drawable.hide) // Change to "eye closed" icon
                    isPasswordVisible = false
                } else {
                    // Show the password
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    togglePasswordVisibility.setImageResource(R.drawable.show) // Change to "eye open" icon
                    isPasswordVisible = true
                }
                // Move cursor to the end
                edtPassword.setSelection(edtPassword.text.length)
            }
          /*  createAccountText.setDebouncedOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            forgetPassword.setDebouncedOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_ForgotFragment)
            }*/


        }
    }

    private fun validateFields() {

        when {
            binding.etEmail.value.isEmpty() ->
                showToast(getString(R.string.mobile_number_cannot_be_empty))

            !binding.etEmail.value.isValidEmail() ->
                showToast(getString(R.string.please_enter_a_valid_mobile_number))

            else -> {
                viewModel.loginApi(  LoginRequest(
                    email_id = binding.etEmail.text.toString().trim(),
                    password = binding.etPassword.text.toString().trim()
                )
                )
            }

        }
    }
    private fun observeLoginApi() {
        viewModel.loginFlow.onEach {
            when (it) {
                is ApiCallingState.Loading -> showProgress()
                is ApiCallingState.Success -> {
                    hideProgress()
                    if (it.value.success) {

                        findNavController().navigate(
                            R.id.action_loginFragment_to_leadFragment)
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

}