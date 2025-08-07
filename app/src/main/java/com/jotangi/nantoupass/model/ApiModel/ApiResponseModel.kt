package com.jotangi.payStation.Model.ApiModel

import android.R.string
import com.google.gson.annotations.SerializedName


data class ApiRespStandard(
    var status : String="",
    var code   : String="",
    var responseMessage: String="") {
    fun clear() {
        status = ""
        code = ""
        responseMessage = ""
    }
}
data class ApiRespErr(
    val status : String="",
    val code   : String="",
    val responseMessage: String=""
)
    data class ApiRespStoreListOK(
        var data: MutableList<DataStoreInfo>?=null
    ) {
        fun clear() {
            if (data != null) {
                data!!.clear()
            }
        }
    }

    data class ApiRespStoreListStatusOK(
        var status          : String="",
        var code            : String="",
        var data            : MutableList<DataStoreInfo>?=null,
        var responseMessage : String=""
    ) {
        fun clear() {
            status                  =""
            code                    =""
            responseMessage         =""
            if (data != null) {
                data!!.clear()
            }
        }
    }
    data class DataStoreInfo(
        var store_id            : String="",
        var store_name          : String="",
        var store_phone         : String="",
        var store_website       : String="",
        var store_open          : String="",
        var store_description   : String="",
        var store_open_time     : String="",
        var store_picture       : String="",
        var store_address       : String="",
        var store_latitude      : String="",
        var store_longitude     : String=""
    )
    data class DataStoreInfo4SimpleShow(
        var store_id            : String="",
        var store_name          : String="",
        var store_description   : String="",
        var store_picture       : String=""
    )


    data class ApiRespGovPlateListOK(
        var data            : MutableList<DataGovParkingFeePaidVO>?=null,
        var status          : String="",
        var responseMessage : String=""
    ) {
        fun clear() {
            status                  =""
            responseMessage         =""
            if (data != null) {
                data!!.clear()
            }
        }
    }
    data class DataGovParkingFeePaidVO(
        val ticket: String,
        val area: String,
        val parkDate: String,
        val payAmount: String,
        val payDate: String,
        val paySource: String
    )
data class TicketResponse(
    val data: List<DataGovParkingFeePaidVO>
)