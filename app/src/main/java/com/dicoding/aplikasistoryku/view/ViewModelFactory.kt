package com.dicoding.aplikasistoryku.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.di.Injection
import com.dicoding.aplikasistoryku.view.login.LoginViewModel
import com.dicoding.aplikasistoryku.view.main.MainViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context, userRepository: UserRepository): ViewModelProvider.Factory {
            return ViewModelFactory(userRepository)
        }
    }
}