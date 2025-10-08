package com.jotangi.nantoupass.ui.gov

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jotangi.nantoupass.config.ApiPassResp4Agency
import com.jotangi.nantoupass.config.ApiPassResp4AgencyUnit
import com.jotangi.nantoupass.config.ApiPassResp4ApplyDetail
import com.jotangi.nantoupass.config.ApiPassResp4Applyitem
import com.jotangi.nantoupass.config.ApiPassResp4Banner
import com.jotangi.nantoupass.config.ApiPassResp4News
import com.jotangi.nantoupass.config.ApiPassResp4Sightseeing
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


    // News - start
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

    fun getNews(context: Context, new_kind: String) {
        val call: Call<ApiPassResp4News> = ApiPassUtility.service.apiGetNews(
            new_kind
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
    // News - end


    // Agency - start
    private val _agency: MutableLiveData<ApiPassResp4Agency> by lazy {
        MutableLiveData<ApiPassResp4Agency>()
    }
    val agency: LiveData<ApiPassResp4Agency> get() = _agency

    fun clearAgency() {
        if (_agency.value != null) {
            _agency.value!!.data = null
            _agency.postValue(null)
        }
    }
    private fun assignAgencyRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_agency.value == null)
            _agency.value =ApiPassResp4Agency("", "", "", null)
        _agency.value!!.code            =resp.code
        _agency.value!!.status          =resp.status
        _agency.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getAgency(context: Context) {
        val call: Call<ApiPassResp4Agency> = ApiPassUtility.service.apiGetAgency(
        )
        call.enqueue(object : Callback<ApiPassResp4Agency> {
            override fun onResponse(
                call: Call<ApiPassResp4Agency>,
                response: Response<ApiPassResp4Agency>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _agency.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋機構發生異常，請回首頁再次進行操作"
                    assignAgencyRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4Agency>, t: Throwable) {
                val msg ="搜尋機構，請回首頁再次進行操作"
                assignAgencyRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Agency - end


    // Agency Unit - start
    private val _agencyunit: MutableLiveData<ApiPassResp4AgencyUnit> by lazy {
        MutableLiveData<ApiPassResp4AgencyUnit>()
    }
    val agencyunit: LiveData<ApiPassResp4AgencyUnit> get() = _agencyunit

    fun clearAgencyUnit() {
        if (_agencyunit.value != null) {
            _agencyunit.value!!.data = null
            _agencyunit.postValue(null)
        }
    }
    private fun assignAgencyUnitRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_agencyunit.value == null)
            _agencyunit.value =ApiPassResp4AgencyUnit("", "", "", null)
        _agencyunit.value!!.code            =resp.code
        _agencyunit.value!!.status          =resp.status
        _agencyunit.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getAgencyunit(context: Context, agency_sid: String) {
        val call: Call<ApiPassResp4AgencyUnit> = ApiPassUtility.service.apiGetAgencyUnit(
            agency_sid
        )
        call.enqueue(object : Callback<ApiPassResp4AgencyUnit> {
            override fun onResponse(
                call: Call<ApiPassResp4AgencyUnit>,
                response: Response<ApiPassResp4AgencyUnit>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _agencyunit.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋各科室單位發生異常，請回首頁再次進行操作"
                    assignAgencyUnitRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4AgencyUnit>, t: Throwable) {
                val msg ="搜尋各科室單位，請回首頁再次進行操作"
                assignAgencyRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Agency - end


    // Sightseeing Unit - start
    private val _sightseeing: MutableLiveData<ApiPassResp4Sightseeing> by lazy {
        MutableLiveData<ApiPassResp4Sightseeing>()
    }
    val sightseeing: LiveData<ApiPassResp4Sightseeing> get() = _sightseeing

    fun clearSightseeing() {
        if (_sightseeing.value != null) {
            _sightseeing.value!!.data = null
            _sightseeing.postValue(null)
        }
    }
    private fun assignSightseeingRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_sightseeing.value == null)
            _sightseeing.value =ApiPassResp4Sightseeing("", "", "", null)
        _sightseeing.value!!.code            =resp.code
        _sightseeing.value!!.status          =resp.status
        _sightseeing.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getSightseeing(context: Context) {
        val call: Call<ApiPassResp4Sightseeing> = ApiPassUtility.service.apiGetSightseeing(
        )
        call.enqueue(object : Callback<ApiPassResp4Sightseeing> {
            override fun onResponse(
                call: Call<ApiPassResp4Sightseeing>,
                response: Response<ApiPassResp4Sightseeing>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _sightseeing.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋景點發生異常，請回首頁再次進行操作"
                    assignSightseeingRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4Sightseeing>, t: Throwable) {
                val msg ="搜尋景點，請回首頁再次進行操作"
                assignSightseeingRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Agency - end


    // Apply items - start
    private val _applyitem: MutableLiveData<ApiPassResp4Applyitem> by lazy {
        MutableLiveData<ApiPassResp4Applyitem>()
    }
    val ApplyItem: LiveData<ApiPassResp4Applyitem> get() = _applyitem

    fun clearApply() {
        if (_applyitem.value != null) {
            _applyitem.value!!.data = null
            _applyitem.postValue(null)
        }
    }
    private fun assignApplyRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_applyitem.value == null)
            _applyitem.value =ApiPassResp4Applyitem("", "", "", null)
        _applyitem.value!!.code            =resp.code
        _applyitem.value!!.status          =resp.status
        _applyitem.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getApplyItems(context: Context) {
        val call: Call<ApiPassResp4Applyitem> = ApiPassUtility.service.apiGetApplyitems(
        )
        call.enqueue(object : Callback<ApiPassResp4Applyitem> {
            override fun onResponse(
                call: Call<ApiPassResp4Applyitem>,
                response: Response<ApiPassResp4Applyitem>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _applyitem.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋申辦服務發生異常，請回首頁再次進行操作"
                    assignApplyRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4Applyitem>, t: Throwable) {
                val msg ="搜尋申辦服務，請回首頁再次進行操作"
                assignApplyRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Apply items - end


    // Apply detail - start
    private val _applydetail: MutableLiveData<ApiPassResp4ApplyDetail> by lazy {
        MutableLiveData<ApiPassResp4ApplyDetail>()
    }
    val ApplyDetail: LiveData<ApiPassResp4ApplyDetail> get() = _applydetail

    fun clearApplyDetail() {
        if (_applydetail.value != null) {
            _applydetail.value!!.data = null
            _applydetail.postValue(null)
        }
    }
    private fun assignApplyDetailRespMessage(code :String, status :String, msg :String) {
        val resp =setResponseMessage(code, status, msg)
        if (_applydetail.value == null)
            _applydetail.value =ApiPassResp4ApplyDetail("", "", "", null)
        _applydetail.value!!.code            =resp.code
        _applydetail.value!!.status          =resp.status
        _applydetail.value!!.responseMessage =resp.responseMessage
        _std_check.clear()
    }

    fun getApplyDetail(context: Context, sid: String) {
        val call: Call<ApiPassResp4ApplyDetail> = ApiPassUtility.service.apiGetApplydetail(
            sid = sid
        )
        call.enqueue(object : Callback<ApiPassResp4ApplyDetail> {
            override fun onResponse(
                call: Call<ApiPassResp4ApplyDetail>,
                response: Response<ApiPassResp4ApplyDetail>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    var resp = response.body()
                    Log.d("CheckPass", resp.toString())
                    if (resp != null) {
                        if (resp.status == "true" && resp.code == "0x0200") {
                            _applydetail.value = resp
                        }
                    }
                } else {
                    Log.d("CheckPass", "3")
                    val msg ="搜尋申辦服務發生異常，請回首頁再次進行操作"
                    assignApplyRespMessage("0x020F", "false", msg)
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        msg
                    )
                }
            }

            override fun onFailure(call: Call<ApiPassResp4ApplyDetail>, t: Throwable) {
                val msg ="搜尋申辦服務，請回首頁再次進行操作"
                assignApplyRespMessage("0x020E", "false", msg)
                AppUtility.showPopDialog(
                    context,
                    null,
                    msg
                )
            }
        })
    }
    // Apply items - end
}