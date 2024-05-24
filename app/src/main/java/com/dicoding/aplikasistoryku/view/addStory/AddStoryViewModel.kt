package com.dicoding.aplikasistoryku.view.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun uploadStory(file: File, description: String, token: String) = repository.uploadStory(file, description, token)

    suspend fun getToken(): String {
        val user = repository.getUser()
        return user.token
    }
}