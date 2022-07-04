package com.androidstrike.androidstrike.ktornotesapp.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Created by Richard Uzor  on 02/07/2022
 */
class CreateAccount: Fragment(R.layout.fragment_create_account) {

    private var _binding: FragmentCreateAccountBinding? = null
    val binding: FragmentCreateAccountBinding?
    get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateAccountBinding.bind(view)
    }

    //we call onDestroy and set the binding to 'null' to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}