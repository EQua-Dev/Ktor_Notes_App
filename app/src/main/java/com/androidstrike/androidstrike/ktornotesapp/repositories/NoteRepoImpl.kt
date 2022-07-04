package com.androidstrike.androidstrike.ktornotesapp.repositories

import com.androidstrike.androidstrike.ktornotesapp.data.local.dao.NoteDao
import com.androidstrike.androidstrike.ktornotesapp.data.remote.NoteApi
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.User
import com.androidstrike.androidstrike.ktornotesapp.utils.Result
import com.androidstrike.androidstrike.ktornotesapp.utils.SessionManager
import com.androidstrike.androidstrike.ktornotesapp.utils.isNetworkConnected
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

    override suspend fun logout(): Result<String> {
        return try {
            sessionManager.logout()
            Result.Success("Logged Out Successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Some Problem Occurred!")
        }

}
}