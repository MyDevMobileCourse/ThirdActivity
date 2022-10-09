package com.example.thirdactivity.api

import retrofit2.Call
import retrofit2.http.*

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("data")
    fun addUser(@Body userData: UserInfo): Call<UserInfo>
    @GET("data")
    fun getUsers():Call<List<UserInfo>>
    @DELETE("data/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Boolean>
}