package com.agilsatriaancangpamungkas.storyapp.view.main


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.agilsatriaancangpamungkas.storyapp.data.response.ListStoryItem
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import kotlinx.coroutines.launch

class MainViewModel(private val mrepository: RepositoryStory) : ViewModel() {

    fun getUser() : LiveData<LoginResult> {
        return mrepository.getSession()
    }

    fun story() : LiveData<PagingData<ListStoryItem>> {
        return mrepository.getAllStories().cachedIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            mrepository.logout()
            Log.d(TAG, "${mrepository.logout()}")
        }
    }

    companion object {
        private const val TAG = "Main Stories"
    }

}

