package com.jotangi.nantouparking.JackyVariant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.jotangi.nantouparking.utility.AppUtility
import com.jotangi.payStation.Model.ApiModel.ApiRespGovPlateListOK

@SuppressLint("StaticFieldLeak")
object Glob {
    lateinit var activity: Activity
    var title: String =""
    lateinit var apiGovPayList: ApiRespGovPlateListOK
    var queryHistoryPlateNo =""

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
}