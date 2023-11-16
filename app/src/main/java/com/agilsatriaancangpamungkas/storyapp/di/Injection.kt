package com.agilsatriaancangpamungkas.storyapp.di

import android.content.Context
import com.agilsatriaancangpamungkas.storyapp.data.api.ApiConfig
import com.agilsatriaancangpamungkas.storyapp.datastore.StoriesPrereferences
import com.agilsatriaancangpamungkas.storyapp.datastore.dataStore
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun storyRepository(context: Context) : RepositoryStory {
        val storyPref = StoriesPrereferences.getInstance(context.dataStore)
        val user = runBlocking { storyPref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return RepositoryStory.getInstance(apiService, storyPref)
    }
}