package com.faircorp.Service

import com.faircorp.model.WindowDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface WindowApiService {
    @GET("window")
    fun findAll(): Call<List<WindowDto>>

    @GET("window/{id}")
    fun findById(@Path("id") id: Long): Call<WindowDto>

    @PUT("window/{id}/switch")
    fun switchStatus(@Path("id") id: Long): Call<WindowDto>
}