package com.dicoding.aplikasistoryku.data.repository

import android.util.Log
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.data.api.ApiService
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): ApiResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = apiService.login(email, password)
                if (!loginResponse.error && !loginResponse.token.isNullOrEmpty()) {
                    val user = UserModel(email, loginResponse.token)
                    userPreference.saveSession(user)
                    ApiResponse.Success("Login successful")

                } else {
                    ApiResponse.Error(loginResponse.message ?: "Login failed")
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during login: ${e.message}")
                e.printStackTrace()
                ApiResponse.Error("Login failed")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService // Tambahkan parameter apiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService) // Gunakan apiService saat membuat instance
            }.also { instance = it }
    }
}