package com.dicoding.aplikasistoryku.data.repository

import android.util.Log
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.data.api.ApiService
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.pref.UserPreference
import com.dicoding.aplikasistoryku.data.response.LoginResponse
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

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: ApiService,
            apiService: UserPreference // Tambahkan parameter apiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference) // Gunakan apiService saat membuat instance
            }.also { instance = it }
    }
}