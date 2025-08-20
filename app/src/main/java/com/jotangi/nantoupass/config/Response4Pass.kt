package com.jotangi.nantoupass.config

import com.google.gson.annotations.SerializedName

data class ApiPassResp4Banner(
    var status : String="",
    var code   : String="",
    var responseMessage: String="",
    var data   : Response4PassBanner? = null
) {
    fun clear() {
        status = ""
        code = ""
        responseMessage = ""
    }
}
data class Response4PassBanner (
    @SerializedName("data")
    var data: List<Response4PassBannerContent>? = null
)
data class Response4PassBannerContent (
    @SerializedName("nid")
    var nid: String?,
    @SerializedName("sid")
    var sid: String?,
    @SerializedName("create_date")
    var create_date: String?,
    @SerializedName("modify_date")
    var modify_date: String?,
    @SerializedName("start_date")
    var start_date: String?,
    @SerializedName("end_date")
    var end_date: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("head_img")
    var head_img: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("avarible")
    var avarible: String?,
    @SerializedName("edit_sid")
    var edit_sid: String?,
    @SerializedName("script")
    var script: String?,
    @SerializedName("remark")
    var remark: String?
)


data class ApiPassResp4News(
    var status : String="",
    var code   : String="",
    var responseMessage: String="",
    var data   : Response4PassNews? = null
) {
    fun clear() {
        status = ""
        code = ""
        responseMessage = ""
    }
}
data class Response4PassNews (
    @SerializedName("data")
    var data: List<Response4PassNewsContent>? = null
)
data class Response4PassNewsContent (
    @SerializedName("nid")
    var nid: String?,
    @SerializedName("sid")
    var sid: String?,
    @SerializedName("create_date")
    var create_date: String?,
    @SerializedName("modify_date")
    var modify_date: String?,
    @SerializedName("new_kind")
    var new_kind: String?,
    @SerializedName("store_sid")
    var store_sid: String?,
    @SerializedName("start_date")
    var start_date: String?,
    @SerializedName("end_date")
    var end_date: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("head_img")
    var head_img: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("avalible")
    var avalible: String?,
    @SerializedName("edit_sid")
    var edit_sid: String?,
    @SerializedName("script")
    var script: String?,
    @SerializedName("remark")
    var remark: String?
)