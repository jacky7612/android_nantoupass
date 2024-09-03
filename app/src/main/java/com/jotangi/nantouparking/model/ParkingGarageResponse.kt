package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingGarageResponse(
    @SerializedName("data")
    val parkingGarageData: List<ParkingGarageVO>
)
