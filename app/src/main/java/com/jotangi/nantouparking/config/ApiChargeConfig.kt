package com.jotangi.nantouparking.config

import com.jotangi.nantouparking.model.*
import com.jotangi.nantouparking.model.charge.ChargeHistoryResponse
import com.jotangi.nantouparking.model.charge.ChargingDataResponse
import com.jotangi.nantouparking.model.charge.Gun4QRcodeResponse
import com.jotangi.nantouparking.model.charge.NearStationResponse
import com.jotangi.nantouparking.model.charge.SetPowerOffResponse
import com.jotangi.nantouparking.model.charge.SetPowerOnResponse
import com.jotangi.nantouparking.model.charge.StandardResponse
import com.jotangi.nantouparking.model.charge.chkChargeResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiChargeConfig {
    companion object {
        // 正式
        var URL_HOST = "https://hcparking.jotangi.net/parking/"

        const val TEST_URL_HOST = "https://hcparking.jotangi.net/parking_test/"
        const val REN_AI_URL_HOST = "http://103.124.73.151/parkingman/"
        const val API_CODE_SUCCESS = "0x0200"
        const val API_CODE_0x0201 = "0x0201"
        const val API_CODE_0x0202 = "0x0202"
        const val API_CODE_0x0203 = "0x0203"
        const val API_CODE_0x0204 = "0x0204"
        const val API_CODE_0x0206 = "0x0206"
        const val API_CODE_NOT_FOUND = "404"
        var PAYMENT_URL = URL_HOST + "pay_charge.php"

    }

    // 充電、繳費狀態(餘額 >= 50)
    @POST("api/JTG_check_charge.php")
    @FormUrlEncoded
    fun apiCheckCharge(
        @Field("member_id"  ) memberId: String,
        @Field("member_pwd" ) memberPwd: String
    ): Call<chkChargeResponse>

    // 搜尋附近充電站點
    @POST("api/JTG_near_station.php")
    @FormUrlEncoded
    fun apiGetNearStation(
        @Field("member_id"      ) member_id: String,
        @Field("member_pwd"     ) member_pwd: String,
        @Field("count_one_time" ) count_one_time: String,
        @Field("skip_docs"      ) skip_docs: String,
        @Field("longitude"      ) longitude: String,
        @Field("latitude"       ) latitude: String,
        @Field("query"          ) query: String
    ): Call<NearStationResponse>

    // 取得使用者歷史充電資訊
    @POST("api/JTG_get_history4user.php")
    @FormUrlEncoded
    fun apiGetHistory4UserList(
        @Field("member_id"      ) memberId      : String,
        @Field("member_pwd"     ) memberPwd     : String,
        @Field("count_one_time" ) count_one_time: String,
        @Field("skip_docs"      ) skip_docs     : String,
        @Field("start_date"     ) start_date    : String,
        @Field("end_date"       ) end_date      : String,
        @Field("select_all"     ) select_all    : String
    ): Call<ChargeHistoryResponse>

    // 傳送掃描充電槍 QRcode
    @POST("api/JTG_trans_qrcode.php")
    @FormUrlEncoded
    fun apiTransQRcode(
        @Field("member_id"  ) memberId: String,
        @Field("member_pwd" ) memberPwd: String,
        @Field("id"         ) gun_dev_id: String,
        @Field("gun_id"     ) gun_number: String
    ): Call<Gun4QRcodeResponse>

    // 啟動充電
    @POST("api/JTG_set_charge.php")
    @FormUrlEncoded
    fun apiSetChargePowerON(
        @Field("member_id"      ) memberId      : String,
        @Field("member_pwd"     ) memberPwd     : String,
        @Field("charge_point_id") gun_device_id : String, // 充電槍id
        @Field("gun_id"         ) gun_number    : String, // 充電槍編號
        @Field("control"        ) control       : String, // 0 :停止; 1 :啟動
        @Field("pre_control"    ) pre_control   : String  // 0 :取消預約; 1 :預約
    ): Call<SetPowerOnResponse>

    // 停止充電
    @POST("api/JTG_set_charge.php")
    @FormUrlEncoded
    fun apiSetChargePowerOFF(
        @Field("member_id"      ) memberId      : String,
        @Field("member_pwd"     ) memberPwd     : String,
        @Field("charge_point_id") gun_device_id : String, // 充電槍id
        @Field("gun_id"         ) gun_number    : String, // 充電槍編號
        @Field("control"        ) control       : String, // 0 :停止; 1 :啟動
        @Field("pre_control"    ) pre_control   : String  // 0 :取消預約; 1 :預約
    ): Call<SetPowerOffResponse>

    // 取得使用者目前充電資訊
    @POST("api/JTG_get_charge4user.php")
    @FormUrlEncoded
    fun apiGetChargingData4User(
        @Field("member_id"      ) memberId      : String,
        @Field("member_pwd"     ) memberPwd     : String
    ): Call<ChargingDataResponse>

    // 判斷充電狀態
    @POST("api/JTG_get_charge4user.php")
    @FormUrlEncoded
    fun apiCheckChargeStatus(
        @Field("member_id"      ) memberId      : String,
        @Field("member_pwd"     ) memberPwd     : String,
        @Field("control_id"     ) control_id    : String
    ): Call<StandardResponse>

    // 取得所選充電站內所有充電槍
    @POST("api/JTG_get_charge.php")
    @FormUrlEncoded
    fun apiGetChargeList(
        @Field("member_id"  ) memberId: String,
        @Field("member_pwd" ) memberPwd: String,
        @Field("id"         ) station_id: String
    ): Call<StandardResponse>
}