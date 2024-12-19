package com.jotangi.nantouparking.model

data class UsePointResponse(
    val status: String,           // "true" or "false"
    val code: String,             // e.g., "0x0200"
    val responseMessage: String   // Message describing the response
)