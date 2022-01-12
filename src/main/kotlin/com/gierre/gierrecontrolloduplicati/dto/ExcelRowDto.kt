package com.gierre.gierrecontrolloduplicati.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExcelRowDto(

    @SerialName(value = "POD")
    val pod: String,

    @SerialName(value = "PDR")
    val pdr: String

) {}