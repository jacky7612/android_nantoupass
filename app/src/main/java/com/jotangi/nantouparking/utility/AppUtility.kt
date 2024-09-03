package com.jotangi.nantouparking.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import com.jotangi.nantouparking.config.AppConfig

object AppUtility {
    private fun getSpref(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            AppConfig.spref,
            Context.MODE_PRIVATE
        )
    }

    fun updateLoginStatus(
        context: Context,
        isLogin: Boolean
    ) {
        getSpref(context).edit()
            .putBoolean(
                AppConfig.IS_LOGIN,
                isLogin
            )
            .commit()
    }

    fun updateLoginId(
        context: Context,
        loginId: String
    ) {
        val updateLoginId = update886PhoneNumber(loginId)

        getSpref(context).edit()
            .putString(
                AppConfig.LOGIN_ID,
                updateLoginId
            )
            .commit()
    }

    fun updateLoginName(
        context: Context,
        loginName: String
    ) {
        getSpref(context).edit()
            .putString(
                AppConfig.LOGIN_NAME,
                loginName
            )
            .commit()
    }

    fun updateLoginPassword(
        context: Context,
        loginPw: String
    ) {
        getSpref(context).edit()
            .putString(
                AppConfig.LOGIN_PW,
                loginPw
            )
            .commit()
    }

    fun updateLoginType(
        context: Context,
        type: String
    ) {
        getSpref(context).edit()
            .putString(
                AppConfig.LOGIN_TYPE,
                type
            )
            .commit()
    }

//    fun updateLoginLicensePlate(){
//
//    }

    fun updateWriteOffCouponNo(
        context: Context,
        couponNo: String
    ) {
        getSpref(context).edit()
            .putString(
                AppConfig.WRITE_OFF_COUPON_NO,
                couponNo
            )
            .commit()
    }

    fun updateLoadingStatus(
        context: Context,
        loadingStatus: Boolean
    ) {
        getSpref(context).edit()
            .putBoolean(
                AppConfig.LOADING_STATUS,
                loadingStatus
            )
            .commit()
    }

    fun getLoginStatus(context: Context): Boolean {
        return getSpref(context).getBoolean(
            AppConfig.IS_LOGIN,
            false
        )
    }

    fun getLoginId(context: Context): String? {
        val curLoginId = getSpref(context).getString(
            AppConfig.LOGIN_ID,
            ""
        )

        return update886PhoneNumber(curLoginId!!)
    }

    fun getLoginName(context: Context): String? {
        return getSpref(context).getString(
            AppConfig.LOGIN_NAME,
            ""
        )
    }

    fun getLoginPassword(context: Context): String? {
        return getSpref(context).getString(
            AppConfig.LOGIN_PW,
            ""
        )
    }

    fun getLoginType(context: Context): String? {
        return getSpref(context).getString(
            AppConfig.LOGIN_TYPE,
            ""
        )
    }

    fun getWriteOffCouponNo(context: Context): String? {
        return getSpref(context).getString(
            AppConfig.WRITE_OFF_COUPON_NO,
            ""
        )
    }

    fun getLoadingStatus(context: Context): Boolean? {
        return getSpref(context).getBoolean(
            AppConfig.LOADING_STATUS,
            false
        )
    }
//    fun updateMemberInfo(
//        context: Context,
//        isLogin: Boolean,
//        result: Any
//    ) {
//        if (result.code == ApiConfig.API_CODE_SUCCESS.toString()) {
//            AppUtility.updateLoginStatus(
//                context,
//                isLogin
//            )
//
//            AppUtility.updateLoginId(
//                requireContext(),
//                binding?.loginIdEditText?.text.toString()
//            )
//            AppUtility.updateLoginPassword(
//                requireContext(),
//                binding?.loginPasswordEditText?.text.toString()
//            )
//
//            showPrivateDialog(
//                result.responseMessage,
//                null
//            )
//        } else {
//            AppUtility.showPopDialog(
//                requireContext(),
//                result.code.toInt(),
//                result.responseMessage
//            )
//        }
//    }

    fun showPopDialog(
        context: Context,
        code: String?,
        message: String?
    ) {
//        val serverMessage = when (code) {
//            ApiConfig.API_CODE_0x0201 -> "已註冊成功"
//            ApiConfig.API_CODE_0x0202 -> context.resources.getString(R.string.signup_response_202)
//            ApiConfig.API_CODE_0x0203 -> context.resources.getString(R.string.signup_response_203)
//            ApiConfig.API_CODE_0x0204 -> context.resources.getString(R.string.signup_response_204)
//            ApiConfig.API_CODE_0x0206 -> context.resources.getString(R.string.signup_response_206)
//            ApiConfig.API_CODE_NOT_FOUND -> context.resources.getString(R.string.public_response_404)
//            null -> message
//            else -> message
//        }

        val alert = AlertDialog.Builder(context)

        alert.setTitle("提醒")
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->

        }

        alert.show()
    }

    //    fun allPayEncrypt(data: String): String? {
//        return try {
//            val cipher = Cipher.getInstance(tw.com.rilink.Keychain.AllPayCipherMode)
//            val keyspec =
//                SecretKeySpec(Base64.decode(tw.com.rilink.Keychain.key, Base64.DEFAULT), "AES")
//            cipher.init(
//                Cipher.ENCRYPT_MODE,
//                keyspec,
//                IvParameterSpec(tw.com.rilink.Keychain.iv.toByteArray())
//            )
//            val encrypted = cipher.doFinal(data.toByteArray())
//            Base64.encodeToString(encrypted, Base64.NO_WRAP)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
    fun passwordValid(password: String): Boolean {
        val rPassword = Regex(
            "[a-z0-9]{6,12}",
            RegexOption.IGNORE_CASE
        )

        return rPassword.matches(password)
    }

    private fun update886PhoneNumber(phoneNumber: String): String {
        var newNumber = phoneNumber
        if (newNumber.contains("+886")) {
            newNumber = newNumber.replace("+886", "0")
        }
        return newNumber
    }
}