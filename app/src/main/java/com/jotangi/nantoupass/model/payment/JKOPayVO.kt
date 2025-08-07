package com.jotangi.nantoupass.model.payment

import com.google.gson.annotations.SerializedName

data class JKOPayVO(
    @SerializedName("StoreOrderNo")
    val storeOrderNo: String,

    @SerializedName("Amount")
    val amount: Int,

    @SerializedName("CurrencyCode")
    val currencyCode: String,

    @SerializedName("PaymentType")
    val paymentType: String,

    @SerializedName("OrderDesc")
    val orderDesc: String,

    @SerializedName("products")
    val products: List<AllPayProduct>,

    @SerializedName("ReturnURL")
    val returnURL: String,

    @SerializedName("DeviceOS")
    val deviceOS: String,

    @SerializedName("AuthType")
    val authType: String,

    @SerializedName("ApiKey")
    val apiKey: String,

    @SerializedName("SecretKey")
    val secretKey: String,

    @SerializedName("StoreId")
    val storeId: String,

    @SerializedName("UnRedeem")
    val unRedeem: Int


)

