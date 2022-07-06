package com.androidstrike.androidstrike.ktornotesapp.ui.notes

import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidstrike.androidstrike.ktornotesapp.R
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentAllNotesBinding
import com.androidstrike.androidstrike.ktornotesapp.databinding.FragmentCreateAccountBinding
import com.androidstrike.androidstrike.ktornotesapp.ui.adapter.NoteAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Created by Richard Uzor  on 02/07/2022
 */
@AndroidEntryPoint
class AllNotes: Fragment(R.layout.fragment_all_notes) {


    private var _binding: FragmentAllNotesBinding? = null
    val binding: FragmentAllNotesBinding?
        get() = _binding //we use this get method for the binding cos we want the binding to initialize only when called

    private lateinit var noteAdapter: NoteAdapter
    private val notesViewModel: NotesViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllNotesBinding.bind(view)
        (activity as AppCompatActivity).setSupportActionBar(binding!!.customToolBar)

        //fab click navigates to new note fragment
        binding?.newNoteFab?.setOnClickListener {
            findNavController().navigate(R.id.action_allNotes_to_newNote)
        }

        setUpRecyclerView()
        subscribeToNotes()
        setUpSwipeLayout()

        notesViewModel.syncNotes()

    }

    //function to set up views and logic for the recycler view UI
    private fun setUpRecyclerView(){
        noteAdapter = NoteAdapter()
        noteAdapter.setOnItemClickListener {

            val action = AllNotesDirections.actionAllNotesToNewNote(it)
            findNavController().navigate(action)

        }
        binding?.noteRecyclerView?.apply {
            adapter = noteAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            //attach the item touch helper to the recycler view for swipe effect
            ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(this)
        }
    }

    //function to update the recycler view notes list
    private fun subscribeToNotes() = lifecycleScope.launch{
        notesViewModel.notes.collect {
            noteAdapter.notes = it.filter { localNote ->
                localNote.noteTitle?.contains(notesViewModel.searchQuery, true) == true ||
                        localNote.description?.contains(notesViewModel.searchQuery, true) == true
            }
        }
    }

    private fun setUpSwipeLayout(){
        binding?.swipeRefeeshLayout?.setOnRefreshListener {
            notesViewModel.syncNotes {
                binding?.swipeRefeeshLayout?.isRefreshing = false
            }
        }
    }

    //item touch helper object to enable swipe effect on each note
    val itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        //when swiped...
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition //get the list position of the selected note
            val note = noteAdapter.notes[position] //get the note at the selected position
            notesViewModel.deleteNote(note.noteId) //delete the note at the selected position using the id
            Snackbar.make( //implement snack bar to show message and implement undo functionality
                requireView(),
                "Note Deleted Successfully",
                Snackbar.LENGTH_LONG
            ).apply { //set action on the snack bar to undo delete
                setAction(
                    "Undo"
                ){
                    //when user clicks to undo delete, call the undo note function from view model
                    notesViewModel.undoDelete(note)
                }
                show()

            }
        }

        //we call this function to divide the expected width of each item by two in the 'super'
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX/2, dY, actionState, isCurrentlyActive)
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

        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as android.widget.SearchView

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                notesViewModel.searchQuery = ""
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                notesViewModel.searchQuery = ""
                return true
            }

        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { //search the entered characters when searched
                    searchNotes(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { //search the entered characters when searched in real time
                    searchNotes(it)
                }
                return true
            }

        })

    }

    //function to actually search the notes with the entered search characters that match saved notes
    private fun searchNotes(query: String) = lifecycleScope.launch{
        notesViewModel.searchQuery = query
        noteAdapter.notes = notesViewModel.notes.first().filter {
            it.noteTitle?.contains(query, true) == true ||
                    it.description?.contains(query, true) == true
        }
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