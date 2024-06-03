package com.dicoding.aplikasistoryku.view.maps

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.data.response.ListStoryItem
import com.dicoding.aplikasistoryku.data.response.StoryResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyResponse = MutableLiveData<StoryResponse>()
    val storyResponse: LiveData<StoryResponse> get() = _storyResponse

    fun fetchStories() {
        viewModelScope.launch {
            val user = repository.getUser()
            val token = user.token
            Log.d("Token", "Token: $token")
            val storyResponse = repository.getStoriesWithLocation(token)

            // Log untuk memeriksa apakah list story kosong atau tidak
            if (storyResponse.listStory.isEmpty()) {
                Log.d("MapsActivity", "List Story is Empty")
            } else {
                Log.d("MapsActivity", "List Story is Not Empty. Total Stories: ${storyResponse.listStory.size}")
            }
//
            _storyResponse.value = storyResponse
        }
    }
}