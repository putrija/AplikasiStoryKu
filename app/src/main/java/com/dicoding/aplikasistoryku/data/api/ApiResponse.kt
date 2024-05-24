package com.dicoding.aplikasistoryku.data.api

sealed class ApiResponse<out R> private constructor() {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}