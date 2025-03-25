package com.jotangi.nantouparking.ui.charge

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.JackyVariant.json_sample
import com.jotangi.nantouparking.map.ParkStatusResponse
import com.jotangi.nantouparking.map.RoadParkStatusResponse
import com.jotangi.nantouparking.model.charge.ChargeHistoryResponse
import com.jotangi.nantouparking.model.charge.ChargingDataResponse
import com.jotangi.nantouparking.model.charge.Gun4QRcodeResponse
import com.jotangi.nantouparking.model.charge.NearStationResponse
import com.jotangi.nantouparking.model.charge.SetPowerOffResponse
import com.jotangi.nantouparking.model.charge.SetPowerOnResponse
import com.jotangi.nantouparking.model.charge.StandardResponse
import com.jotangi.nantouparking.model.charge.chkChargeResponse
import com.jotangi.nantouparking.utility.ApiChargeUtility
import com.jotangi.nantouparking.utility.AppUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChargeViewModel: ViewModel() {
    // 通用 - start
    private lateinit var _std_check: StandardResponse
    // 通用 - end

    // 檢查儲值金金額 - start
    private val _charge_check: MutableLiveData<chkChargeResponse> by lazy {
        MutableLiveData<chkChargeResponse>()
    }
    val chargeCheck: LiveData<chkChargeResponse> get() = _charge_check
    // 檢查儲值金金額 - end

    // 附近站點 - start
    private val _charge_station: MutableLiveData<NearStationResponse> by lazy {
        MutableLiveData<NearStationResponse>()
    }
    val chargeStation: LiveData<NearStationResponse> get() = _charge_station
    // 附近站點 - end

    // 歷史紀錄 - start
    private val _charge_history: MutableLiveData<ChargeHistoryResponse> by lazy {
        MutableLiveData<ChargeHistoryResponse>()
    }
    val chargeHistory: LiveData<ChargeHistoryResponse> get() = _charge_history
    // 附近站點 - end

    // 掃描 QRcode - start
    private val _charge_qrcode: MutableLiveData<Gun4QRcodeResponse> by lazy {
        MutableLiveData<Gun4QRcodeResponse>()
    }
    val chargeQrcode: LiveData<Gun4QRcodeResponse> get() = _charge_qrcode
    // 掃描 QRcode - end

    // 啟動充電槍 - start
    private val _charge_power_on: MutableLiveData<SetPowerOnResponse> by lazy {
        MutableLiveData<SetPowerOnResponse>()
    }
    val chargePowerON: LiveData<SetPowerOnResponse> get() = _charge_power_on
    // 啟動充電槍 - end

    // 停止充電槍 - start
    private val _charge_power_off: MutableLiveData<SetPowerOffResponse> by lazy {
        MutableLiveData<SetPowerOffResponse>()
    }
    val chargePowerOFF: LiveData<SetPowerOffResponse> get() = _charge_power_off
    // 停止充電槍 - end

    // 取得使用者目前充電資訊 - start
    private val _charging_data: MutableLiveData<ChargingDataResponse> by lazy {
        MutableLiveData<ChargingDataResponse>()
    }
    val chargingData: LiveData<ChargingDataResponse> get() = _charging_data
    // 取得使用者目前充電資訊 - end

    // 判斷充電狀態 - start
    private val _check_charge_status: MutableLiveData<StandardResponse> by lazy {
        MutableLiveData<StandardResponse>()
    }
    val checkChargeStatus: LiveData<StandardResponse> get() = _check_charge_status
    // 判斷充電狀態 - end
    private val _roadParkStatus = MutableLiveData<RoadParkStatusResponse>()
    val roadParkStatus: LiveData<RoadParkStatusResponse> get() = _roadParkStatus

    private val _parkStatus = MutableLiveData<ParkStatusResponse>()
    val parkStatus: LiveData<ParkStatusResponse> get() = _parkStatus

    init {
        _std_check =StandardResponse("","","")

        clear()
    }

    fun clear() {
        if (_charge_station.value   != null) {
            _charge_station.value!!.clear()
            _charge_station.postValue(null)
        }
        clearQRcode()
        clearCheckData()
        clearHistoryData()
        clearChargingData()
        clearPowerON()
        clearPowerOFF()
    }

    fun clearQRcode() {
        if (_charge_qrcode.value    != null) {
            _charge_qrcode.value!!.clear()
            _charge_qrcode.postValue(null)
        }
    }

    fun clearCheckData() {
        if (_charge_check.value     != null) {
            _charge_check.value!!.clear()
            _charge_check.postValue(null)
        }
    }

    fun clearHistoryData() {
        if (_charge_history.value   != null) {
            _charge_history.value!!.clear()
            _charge_history.postValue(null)
        }
    }

    fun clearChargingData() {
        if (_charging_data.value   != null) {
            _charging_data.value!!.clear()
            _charging_data.postValue(null)
        }
    }

    fun clearCheckChargeStatus() {
        if (_check_charge_status.value    != null) {
            _check_charge_status.value!!.clear()
            _check_charge_status.postValue(null)
        }
    }

    fun clearPowerON() {
        if (_charge_power_on.value    != null) {
            _charge_power_on.value!!.clear()
            _charge_power_on.postValue(null)
        }
    }

    fun clearPowerOFF() {
        if (_charge_power_off.value    != null) {
            _charge_power_off.value!!.clear()
            _charge_power_off.postValue(null)
        }
    }

    // 充電、繳費狀態(餘額 >= 50)
    fun checkCharge(context: Context,
                       member_id       : String,
                       member_pwd      : String
    ) {
        val call: Call<chkChargeResponse> = ApiChargeUtility.service.apiCheckCharge(
            member_id,
            member_pwd
        )
        call.enqueue(object : Callback<chkChargeResponse> {
            override fun onResponse(
                call: Call<chkChargeResponse>,
                response: Response<chkChargeResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (Glob.CheckChargeSampleResponse) {
                    val gson = Gson()
                    val jsonString = json_sample.checkCharge
                    val js = gson.fromJson(jsonString, chkChargeResponse::class.java)
                    _charge_check.value = js
                    return
                }
                var resp = response.body()
                if (resp != null) {
                    _charge_check.value =resp
//                    _charge_check.value!!.status ="false"
//                    _charge_check.value!!.code   ="0x0201"
//                  _charge_check.value =setResponseMessage(resp.code, resp.status, "檢查儲值金金額成功")
                    _std_check.clear()
                } else {
                    val msg ="檢查儲值金發生異常，請回首頁再次進行操作"
                    assignCheckChargeRespMessage("0x020F", "false", msg)
                    _std_check.clear()
                }
            }

            override fun onFailure(call: Call<chkChargeResponse>, t: Throwable) {
                val msg ="檢查儲值金金額失敗，請回首頁再次進行操作"
                assignCheckChargeRespMessage("0x020E", "false", msg)
                _std_check!!.clear()
            }
        })
    }

    // 搜尋附近充電站點
    fun getNearStation(context: Context,
        member_id       : String,
        member_pwd      : String,
        count_one_time  : String ="10",
        skip_docs       : String ="0",
        longitude       : String ="120.820776399519",
        latitude        : String ="24.5639391939057",
        query           : String =""
    ) {
        val call: Call<NearStationResponse> = ApiChargeUtility.service.apiGetNearStation(
            member_id,
            member_pwd,
            count_one_time,
            skip_docs,
            longitude,
            latitude,
            query
        )
        call.enqueue(object : Callback<NearStationResponse> {
            override fun onResponse(
                call: Call<NearStationResponse>,
                response: Response<NearStationResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    Log.d("micCheclPOP2", "2")
                    var resp = response.body()
                    Log.d("micCheckPOP4", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _charge_station.value = resp
                            assignStationRespMessage("0x0200", "true", "搜尋附近充電站成功")
                        }
                    }
                } else {
                    Log.d("micCheclPOP2", "3")
                    val msg ="搜尋附近充電站發生異常，請回首頁再次進行操作"
                    assignStationRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<NearStationResponse>, t: Throwable) {
                val msg ="搜尋附近充電站失敗，請回首頁再次進行操作"
                assignStationRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }



    // 取得歷史資訊
    fun getHistory(context: Context,
                       member_id       : String,
                       member_pwd      : String,
                       start_date      : String,
                       end_date        : String,
                       select_all      : String ="false",
                       count_one_time  : String ="50",
                       skip_docs       : String ="0"
    ) {
        val call: Call<ChargeHistoryResponse> = ApiChargeUtility.service.apiGetHistory4UserList(
            member_id,
            member_pwd,
            count_one_time,
            skip_docs,
            start_date,
            end_date,
            select_all
        )
        call.enqueue(object : Callback<ChargeHistoryResponse> {
            override fun onResponse(
                call: Call<ChargeHistoryResponse>,
                response: Response<ChargeHistoryResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (Glob.ChargeHistorySampleResponse) {
                    val gson = Gson()
                    val jsonString = json_sample.ChargeHistory4User
                    val js = gson.fromJson(jsonString, ChargeHistoryResponse::class.java)
                    _charge_history.value = js
                    return
                }

                if (response.body() != null) {
                    var resp = response.body()
                    if (resp != null) {
                        _charge_history.value = resp
                    }
                } else {
                    val msg ="查詢充電歷史紀錄發生異常，請回首頁再次進行操作"
                    assignHistoryRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ChargeHistoryResponse>, t: Throwable) {
                val msg ="查詢充電歷史紀錄失敗，請回首頁再次進行操作"
                assignHistoryRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }

    // 掃描 QRcode
    fun sendQRcode(context: Context,
                   member_id       : String,
                   member_pwd      : String,
                   gun_dev_id      : String,
                   gun_number      : String
    ) {
        var d_gun_dev_id =gun_dev_id
        var d_gun_number =gun_number

        if (Glob.QRcodeSampleResponse) {
            val gson = Gson()
            val jsonString      = json_sample.transQrcode
            val js = gson.fromJson(jsonString, Gun4QRcodeResponse::class.java)
            _charge_qrcode.value = js
            return
        }
        val call: Call<Gun4QRcodeResponse> = ApiChargeUtility.service.apiTransQRcode(
            member_id,
            member_pwd,
            d_gun_dev_id,
            d_gun_number,
        )
        call.enqueue(object : Callback<Gun4QRcodeResponse> {
            override fun onResponse(
                call: Call<Gun4QRcodeResponse>,
                response: Response<Gun4QRcodeResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    if (resp != null) {
                        _charge_qrcode.value = resp
                    }
                } else {
                    val msg ="掃描 QRcode 發生異常，請回首頁再次進行操作"
                    assignQRcodeRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<Gun4QRcodeResponse>, t: Throwable) {
                val msg ="掃描 QRcode 失敗，請回首頁再次進行操作"
                assignQRcodeRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }

    // 啟動充電槍
    fun setPowerON(context: Context,
                 memberId      : String,
                 memberPwd     : String,
                 gun_device_id : String,        // 充電槍id
                 gun_number    : String,        // 充電槍編號
                 control       : String,        // 0 :停止; 1 :啟動
                 pre_control   : String ="1"    // 0 :取消預約; 1 :預約
    ) {
        val call: Call<SetPowerOnResponse> = ApiChargeUtility.service.apiSetChargePowerON(
            memberId     ,
            memberPwd    ,
            gun_device_id,
            gun_number   ,
            control      ,
            pre_control
        )
        call.enqueue(object : Callback<SetPowerOnResponse> {
            override fun onResponse(
                call: Call<SetPowerOnResponse>,
                response: Response<SetPowerOnResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (Glob.PowerONSampleResponse) {
                    val gson = Gson()
                    val jsonString = json_sample.setPower
                    val js = gson.fromJson(jsonString, SetPowerOnResponse::class.java)
                    _charge_power_on.value = js
                    return
                }

                if (response.body() != null) {
                    var resp = response.body()
                    if (resp != null) {
                        _charge_power_on.value = resp
                    }
                } else {
                    val msg ="啟動充電槍發生異常，請回首頁再次進行操作"
                    assignPowerOnRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<SetPowerOnResponse>, t: Throwable) {
                val msg ="啟動充電槍失敗，請回首頁再次進行操作"
                assignPowerOnRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }

    // 停止充電槍
    fun setPowerOFF(context: Context,
                 memberId      : String,
                 memberPwd     : String,
                 gun_device_id : String,        // 充電槍id
                 gun_number    : String,        // 充電槍編號
                 control       : String ="0",   // 0 :停止; 1 :啟動
                 pre_control   : String ="1"    // 0 :取消預約; 1 :預約
    ) {
        val call: Call<SetPowerOffResponse> = ApiChargeUtility.service.apiSetChargePowerOFF(
            memberId     ,
            memberPwd    ,
            gun_device_id,
            gun_number   ,
            control      ,
            pre_control
        )
        call.enqueue(object : Callback<SetPowerOffResponse> {
            override fun onResponse(
                call: Call<SetPowerOffResponse>,
                response: Response<SetPowerOffResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (Glob.PowerOFFSampleResponse) {
                    val gson = Gson()
                    val jsonString = json_sample.setPower
                    val js = gson.fromJson(jsonString, SetPowerOffResponse::class.java)
                    _charge_power_off.value = js
                    return
                }
                if (response.body() != null) {
                    var resp = response.body()
                    if (resp != null) {
                        _charge_power_off.value = resp
                    }
                } else {
                    val msg ="停止充電槍發生異常，請回首頁再次進行操作"
                    assignPowerOffRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<SetPowerOffResponse>, t: Throwable) {
                val msg ="停止充電槍失敗，請回首頁再次進行操作"
                assignPowerOffRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }

    // 取得使用者目前充電資訊
    fun getChargingData(context: Context,
                    memberId      : String,
                    memberPwd     : String
    ) {
        val call: Call<ChargingDataResponse> = ApiChargeUtility.service.apiGetChargingData4User(
            memberId,
            memberPwd
        )
        call.enqueue(object : Callback<ChargingDataResponse> {
            override fun onResponse(
                call: Call<ChargingDataResponse>,
                response: Response<ChargingDataResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (Glob.ChargingDataSampleResponse) {
                    val gson = Gson()
                    val jsonString = json_sample.setPower
                    val js = gson.fromJson(jsonString, ChargingDataResponse::class.java)
                    _charging_data.value = js
                    return
                }
                if (response.body() != null) {
                    var resp = response.body()
                    if (resp != null) {
                        _charging_data.value = resp
                    }
                } else {
                    val msg ="取得使用者目前充電資訊發生異常，請回首頁再次進行操作"
                    assignChargingRespMessage("0x020F", "false", msg)
//                    AppUtility.showPopDialog(
//                        context,
//                        statusCode.toString(),
//                        msg
//                    )
                }
            }

            override fun onFailure(call: Call<ChargingDataResponse>, t: Throwable) {
                val msg ="取得使用者目前充電資訊失敗，請回首頁再次進行操作"
                assignChargingRespMessage("0x020E", "false", msg)
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    msg
//                )
            }
        })
    }

    // 判斷充電狀態
    fun curChargeStatus(context    : Context,
                          memberId   : String,
                          memberPwd  : String,
                          control_id :String
    ) {
        val call: Call<StandardResponse> = ApiChargeUtility.service.apiCheckChargeStatus(
            memberId,
            memberPwd,
            control_id
        )
        call.enqueue(object : Callback<StandardResponse> {
            override fun onResponse(
                call: Call<StandardResponse>,
                response: Response<StandardResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (Glob.curChargeStatusSampleResponse) {
                    val gson = Gson()
                    val jsonString = json_sample.setPower
                    val js = gson.fromJson(jsonString, ChargingDataResponse::class.java)
                    _charging_data.value = js
                    return
                }
                if (response.body() != null) {
                    var resp = response.body()
                    if (resp != null) {
                        _check_charge_status.value = resp
                    }
                } else {
                    val msg ="取得使用者目前充電資訊發生異常，請回首頁再次進行操作"
                    assignChargingRespMessage("0x020F", "false", msg)
//                    AppUtility.showPopDialog(
//                        context,
//                        statusCode.toString(),
//                        msg
//                    )
                }
            }

            override fun onFailure(call: Call<StandardResponse>, t: Throwable) {
                val msg ="取得使用者目前充電資訊失敗，請回首頁再次進行操作"
                assignChargingRespMessage("0x020E", "false", msg)
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    msg
//                )
            }
        })
    }
    // ---------------------------------------------------------------------------------------------
    private fun assignCheckChargeRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charge_check.value == null)
            _charge_check.value =chkChargeResponse("", "", "", "","")
        _charge_check.value!!.code            =resp.code
        _charge_check.value!!.status          =resp.status
        _charge_check.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }private fun assignStationRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charge_station.value == null)
            _charge_station.value =NearStationResponse("", null, "", "")
        _charge_station.value!!.code            =resp.code
        _charge_station.value!!.status          =resp.status
        _charge_station.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }
    private fun assignHistoryRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charge_history.value == null)
            _charge_history.value =ChargeHistoryResponse("", null, "", "")
        _charge_history.value!!.code            =resp.code
        _charge_history.value!!.status          =resp.status
        _charge_history.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }
    private fun assignQRcodeRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charge_qrcode.value == null)
            _charge_qrcode.value =Gun4QRcodeResponse("", null, "", "")
        _charge_qrcode.value!!.code            =resp.code
        _charge_qrcode.value!!.status          =resp.status
        _charge_qrcode.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }
    private fun assignPowerOnRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charge_power_on.value == null)
            _charge_power_on.value =SetPowerOnResponse("", null, "", "")
        _charge_power_on.value!!.code            =resp.code
        _charge_power_on.value!!.status          =resp.status
        _charge_power_on.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }
    private fun assignPowerOffRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charge_power_off.value == null)
            _charge_power_off.value =SetPowerOffResponse("", "", "")
        _charge_power_off.value!!.code            =resp.code
        _charge_power_off.value!!.status          =resp.status
        _charge_power_off.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }
    private fun assignChargingRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_charging_data.value == null)
            _charging_data.value =ChargingDataResponse("", listOf(),"","")
        _charging_data.value!!.code = resp.code
        _charging_data.value!!.status = resp.status
        _charging_data.value!!.responseMessage = resp.responseMessage

        _std_check.clear()
    }
    private fun setResponseMessage(code :String, status :String, msg :String): StandardResponse {
        _std_check.code =code
        _std_check.status =status
        _std_check.responseMessage =msg
        return _std_check
    }

    fun fetchRoadParkStatus() {
        val call: Call<RoadParkStatusResponse> = ApiChargeUtility.service.apiGetRoadParkStatus()
        call.enqueue(object : Callback<RoadParkStatusResponse> {
            override fun onResponse(
                call: Call<RoadParkStatusResponse>,
                response: Response<RoadParkStatusResponse>
            ) {
                if (response.isSuccessful) {
                    _roadParkStatus.value = response.body()
                } else {
                    Log.e("API_ERROR", "Failed to fetch road park status")
                }
            }

            override fun onFailure(call: Call<RoadParkStatusResponse>, t: Throwable) {
                Log.e("API_ERROR", "API call failed: ${t.message}")
            }
        })
    }

    fun fetchAllParkStatus() {
        val call = ApiChargeUtility.service.getAllParkStatus()
        call.enqueue(object : Callback<ParkStatusResponse> {
            override fun onResponse(call: Call<ParkStatusResponse>, response: Response<ParkStatusResponse>) {
                if (response.isSuccessful) {
                    _parkStatus.value = response.body()
                } else {
                    Log.e("API_ERROR", "Error fetching park status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ParkStatusResponse>, t: Throwable) {
                Log.e("API_ERROR", "API call failed: ${t.message}")
            }
        })
    }
}