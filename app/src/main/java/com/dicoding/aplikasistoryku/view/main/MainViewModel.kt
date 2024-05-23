package com.dicoding.aplikasistoryku.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStoriesFromApi() {
        viewModelScope.launch {
            try {
                val user = repository.getUser()
                val token = user.token
                Log.d("Token", "Token: $token")
                val response = repository.getStories(token)

                Log.d("API Response", response.toString())

            } catch (e: Exception) {
                Log.e("Error", "Failed to fetch stories: ${e.message}")
            }
        }
    }

}