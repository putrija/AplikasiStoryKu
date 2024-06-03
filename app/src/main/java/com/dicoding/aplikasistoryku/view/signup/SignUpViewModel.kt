package com.dicoding.aplikasistoryku.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                when (val registerResponse = userRepository.register(name, email, password)) {
                    is ApiResponse.Success -> {
                        val message = registerResponse.data.message
                        _toastMessage.value = message ?: "Registration successful"
                        _navigateToLogin.value = true
                    }

                    is ApiResponse.Error -> {
                        _toastMessage.value = registerResponse.errorMessage ?: "Registration failed"
                    }

                    ApiResponse.Loading -> TODO()
                }
            } catch (e: HttpException) {
                _toastMessage.value = "Registration failed: ${e.message()}"
            } catch (e: Exception) {
                _toastMessage.value = "Registration failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
}