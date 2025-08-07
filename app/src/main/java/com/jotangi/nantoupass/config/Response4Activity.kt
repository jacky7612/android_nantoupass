package com.jotangi.nantoupass.config

import com.google.gson.annotations.SerializedName

data class Response4Activity (
    @SerializedName("aid")
    val aid: String,

    @SerializedName("activity_picture1")
    val activity_picture1: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("body_picture1")
    val body_picture1: String,

    @SerializedName("body_picture2")
    val body_picture2: String,

    @SerializedName("body_picture3")
    val body_picture3: String,

    @SerializedName("link_text")
    val link_text: String,

    @SerializedName("link_url")
    val link_url: String,

    @SerializedName("activity_start_date")
    val activity_start_date: String,

    @SerializedName("activity_end_date")
    val activity_end_date: String,

    @SerializedName("activity_created_at")
    val activity_created_at: String,

    @SerializedName("activity_updated_at")
    val activity_updated_at: String,

    @SerializedName("activity_trash")
    val activity_trash: String
)