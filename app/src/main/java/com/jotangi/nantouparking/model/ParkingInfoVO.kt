package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingInfoVO(
    @SerializedName("id")
    val parkingSpaceId: String,

    @SerializedName("parked")
    val isParked: String,

    @SerializedName("parked_time")
    val parkedTime: String
)
