package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingRoadFeeUnPaidResponse(
    @SerializedName("data")
    val unPaidItems: List<ParkingRoadFeeUnPaidVO>,

    @SerializedName("responseMessage")
    val responseMessage: String

)