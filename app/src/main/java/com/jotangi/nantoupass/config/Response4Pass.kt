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


data class ApiPassResp4Agency(
    var status : String="",
    var code   : String="",
    var responseMessage: String="",
    var data   : Response4PassAgency? = null
) {
    fun clear() {
        status = ""
        code = ""
        responseMessage = ""
    }
}
data class Response4PassAgency (
    @SerializedName("data")
    var data: List<Response4PassAgencyContent>? = null
)
data class Response4PassAgencyContent (
    @SerializedName("nid")
    var nid: String?,
    @SerializedName("sid")
    var sid: String?,
    @SerializedName("create_date")
    var create_date: String?,
    @SerializedName("modify_date")
    var modify_date: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("group_imgurl")
    var group_imgurl: String?,
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


data class ApiPassResp4AgencyUnit(
    var status : String="",
    var code   : String="",
    var responseMessage: String="",
    var data   : Response4PassAgencyUnit? = null
) {
    fun clear() {
        status = ""
        code = ""
        responseMessage = ""
    }
}
data class Response4PassAgencyUnit (
    @SerializedName("data")
    var data: List<Response4PassAgencyUnitContent>? = null
)
data class Response4PassAgencyUnitContent (
    @SerializedName("nid")
    var nid: String?,
    @SerializedName("sid")
    var sid: String?,
    @SerializedName("create_date")
    var create_date: String?,
    @SerializedName("modify_date")
    var modify_date: String?,
    @SerializedName("sort")
    var sort: String?,
    @SerializedName("agency_sid")
    var agency_sid: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("services")
    var services: List<Response4PassAgencyUnitServices>?,
    @SerializedName("tel")
    var tel: String?,
    @SerializedName("web_url")
    var web_url: String?,
    @SerializedName("avalible")
    var avalible: String?,
    @SerializedName("edit_sid")
    var edit_sid: String?,
    @SerializedName("script")
    var script: String?,
    @SerializedName("remark")
    var remark: String?
)
data class Response4PassAgencyUnitServices (
    var name: String?,
    @SerializedName("service")
    var service: String?,
    @SerializedName("contact")
    var contact: String?
)


data class ApiPassResp4Sightseeing(
    var status : String="",
    var code   : String="",
    var responseMessage: String="",
    var data   : Response4PassSightseeing? = null
) {
    fun clear() {
        status = ""
        code = ""
        responseMessage = ""
    }
}
data class Response4PassSightseeing (
    @SerializedName("data")
    var data: List<Response4PassSightseeingContent>? = null
)
data class Response4PassSightseeingContent (
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
    @SerializedName("shop_open")
    var shop_open: String?,
    @SerializedName("head_img")
    var head_img: String?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("web_url")
    var web_url: String?,
    @SerializedName("ar_url")
    var ar_url: String?,
    @SerializedName("address")
    var address: String?,
    @SerializedName("tel")
    var tel: String?,
    @SerializedName("avalible")
    var avalible: String?,
    @SerializedName("edit_sid")
    var edit_sid: String?,
    @SerializedName("script")
    var script: String?,
    @SerializedName("remark")
    var remark: String?
)