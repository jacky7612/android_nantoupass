package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class ParkingInfoBuildingVO(
    @SerializedName("road")
    val road: String,

    @SerializedName("cellCount")
    val cellCount: String,

    @SerializedName("emptyCount")
    val emptyCount: String
)
