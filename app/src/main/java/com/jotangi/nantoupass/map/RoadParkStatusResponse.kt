package com.jotangi.nantoupass.map

data class RoadParkStatusResponse(
    val data: List<RoadParkStatusItem>
)

data class RoadParkStatusItem(
    val BillSegmentName: String,
    val ParkingSpaceCode: String,
    val Longitude: String,
    val Latitude: String,
    val Status: String,
    val update_time: String
)
