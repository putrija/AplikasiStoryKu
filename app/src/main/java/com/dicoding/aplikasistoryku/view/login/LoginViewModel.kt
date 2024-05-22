package com.dicoding.aplikasistoryku.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun login(email: String, password: String): LiveData<ApiResponse<LoginResponse>> {
        val result = MutableLiveData<ApiResponse<LoginResponse>>()
        viewModelScope.launch {
            val response = userRepository.login(email, password)
            result.value = response
        }
        return result
    }

}