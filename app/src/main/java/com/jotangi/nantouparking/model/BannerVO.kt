package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class BannerVO(
    @SerializedName("bid")
    val bid: String,

    @SerializedName("banner_subject")
    val bannerSubject: String,

    @SerializedName("banner_date")
    val bannerDate: String,

    @SerializedName("banner_enddate")
    val bannerEndDate: String,

    @SerializedName("banner_descript")
    val bannerDescription: String,

    @SerializedName("banner_picture")
    val bannerPic: String,

    @SerializedName("banner_link")
    val bannerLink: String
)
