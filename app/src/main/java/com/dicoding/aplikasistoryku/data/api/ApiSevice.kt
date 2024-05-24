package com.dicoding.aplikasistoryku.data.api

import com.dicoding.aplikasistoryku.data.response.DetailStoryResponse
import com.dicoding.aplikasistoryku.data.response.LoginResponse
import com.dicoding.aplikasistoryku.data.response.RegisterResponse
import com.dicoding.aplikasistoryku.data.response.StoryResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
    ): StoryResponse

    @GET("/v1/stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): DetailStoryResponse


}