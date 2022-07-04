package com.androidstrike.androidstrike.ktornotesapp.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentUserInfoBinding
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
class UserInfo: Fragment(R.layout.fragment_user_info) {


    private var _binding: FragmentUserInfoBinding? = null
    val binding: FragmentUserInfoBinding?
        get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        binding?.createAccountBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfo_to_createAccount)
        }
        binding?.loginBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfo_to_login)
        }

        binding?.logoutBtn?.setOnClickListener {
            userViewModel.logout()
        }
        subscribeToCurrentUserEvents()

    }

    override fun onStart() {
        super.onStart()
        userViewModel.getCurrentUser()
    }

    //function to handle the responses from the network call using the current user state in view model
    @SuppressLint("SetTextI18n")
    private fun subscribeToCurrentUserEvents() = lifecycleScope.launch {
        userViewModel.currentUserState.collect { result ->
            when(result){
                is Result.Success -> {
                    userLoggedIn()
                    binding?.userTxt?.text = result.data?.name ?: "No Name"
                    binding?.userEmail?.text = result.data?.email ?: "No Email"
                }
                is Result.Error -> {
                    userLoggedOut()
                    binding?.userTxt?.text = "Not Logged In"
                }
                is Result.Loading -> {
                    binding?.userProgressBar?.isVisible = true
                }
            }
        }
    }

    //function to toggle UI views visibility when user is logged in
    private fun userLoggedIn(){
        binding?.userProgressBar?.isVisible = false
        binding?.loginBtn?.isVisible = false
        binding?.createAccountBtn?.isVisible = false
        binding?.logoutBtn?.isVisible = true
        binding?.userEmail?.isVisible = true
    }

    //function to toggle UI views visibility when user is logged out
    private fun userLoggedOut(){
        binding?.userProgressBar?.isVisible = false
        binding?.loginBtn?.isVisible = true
        binding?.createAccountBtn?.isVisible = true
        binding?.logoutBtn?.isVisible = false
        binding?.userEmail?.isVisible = false
    }

    //we call onDestroy and set the binding to 'null' to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}