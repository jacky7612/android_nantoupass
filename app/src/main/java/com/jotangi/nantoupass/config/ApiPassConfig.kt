package com.jotangi.nantoupass.config

import com.jotangi.nantoupass.map.ParkStatusResponse
import com.jotangi.nantoupass.map.RoadParkStatusResponse
import com.jotangi.nantoupass.model.charge.ChargeHistoryResponse
import com.jotangi.nantoupass.model.charge.ChargingDataResponse
import com.jotangi.nantoupass.model.charge.Gun4QRcodeResponse
import com.jotangi.nantoupass.model.charge.NearStationResponse
import com.jotangi.nantoupass.model.charge.SetPowerOffResponse
import com.jotangi.nantoupass.model.charge.SetPowerOnResponse
import com.jotangi.nantoupass.model.charge.StandardResponse
import com.jotangi.nantoupass.model.charge.chkChargeResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiPassConfig {
    companion object {
        // 正式
        var URL_HOST = "https://miaoparking.jotangi.net/nantoupass_be/"
        const val API_CODE_SUCCESS = "0x0200"
        const val API_CODE_0x0201 = "0x0201"
        const val API_CODE_0x0202 = "0x0202"
        const val API_CODE_0x0203 = "0x0203"
        const val API_CODE_0x0204 = "0x0204"
        const val API_CODE_0x0206 = "0x0206"
        const val API_CODE_NOT_FOUND = "404"

    }

    // 取得Banner
    @GET("api/JTG_banner.php")
    fun apiGetBanner(): Call<ApiPassResp4Banner>

    // 取得最新消息
    @GET("api/JTG_news.php")
    fun apiGetNews(
        @Query("new_kind") new_kind: String // 不填:全取;0:一般訊息;1:商家訊息
    ): Call<ApiPassResp4News>

    // 取得機構
    @GET("api/JTG_agency.php")
    fun apiGetAgency(): Call<ApiPassResp4Agency>

    // 取得機構各科室單位
    @GET("api/JTG_agencyunit.php")
    fun apiGetAgencyUnit(
        @Query("agency_sid") agency_sid: String
    ): Call<ApiPassResp4AgencyUnit>

    // 取得景點
    @GET("api/JTG_sightseeing.php")
    fun apiGetSightseeing(
    ): Call<ApiPassResp4Sightseeing>
}