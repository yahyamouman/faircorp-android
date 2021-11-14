package com.faircorp.model

data class HeaterDto(val id: Long,
                     val name: String,
                     val heaterStatus: HeaterStatus,
                     val power: Long,
                     val roomId: Long)