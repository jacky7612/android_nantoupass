package com.jotangi.nantouparking.config

import com.jotangi.nantouparking.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiConfig {
    companion object {
        // 正式
        var URL_HOST = "https://hcparking.jotangi.net/parking_nantou/"
//            if (BuildConfig.DEBUG) {
//            "https://hcparking.jotangi.net/parking_test/"
//        } else {
//            "https://hcparking.jotangi.net/parking/"
//        }
        const val TEST_URL_HOST = "https://hcparking.jotangi.net/parking_test/"
        const val REN_AI_URL_HOST = "http://103.124.73.151/parkingman/"
        const val API_CODE_SUCCESS = "0x0200"
        const val API_CODE_0x0201 = "0x0201"
        const val API_CODE_0x0202 = "0x0202"
        const val API_CODE_0x0203 = "0x0203"
        const val API_CODE_0x0204 = "0x0204"
        const val API_CODE_0x0206 = "0x0206"
        const val API_CODE_NOT_FOUND = "404"
        const val PAYMENT_URL = "pay_bill_E.php"
    }

    // 登入
    @POST("api/user_login.php")
    @FormUrlEncoded
    fun apiLogin(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("FCM_Token") fcmToken: String
    ): Call<LoginResponse>

    // region 一般會員
    // 取得會員資料
    @POST("api/member_info.php")
    @FormUrlEncoded
    fun apiGetMemberInfo(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String
    ): Call<List<MemberInfoVO>>

    // 編輯會員資料
    @POST("api/user_edit.php")
    @FormUrlEncoded
    fun apiEditMemberInfo(
        @Field("member_name") memberName: String,
        @Field("member_email") memberEmail: String,
        @Field("member_id") memberId: String,
        @Field("member_plateNo") memberPlateNo: String,
        @Field("member_pwd") memberPwd: String,
        @Field(" member_carrier")  memberCarrier: String

    ): Call<MemberInfoEditResponse>

    // 修改密碼
    @POST("api/user_changepwd.php")
    @FormUrlEncoded
    fun apiUpdatePassword(
        @Field("member_id") memberId: String,
        @Field("old_password") oldPwd: String,
        @Field("new_password") newPwd: String
    ): Call<UpdatePasswordResponse>


    // 取得優惠券資料
    @POST("api/mycoupon_list.php")
    @FormUrlEncoded
    fun apiGetCoupon(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
    ): Call<List<CouponVO>>

    // 取得已繳費紀錄
    @POST("api/bill_list_E.php")
    @FormUrlEncoded
    fun apiGetParkingFeePaidList(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("plateNo") plateNo: String,
        @Field("plateId") parkingId: String
    ): Call<List<ParkingFeePaidVO>>

    // 取得已繳費紀錄-縣府
    @GET("api/bills_paid_record.php")
    @FormUrlEncoded
    fun apiGetGovParkingFeePaidList(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("plateNo") plateNo: String,
        @Field("plateId") parkingId: String
    ): Call<List<GovParkingFeePaidVO>>

    // 取得已繳費紀錄明細
    @POST("api/bill_info_E.php")
    @FormUrlEncoded
    fun apiGetParkingFeePaidDetail(@Field("bill_no") orderNo: String): Call<List<ParkingFeePaidDetailVO>>

    // 登出
    @POST("api/user_logout.php")
    @FormUrlEncoded
    fun apiLogout(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
    ): Call<LogoutResponse>

    // 註冊
    @POST("api/user_register.php")
    @FormUrlEncoded
    fun apiSignup(
        @Field("member_name") memberName: String,
        @Field("member_email") memberEmail: String,
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("plate_number") memberPlateNumber: String,
        @Field("carrier") carrier: String
    ): Call<SignupResponse>

    // 忘記密碼
    @POST("api/user_changepwd.php")
    @FormUrlEncoded
    fun apiForgetPassword(
        @Field("member_id") memberId: String,
        @Field("new_password") newPwd: String
    ): Call<ForgetPasswordResponse>

    // 取得會員車牌記錄
    @POST("api/plate_number_E.php")
    @FormUrlEncoded
    fun apiGetPlateNumber(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("type") parkingType: String
    ): Call<List<PlateNumberVO>>

    // 新增會員車牌
    @POST("api/plate_number_add_E.php")
    @FormUrlEncoded
    fun apiAddPlateNumber(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("plate_number") plateNo: String,
        @Field("type") parkingType: String
    ): Call<AddPlateNoResponse>

    // 刪除會員車牌
    @POST("api/plate_number_delete_E.php")
    @FormUrlEncoded
    fun apiDeletePlateNumber(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("plate_number") plateNo: String,
        @Field("type") parkingType: String
    ): Call<DeletePlateNumberResponse>

    // 取得未繳費紀錄
//    @GET("api/search_plateNo_E.php")

    // 取得路邊未繳費記錄
    @GET("api/searchRoadPlateNo.php")
    fun apiGetParkingRoadFeeUnPaidList(@Query("plateNo") plateNo: String): Call<ParkingRoadFeeUnPaidResponse>

    // 取得停車場未繳費紀錄
    @GET("api/searchParkPlateNo.php")
    fun apiGetParkingGarageFeeUnPaidList(
        @Query("plateNo") plateNo: String,
        @Query("plateId") garageId: String
    ): Call<ParkingGarageFeeUnPaidResponse>

    // 取得 banner
    @POST("api/banner_list.php")
    fun apiGetBannerList(): Call<List<BannerVO>>

    // 取得商店清單
    @POST("api/store_list.php")
    @FormUrlEncoded
    fun apiGetStoreList(@Field("store_id") storeId: String): Call<List<StoreVO>>

    // 取得車格數量
    @POST("api/road_park_status.php")
    fun apiGetParkingSpaceList(): Call<ParkingInfoBuildingResponse>

    // 取得停車場資訊
    @GET("api/searchParkingLot.php")
    fun apiGetParkingGarageList(): Call<ParkingGarageResponse>

    // 鎖定繳費單
    @GET("api/lock_bill_E.php")
    fun apiLockBill(
        @Query("plate_number") plateNo: String,
        @Query("bill_no") billNo: String
    ): Call<LockBillResponse>

    // 取得景點列表
    @POST("api/store_list1_E.php")
//    @FormUrlEncoded
    fun apiGetAttractions(): Call<List<AttractionVO>>
    // endregion

    // region 店長核銷
    @POST("api/apply_coupon2.php")
    @FormUrlEncoded
    fun apiManagerWriteOffTicket(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("coupon_no") couponNo: String
    ): Call<WriteOffTicketResponse>
    // endregion

    // region 刪除帳號
    @POST("api/user_delete.php")
    @FormUrlEncoded
    fun apiMemberDelete(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String
    ): Call<BaseResponse>
    // endregion

    @POST("api/store_point_record.php")
    @FormUrlEncoded
    fun apiStorePointRecord(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("start_date") startDate: String,
        @Field("end_date") endDate: String
    ): Call<StorePointRecordResponse>

    @FormUrlEncoded
    @POST("api/member_point_list.php")
    fun getPointRecords(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("point_type") pointType: String
    ): Call<ApiResponse<List<PointRecord2Response>>>

    @FormUrlEncoded
    @POST("api/member_info.php")
    fun getMemberInfo(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String
    ): Call<List<MemberInfo>>

    @FormUrlEncoded
    @POST("api/user_point_get.php")
    fun getUserPoints(
        @Field("member_id") memberId: String,
        @Field("member_pwd") memberPwd: String,
        @Field("point_num") pointNum: String,
        @Field("point_type") pointType: String
    ): Call<GetPointResponse>

    @FormUrlEncoded
    @POST("api/user_point_use.php")
    fun useUserPoints(
        @Field("member_id") memberId: String,
        @Field("store_id") storeId: String,
        @Field("point_num") pointNum: String,
        @Field("product_price") productPrice: String
    ): Call<UsePointResponse>

    @GET("api/all_park_status.php")
    fun getAllParkStatus(): Call<AllParkStatusResponse>
}