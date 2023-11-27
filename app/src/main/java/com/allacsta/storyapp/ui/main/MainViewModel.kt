package com.allacsta.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.allacsta.storyapp.data.StoryPagingSource
import com.allacsta.storyapp.data.api.ListStory
import com.allacsta.storyapp.data.pref.UserModel
import com.allacsta.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

//    fun getSession(): LiveData<UserModel>{
//        return repository.getSession().asLiveData()
//    }

    fun logout(){
        viewModelScope.launch{
            repository.logout()
        }
    }

    fun getStories() = repository.getStories()

    val story: LiveData<PagingData<ListStory>> =
        repository.getStories().cachedIn(viewModelScope)

}