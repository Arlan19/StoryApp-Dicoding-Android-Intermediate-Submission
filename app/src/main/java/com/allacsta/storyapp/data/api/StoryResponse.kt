package com.allacsta.storyapp.data.api

import com.google.gson.annotations.SerializedName


data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
)
