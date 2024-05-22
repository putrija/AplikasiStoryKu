package com.dicoding.aplikasistoryku.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)