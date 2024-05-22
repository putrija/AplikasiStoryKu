package com.dicoding.aplikasistoryku.di

import android.content.Context
import com.dicoding.aplikasistoryku.data.api.ApiConfig
import com.dicoding.aplikasistoryku.data.api.ApiService
import com.dicoding.aplikasistoryku.data.pref.UserPreference
import com.dicoding.aplikasistoryku.data.pref.dataStore
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context, apiService: ApiService): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref, apiService) // Meneruskan apiService saat membuat instance UserRepository
    }
}