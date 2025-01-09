package com.jotangi.nantouparking.map

data class ParkStatusResponse(
    val status: String,
    val responseMessage: String,
    val data: List<ParkingLot>
)

data class ParkingLot(
    val road: String,
    val address: String,
    val cellCount: Int,
    val emptyCount: Int,
    val lat: String,
    val lng: String,
    val update_time: String
)
