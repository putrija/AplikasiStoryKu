package com.dicoding.aplikasistoryku.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.data.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyResponse = MutableLiveData<StoryResponse>()
    val storyResponse: LiveData<StoryResponse> get() = _storyResponse

    fun getStoriesFromApi() {
        viewModelScope.launch {
            val user = repository.getUser()
            val token = user.token
            Log.d("Token", "Token: $token")
            val storyResponse = repository.getStoriesWithLocation(token)

            if (storyResponse.listStory.isEmpty()) {
                Log.d("MapsActivity", "List Story is Empty")
            } else {
                Log.d(
                    "MapsActivity",
                    "List Story is Not Empty. Total Stories: ${storyResponse.listStory.size}"
                )
            }

            _storyResponse.value = storyResponse
        }
    }
}