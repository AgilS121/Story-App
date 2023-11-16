package com.agilsatriaancangpamungkas.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.agilsatriaancangpamungkas.storyapp.data.api.ApiConfig
import com.agilsatriaancangpamungkas.storyapp.data.api.ApiService
import com.agilsatriaancangpamungkas.storyapp.data.response.ListStoryItem
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseLogin
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseRegister
import com.agilsatriaancangpamungkas.storyapp.datastore.StoriesPrereferences
import com.agilsatriaancangpamungkas.storyapp.paging.StoriesPagingSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepositoryStory private constructor(
    private val apiService: ApiService,
    private val storyPref: StoriesPrereferences
){
    suspend fun register(name: String, email: String, password: String) : ResponseRegister {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String) : ResponseLogin {
        return apiService.login(email, password)
    }

    fun getSession(): LiveData<LoginResult> {
        return storyPref.getSession().asLiveData()
    }

    suspend fun saveSession(dataSession: LoginResult) {
        return storyPref.saveSession(dataSession)
    }

    fun getToken() = runBlocking { storyPref.getToken().first() }

    suspend fun logout() = storyPref.logout()

    fun getAllStories() : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(ApiConfig.getApiService(getToken()))
            }
        ).liveData
    }


    suspend fun getDetailStories( idStory: String) = ApiConfig.getApiService(getToken()).getDetailStories(idStory)

    suspend fun addDataStories( file: MultipartBody.Part, desc : RequestBody, lat: Float? = null, lon: Float? = null) = ApiConfig.getApiService(getToken()).uploadImage(file, desc, lat, lon)

    suspend fun getLocation() = ApiConfig.getApiService(getToken()).getStoriesWithLocation()

    companion object {
        @Volatile
        private var instance: RepositoryStory? = null
        fun getInstance(
            apiService: ApiService,
            storyPref: StoriesPrereferences
        ) : RepositoryStory = instance ?: synchronized(this) {
            instance ?: RepositoryStory( apiService, storyPref )
        }.also { instance = it}
    }
}