package com.allacsta.storyapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryData (
    val name: String,
    val description: String,
    val photo: String
):Parcelable