package com.jotangi.nantouparking.model.charge

import com.google.gson.annotations.SerializedName


class TmpStandardResponse()
{
    var status: String =""
    var code: String =""
    var responseMessage =""
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
    }
}

data class StandardResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("code")
    var code: String,

    @SerializedName("responseMessage")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
    }
}

data class chkChargeResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("code")
    var code: String,

    @SerializedName("responseMessage")
    var responseMessage: String,

    @SerializedName("charge_id")
    var charge_id: String,

    @SerializedName("gun_no")
    var gun_no: String,
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
        charge_id       =""
        gun_no          =""
    }
}

// 搜尋附近充電站點
data class NearStationResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("data")
    var data: List<DataStation>?,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
        data            =listOf()
    }
}
data class DataStation(
    @SerializedName("LatLng")
    val latLng: List<Double>, // Consider changing to latLng for readability

    @SerializedName("address")
    val address: String,

    @SerializedName("distance")
    val distance: String,

    @SerializedName("charger_status_info")
    val charger_status_info: List<DataChargeStatusInfo>,

    @SerializedName("marker_id")
    val station_id: String, // Use camelCase for consistency

    @SerializedName("name")
    val station_name: String, // Use camelCase for consistency

    @SerializedName("open")
    val open_status: String, // Use camelCase for consistency

    @SerializedName("type")
    val AC_DC_type: String // Use camelCase for consistency
)
data class DataChargeStatusInfo(
    @SerializedName("type")
    val type: String, // Consider changing to latLng for readability

    @SerializedName("detail")
    val detail: DataChargeStatusInfo_Detail,
)
data class DataChgStatusArray(
    @SerializedName("status")
    val status: String, // "0":關機/新設定; "1":待機; "2":充電中; "3":預約中; "4":離線

    @SerializedName("count")
    val count: String // 數量
)
data class DataChargeStatusInfo_Detail(
    @SerializedName("name")
    val name: String, // Consider changing to latLng for readability

    @SerializedName("status_array")
    val status_array: List<DataChgStatusArray>, // "0":關機/新設定; "1":待機; "2":充電中; "3":預約中; "4":離線

    @SerializedName("gun_count")
    val gun_count: String, // 總槍數量

    @SerializedName("avalible_count")
    val avalible_count: String // 可用數量
)
// 取得使用者歷史充電資訊
data class ChargeHistoryResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("data")
    var data: List<DataChargeHistory>?,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
        data            =listOf()
    }
}
data class DataChargeHistory(
    @SerializedName("charge_point_id")      // 充電樁id
    val charge_point_id: String,

    @SerializedName("charge_time")          // 充電時間
    val charge_time: String,

    @SerializedName("charger_name")         // 充電樁名稱
    val charge_name: String,

    @SerializedName("gun_name")             // 槍位名稱
    val gun_name: String,

    @SerializedName("header_type")          // 槍頭名稱
    val header_type: String,

    @SerializedName("kwh")                  // 度電
    val kwh: String,

    @SerializedName("month")                // 月份
    val month: String,

    @SerializedName("order_id")             // 訂單id
    val order_id: String,

    @SerializedName("order_serial_number")  // 訂單序號
    val order_serial_number: String,

    @SerializedName("pay_status")           // 付款狀態
    val pay_status: String,

    @SerializedName("price")                // 價格
    val price: String,

    @SerializedName("start_soc")            // start_soc
    val start_soc: String,

    @SerializedName("stop_soc")             // stop_soc
    val stop_soc: String,

    @SerializedName("start_time")           // 開始時間
    val start_time: String,

    @SerializedName("stop_time")            // 結束時間
    val stop_time: String,

    @SerializedName("station_id")           // 站點id
    val station_id: String,

    @SerializedName("station_name")         // 站點名稱
    val station_name: String,

    @SerializedName("stop_reason")          // 結束原因
    val stop_reason: String,

    @SerializedName("charge_history")       // 充電槍用電類型
    val charge_history: List<SubChargeHistory>
)
data class SubChargeHistory(
    @SerializedName("start_time")   // 開始時間
    val start_time  : String,

    @SerializedName("end_time")     // 結束時間
    val end_time    : String,

    @SerializedName("fee")          // 費用
    val fee         : String,

    @SerializedName("kwh")          // 度電
    val station_id  : String,

    @SerializedName("price")        // 價格
    val price       : String
)

