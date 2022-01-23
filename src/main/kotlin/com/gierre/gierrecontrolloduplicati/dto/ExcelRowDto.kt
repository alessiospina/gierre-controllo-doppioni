package com.gierre.gierrecontrolloduplicati.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDateTime

@Serializable
data class ExcelRowDto(

    @Transient
    val id: Long? = null,

    @SerialName(value = "POD")
    val pod: String,

    @SerialName(value = "PDR")
    val pdr: String,

    @SerialName(value = "Data creaz.")
    val data_creaz: String,

    @Transient
    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {}