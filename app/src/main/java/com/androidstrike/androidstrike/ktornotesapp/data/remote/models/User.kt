package com.androidstrike.androidstrike.ktornotesapp.data.remote.models

/**
 * Created by Richard Uzor  on 02/07/2022
 */

//Data class to receive the info of user
data class User(
    val name: String? = null,
    val email: String,
    val password: String
)
