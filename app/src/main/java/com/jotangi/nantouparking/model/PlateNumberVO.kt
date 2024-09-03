package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class PlateNumberVO(
    @SerializedName("plateNo")
    val plateNo: String
)