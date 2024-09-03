package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingInfoBuildingVO(
    @SerializedName("road")
    val road: String,

    @SerializedName("cellCount")
    val cellCount: String,

    @SerializedName("emptyCount")
    val emptyCount: String
)
