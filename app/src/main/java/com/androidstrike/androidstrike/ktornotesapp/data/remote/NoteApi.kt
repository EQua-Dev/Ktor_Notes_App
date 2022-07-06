package com.androidstrike.androidstrike.ktornotesapp.data.remote

import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.RemoteNote
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.SimpleResponse
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.User
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.API_VERSION
import retrofit2.http.*

/**
 * Created by Richard Uzor  on 02/07/2022
 */

//Interface to handle all network calls to the server
interface NoteApi {

    //= = = = = = = = USER = = = = = = = = =


    //CREATE USER
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/users/register")
    suspend fun createAccount(
        @Body user: User
    ): SimpleResponse


    //LOGIN USER
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/users/login")
    suspend fun login(
        @Body user: User
    ): SimpleResponse

    //= = = = = = = = NOTES = = = = = = = = =


    //CREATE NOTE
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes/new_note")
    suspend fun createNote(
        //todo include header at the retrofit initialisation build just like Belal Khan taught
        @Header("Authorization") token: String,
        @Body note: RemoteNote
    ): SimpleResponse

    //GET NOTES
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes")
    suspend fun getNotes(
        //todo include header at the retrofit initialisation build just like Belal Khan taught
        @Header("Authorization") token: String,
    ): List<RemoteNote>


    //UPDATE NOTE
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes/update")
    suspend fun updateNote(
        //todo include header at the retrofit initialisation build just like Belal Khan taught
        @Header("Authorization") token: String,
        @Body note: RemoteNote
    ): SimpleResponse


    //DELETE NOTE
    @Headers("Content-Type: application/json")
    @DELETE("$API_VERSION/notes/delete")
    suspend fun deleteNote(
        //todo include header at the retrofit initialisation build just like Belal Khan taught
        @Header("Authorization") token: String,
        @Query("id") noteId: String
        ): SimpleResponse


}