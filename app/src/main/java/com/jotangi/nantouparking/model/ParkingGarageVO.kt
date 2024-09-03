package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingGarageVO(
    @SerializedName("pid")
    val parkingGarageId: String,

    @SerializedName("name")
    val parkingGarageName: String,

    @SerializedName("address")
    val parkingGarageAddress: String
)
