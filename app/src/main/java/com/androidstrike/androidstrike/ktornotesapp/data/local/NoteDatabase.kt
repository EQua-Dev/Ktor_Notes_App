package com.androidstrike.androidstrike.ktornotesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidstrike.androidstrike.ktornotesapp.data.local.dao.NoteDao
import com.androidstrike.androidstrike.ktornotesapp.data.local.models.LocalNote

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * Build DB in the App Module Dependency Provider
 */

@Database(entities = [LocalNote::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
}