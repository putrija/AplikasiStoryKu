package com.dicoding.aplikasistoryku.view.addStory

import androidx.lifecycle.ViewModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun uploadStory(file: File, description: String, token: String, lat: Double, lon: Double) =
        repository.uploadStory(file, description, token, lat, lon)

    suspend fun getToken(): String {
        val user = repository.getUser()
        return user.token
    }
}