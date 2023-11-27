package com.allacsta.storyapp.utils

import com.allacsta.storyapp.data.api.ListStory

object DataDummy{
    fun generateDummyStory(): List<ListStory> {
        val items = arrayListOf<ListStory>()
        for (i in 0..100) {
            val storyItem = ListStory(
                "story-FvU4u0Vp2S3PMsFg",
                "boboboy",
                "Lorem Ipsum",
                "https://story-api.dicoding.dev/images/stories/photos-1698951245985_uJGDoctQ.jpg",
                "2023-10-08T06:34:18.598Z",
                -6.8957643,
                107.6338462
            )
            items.add(storyItem)
        }
        return items
    }
}