// 傳送掃描充電槍 QRcode
data class Gun4QRcodeResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("data")
    var data: Data4Gun?,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
        data?.clear()
    }
}
data class Data4Gun(
    @SerializedName("ID")                   // 充電槍id
    var ID: String,

    @SerializedName("address")              // 地址
    var address: String,

    @SerializedName("charger_name")         // 充電槍名稱
    var charge_name: String,

//    @SerializedName("gun_name")             // 槍位名稱
//    var gun_name: String,

    @SerializedName("header_type")          // 槍頭型態
    var header_type: String,

    @SerializedName("header_type_name")     // 槍頭型態名稱
    var header_type_name: String,

    @SerializedName("kw")                   // 電度
    var kw: String,

    @SerializedName("order_id")                 // 訂單序號
    var order_id: String,

    @SerializedName("port")                 // 槍埠
    var gun_number: Int,

    @SerializedName("pre_control")              // 訂單序號
    var pre_control: Boolean,

    @SerializedName("pre_control_user")         // 訂單序號
    var pre_control_user: Boolean,

    @SerializedName("price")                // 價格
    var price: String,

    @SerializedName("station_name")         // 站點名稱
    var station_name: String,

    @SerializedName("status")               // 可用狀態
    var avalible: String,

    @SerializedName("type")                 // 電流型態
    var type: String
) {
    fun clear() {
        ID                  =""
        address             =""
        charge_name         =""
        header_type         =""
        header_type_name    =""
        kw                  =""
        gun_number          =1

        order_id            =""
        pre_control         =false
        pre_control_user    =false
        price               =""
        station_name        =""
        avalible            =""
        type                =""
    }
}

// 啟動與停止充電槍
data class SetPowerOnResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("data")
    var data: Power4Gun?,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
        data?.clear()
    }
}
data class Power4Gun(
    @SerializedName("ID"  )     // 控制(訂單)編號
    var ID: String,

    @SerializedName("code")     // 地址
    var code: String
) {
    fun clear() {
        ID      =""
        code    =""
    }
}

// 停止充電槍
data class SetPowerOffResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
    }
}


// 取得使用者目前充電資訊
data class ChargingDataResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("data")
    var data: List<ChargingData>,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
        data =listOf()
    }
}
data class ChargingData(
    @SerializedName("ID"                 )   // 控制(訂單)編號
    var ID: String,

    @SerializedName("card_number"        )  // 卡號
    var card_number: String,

    @SerializedName("charge_point_id"    )  // 充電槍id
    var charge_point_id: String,

    @SerializedName("charge_start_time"  )  // 充電開始時間
    var charge_start_time: String,

    @SerializedName("charge_time"        )  // 充電時間
    var charge_time: String,

    @SerializedName("charge_name"        )  // 充電名稱
    var charge_name: String,

    @SerializedName("gun"                )  // 充電槍編號
    var gun_number: String,

    @SerializedName("gun_header"         )  // 充電槍頭接口
    var gun_header: String,

    @SerializedName("gun_name"           )  // 充電槍名稱
    var gun_name: String,

    @SerializedName("kwh_total"          )  // 總電度
    var kwh_total: String,

    @SerializedName("now_kw"             )  // 目前度數
    var now_kw: String,

    @SerializedName("now_price"          )  // 目前金額
    var now_price: String,

    @SerializedName("now_soc"            )  // now_soc
    var now_soc: String,

    @SerializedName("order_serial_number")  // 訂單號碼
    var order_no: String,

    @SerializedName("station_name"       )  // now_soc
    var station_name: String,

    @SerializedName("total_price"        )  // now_soc
    var total_price: String
) {
    fun clear() {
        ID                  =""
        card_number         =""
        charge_point_id     =""
        charge_start_time   =""
        charge_time         =""
        charge_name         =""
        gun_number          =""
        gun_header          =""
        gun_name            =""
        kwh_total           =""
        now_kw              =""
        now_price           =""
        now_soc             =""
        order_no            =""
        station_name        =""
        total_price         =""
    }
}

// 判斷充電狀態
data class CheckChargeResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("status_code")
    var code: String,

    @SerializedName("status_message")
    var responseMessage: String
) {
    fun clear() {
        status          =""
        code            =""
        responseMessage =""
    }
}