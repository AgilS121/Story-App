package com.agilsatriaancangpamungkas.storyapp.view.addDataStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseAddStories
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddDataViewModel(private val adRepository: RepositoryStory): ViewModel() {

    private val _responseAddData = MutableLiveData<ResponseAddStories>()
    val responseAddStories: LiveData<ResponseAddStories> = _responseAddData

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun addDataStories( file: MultipartBody.Part, desc: RequestBody, lat: Float? = null, lon: Float? = null) {
        _showLoading.value = true

        Log.d("cek dlu ini", "$file || $desc")

        viewModelScope.launch {
           try {
               val response = adRepository.addDataStories(file, desc, lat, lon)

               _responseAddData.postValue(response)
               _showLoading.value = false

               Log.d(TAG, "message $response")
           } catch (e: HttpException) {
               val jsonInString = e.response()?.errorBody()?.string()
               val errorBody = Gson().fromJson(jsonInString, ResponseAddStories::class.java)
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
        return adRepository.getSession()
    }

    companion object {
        private const val TAG ="UPLOADIMG"
    }
}