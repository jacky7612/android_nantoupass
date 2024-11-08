package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class MemberInfoVO(
//    @SerializedName("mid")
//    val mid: String,
//
    @SerializedName("member_id")
    val memberId: String,

    @SerializedName("member_pwd")
    val memberPassword: String,

    @SerializedName("member_name")
    val memberName: String,

    @SerializedName("member_type")
    val memberType: String,
//
//    @SerializedName("member_gender")
//    val code: String,

    @SerializedName("member_email")
    val memberEmail: String,

//    @SerializedName("member_birthday")
//    val code: String,

//    @SerializedName("member_address")
//    val memberAddress: String,

    @SerializedName("member_plateNo")
    val memberPlate: String?,

//    @SerializedName("member_phone")
//    val code: String,
//
//    @SerializedName("member_picture")
//    val code: String,
//
//    @SerializedName("member_totalpoints")
//    val code: String,
//
//    @SerializedName("member_usingpoints")
//    val code: String,
//
    @SerializedName("member_status")
    val memberStatus: String

//    @SerializedName("recommend_code")
//    val code: String,
//
//    @SerializedName("member_sid")
//    val responseMessage: String
)