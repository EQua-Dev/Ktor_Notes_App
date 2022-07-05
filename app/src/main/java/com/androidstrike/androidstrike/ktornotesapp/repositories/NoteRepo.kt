package com.androidstrike.androidstrike.ktornotesapp.repositories

import com.androidstrike.androidstrike.ktornotesapp.data.local.models.LocalNote
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.User
import com.androidstrike.androidstrike.ktornotesapp.utils.Result

/**
 * Created by Richard Uzor  on 04/07/2022
 */
 /*
 * We are making it an interface because it is the best practice then implement in class
 * Also because if you want to make test cases in your app, it'll be helpful in creating fake repos
 * */
interface NoteRepo {

    suspend fun createUser(user: User): Result<String>
    suspend fun login(user: User): Result<String>
    suspend fun getUser(): Result<User>
    suspend fun logout(): Result<String>

    //NOTES
    suspend fun createNote(note: LocalNote) : Result<String>
    suspend fun updateNote(note: LocalNote) : Result<String>


}