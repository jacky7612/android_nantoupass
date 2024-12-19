package com.jotangi.nantouparking.model

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

