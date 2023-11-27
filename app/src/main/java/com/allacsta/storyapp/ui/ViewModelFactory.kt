package com.allacsta.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.allacsta.storyapp.data.repository.UserRepository
import com.allacsta.storyapp.di.Injection
import com.allacsta.storyapp.ui.login.LoginViewModel
import com.allacsta.storyapp.ui.main.MainViewModel
import com.allacsta.storyapp.ui.maps.MapsViewModel
import com.allacsta.storyapp.ui.register.RegisterViewModel
import com.allacsta.storyapp.ui.splash.SplashViewModel
import com.allacsta.storyapp.ui.story.StoryViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, latest: Boolean): ViewModelFactory {
            synchronized(this){
                if (INSTANCE== null || latest){
                    INSTANCE = Injection.provideRepository(context)?.let { ViewModelFactory(it) }
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}