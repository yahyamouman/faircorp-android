package com.faircorp.Service

import com.faircorp.model.RoomDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomApiService {
        @GET("room")
        fun findAll(): Call<List<RoomDto>>

        @GET("room/{id}")
        fun findById(@Path("id") id: Long): Call<RoomDto>
}