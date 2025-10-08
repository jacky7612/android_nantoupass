package com.jotangi.nantoupass.config

class AppConfig {
    companion object {
        const val spref = "com.jotangi.nantoupass"

        const val IS_LOGIN = "is_login"
        const val LOGIN_ID = "login_id"
        const val LOGIN_NAME = "login_name"
        const val LOGIN_PW = "login_pw"
        const val LOGIN_TYPE = "login_type"
        const val WRITE_OFF_COUPON_NO = "write_off_coupon_no"
        const val LOADING_STATUS = "loading_status"

        // 付款
        const val LINE_URL = "market://details?id=jp.naver.line.android"

        // 停車資訊
        const val ZHU_DONG_TOWN_OFFICE_GPS = "24.73704328258781, 121.092230017034"
        const val ZHU_DONG_TOWN_OFFICE_LONGITUDE = "24.73704328258781"
        const val ZHU_DONG_TOWN_OFFICE_LATITUDE = "121.092230017034"
        const val ZHU_DONG_BUILDING_OFFICE_LONGITUDE = "24.735606828067137"
        const val ZHU_DONG_BUILDING_OFFICE_LATITUDE = "121.09073115770094"

        // 跳轉
        const val NUMBER_HEALTH = "com.jotangi.NumberHealthy"
        const val BRAIN_BLE = "com.darin.OberonBLE.Ent"
        const val BRAIN_BLE_DOWNLOAD = "https://oberonhc.com/OberonBLE/OberonBLE_1.3.24.apk"
        const val BRAIN_USB = "com.darin.OberonUSB"
        const val BRAIN_USB_DOWNLOAD = "https://oberonhc.com/OberonUSB/OberonUSB_2.0.38.apk"

        const val AREA_MAIN_WEB = "https://www.ntc.gov.tw/"
        const val MAYOR_LINE_AT = "https://lin.ee/xcy8Amo"
        const val MAYOR_FB = "https://www.facebook.com/jimbest26"
        const val CUSTOMER_SERVICE_PHONE = "0437025499"

    }
}