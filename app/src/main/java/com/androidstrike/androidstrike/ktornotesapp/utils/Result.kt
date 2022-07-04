package com.androidstrike.androidstrike.ktornotesapp.utils

/**
 * Created by Richard Uzor  on 04/07/2022
 */

/*
* This sealed class will work as a repository class for our network request
* It will contain classes for all the possible states and responses from a network call
* */

sealed class Result<T>(val data: T? = null, val errorMessage: String? = null){

    class Success<T>(data: T, errorMessage: String? = null): Result<T>(data, errorMessage)
    class Error<T>(errorMessage: String, data: T? = null): Result<T>(data, errorMessage)
    class Loading<T>:Result<T>()

}
