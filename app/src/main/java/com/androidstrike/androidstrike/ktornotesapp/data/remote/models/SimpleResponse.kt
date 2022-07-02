package com.androidstrike.androidstrike.ktornotesapp.data.remote.models

/**
 * Created by Richard Uzor  on 02/07/2022
 */

//Data class to handle the query response from the network call
data class SimpleResponse(
    val success: Boolean,
    val message: String
)
