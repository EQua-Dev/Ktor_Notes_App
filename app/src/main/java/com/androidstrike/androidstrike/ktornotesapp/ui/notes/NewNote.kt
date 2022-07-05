package com.androidstrike.androidstrike.ktornotesapp.ui.notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentNewNoteBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Created by Richard Uzor  on 02/07/2022
 */
@AndroidEntryPoint
class NewNote: Fragment(R.layout.fragment_new_note) {


    private var _binding: FragmentNewNoteBinding? = null
    val binding: FragmentNewNoteBinding?
        get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called

    val noteViewModel: NotesViewModel by activityViewModels()
    val args: NewNoteArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewNoteBinding.bind(view)

        //fetch the current note details from the arguments (if available)
        noteViewModel.oldNote = args.note

        //if there is an old note, set the text views to its values
        noteViewModel.oldNote?.noteTitle.let {
            binding?.newNoteTitleEditText?.setText(it)
        }
        noteViewModel.oldNote?.description.let {
            binding?.newNoteDescriptionEditText?.setText(it)
        }

        binding?.date?.isVisible = noteViewModel.oldNote != null
        noteViewModel.oldNote?.date?.let {
            binding?.date?.text = noteViewModel.milliToDate(it)
        }

    }

    //we invoke the save and update functions in onPause and so if user goes back or app crashes, note is saved
    override fun onPause() {
        super.onPause()
        if (noteViewModel.oldNote == null){
            createNote()
        }else{
            updateNote()
        }
    }

    //function to create new note
    private fun createNote(){

        //get data from UI
        val noteTitle = binding?.newNoteTitleEditText?.text.toString().trim()
        val description = binding?.newNoteDescriptionEditText?.text.toString().trim()

        //null error handling
        if (noteTitle.isNullOrEmpty() && description.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Note is Empty", Toast.LENGTH_SHORT).show()
            return
        }

        //create new note by calling the function from view model and pass the params
        noteViewModel.createNote(noteTitle, description)
        
    }

    //function to create new note
    private fun updateNote(){

        //get data from UI
        val noteTitle = binding?.newNoteTitleEditText?.text.toString().trim()
        val description = binding?.newNoteDescriptionEditText?.text.toString().trim()

        //null error handling
        if (noteTitle.isNullOrEmpty() && description.isNullOrEmpty()){
            // todo: delete note
            return
        }

        //create new note by calling the function from view model and pass the params
        noteViewModel.updateNote(noteTitle, description)

    }

    //we call onDestroy and set the binding to 'null' to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}