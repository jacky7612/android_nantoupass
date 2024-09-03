package com.jotangi.nantouparking.JackyVariant

import android.app.Activity
import com.jotangi.payStation.Model.ApiModel.ApiRespGovPlateListOK

object Glob {
    lateinit var activity: Activity
    var title: String =""
    lateinit var apiGovPayList: ApiRespGovPlateListOK
    var queryHistoryPlateNo =""
}