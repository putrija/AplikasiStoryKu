package com.dicoding.aplikasistoryku.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(email: String, password: String): LiveData<ApiResponse<String>> {
        val result = MutableLiveData<ApiResponse<String>>()
        viewModelScope.launch {
            result.value = userRepository.login(email, password)
        }
        return result
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}