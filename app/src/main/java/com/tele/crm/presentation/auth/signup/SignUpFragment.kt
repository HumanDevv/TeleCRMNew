package com.tele.crm.presentation.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tele.crm.R
import com.tele.crm.databinding.FragmentLoginBinding
import com.tele.crm.databinding.FragmentSignUpBinding
import com.tele.crm.utils.extension.setDebouncedOnClickListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSignUpBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClickEvents()

    }

    private fun handleClickEvents() {
        binding.apply {

            btnCreateAcccount.setDebouncedOnClickListener {
                // validateFields()
                findNavController().navigate(R.id.action_loginFragment_to_leadFragment)
            }

            tvLogin.setDebouncedOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }



        }
    }

}