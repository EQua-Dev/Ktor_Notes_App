package com.androidstrike.androidstrike.ktornotesapp.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentUserInfoBinding

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Created by Richard Uzor  on 02/07/2022
 */
class UserInfo: Fragment(R.layout.fragment_user_info) {


    private var _binding: FragmentUserInfoBinding? = null
    val binding: FragmentUserInfoBinding?
        get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        binding?.createAccountBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfo_to_createAccount)
        }
        binding?.loginBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfo_to_login)
        }

    }

    //we call onDestroy and set the binding to 'null' to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}