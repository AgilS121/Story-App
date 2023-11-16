package com.agilsatriaancangpamungkas.storyapp.view.detailStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agilsatriaancangpamungkas.storyapp.data.response.ErrorResponse
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseDetailStory
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import com.agilsatriaancangpamungkas.storyapp.view.addDataStory.AddDataViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailStoryViewModel(private val dRepository: RepositoryStory) : ViewModel() {

    private val _responseDetailStories = MutableLiveData<ResponseDetailStory>()
    val responseDetailStory: LiveData<ResponseDetailStory> = _responseDetailStories

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getDetailStories( storyID: String) {
        Log.d(TAG, "ini id story $storyID")
        viewModelScope.launch {
            try {
                val response = dRepository.getDetailStories(storyID)
                _responseDetailStories.postValue(response)
                Log.d(TAG, "ini response detail story $response")
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.e("Register", "Error: $errorMessage")
                _showLoading.value = false
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                _errorMessage.value = "Tidak Ada Koneksi"
                _showLoading.value = false
            }
        }
    }

    fun getUser() : LiveData<LoginResult> {
        return dRepository.getSession()
    }

    companion object {
        private const val TAG = "DetailStories"
    }
}