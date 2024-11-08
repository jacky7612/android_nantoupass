package com.jotangi.payStation.Model.ApiModel

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.model.MemberInfoVO
import okhttp3.MultipartBody

// 兆豐 SKCB Kiosk protocol
class ApiEntry {
    fun getStoreList(StoreId: String, StoreType: String, callback: (String?, Exception?) -> Unit) {
        val apiRequest: ApiRequest = ApiRequest()
        val url="${ApiConfig.URL_HOST}api/store_list.php"
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("store_id", StoreId)
            .addFormDataPart("store_type", StoreType)
            .build()
        apiRequest.apiPostByFormData(url, requestBody, callback)
    }
    //    fun parseStoreList(responseBody: String): Any? {
//        Common.apiStoreList =ApiRespStoreListOK()
//        Common.apiStoreList!!.clear()
//        val gson = Gson()
//        Log.d(TAG, responseBody)
//
//        if (responseBody.contains("\"store_id\":")) {
//            val apiRespData =gson.fromJson(responseBody, ApiRespStoreListOK::class.java)
//            Log.i(TAG, "parseStoreList :$apiRespData")
//            Common.apiStoreList =apiRespData
//            Log.i(TAG, "parseStoreList ok")
//            Common.apiStoreListStatus!!.status          ="true"
//            Common.apiStoreListStatus!!.responseMessage ="succeed"
//            Common.apiStoreListStatus!!.code            ="0x0200"
//            return apiRespData
//        }
//        val apiRespData=gson.fromJson(responseBody, ApiRespErr::class.java)
//        Common.apiStoreListStatus!!.status          =apiRespData.status
//        Common.apiStoreListStatus!!.responseMessage =apiRespData.responseMessage
//        Common.apiStoreListStatus!!.code            =apiRespData.code
//        return apiRespData
//    }
    fun getGovPlateList(plate_no: String, callback: (String?, Exception?) -> Unit) {
        val apiRequest: ApiRequest = ApiRequest()
        val url="${ApiConfig.URL_HOST}api/bills_paid_record.php"
        val requestParam = "?plateNo=${plate_no}"
        apiRequest.apiGetByParam(url, requestParam, callback)
    }
    fun parseGovPlateList(responseBody: String): Any? {
        Glob.apiGovPayList =ApiRespGovPlateListOK()
        Glob.apiGovPayList!!.clear()
        val gson = Gson()
        Log.d(TAG, responseBody)

        if (responseBody.contains("\"data\":")) {
            val apiRespData =gson.fromJson(responseBody, ApiRespGovPlateListOK::class.java)
            Log.i(TAG, "parseGovPlateList :$apiRespData")
            Glob.apiGovPayList =apiRespData
            Log.i(TAG, "parseGovPlateList ok")
            return apiRespData
        }
        val apiRespData=gson.fromJson(responseBody, ApiRespErr::class.java)
        Glob.apiGovPayList!!.status          =apiRespData.status
        Glob.apiGovPayList!!.responseMessage =apiRespData.responseMessage
        return apiRespData
    }
    fun getMemberInfo(memberId: String, memberPwd: String, callback: (String?, Exception?) -> Unit) {
        val apiRequest: ApiRequest = ApiRequest()
        val url="${ApiConfig.URL_HOST}api/member_info.php"
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("member_id", memberId)
            .addFormDataPart("member_pwd", memberPwd)
            .build()
        apiRequest.apiPostByFormData(url, requestBody, callback)
    }
    fun parseMemberInfo(responseBody: String): List<MemberInfoVO> {
        val gson = Gson()
        Log.d(TAG, responseBody)

        var memberList :List<MemberInfoVO> = listOf()
        if (responseBody.contains("\"member_id\":")) {
            val memberInfoType = object : TypeToken<List<MemberInfoVO>>() {}.type
            memberList = gson.fromJson(responseBody, memberInfoType)
            return memberList
        }
        return memberList
    }

}