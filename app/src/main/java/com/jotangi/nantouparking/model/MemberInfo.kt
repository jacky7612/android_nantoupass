package com.jotangi.nantouparking.model

data class MemberInfo(
    val mid: String,
    val member_id: String,
    val member_pwd: String,
    val member_name: String,
    val member_type: String,
    val member_gender: String,
    val member_email: String,
    val member_birthday: String?,
    val member_address: String,
    val member_plateNo: String,
    val member_carrier: String?,
    val member_phone: String,
    val member_picture: String?,
    val member_totalpoints: String,
    val member_status: String,
    val recommend_code: String,
    val member_sid: String
)

data class ErrorResponse(
    val status: String,
    val code: String,
    val responseMessage: String
)
