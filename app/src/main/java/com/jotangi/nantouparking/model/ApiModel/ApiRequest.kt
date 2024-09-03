package com.jotangi.payStation.Model.ApiModel

import android.content.ContentValues.TAG
import android.util.Log
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ApiRequest {
    var ResponseBody: String=""
    fun apiPostByFormData(Url: String, parameters: MultipartBody, callback: (String?, Exception?) -> Unit) {
//        val url: String="http://202.5.253.133/cdil_b2b_test/api/JTG_login.php"
        val client = OkHttpClient()

        // Create a form body with the data to be sent in the POST request
//        val requestBody= MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("uid", "admin")
//            .addFormDataPart("upwd", "admin")
//            .build()

        Log.i(TAG, "[core] api post core")
        val request = Request.Builder()
            .url(Url)
            .post(parameters)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
//                if (responseBody != null) {
//                    if (responseBody.isNotEmpty()) {
//                        Glob.apiResponse = responseBody
//                    }
//                }
                Log.i(TAG, "[core] api response :$responseBody")
                callback(responseBody, null)
                Log.i(TAG, "[core] api Ack Response :$responseBody")
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null, e)
                Log.i(TAG, "[core] api Ack Request failed IOException :${e.message}")
            }
        })
    }
    fun apiGetByParam(Url: String, parameters: String, callback: (String?, Exception?) -> Unit) {
        val client = OkHttpClient()

        Log.i(TAG, "[core] api GET core")
        val request = Request.Builder()
            .url("${Url}${parameters}")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.i(TAG, "[core] api response :$responseBody")
                callback(responseBody, null)
                Log.i(TAG, "[core] api Ack Response :$responseBody")
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null, e)
                Log.i(TAG, "[core] api Ack Request failed IOException :${e.message}")
            }
        })
    }
}