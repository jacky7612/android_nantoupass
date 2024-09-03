package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingInfoResponse(
    @SerializedName("data")
    val parkingData: List<ParkingInfoVO>
)
