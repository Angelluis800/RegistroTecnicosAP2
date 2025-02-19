package edu.ucne.registrotecnicos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticuloDto(
    @SerializedName("itemId") val itemId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("cost") val cost: Double,
    @SerializedName("revenue") val revenue: Double,
    @SerializedName("price") val price: Double
)
