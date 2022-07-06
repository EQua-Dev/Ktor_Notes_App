package com.androidstrike.androidstrike.ktornotesapp.repositories

import com.androidstrike.androidstrike.ktornotesapp.data.local.dao.NoteDao
import com.androidstrike.androidstrike.ktornotesapp.data.local.models.LocalNote
import com.androidstrike.androidstrike.ktornotesapp.data.remote.NoteApi
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.RemoteNote
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.User
import com.androidstrike.androidstrike.ktornotesapp.utils.Result
import com.androidstrike.androidstrike.ktornotesapp.utils.SessionManager
import com.androidstrike.androidstrike.ktornotesapp.utils.isNetworkConnected
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 04/07/2022
 */
/**
 * A class to implement the functions of the note repo (repository)  interface
 * We also inject the constructors needed in this class using Hilt
 */
class NoteRepoImpl @Inject constructor(
    private val noteApi: NoteApi,
    val noteDao: NoteDao,
    private val sessionManager: SessionManager
) : NoteRepo {
    override suspend fun createUser(user: User): Result<String> {
        return try {
            //check if there is internet connection
            if (!isNetworkConnected(sessionManager.context)) {
                Result.Error<String>("No Internet Connection!")
            }

            //create user using api
            val result = noteApi.createAccount(user)
            if (result.success) {
                //save the details in data store
                sessionManager.updateSession(result.message, user.name ?: "", user.email)
                Result.Success("User Created Successfully!")
            } else {
                Result.Error<String>(result.message)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error<String>(e.message ?: "Some Problem Occurred!")
        }
    }

    override suspend fun login(user: User): Result<String> {
        return try {
            //check if there is internet connection
            if (!isNetworkConnected(sessionManager.context)) {
                Result.Error<String>("No Internet Connection!")
            }

            //log in user using api
            val result = noteApi.login(user)
            if (result.success) {
                //save details in the data store
                sessionManager.updateSession(result.message, user.name ?: "", user.email)
                getAllNotesFromServer() //get notes from server for specific user once user logs in
                Result.Success("Logged In Successfully!")
            } else {
                Result.Error<String>(result.message)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error<String>(e.message ?: "Some Problem Occurred!")
        }
    }

    override suspend fun getUser(): Result<User> {
        return try {
            //get the details from the data store
            val name = sessionManager.getCurrentUserName()
            val email = sessionManager.getCurrentUserEmail()
            if (name == null || email == null) {
                //if the details are null, it means there is no "current user"
                Result.Error<User>("User not Logged In!")
            }
            Result.Success(User(name, email!!, ""))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Some Problem Occurred!")
        }
    }

    //function to log out user
    override suspend fun logout(): Result<String> {
        return try {
            //clear the data store
            sessionManager.logout()
            Result.Success("Logged Out Successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Some Problem Occurred!")
        }

    }

    // NOTES

    //function to create a new note
    override suspend fun createNote(note: LocalNote): Result<String> {

        try {
            //first insert the new note into local db
            noteDao.insertNote(note)
            val token = sessionManager.getJwtToken() //fetch jwt token

            //if (token == null){
            //if the user is not logged in
                ?: return Result.Success("Note is Saved in Local Database")
            if (!isNetworkConnected(sessionManager.context)){ //check internet connection
                return Result.Error("No Internet Connection")
            }
            //}

            //push the note to the cloud db
            val result = noteApi.createNote(
                "Bearer $token",
                RemoteNote(
                    noteTitle = note.noteTitle,
                    description = note.description,
                    date = note.date,
                    id = note.noteId
                )
            )

            //if pushed to the cloud db, update the 'connected' attribute in local db
            return if (result.success) {
                noteDao.insertNote(note.also { it.connected = true })
                Result.Success("Note Saved Successfully!")
            } else {
                Result.Error(result.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e.message ?: "Some Error Occurred!")

        }
    }

    //function to create a new note
    override suspend fun updateNote(note: LocalNote): Result<String> {
        try {
            //first insert the updated note into local db
            noteDao.insertNote(note)
            val token = sessionManager.getJwtToken()

            //if (token == null){
            //if the user is not logged in
                ?: return Result.Success("Note is Updated in Local Database")
            if (!isNetworkConnected(sessionManager.context)){ //check internet connection
                return Result.Error("No Internet Connection")
            }

            //}

            //push the updated note to the cloud db
            val result = noteApi.updateNote(
                "Bearer $token",
                RemoteNote(
                    noteTitle = note.noteTitle,
                    description = note.description,
                    date = note.date,
                    id = note.noteId
                )
            )

            //if pushed to the cloud db, update the 'connected' attribute in local db
            return if (result.success) {
                noteDao.insertNote(note.also { it.connected = true })
                Result.Success("Note Updated Successfully!")
            } else {
                Result.Error(result.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e.message ?: "Some Error Occurred!")

        }
    }

    //function to get all notes saved  in local db
    override fun getAllNotes(): Flow<List<LocalNote>> = noteDao.getAllNotesOrderByDate()

    //function to get all notes saved in the cloud db
    override suspend fun getAllNotesFromServer() {
        //if the user is not logged in, do nothing
        val token = sessionManager.getJwtToken() ?: return
        try{
            if (!isNetworkConnected(sessionManager.context)) { //if there is no internet connection
                return
            }

            //make network call to fetch notes from server
            val result = noteApi.getNotes("Bearer $token")
            result.forEach { remoteNote ->
                //insert each note into the local db
                noteDao.insertNote(
                    LocalNote(
                        noteTitle = remoteNote.noteTitle,
                        description = remoteNote.description,
                        date = remoteNote.date,
                        connected = true,
                        noteId = remoteNote.id
                    )
                )
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    //function to delete a selected note
    override suspend fun deleteNote(noteId: String) {
        try {
            //update the status of the note to be locally deleted
            noteDao.deleteNoteLocally(noteId)
            //if user is not logged in, delete the note from the local db
            val token = sessionManager.getJwtToken() ?: kotlin.run {
                noteDao.deleteNote(noteId)
                return
            }
            if (!isNetworkConnected(sessionManager.context)) { //if there is no internet connection
                return
            }

            //if user is logged in, make the network call to delete the note from cloud db
            val response = noteApi.deleteNote(
                "Bearer $token",
                noteId
            )

            //delete note from local db if deleted from cloud db
            if (response.success){
                noteDao.deleteNote(noteId)
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}