package com.faircorp.model

data class WindowDto(val id: Long,
                     val name: String,
                     val windowStatus: Status,
                     val roomName: String,
                     val roomId: Long)