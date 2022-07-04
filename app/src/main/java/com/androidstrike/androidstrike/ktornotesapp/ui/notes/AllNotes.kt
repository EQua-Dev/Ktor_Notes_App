package com.androidstrike.androidstrike.ktornotesapp.ui.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentAllNotesBinding
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Created by Richard Uzor  on 02/07/2022
 */
class AllNotes: Fragment(R.layout.fragment_all_notes) {


    private var _binding: FragmentAllNotesBinding? = null
    val binding: FragmentAllNotesBinding?
        get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllNotesBinding.bind(view)
        (activity as AppCompatActivity).setSupportActionBar(binding!!.customToolBar)

        //fab click navigates to new note fragment
        binding?.newNoteFab?.setOnClickListener {
            findNavController().navigate(R.id.action_allNotes_to_newNote)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //set up menu click options
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.account -> {
                findNavController().navigate(R.id.action_allNotes_to_userInfo)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //we call onDestroy and set the binding to 'null' to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}