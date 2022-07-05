package com.androidstrike.androidstrike.ktornotesapp.data.remote.models

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/*
* All attributes of this class must match that which we created in the server db
* */
data class RemoteNote(
    val noteTitle: String?,
    val description: String?,
    val date: Long,
    val id: String
)
