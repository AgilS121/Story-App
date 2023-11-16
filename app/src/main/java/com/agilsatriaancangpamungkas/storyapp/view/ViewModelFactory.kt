package com.agilsatriaancangpamungkas.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agilsatriaancangpamungkas.storyapp.di.Injection
import com.agilsatriaancangpamungkas.storyapp.repository.RepositoryStory
import com.agilsatriaancangpamungkas.storyapp.view.addDataStory.AddDataViewModel
import com.agilsatriaancangpamungkas.storyapp.view.detailStory.DetailStoryViewModel
import com.agilsatriaancangpamungkas.storyapp.view.viewModel.AuthViewModel
import com.agilsatriaancangpamungkas.storyapp.view.main.MainViewModel
import com.agilsatriaancangpamungkas.storyapp.view.maps.MapsViewModel

class ViewModelFactory private constructor(private val authRepository: RepositoryStory) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(AddDataViewModel::class.java)) {
            return AddDataViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknow View Model class : " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.storyRepository(context))
            }.also { instance = it }
    }

}