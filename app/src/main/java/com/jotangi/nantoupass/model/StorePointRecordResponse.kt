package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class StorePointRecordResponse(
    val status: String,
    val code: String,
    val responseMessage: String,
    val data: List<PointRecordResponse>?
)

data class PointRecordResponse(
    val point_created_at: String,
    val member_id: String,
    val point_use: String,
    val product_price: String
)

data class MgrPointRecordsResponse(

    @SerializedName("point_created_at")
    val pointCreatedAt: String,

    @SerializedName("member_id")
    val member_id: String,

    @SerializedName("point_use")
    val point_use: String,

    @SerializedName("product_price")
    val product_price: String
)

