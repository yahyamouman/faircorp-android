package com.faircorp.Service

import com.faircorp.model.DoorDto
import com.faircorp.model.WindowDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface DoorApiService {
    @GET("door")
    fun findAll(): Call<List<DoorDto>>

    @GET("door/{id}")
    fun findById(@Path("id") id: Long): Call<DoorDto>

    @PUT("door/{id}/switch")
    fun switchStatus(@Path("id") id: Long): Call<DoorDto>

    @PUT("door/{id}/lock")
    fun lock(@Path("id") id: Long): Call<DoorDto>
}