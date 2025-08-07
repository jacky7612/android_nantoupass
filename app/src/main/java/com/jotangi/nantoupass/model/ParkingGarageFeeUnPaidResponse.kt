package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class ParkingGarageFeeUnPaidResponse(
    @SerializedName("data")
    val unPaidItems: List<ParkingGarageFeeUnPaidVO>,

    @SerializedName("responseMessage")
    val responseMessage: String

)