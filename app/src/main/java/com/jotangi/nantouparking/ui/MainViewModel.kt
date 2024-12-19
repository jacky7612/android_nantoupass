package com.jotangi.nantouparking.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.model.*
import com.jotangi.nantouparking.utility.ApiUtility
import com.jotangi.nantouparking.utility.AppUtility
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel : ViewModel() {

    // region member
    private val _signupData: MutableLiveData<SignupResponse> by lazy {
        MutableLiveData<SignupResponse>()
    }
    val signupData: LiveData<SignupResponse> get() = _signupData

    private val _loginData: MutableLiveData<LoginResponse> by lazy {
        MutableLiveData<LoginResponse>()
    }
    val loginData: LiveData<LoginResponse> get() = _loginData

    private val _memberInfoData: MutableLiveData<List<MemberInfoVO>> by lazy {
        MutableLiveData<List<MemberInfoVO>>()
    }
    val memberInfoData: LiveData<List<MemberInfoVO>> get() = _memberInfoData

    private val _memberInfoEditData: MutableLiveData<MemberInfoEditResponse> by lazy {
        MutableLiveData<MemberInfoEditResponse>()
    }
    val memberInfoEditData: LiveData<MemberInfoEditResponse> get() = _memberInfoEditData

    private val _couponData: MutableLiveData<List<CouponVO>> by lazy {
        MutableLiveData<List<CouponVO>>()
    }

    private val _exchangeableCouponData: MutableLiveData<List<CouponVO>> by lazy {
        MutableLiveData<List<CouponVO>>()
    }
    val exchangeableCouponData: LiveData<List<CouponVO>> get() = _exchangeableCouponData

    private val _exchangedCouponData: MutableLiveData<List<CouponVO>> by lazy {
        MutableLiveData<List<CouponVO>>()
    }
    val exchangedCouponData: LiveData<List<CouponVO>> get() = _exchangedCouponData

    private val _updatePasswordData: MutableLiveData<UpdatePasswordResponse> by lazy {
        MutableLiveData<UpdatePasswordResponse>()
    }
    val updatePasswordData: LiveData<UpdatePasswordResponse> get() = _updatePasswordData

    private val _logoutData: MutableLiveData<LogoutResponse> by lazy {
        MutableLiveData<LogoutResponse>()
    }
    val logoutData: LiveData<LogoutResponse> get() = _logoutData

    private val _forgetPasswordData: MutableLiveData<ForgetPasswordResponse> by lazy {
        MutableLiveData<ForgetPasswordResponse>()
    }
    val forgetPasswordData: LiveData<ForgetPasswordResponse> get() = _forgetPasswordData
    // endregion

    // region parking
    var hasData = false
    private val _parkingFeePaidData: MutableLiveData<List<ParkingFeePaidVO>> by lazy {
        MutableLiveData<List<ParkingFeePaidVO>>()
    }
    val parkingFeePaidData: LiveData<List<ParkingFeePaidVO>> get() = _parkingFeePaidData

    private val _parkingFeePaidDetailData: MutableLiveData<List<ParkingFeePaidDetailVO>> by lazy {
        MutableLiveData<List<ParkingFeePaidDetailVO>>()
    }
    val parkingFeePaidDetailData: LiveData<List<ParkingFeePaidDetailVO>> get() = _parkingFeePaidDetailData

    private val _plateNumberData: MutableLiveData<List<PlateNumberVO>> by lazy {
        MutableLiveData<List<PlateNumberVO>>()
    }
    val plateNumberData: LiveData<List<PlateNumberVO>> get() = _plateNumberData

    private val _parkingGarageData: MutableLiveData<List<ParkingGarageVO>> by lazy {
        MutableLiveData<List<ParkingGarageVO>>()
    }
    val parkingGarageData: LiveData<List<ParkingGarageVO>> get() = _parkingGarageData

    private val _parkingRoadFeeUnPaidData: MutableLiveData<ParkingRoadFeeUnPaidResponse> by lazy {
        MutableLiveData<ParkingRoadFeeUnPaidResponse>()
    }
    val parkingRoadFeeUnPaidData: LiveData<ParkingRoadFeeUnPaidResponse> get() = _parkingRoadFeeUnPaidData

    private val _parkingGarageFeeUnPaidData: MutableLiveData<ParkingGarageFeeUnPaidResponse> by lazy {
        MutableLiveData<ParkingGarageFeeUnPaidResponse>()
    }
    val parkingGarageFeeUnPaidData: LiveData<ParkingGarageFeeUnPaidResponse> get() = _parkingGarageFeeUnPaidData

    private val _addPlateNoData: MutableLiveData<AddPlateNoResponse> by lazy {
        MutableLiveData<AddPlateNoResponse>()
    }
    val addPlateNoData: LiveData<AddPlateNoResponse> get() = _addPlateNoData

    private val _deletePlateNumberData: MutableLiveData<DeletePlateNumberResponse> by lazy {
        MutableLiveData<DeletePlateNumberResponse>()
    }
    val deletePlateNumberData: LiveData<DeletePlateNumberResponse> get() = _deletePlateNumberData

    private val _bannerData: MutableLiveData<List<BannerVO>> by lazy {
        MutableLiveData<List<BannerVO>>()
    }
    val bannerData: LiveData<List<BannerVO>> get() = _bannerData

    private val _storeData: MutableLiveData<List<StoreVO>> by lazy {
        MutableLiveData<List<StoreVO>>()
    }
    val storeData: LiveData<List<StoreVO>> get() = _storeData

    private val _parkingSpaceData: MutableLiveData<ParkingInfoResponse> by lazy {
        MutableLiveData<ParkingInfoResponse>()
    }
    val parkingSpaceData: LiveData<ParkingInfoResponse> get() = _parkingSpaceData

    private val _buildingParkingSpaceData: MutableLiveData<ParkingInfoBuildingResponse> by lazy {
        MutableLiveData<ParkingInfoBuildingResponse>()
    }
    val buildingParkingSpaceData: LiveData<ParkingInfoBuildingResponse> get() = _buildingParkingSpaceData

    private val _billIsLock: MutableLiveData<LockBillResponse> by lazy {
        MutableLiveData<LockBillResponse>()
    }
    val billIsLock: LiveData<LockBillResponse> get() = _billIsLock

    private val _attractions: MutableLiveData<List<AttractionVO>> by lazy {
        MutableLiveData<List<AttractionVO>>()
    }
    val attractions: LiveData<List<AttractionVO>> get() = _attractions
    // endregion

    // region coupon
    private val _applyCouponData: MutableLiveData<WriteOffTicketResponse> by lazy {
        MutableLiveData<WriteOffTicketResponse>()
    }
    val applyCouponData: LiveData<WriteOffTicketResponse> get() = _applyCouponData
    // endregion

    private val _isDelete: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val _pointRecordsData = MutableLiveData<List<PointRecordResponse>>()
    val pointRecordsData: LiveData<List<PointRecordResponse>> get() = _pointRecordsData

    private val _pointRecords = MutableLiveData<List<PointRecord2Response>>()
    val pointRecords: LiveData<List<PointRecord2Response>> get() = _pointRecords

    private val _memberInfo = MutableLiveData<List<MemberInfo>>()
    val memberInfo: LiveData<List<MemberInfo>> get() = _memberInfo

    private val _errorResponse = MutableLiveData<ErrorResponse>()
    val errorResponse: LiveData<ErrorResponse> get() = _errorResponse

    private val _getPointResponse = MutableLiveData<GetPointResponse>()
    val getPointResponse: LiveData<GetPointResponse> get() = _getPointResponse

    val isDelete: LiveData<Boolean> get() = _isDelete

    fun clearData() {
        _memberInfoData.value = listOf()
        _memberInfoEditData.value = null
        _couponData.value = listOf()
        _exchangeableCouponData.value = listOf()
        _exchangedCouponData.value = listOf()
        _parkingFeePaidData.value = listOf()
        _parkingFeePaidDetailData.value = listOf()
        _plateNumberData.value = listOf()
        _parkingRoadFeeUnPaidData.value = null
        _parkingGarageFeeUnPaidData.value = null
        _bannerData.value = listOf()
        _storeData.value = listOf()
        _parkingSpaceData.value = null
        _billIsLock.value = null
        _loginData.value = null
        _logoutData.value = null
        _signupData.value = null
        hasData = false
    }
    fun clearForgetPwdData() {
        if (_forgetPasswordData.value != null) {
            _forgetPasswordData.value?.code = ""
            _forgetPasswordData.value?.status = ""
            _forgetPasswordData.value?.responseMessage = ""
        }
    }

    fun signup(
        context: Context,
        memberName: String,
        memberEmail: String,
        memberId: String,
        memberPassword: String,
        memberLicensePlate: String
    ) {
        val call: Call<SignupResponse> = ApiUtility.service.apiSignup(
            memberName,
            memberEmail,
            memberId,
            memberPassword,
            memberLicensePlate
        )
        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _signupData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun login(
        context: Context,
        memberId: String,
        memberPassword: String
    ) {
        val call: Call<LoginResponse> = ApiUtility.service.apiLogin(
            memberId,
            memberPassword,
            ""
        )
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0201 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
                    } else {
                        _loginData.value = response.body()
//                        getMemberInfo(
//                            context,
//                            memberId,
//                            memberPassword,
//                            response.body()
//                        )
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun getMemberInfo(
        context: Context,
        memberId: String,
        memberPassword: String,
        loginResult: LoginResponse?
    ) {


//        val gson = Gson()
//            val jsonString = "[\n" +
//                    "    {\n" +
//                    "        \"0\": \"1\",\n" +
//                    "        \"mid\": \"1\",\n" +
//                    "        \"1\": \"0912345678\",\n" +
//                    "        \"member_id\": \"0912345678\",\n" +
//                    "        \"2\": \"1234qwer\",\n" +
//                    "        \"member_pwd\": \"1234qwer\",\n" +
//                    "        \"3\": \"Mike Chen\",\n" +
//                    "        \"member_name\": \"Mike Chen\",\n" +
//                    "        \"4\": \"1\",\n" +
//                    "        \"member_type\": \"1\",\n" +
//                    "        \"5\": \"0\",\n" +
//                    "        \"member_gender\": \"0\",\n" +
//                    "        \"6\": \"mike.chen@jotangi.com.tw\",\n" +
//                    "        \"member_email\": \"mike.chen@jotangi.com.tw\",\n" +
//                    "        \"7\": \"1900-01-01\",\n" +
//                    "        \"member_birthday\": \"1900-01-01\",\n" +
//                    "        \"8\": \"台北市內湖區明德路1號很多樓\",\n" +
//                    "        \"member_address\": \"台北市內湖區明德路1號很多樓\",\n" +
//                    "        \"9\": \"0912345678\",\n" +
//                    "        \"member_phone\": \"0912345678\",\n" +
//                    "        \"10\": \"uploads/mike.jpg\",\n" +
//                    "        \"member_picture\": \"uploads/mike.jpg\",\n" +
//                    "        \"11\": \"10058\",\n" +
//                    "        \"member_totalpoints\": \"10058\",\n" +
//                    "        \"12\": \"1143\",\n" +
//                    "        \"member_usingpoints\": \"1143\",\n" +
//                    "        \"13\": \"1\",\n" +
//                    "        \"member_status\": \"1\",\n" +
//                    "        \"14\": \"654321\",\n" +
//                    "        \"recommend_code\": \"654321\",\n" +
//                    "        \"15\": \"0\",\n" +
//                    "        \"member_sid\": \"0\",\n" +
//                    "        \"16\": null,\n" +
//                    "        \"member_plateNo\": null\n" +
//                    "    }\n" +
//                    "]"
//
//        val memberInfoType = object : TypeToken<List<MemberInfoVO>>() {}.type
//        val memberList: List<MemberInfoVO> = gson.fromJson(jsonString, memberInfoType)
//        _memberInfoData.value = memberList
//            return

        val call: Call<List<MemberInfoVO>> = ApiUtility.service.apiGetMemberInfo(
            memberId,
            memberPassword
        )
        call.enqueue(object : Callback<List<MemberInfoVO>> {
            override fun onResponse(
                call: Call<List<MemberInfoVO>>,
                response: Response<List<MemberInfoVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _memberInfoData.value = response.body()

                    if (loginResult != null) {
                        _loginData.value = loginResult
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<MemberInfoVO>>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun editMemberInfo(
        context: Context,
        memberName: String,
        memberEmail: String,
        memberId: String,
        memberPlateNo: String,
        memberPassword: String
    ) {
        val call: Call<MemberInfoEditResponse> = ApiUtility.service.apiEditMemberInfo(
            memberName,
            memberEmail,
            memberId,
            memberPlateNo,
            memberPassword
        )
        call.enqueue(object : Callback<MemberInfoEditResponse> {
            override fun onResponse(
                call: Call<MemberInfoEditResponse>,
                response: Response<MemberInfoEditResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0201 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
                    } else {
                        _memberInfoEditData.value = response.body()
                    }
                } else {
//                    AppUtility.showPopDialog(
//                        context,
//                        statusCode.toString(),
//                        null
//                    )
                }
            }

            override fun onFailure(call: Call<MemberInfoEditResponse>, t: Throwable) {
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                )
            }
        })
    }

    fun clearMemberData() {
        _memberInfoData.value = null
        _memberInfoEditData.value = null
    }

    fun getMyCoupon(
        context: Context,
        memberId: String,
        memberPassword: String
    ) {
        val call: Call<List<CouponVO>> = ApiUtility.service.apiGetCoupon(
            memberId,
            memberPassword
        )
        call.enqueue(object : Callback<List<CouponVO>> {
            override fun onResponse(
                call: Call<List<CouponVO>>,
                response: Response<List<CouponVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _couponData.value = response.body()
                    getExchangeableCoupon()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<CouponVO>>, t: Throwable) {
                _couponData.value = null
                getExchangeableCoupon()
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                )
            }
        })
    }

    fun getExchangeableCoupon() {
        if (_couponData.value != null) {
//            _exchangeableCouponData.value = _couponData.value?.filter { it.usingFlag == "0" }
            val tempData = _couponData.value?.filter { !isExpired(it.couponEndDate) }
            if (!tempData.isNullOrEmpty()) {
                _exchangeableCouponData.value = tempData.filter { it.usingFlag == "0" }
            } else {
                _exchangeableCouponData.value = null
            }
        } else {
            _exchangeableCouponData.value = null
        }
    }

    fun getExchangedCoupon() {
        if (_couponData.value != null) {
//            _exchangedCouponData.value = _couponData.value?.filter { it.usingFlag == "1" }
            val tempData = _couponData.value?.filter { !isExpired(it.couponEndDate) }
            if (!tempData.isNullOrEmpty()) {
                _exchangedCouponData.value = tempData.filter { it.usingFlag == "1" }
            } else {
                _exchangedCouponData.value = null
            }
        } else {
            _exchangedCouponData.value = null
        }
    }

    fun getExpiredCoupon() {
        if (_couponData.value != null) {
            _exchangedCouponData.value = _couponData.value?.filter { isExpired(it.couponEndDate) }
        } else {
            _exchangedCouponData.value = null
        }
    }

    private fun isExpired(couponDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val today = sdf.format(Date())
        val currentDate: Date = sdf.parse(today)

        val couponDay: Date = sdf.parse(couponDate)!!

        if (couponDay < currentDate) {
            return true
        }

        return false
    }

    fun getParkingFeePaidList(
        context: Context,
        memberId: String,
        memberPassword: String,
        plateNo: String,
        parkingId: String
    ) {
        val call: Call<List<ParkingFeePaidVO>> = ApiUtility.service.apiGetParkingFeePaidList(
            memberId,
            memberPassword,
            plateNo,
            parkingId
        )
        call.enqueue(object : Callback<List<ParkingFeePaidVO>> {
            override fun onResponse(
                call: Call<List<ParkingFeePaidVO>>,
                response: Response<List<ParkingFeePaidVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _parkingFeePaidData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<ParkingFeePaidVO>>, t: Throwable) {
                _parkingFeePaidData.value = null
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                )
            }
        })
    }

    fun getParkingFeePaidDetail(
        context: Context,
        billNo: String,
        loginPassword: String,
        billNo1: String
    ) {
        val call: Call<List<ParkingFeePaidDetailVO>> =
            ApiUtility.service.apiGetParkingFeePaidDetail(billNo)
        call.enqueue(object : Callback<List<ParkingFeePaidDetailVO>> {
            override fun onResponse(
                call: Call<List<ParkingFeePaidDetailVO>>,
                response: Response<List<ParkingFeePaidDetailVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _parkingFeePaidDetailData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<ParkingFeePaidDetailVO>>, t: Throwable) {
                _parkingFeePaidDetailData.value = null
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                )
            }
        })
    }

    fun logout(context: Context) {
        var id =AppUtility.getLoginId(context)
        var pwd =AppUtility.getLoginPassword(context)
        val call: Call<LogoutResponse> = ApiUtility.service.apiLogout(
            AppUtility.getLoginId(context)!!,
            AppUtility.getLoginPassword(context)!!
        )
        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0201 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
                        Glob.clearMemberInfo(context)
                        clearData()
                    } else {
                        _logoutData.value = response.body()
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun forgetPassword(
        context: Context,
        memberId: String,
        memberPassword: String
    ) {
        val call: Call<ForgetPasswordResponse> = ApiUtility.service.apiForgetPassword(
            memberId,
            memberPassword
        )
        call.enqueue(object : Callback<ForgetPasswordResponse> {
            override fun onResponse(
                call: Call<ForgetPasswordResponse>,
                response: Response<ForgetPasswordResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0201 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
                    } else {
                        _forgetPasswordData.value = response.body()
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun getPlateNumber(
        context: Context,
        memberId: String,
        memberPassword: String,
        parkingType: String
    ) {
        val call: Call<List<PlateNumberVO>> = ApiUtility.service.apiGetPlateNumber(
            memberId,
            memberPassword,
            parkingType
        )
        call.enqueue(object : Callback<List<PlateNumberVO>> {
            override fun onResponse(
                call: Call<List<PlateNumberVO>>,
                response: Response<List<PlateNumberVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _plateNumberData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<PlateNumberVO>>, t: Throwable) {
                println("---------$call")
                println("---------$call")
                println("---------$call")

                _plateNumberData.value = null
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                )
            }
        })
    }

    fun clearPlateNoList() {
        _plateNumberData.value = null
    }

    fun addPlateNo(
        context: Context,
        memberId: String,
        memberPassword: String,
        plateNo: String,
        parkingType: String
    ) {
        val call: Call<AddPlateNoResponse> = ApiUtility.service.apiAddPlateNumber(
            memberId,
            memberPassword,
            plateNo,
            parkingType
        )
        call.enqueue(object : Callback<AddPlateNoResponse> {
            override fun onResponse(
                call: Call<AddPlateNoResponse>,
                response: Response<AddPlateNoResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
//                    } else if (response.body()!!.code == ApiConfig.API_CODE_0x0201){
//                        Toast.makeText(
//                            requireActivity(),
//                            "目前沒有符合的紀錄唷！",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    } else {
                        _addPlateNoData.value = response.body()
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<AddPlateNoResponse>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun getParkingRoadFeeUnPaidList(
        context: Context,
        plateNo: String
    ) {
        val call: Call<ParkingRoadFeeUnPaidResponse> =
            ApiUtility.service.apiGetParkingRoadFeeUnPaidList(plateNo)
        call.enqueue(object : Callback<ParkingRoadFeeUnPaidResponse> {
            override fun onResponse(
                call: Call<ParkingRoadFeeUnPaidResponse>,
                response: Response<ParkingRoadFeeUnPaidResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    hasData = true
                    _parkingRoadFeeUnPaidData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<ParkingRoadFeeUnPaidResponse>, t: Throwable) {
//                _parkingRoadFeeUnPaidData.value = null
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun getParkingGarageFeeUnPaidList(
        context: Context,
        plateNo: String,
        garageId: String
    ) {
        val call: Call<ParkingGarageFeeUnPaidResponse> =
            ApiUtility.service.apiGetParkingGarageFeeUnPaidList(
                plateNo,
                garageId
            )
        call.enqueue(object : Callback<ParkingGarageFeeUnPaidResponse> {
            override fun onResponse(
                call: Call<ParkingGarageFeeUnPaidResponse>,
                response: Response<ParkingGarageFeeUnPaidResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    hasData = true
                    _parkingGarageFeeUnPaidData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<ParkingGarageFeeUnPaidResponse>, t: Throwable) {
//                _parkingGarageFeeUnPaidData.value = null
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun deleteParkingFeeUnPaidPlate(
        context: Context,
        memberId: String,
        memberPassword: String,
        plateNo: String,
        parkingType: String
    ) {
        val call: Call<DeletePlateNumberResponse> =
            ApiUtility.service.apiDeletePlateNumber(
                memberId,
                memberPassword,
                plateNo,
                parkingType
            )
        call.enqueue(object : Callback<DeletePlateNumberResponse> {
            override fun onResponse(
                call: Call<DeletePlateNumberResponse>,
                response: Response<DeletePlateNumberResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0201 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
                    } else {
                        _deletePlateNumberData.value = response.body()
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<DeletePlateNumberResponse>, t: Throwable) {
                _deletePlateNumberData.value = null
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun clearDeletePlateData() {
        _deletePlateNumberData.value = null
    }

    fun getParkingGarage(context: Context) {
        val call: Call<ParkingGarageResponse> = ApiUtility.service.apiGetParkingGarageList()

        call.enqueue(object : Callback<ParkingGarageResponse> {
            override fun onResponse(
                call: Call<ParkingGarageResponse>,
                response: Response<ParkingGarageResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _parkingGarageData.value = response.body()!!.parkingGarageData
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<ParkingGarageResponse>, t: Throwable) {
                println("---------$call")
                println("---------$call")
                println("---------$call")

                _parkingGarageData.value = null
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                )
            }
        })
    }

    fun clearParkingGarageList() {
        _parkingGarageData.value = null
    }

    fun getParkingInfoLeftList(context: Context) {
        val call: Call<ParkingInfoBuildingResponse> =
            ApiUtility.service.apiGetParkingSpaceList()
        call.enqueue(object : Callback<ParkingInfoBuildingResponse> {
            override fun onResponse(
                call: Call<ParkingInfoBuildingResponse>,
                response: Response<ParkingInfoBuildingResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _buildingParkingSpaceData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<ParkingInfoBuildingResponse>, t: Throwable) {
                _buildingParkingSpaceData.value = null
            }
        })

    }

    fun clearParkingInfoList() {
        _buildingParkingSpaceData.value = null
    }

    fun getMainBannerData(
        context: Context
//        memberId: String,
//        memberPassword: String
    ) {
        val call: Call<List<BannerVO>> = ApiUtility.service.apiGetBannerList()
//            memberId,
//            memberPassword
//        )
        call.enqueue(object : Callback<List<BannerVO>> {
            override fun onResponse(
                call: Call<List<BannerVO>>,
                response: Response<List<BannerVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _bannerData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<BannerVO>>, t: Throwable) {
                _bannerData.value = null
            }
        })
    }

    fun getStoreData(
        context: Context,
        storeId: String
    ) {
        val call: Call<List<StoreVO>> = ApiUtility.service.apiGetStoreList(storeId)
        call.enqueue(object : Callback<List<StoreVO>> {
            override fun onResponse(
                call: Call<List<StoreVO>>,
                response: Response<List<StoreVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _storeData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<StoreVO>>, t: Throwable) {
                _storeData.value = null
            }
        })
    }

    fun updatePassword(
        context: Context,
        memberId: String,
        memberOldPassword: String,
        memberNewPassword: String
    ) {
        val call: Call<UpdatePasswordResponse> = ApiUtility.service.apiUpdatePassword(
            memberId,
            memberOldPassword,
            memberNewPassword
        )
        call.enqueue(object : Callback<UpdatePasswordResponse> {
            override fun onResponse(
                call: Call<UpdatePasswordResponse>,
                response: Response<UpdatePasswordResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    if (
                        response.body()!!.code == ApiConfig.API_CODE_0x0201 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0202 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0203 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0204 ||
                        response.body()!!.code == ApiConfig.API_CODE_0x0206
                    ) {
                        AppUtility.showPopDialog(
                            context,
                            response.body()!!.code,
                            response.body()!!.responseMessage
                        )
                    } else {
                        _updatePasswordData.value = response.body()
                    }
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )

                }
            }

            override fun onFailure(call: Call<UpdatePasswordResponse>, t: Throwable) {
                _updatePasswordData.value = null
//                AppUtility.showPopDialog(
//                    context,
//                    null,
//                    ApiUtility.apiFailureMessage(call, t)
//                    context.resources.getString(R.string.default_response_message)
//                )
            }
        })
    }

    fun clearUpdatePasswordData() {
        _updatePasswordData.value = null
    }

    fun lockBill(
        context: Context,
        plateNo: String,
        billNo: String
    ) {
        val call: Call<LockBillResponse> = ApiUtility.service.apiLockBill(
            plateNo,
            billNo
        )
        call.enqueue(object : Callback<LockBillResponse> {
            override fun onResponse(
                call: Call<LockBillResponse>,
                response: Response<LockBillResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _billIsLock.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<LockBillResponse>, t: Throwable) {
//                _parkingRoadFeeUnPaidData.value = null

            }
        })
    }

//    fun clearUnPaidData() {
//        if (_parkingRoadFeeUnPaidData.value != null) {
//            _parkingRoadFeeUnPaidData.value = null
//        }
//    }

    fun reimburseCoupon(context: Context) {
        val call: Call<WriteOffTicketResponse> = ApiUtility.service.apiManagerWriteOffTicket(
            AppUtility.getLoginId(context)!!,
            AppUtility.getLoginPassword(context)!!,
            AppUtility.getWriteOffCouponNo(context)!!
        )
        call.enqueue(object : Callback<WriteOffTicketResponse> {
            override fun onResponse(
                call: Call<WriteOffTicketResponse>,
                response: Response<WriteOffTicketResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _applyCouponData.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<WriteOffTicketResponse>, t: Throwable) {
                _applyCouponData.value = null
            }
        })
    }

    fun clearApplyCouponData() {
        _applyCouponData.value = null
    }

    fun getAttractions(context: Context) {
        val call: Call<List<AttractionVO>> = ApiUtility.service.apiGetAttractions()
        call.enqueue(object : Callback<List<AttractionVO>> {
            override fun onResponse(
                call: Call<List<AttractionVO>>,
                response: Response<List<AttractionVO>>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                if (response.body() != null) {
                    _attractions.value = response.body()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        statusCode.toString(),
                        null
                    )
                }
            }

            override fun onFailure(call: Call<List<AttractionVO>>, t: Throwable) {
                _attractions.value = null
            }
        })
    }


    fun deleteAccount(context: Context) {
        val call: Call<BaseResponse> = ApiUtility.service.apiMemberDelete(
            AppUtility.getLoginId(context)!!,
            AppUtility.getLoginPassword(context)!!
        )
        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                val statusCode = response.code()
                val url = response.raw().request.url.toString()
                Log.d("目前 status code & URL 是", "\n" + statusCode + "\n" + url)

                var responseMessage =response.body()!!.responseMessage
                _isDelete.value =
                    response.body() != null && response.body()!!.code == ApiConfig.API_CODE_SUCCESS
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                _isDelete.value = null
            }
        })
    }

    fun clearDeleteAccount() {
        _isDelete.value = null
    }

    fun getStorePointRecords(
        context: Context,
        memberId: String,
        memberPwd: String,
        startDate: String,
        endDate: String
    ) {
        val call: Call<StorePointRecordResponse> = ApiUtility.service.apiStorePointRecord(
            memberId,
            memberPwd,
            startDate,
            endDate
        )

        call.enqueue(object : Callback<StorePointRecordResponse> {
            override fun onResponse(
                call: Call<StorePointRecordResponse>,
                response: Response<StorePointRecordResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _pointRecordsData.value = response.body()?.data ?: emptyList()
                } else {
                    AppUtility.showPopDialog(
                        context,
                        response.code().toString(),
                        response.message()
                    )
                }
            }

            override fun onFailure(call: Call<StorePointRecordResponse>, t: Throwable) {
                AppUtility.showPopDialog(
                    context,
                    null,
                    ApiUtility.apiFailureMessage(call, t)
                )
            }
        })
    }

    fun fetchPointRecords(memberId: String, memberPwd: String, pointType: String) {
        val call = ApiUtility.service.getPointRecords(memberId, memberPwd, pointType)
        call.enqueue(object : Callback<ApiResponse<List<PointRecord2Response>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<PointRecord2Response>>>,
                response: Response<ApiResponse<List<PointRecord2Response>>>
            ) {
                if (response.isSuccessful && response.body()?.status == "true") {
                    val data = response.body()?.data ?: emptyList()
                    _pointRecords.postValue(data)
                } else {
                    _pointRecords.postValue(emptyList())
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<List<PointRecord2Response>>>,
                t: Throwable
            ) {
                _pointRecords.postValue(emptyList())
            }
        })
    }

    fun fetchMemberInfo(memberId: String, memberPwd: String) {
        val call = ApiUtility.service.getMemberInfo(memberId, memberPwd)
        call.enqueue(object : Callback<List<MemberInfo>> {
            override fun onResponse(
                call: Call<List<MemberInfo>>,
                response: Response<List<MemberInfo>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _memberInfo.postValue(it)
                    } ?: run {
                        // Handle case where response is successful but body is null
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                            _errorResponse.postValue(error)
                        }
                    }
                } else {
                    // Handle API error
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        _errorResponse.postValue(error)
                    }
                }
            }

            override fun onFailure(call: Call<List<MemberInfo>>, t: Throwable) {
                // Handle network failure
                _errorResponse.postValue(
                    ErrorResponse(
                        status = "false",
                        code = "NETWORK_ERROR",
                        responseMessage = t.localizedMessage ?: "Unknown error"
                    )
                )
            }
        })
    }

    fun fetchUserPoints(memberId: String, memberPwd: String, pointNum: String, pointType: String) {
        val call = ApiUtility.service.getUserPoints(memberId, memberPwd, pointNum, pointType)
        call.enqueue(object : Callback<GetPointResponse> {
            override fun onResponse(call: Call<GetPointResponse>, response: Response<GetPointResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _getPointResponse.postValue(response.body())
                } else {
                    // Handle error when response is not successful
                    _getPointResponse.postValue(
                        GetPointResponse(
                            status = "false",
                            code = "ERROR",
                            responseMessage = "Unexpected error occurred"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<GetPointResponse>, t: Throwable) {
                // Handle network failure
                _getPointResponse.postValue(
                    GetPointResponse(
                        status = "false",
                        code = "NETWORK_ERROR",
                        responseMessage = t.localizedMessage ?: "Network error"
                    )
                )
            }
        })
    }
}