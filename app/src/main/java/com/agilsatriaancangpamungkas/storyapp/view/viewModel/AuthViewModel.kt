package com.agilsatriaancangpamungkas.storyapp.view.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agilsatriaancangpamungkas.storyapp.data.response.ErrorResponse
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseLogin
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseRegister
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(private val authRepository: RepositoryStory) : ViewModel() {

    private val _responseRegister = MutableLiveData<ResponseRegister>()
    val responseRegister: LiveData<ResponseRegister> = _responseRegister

    private val _responseLogin = MutableLiveData<ResponseLogin>()
    val responseLogin: LiveData<ResponseLogin> = _responseLogin

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun register(name: String, email: String, password: String) {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val response = authRepository.register(name, email, password)
                _responseRegister.postValue(response)
                Log.d(TAG, "Response Register: $response")
                _showLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.e(TAG, "Error Register: $errorMessage")
                _showLoading.value = false
            } catch (e: Exception) {
                Log.e(TAG, "Error Register ${e.message}")
                _errorMessage.value = "Tidak Ada Koneksi"
                _showLoading.value = false
            }
        }
    }

    suspend fun login(email: String, password: String) {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                _responseLogin.postValue(response)
                Log.d(TAG, "Response Login: $response")

                val loginResult = LoginResult(
                    response.loginResult?.name ?: "",
                    response.loginResult?.userId ?: "",
                    response.loginResult?.token ?: ""
                )
                saveSession(loginResult)

                Log.d(TAG, "${loginResult}")
                _showLoading.value = false

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d(TAG, "Error Login: $errorMessage")
                _showLoading.value = false
            } catch (e: Exception) {
                Log.e(TAG, "Error Login ${e.message}")
                _errorMessage.value = "Tidak Ada Koneksi"
                _showLoading.value = false
            }
        }
    }

    private fun saveSession(dataSession: LoginResult) {
        viewModelScope.launch {
            authRepository.saveSession(dataSession)
        }
    }

    companion object {
        private const val TAG = "Auth Model"
    }
}