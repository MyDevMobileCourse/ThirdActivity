package com.example.thirdactivity.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("data")
    fun addUser(@Body userData: UserInfo): Call<UserInfo>
    @GET("data")
    fun getUsers():Call<List<UserInfo>>
}