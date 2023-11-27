package com.allacsta.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.allacsta.storyapp.data.repository.UserRepository
import java.io.File

class StoryViewModel(private val repository: UserRepository): ViewModel() {

    fun uploadStory(file: File, description: String, lat: String?, lon: String?) = repository.uploadStory(file, description, lat, lon)
}