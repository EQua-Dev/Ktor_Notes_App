package com.androidstrike.androidstrike.ktornotesapp.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentLoginBinding
import com.androidstrike.androidstrike.ktornotesapp.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Created by Richard Uzor  on 02/07/2022
 */
@AndroidEntryPoint
class Login: Fragment(R.layout.fragment_login) {


    private var _binding: FragmentLoginBinding? = null
    val binding: FragmentLoginBinding?
        get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called


    // we get the user view model by 'activity view models' in order to get a single instance of the view model
    private val userViewModel: UserViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        subscribeToRegisterEvents()

        //get the values from the UI
        binding?.loginBtn?.setOnClickListener {
            val email = binding!!.emailEditTxt.text.toString()
            val password = binding!!.passwordEdtTxt.text.toString()

            //call the login user function from the  user view model and pass the fetched UI values
            userViewModel.login(
                email.trim(),
                password.trim()
            )
        }

    }

    //function to handle the responses from the network call using the login state in view model
    private fun subscribeToRegisterEvents() = lifecycleScope.launch {
        userViewModel.loginState.collect { result ->
            when(result){
                is Result.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), result.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressBar()
                }
            }
        }
    }


    private fun showProgressBar(){
        binding?.loginProgressBar?.isVisible = true
    }

    private fun hideProgressBar(){
        binding?.loginProgressBar?.isVisible = false
    }


    //we call onDestroy and set the binding to 'null' to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}