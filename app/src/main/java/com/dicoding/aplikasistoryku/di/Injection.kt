package com.dicoding.aplikasistoryku.di

import android.content.Context
import com.dicoding.aplikasistoryku.data.api.ApiConfig
import com.dicoding.aplikasistoryku.data.pref.UserPreference
import com.dicoding.aplikasistoryku.data.pref.dataStore
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
    val pref = UserPreference.getInstance(context.dataStore)
    val apiService = ApiConfig.getApiService()
    return UserRepository.getInstance(apiService, pref)
}
}