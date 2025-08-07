package com.jotangi.nantoupass.ui.main

import com.google.gson.annotations.SerializedName
import com.jotangi.nantoupass.model.ParkingFeePaidVO

data class ParkingFeePaidResponse(
    val status: String,               // Indicates success or failure ("true" or "false")
    val code: String,                 // API-specific code for the result
    val responseMessage: List<ParkingFeePaidVO>? // List of ParkingFeePaidVO (can be empty)
)

data class ParkingFeePaidVO(
    @SerializedName("bill_no")
    val bill_no: String?,             // Bill number

    @SerializedName("bill_updated_at")
    val bill_updated_at: String?,     // Updated timestamp

    @SerializedName("bill_amount")
    val bill_amount: String?,         // Bill amount

    @SerializedName("bill_pay")
    val bill_pay: String?,            // Amount paid

    @SerializedName("plateNo")
    val plateNo: String?,             // Plate number

    @SerializedName("pay_type")
    val pay_type: String?,            // Payment type

    @SerializedName("pay_status")
    val pay_status: String?,          // Payment status

    @SerializedName("bill_status")
    val bill_status: String?,         // Bill status

    @SerializedName("plateId")
    val plateId: String?              // Plate ID
)

