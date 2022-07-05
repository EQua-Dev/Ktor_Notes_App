package com.androidstrike.androidstrike.ktornotesapp.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstrike.androidstrike.ktornotesapp.data.local.models.LocalNote
import com.androidstrike.androidstrike.ktornotesapp.repositories.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 05/07/2022
 */
/**
 * Created by Richard Uzor  on 05/07/2022
 */
@HiltViewModel
class NotesViewModel @Inject constructor(
    val noteRepo: NoteRepo
): ViewModel() {

    var oldNote: LocalNote? = null

    //function to create a new note
    fun createNote(
        noteTitle: String?,
        description: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        val localNote = LocalNote(
            noteTitle = noteTitle,
            description = description
        )
        noteRepo.createNote(localNote)
    }

    //function to update a note
    fun updateNote(
        noteTitle: String?,
        description: String?
    ) = viewModelScope.launch(Dispatchers.IO) {

        if (noteTitle == oldNote?.noteTitle && description == oldNote?.description && oldNote?.connected == true){
            //do nothing if the note details are unchanged
            return@launch
        }

        val note = LocalNote(
            noteTitle = noteTitle,
            description = description,
            noteId = oldNote!!.noteId
        //if you want to keep the creation time, (date = oldNote!!.date)
        )

        noteRepo.updateNote(note)
    }

    //function to convert time in milliseconds to readable date
    fun milliToDate(time: Long): String{
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat("hh:mm a | MMM d, yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

}









