package com.dicoding.aplikasistoryku.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.data.api.ApiService
import com.dicoding.aplikasistoryku.data.pagingSource.StoryPagingSource
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.pref.UserPreference
import com.dicoding.aplikasistoryku.data.response.FileUploadResponse
import com.dicoding.aplikasistoryku.data.response.ListStoryItem
import com.dicoding.aplikasistoryku.data.response.LoginResponse
import com.dicoding.aplikasistoryku.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String): ApiResponse<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(email, password)
                if (!loginResponse.error!! && loginResponse.loginResult != null) {
                    val user = UserModel(email, loginResponse.loginResult.token ?: "")
                    saveSession(user)
                    ApiResponse.Success(loginResponse)
                } else {
                    ApiResponse.Error(loginResponse.message ?: "Login failed")
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during login: ${e.message}")
                e.printStackTrace()
                ApiResponse.Error("Login Gagal!")
            }
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getUser(): UserModel {
        return userPreference.getUser().first()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(userPreference, apiService)
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        return apiService.getStoriesWithLocation("Bearer $token")
    }

    fun uploadStory(imageFile: File, description: String, token: String, lat: Double, lon: Double) =
        liveData {
            emit(ApiResponse.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val latPart = lat.toString().toRequestBody("text/plain".toMediaType())
            val lonPart = lon.toString().toRequestBody("text/plain".toMediaType())
            try {
                val successResponse =
                    apiService.uploadStory(
                        "Bearer $token",
                        multipartBody,
                        requestBody,
                        latPart,
                        lonPart
                    )
                emit(ApiResponse.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                emit(ApiResponse.Error(errorResponse.message))
            }
        }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: ApiService,
            apiService: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(
                    apiService,
                    userPreference
                )
            }.also { instance = it }
    }
}