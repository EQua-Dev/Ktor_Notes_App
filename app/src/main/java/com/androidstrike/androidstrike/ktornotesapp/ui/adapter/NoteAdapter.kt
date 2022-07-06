package com.androidstrike.androidstrike.ktornotesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.data.local.models.LocalNote
import com.androidstrike.androidstrike.ktornotesapp.databinding.ItemNoteBinding

/**
 * Created by Richard Uzor  on 06/07/2022
 */
/**
 * Created by Richard Uzor  on 06/07/2022
 */
class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root)

    //implement diff util object to perform list similarity checks and updates
    val diffUtil = object : DiffUtil.ItemCallback<LocalNote>(){
        override fun areItemsTheSame(oldItem: LocalNote, newItem: LocalNote): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: LocalNote, newItem: LocalNote): Boolean {
            return oldItem == newItem
        }

    }

        //implement the getter and setter for the diff util with the notes
    val differ = AsyncListDiffer(this, diffUtil)
    var notes: List<LocalNote>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = notes[position]
        holder.binding.apply {
            //Update UI Views
            //make the views visible if they are not empty
            noteText.isVisible = note.noteTitle != null
            noteDescription.isVisible = note.description != null

            note.noteTitle?.let {
                noteText.text = it
            }
            note.description?.let {
                noteDescription.text = it
            }

            noteSync.setBackgroundResource(
                if (note.connected) R.drawable.synced
            else R.drawable.not_sync
            )

            //when item is clicked...
            root.setOnClickListener{
                onItemClickListener?.invoke(note)
            }
        }

    }

    //create click listener for the note items to always be passed as a parameter of the event
    private var onItemClickListener: ((LocalNote) -> Unit)? = null
    fun setOnItemClickListener(listener: (LocalNote) -> Unit){
        onItemClickListener = listener
    }


    override fun getItemCount(): Int {
        return notes.size
    }

}