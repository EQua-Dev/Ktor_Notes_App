package com.androidstrike.androidstrike.ktornotesapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Created by Richard Uzor  on 02/07/2022
 */

/*
* Entity annotation because it will be used to create the entities (columns) in room db
* The model for the local note contains different attributes from that pushed to the cloud
* */
@Entity
data class LocalNote(
    var noteTitle: String? = null,
    var description: String? = null,
    var date: Long = System.currentTimeMillis(),
    var connected: Boolean = false,
    var locallyDeleted: Boolean = false, //because if note is deleted when internet !connected
    @PrimaryKey(autoGenerate = false)
    var noteId: String = UUID.randomUUID().toString()
): Serializable
