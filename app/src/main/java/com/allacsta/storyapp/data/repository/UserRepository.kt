package com.allacsta.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.allacsta.storyapp.data.StoryPagingSource
import com.allacsta.storyapp.data.StoryRemoteMediator
import com.allacsta.storyapp.data.api.ApiService
import com.allacsta.storyapp.data.api.ListStory
import com.allacsta.storyapp.data.api.LoginResponse
import com.allacsta.storyapp.data.api.RegisterResponse
import com.allacsta.storyapp.data.api.StoryResponse
import com.allacsta.storyapp.data.database.StoryDatabase
import com.allacsta.storyapp.data.pref.UserModel
import com.allacsta.storyapp.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
){

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

//    fun getStories() = liveData {
//        emit(ResultState.Loading)
//        try {
//            val successResponse = apiService.getStories()
//            emit(ResultState.Success(successResponse))
//        }catch (e : java.lang.Exception){
//            val ex = (e as? HttpException)?.response()?.errorBody()?.string()
//            emit(ResultState.Error(ex.toString()))
//        }
//    }

    fun getStories() : LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }


    fun getStoriesWithLocation() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesWithLocation()
            emit(ResultState.Success(successResponse))
        }catch (e : java.lang.Exception){
            val ex = (e as? HttpException)?.response()?.errorBody()?.string()
            emit(ResultState.Error(ex.toString()))
        }
    }

    fun uploadStory(imageFile: File, description: String, lat: String?, lon: String?) = liveData {
        emit(ResultState.Loading)
        val desc = description.toRequestBody("text/plain".toMediaType())
        val latitude = lat?.toRequestBody("text/plain".toMediaType())
        val longitude = lon?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory(multipartBody, desc,
                latitude, longitude
            )
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveSession(user: UserModel){
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }


    companion object{
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            storyDatabase: StoryDatabase,
            latest : Boolean
        ): UserRepository ?{
            if (userPreference == null || latest) {
                synchronized(this) {
                    instance = UserRepository(userPreference, apiService, storyDatabase)
                }
            }
            return instance
        }

    }
}