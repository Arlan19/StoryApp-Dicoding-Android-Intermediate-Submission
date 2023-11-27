package com.allacsta.storyapp.di

import android.content.Context
import com.allacsta.storyapp.data.api.ApiConfig
import com.allacsta.storyapp.data.database.StoryDatabase
import com.allacsta.storyapp.data.pref.UserPreference
import com.allacsta.storyapp.data.pref.dataStore
import com.allacsta.storyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository?{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(pref, apiService,database, true)
    }
}