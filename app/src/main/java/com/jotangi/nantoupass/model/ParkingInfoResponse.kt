package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class ParkingInfoResponse(
    @SerializedName("data")
    val parkingData: List<ParkingInfoVO>
)
