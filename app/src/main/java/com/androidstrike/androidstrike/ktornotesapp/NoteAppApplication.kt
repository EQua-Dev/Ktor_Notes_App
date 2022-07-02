package com.androidstrike.androidstrike.ktornotesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * This is our application class for providing all the dependency injections
 * It extends application
 * It is annotated with @HiltAndroidApp to let Hilt know that this is the application class
 * Add the class to android manifest by stating its name in the application tag
 */


@HiltAndroidApp
class NoteAppApplication: Application() {
}