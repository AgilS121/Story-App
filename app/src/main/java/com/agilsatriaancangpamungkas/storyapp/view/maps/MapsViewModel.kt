package com.agilsatriaancangpamungkas.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agilsatriaancangpamungkas.storyapp.data.response.ErrorResponse
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseStoryAll
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import com.agilsatriaancangpamungkas.storyapp.view.main.MainViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val lrepository: RepositoryStory) : ViewModel() {

    private val _responseDataLocation = MutableLiveData<ResponseStoryAll>()
    val responseDataLocation : LiveData<ResponseStoryAll> = _responseDataLocation

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getUser() : LiveData<LoginResult> {
        return lrepository.getSession()
    }

    fun getLocation() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val response = lrepository.getLocation()
                if (response != null) {
                    _responseDataLocation.postValue(response)
                    _showLoading.value = false
                    Log.d(TAG, "Response sukses $response")
                } else {
                    Log.d(TAG, "Response Gagal $response")
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d(TAG, "Error: $errorMessage")
                _showLoading.value = false
            } catch (e: Exception) {
                Log.e(TAG, "Error ${e.message}")
                _errorMessage.value = "Tidak Ada Koneksi"
                _showLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "Maps"
    }
}