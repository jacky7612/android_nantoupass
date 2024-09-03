package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class AttractionVO(
    @SerializedName("sid")
    val sid: String,

    @SerializedName("store_id")
    val storeId: String,

    @SerializedName("store_type")
    val storeType: String,

    @SerializedName("store_name")
    val storeName: String,

    @SerializedName("shopping_area")
    val shoppingArea: String,

    @SerializedName("store_descript")
    val storeDescript: String,

    @SerializedName("store_address")
    val storeAddress: String,

    @SerializedName("store_latitude")
    val storeLatitude: String,

    @SerializedName("store_longitude")
    val storeLongitude: String,

    @SerializedName("store_phone")
    val storePhone: String,

    @SerializedName("store_picture")
    val storePicture: String,
)
