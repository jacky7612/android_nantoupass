package com.jotangi.nantoupass.ui.gov

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jotangi.nantoupass.config.ApiPassResp4Banner
import com.jotangi.nantoupass.config.ApiPassResp4News
import com.jotangi.nantoupass.model.charge.StandardResponse
import com.jotangi.nantoupass.utility.ApiPassUtility
import com.jotangi.nantoupass.utility.AppUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PassViewModel: ViewModel() {
    // 通用 - start
    private lateinit var _std_check: StandardResponse
    // 通用 - end

    init {
        _std_check =StandardResponse("","","")

        clear()
    }

    fun clear() {

    }

    private fun setResponseMessage(code :String, status :String, msg :String): StandardResponse {
        _std_check.code =code
        _std_check.status =status
        _std_check.responseMessage =msg
        return _std_check
    }

    // Banner - start
    private val _banner: MutableLiveData<ApiPassResp4Banner> by lazy {
        MutableLiveData<ApiPassResp4Banner>()
    }
    val banner: LiveData<ApiPassResp4Banner> get() = _banner
    fun clearBanner() {
        if (_banner.value != null) {
            _banner.value!!.data = null
            _banner.postValue(null)
        }
    }
    private fun assignBannerRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_banner.value == null)
            _banner.value =ApiPassResp4Banner("", "", "", null)
        _banner.value!!.code            =resp.code
        _banner.value!!.status          =resp.status
        _banner.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getBanner(context: Context) {
        val call: Call<ApiPassResp4Banner> = ApiPassUtility.service.apiGetBanner(
        )
        call.enqueue(object : Callback<ApiPassResp4Banner> {
            override fun onResponse(
                call: Call<ApiPassResp4Banner>,
                response: Response<ApiPassResp4Banner>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _banner.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋Banner發生異常，請回首頁再次進行操作"
                    assignBannerRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4Banner>, t: Throwable) {
                val msg ="搜尋Banner失敗，請回首頁再次進行操作"
                assignBannerRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Banner - end


    // Banner - start
    private val _news: MutableLiveData<ApiPassResp4News> by lazy {
        MutableLiveData<ApiPassResp4News>()
    }
    val news: LiveData<ApiPassResp4News> get() = _news

    fun clearNews() {
        if (_news.value != null) {
            _news.value!!.data = null
            _news.postValue(null)
        }
    }
    private fun assignNewsRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_news.value == null)
            _news.value =ApiPassResp4News("", "", "", null)
        _news.value!!.code            =resp.code
        _news.value!!.status          =resp.status
        _news.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getNews(context: Context) {
        val call: Call<ApiPassResp4News> = ApiPassUtility.service.apiGetNews(
        )
        call.enqueue(object : Callback<ApiPassResp4News> {
            override fun onResponse(
                call: Call<ApiPassResp4News>,
                response: Response<ApiPassResp4News>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _news.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋最新消息發生異常，請回首頁再次進行操作"
                    assignNewsRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4News>, t: Throwable) {
                val msg ="搜尋最新消息，請回首頁再次進行操作"
                assignNewsRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Banner - end
}