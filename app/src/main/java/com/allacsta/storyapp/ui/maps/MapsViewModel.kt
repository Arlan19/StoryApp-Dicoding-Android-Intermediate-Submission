package com.allacsta.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.allacsta.storyapp.data.repository.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getStoriesWithLocation() = repository.getStoriesWithLocation()

}