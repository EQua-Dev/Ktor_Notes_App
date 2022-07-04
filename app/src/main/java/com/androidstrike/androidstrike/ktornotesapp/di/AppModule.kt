package com.androidstrike.androidstrike.ktornotesapp.di

import android.content.Context
import androidx.room.Room
import com.androidstrike.androidstrike.ktornotesapp.data.local.NoteDatabase
import com.androidstrike.androidstrike.ktornotesapp.data.local.dao.NoteDao
import com.androidstrike.androidstrike.ktornotesapp.data.remote.NoteApi
import com.androidstrike.androidstrike.ktornotesapp.repositories.NoteRepo
import com.androidstrike.androidstrike.ktornotesapp.repositories.NoteRepoImpl
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.BASE_URL
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.LOCAL_DB_NAME
import com.androidstrike.androidstrike.ktornotesapp.utils.SessionManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Richard Uzor  on 02/07/2022
 */

@Module
@InstallIn(SingletonComponent::class) //singletonComponent because we want a single instance of all components we inject here
object AppModule {

    //Provide Gson
    @Singleton
    @Provides
    fun provideGson() = Gson()

    //Provide Session Manager
    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context
    ) = SessionManager(context)

    //Create and Provide Note Database
    @Singleton
    @Provides
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        LOCAL_DB_NAME
    ).build()

    //Provide Note Database DAO
    @Singleton
    @Provides
    fun provideNoteDao(
        noteDb: NoteDatabase
    ) = noteDb.getNoteDao()


    //Initialize, Build and Provide Retrofit Instance
    @Singleton
    @Provides
    fun provideNoteApi(): NoteApi{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client =OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteApi::class.java)
    }

    //provide note repo returning the implementation parameters
    @Singleton
    @Provides
    fun providesNoteRepo(
        noteApi: NoteApi,
        noteDao: NoteDao,
        sessionManager: SessionManager
    ): NoteRepo{
        return NoteRepoImpl(
            noteApi, noteDao, sessionManager
        )
    }

}