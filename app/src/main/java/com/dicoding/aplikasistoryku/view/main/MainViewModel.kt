package com.dicoding.aplikasistoryku.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.pref.UserModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>>
        get() = _stories

    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> get() = _logoutResult

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logoutResult.value = true
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

                if (response.listStory.isNotEmpty()) {
                    Log.d("List Story", "Data listStory is not empty")
                } else {
                    Log.d("List Story", "Data listStory is empty")
                }

                _stories.value = response.listStory

            } catch (e: Exception) {
                Log.e("Error", "Failed to fetch stories: ${e.message}")
            }
        }
    }

}