package com.gierre.gierrecontrolloduplicati.dto

import com.google.gson.annotations.SerializedName

data class ResponseExcelRowDto(
    val POD: String,
    val PDR: String,

    @SerializedName("Data creaz.")
    val data_creaz: String
)