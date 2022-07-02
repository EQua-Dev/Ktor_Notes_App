package com.androidstrike.androidstrike.ktornotesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidstrike.androidstrike.ktornotesapp.data.local.models.LocalNote
import kotlinx.coroutines.flow.Flow

/**
 * Created by Richard Uzor  on 02/07/2022
 */

/*
* Room DAO interface to interact with the db
* */

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: LocalNote)

    //select all notes that have not being locally deleted and sort them by date in descending order
    @Query("SELECT * FROM LocalNote WHERE locallyDeleted =0 ORDER BY date DESC")
    fun getAllNotesOrderByDate(): Flow<List<LocalNote>>

    //function to actually delete the local note
    @Query("DELETE FROM LocalNote WHERE noteId=:noteId")
    suspend fun deleteNote(noteId: String)

    //we do not really want to delete the note, so we just update the deleted status of the local note to true (1)
    @Query("UPDATE LocalNote SET locallyDeleted = 1 WHERE noteId = :noteId")
    suspend fun deleteNoteLocally(noteId: String)

    //function to get all the notes that are not on the server
    @Query("SELECT * FROM LocalNote WHERE connected = 0")
    suspend fun getAllLocalNotes(): List<LocalNote>

    //function to get all locally deleted notes
    @Query("SELECT * FROM LocalNote WHERE locallyDeleted = 1")
    suspend fun getAllLocallyDeletedNotes(): List<LocalNote>

}