package com.faircorp.model

data class RoomDto(val roomId: Long,
                   val roomName: String,
                   val floor: Int,
                   val buildingId: Long,
                   val currentTemperature: Double?,
                   val targetTemperature: Double?)