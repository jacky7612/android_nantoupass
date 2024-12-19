package com.jotangi.nantouparking.model

data class GetPointResponse(
    val status: String,           // "true" or "false"
    val code: String,             // e.g., "0x0205"
    val responseMessage: String   // Message describing the response
)