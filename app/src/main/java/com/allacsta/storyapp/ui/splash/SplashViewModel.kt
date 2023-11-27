package com.allacsta.storyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.allacsta.storyapp.data.pref.UserModel
import com.allacsta.storyapp.data.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}