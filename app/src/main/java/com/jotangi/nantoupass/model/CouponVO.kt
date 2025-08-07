package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class CouponVO(
    @SerializedName("coupon_id")
    val couponId: String,

    @SerializedName("coupon_no")
    val couponNo: String,

    @SerializedName("coupon_name")
    val couponName: String,

    @SerializedName("coupon_description")
    val couponDescription: String,

    @SerializedName("coupon_enddate")
    val couponEndDate: String,

    @SerializedName("using_flag")
    val usingFlag: String,

    @SerializedName("coupon_picture")
    val couponPicture: String
)
