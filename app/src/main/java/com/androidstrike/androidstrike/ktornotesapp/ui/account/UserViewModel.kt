package com.androidstrike.androidstrike.ktornotesapp.ui.account

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstrike.androidstrike.ktornotesapp.data.remote.NoteApi
import com.androidstrike.androidstrike.ktornotesapp.data.remote.models.User
import com.androidstrike.androidstrike.ktornotesapp.repositories.NoteRepo
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.MAXIMUM_PASSWORD_LENGTH
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.MINIMUM_PASSWORD_LENGTH
import com.androidstrike.androidstrike.ktornotesapp.utils.Result
import com.androidstrike.androidstrike.ktornotesapp.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by Richard Uzor  on 04/07/2022
 */
/**
 * Created by Richard Uzor  on 04/07/2022
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    val noteRepo: NoteRepo,
    val noteApi: NoteApi,
    val sessionManager: SessionManager
): ViewModel() {

    //create states to be updated at each phase of the network functions
    
    //state for register user
    private val _registerState = MutableSharedFlow<Result<String>>()
    val registerState: SharedFlow<Result<String>> = _registerState

    //state for login user
    private val _loginState = MutableSharedFlow<Result<String>>()
    val loginState: SharedFlow<Result<String>> = _loginState

    //state for getting current user info
    private val _currentUserState = MutableSharedFlow<Result<User>>()
    val currentUserState: SharedFlow<Result<User>> = _currentUserState

    //function to create a user
    fun createUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) = viewModelScope.launch {
        _registerState.emit(Result.Loading())//set the state to loading

        //input validations
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            _registerState.emit(Result.Error("Some Fields are Empty"))
            return@launch
        }

        //email validity check
        if (!isEmailValid(email)){
            _registerState.emit(Result.Error("Email is not Valid"))
            return@launch
        }

        //password validity check
        if (!isPasswordValid(password)){
            _registerState.emit(Result.Error("Password length should be between $MINIMUM_PASSWORD_LENGTH and $MAXIMUM_PASSWORD_LENGTH"))
            return@launch
        }

        //new user
        val newUser = User(
            name,
            email,
            password
        )

        //use the state to call the create user api
        _registerState.emit(noteRepo.createUser(newUser))

    }

    //function to create a user
    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginState.emit(Result.Loading()) //set the state to loading

        //input validations
        if (email.isEmpty() || password.isEmpty()){
            _registerState.emit(Result.Error("Some Fields are Empty"))
            return@launch
        }

        //email validity check
        if (!isEmailValid(email)){
            _loginState.emit(Result.Error("Email is not Valid"))
            return@launch
        }

        //logged user
        val newUser = User(
            email = email,
            password = password
        )

        //use the state to call the login user api
        _loginState.emit(noteRepo.login(newUser))

    }

    //function to get user info
    fun getCurrentUser() = viewModelScope.launch {
        _currentUserState.emit(Result.Loading())
        _currentUserState.emit(noteRepo.getUser())
    }

    //function to logout user
    fun logout() = viewModelScope.launch {
        val result = noteRepo.logout()
        if (result is Result.Success){
            getCurrentUser()
        }
    }

    //email validation function
    private fun isEmailValid(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //password validation function
    private fun isPasswordValid(password: String): Boolean{
        return (password.length in MINIMUM_PASSWORD_LENGTH..MAXIMUM_PASSWORD_LENGTH)
    }
}