package com.faircorp.model

data class DoorDto(val id: Long,
                     val name: String,
                     val doorStatus: DoorStatus,
                     val roomName: String,
                     val roomId: Long)
