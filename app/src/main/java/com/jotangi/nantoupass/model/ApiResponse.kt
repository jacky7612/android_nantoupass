package com.jotangi.nantoupass.model

data class ApiResponse<T>(
    val status: String,           // Example: "true"
    val code: String,             // Example: "0x0200"
    val responseMessage: String,  // Example: "success"
    val data: T                   // Example: List<PointRecordResponse>
)

data class PointRecord2Response(
    val point_created_at: String, // Example: "2024-12-19 11:38:17"
    val point: String,            // Example: "-10"
    val point_type: String,       // Example: "0"
    val store_name: String        // Example: "泡泡阿伯"
)