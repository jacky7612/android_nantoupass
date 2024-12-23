package com.jotangi.nantouparking.model

data class AllParkStatusResponse(
    val status: String,
    val responseMessage: String,
    val data: List<ParkStatus>
)

data class ParkStatus(
    val road: String,
    val address: String,
    val cellCount: Int,
    val emptyCount: Int
)
