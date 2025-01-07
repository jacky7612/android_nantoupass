package com.jotangi.nantouparking.JackyVariant

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantouparking.utility.AppUtility
import com.jotangi.payStation.Model.ApiModel.ApiRespGovPlateListOK
import com.jotangi.nantouparking.jackyModels.map.JChargeMapData
import com.jotangi.nantouparking.jackyModels.map.JMapData
import com.jotangi.nantouparking.model.charge.Data4Gun
import com.jotangi.nantouparking.model.charge.DataChargeHistory

@SuppressLint("StaticFieldLeak")
object Glob {
    lateinit var activity     : Activity
    lateinit var apiGovPayList: ApiRespGovPlateListOK

    var SkipPowerOFFRealResponse        : Boolean            =false  // 假成功訊號，停止充電
    var ChargeStartSkipWait             : Boolean            =false  // 跳過等待 充電啟動中
    var PowerONSampleResponse           : Boolean            =false  // 啟動充電
    var PowerOFFSampleResponse          : Boolean            =false  // 停止充電
    var GetOrderSampleResponse          : Boolean            =false  // 停止充電直接取訂單號碼，跳過PowerOFF API
    var CheckChargeSampleResponse       : Boolean            =false  // 充電、繳費狀態(餘額 >= 50)
    var QRcodeSampleResponse            : Boolean            =false  // QRcode
    var ChargeStartDirect2NextPage      : Boolean            =false  // 不經過api直接進入下一頁
    var ChargingDataSampleResponse      : Boolean            =false  // 取得使用者目前充電資訊
    var ChargeHistorySampleResponse     : Boolean            =false  // 取得歷史資訊
    var curChargeStatusSampleResponse   : Boolean            =false  // 未用到

    var QRcodeAutoNext                  : Boolean            =true
    var isScanPage                      : Boolean            =false

    var title                 : String             =""
    var queryHistoryPlateNo   : String             =""
    var map_position          : Int                =-1
    var DoLocaleCurrentLatLng : Boolean            =false
    var CurMarkerInfo         : JMapData?          =null
    var CurChargeMarkerInfo   : JChargeMapData?    =null
    var MapMode               : String             ="charge"
    var ParkingName           : String             =""
    var Back2Home             : Boolean            =true
    var curChargeHistoryData  : DataChargeHistory? =null
    var curChargeGunData      : Data4Gun?          =null
    var curChargeInfo         : ChargeInfo?        =null

    fun clear() {
        CurMarkerInfo?.clear()
        curChargeGunData?.clear()
        curChargeInfo?.clear()
    }

    fun clearMemberInfo(context : Context) {

        AppUtility.updateLoginStatus(
            context,
            false
        )

        AppUtility.updateLoginId(
            context,
            ""
        )

        AppUtility.updateLoginPassword(
            context,
            ""
        )

        AppUtility.updateWriteOffCouponNo(
            context,
            ""
        )
    }
    fun setImageViaDrawable(bt: ImageView, imgId: Int) {
        // Get the drawable

        val drawable: Drawable? = ContextCompat.getDrawable(
            activity,
            imgId
        ) // Replace with your drawable resource

        // Set the drawable to the ImageView
        bt.setImageDrawable(drawable)
    }
    fun assign(input: JMapData) {
        CurMarkerInfo?.title =input.title
        CurMarkerInfo?.descript =input.descript
        CurMarkerInfo?.position =input.position
    }

    private val REQUEST_CAMERA: Int = 22
    fun checkPermission() {
        //先獲取相機權限
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        }
    }

    fun Pair<Double, Double>.toLatLng(): LatLng {
        return LatLng(this.first, this.second)
    }
}


class ChargeInfo {
    var MaskQRCode: String = ""
    var QRCode: String = ""
    var staionId: String = ""
    var gunDeviceId: String = ""
    var gunNumber: String = ""
    var controlId: String = ""

    var amount: String = ""
    var bill_no: String = ""
    var description: String = ""
    var start_time: String = ""
    var stop_time: String = ""
    var charge_time: String = ""

    fun clear() {
        MaskQRCode = ""
        QRCode = ""
        staionId = ""
        gunDeviceId = ""
        gunNumber = ""
        controlId = ""

        amount = ""
        bill_no = ""
        description = ""
        start_time = ""
        stop_time = ""
        charge_time = ""
    }
}