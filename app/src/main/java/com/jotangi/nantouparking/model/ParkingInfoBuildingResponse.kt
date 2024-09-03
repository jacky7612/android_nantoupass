package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingInfoBuildingResponse(
    @SerializedName("data")
    val parkingData: List<ParkingInfoBuildingVO>
)
