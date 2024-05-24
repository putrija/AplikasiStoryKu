package com.dicoding.aplikasistoryku.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasistoryku.data.repository.UserRepository

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val tokenLiveData = MutableLiveData<String>()

    suspend fun getToken(): LiveData<String> {
        val user = repository.getUser()
        tokenLiveData.value = user.token
        return tokenLiveData
    }
}