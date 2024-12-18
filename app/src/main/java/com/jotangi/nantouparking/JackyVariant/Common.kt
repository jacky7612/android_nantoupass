package com.jotangi.nantouparking.JackyVariant

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ImageView
import coil.ImageLoader
import coil.request.ImageRequest
import com.jotangi.payStation.Model.ApiModel.ApiRespStoreListOK
import com.jotangi.payStation.Model.ApiModel.ApiRespStoreListStatusOK
import com.jotangi.payStation.Model.ApiModel.DataStoreInfo
import com.jotangi.payStation.Model.ApiModel.DataStoreInfo4SimpleShow

@SuppressLint("StaticFieldLeak")
object Common {
    lateinit var activity: Activity
    var title: String =""
    var store_type: String =""
    var SelPosition = -1
    var detail_data: DataStoreInfo =DataStoreInfo()
    fun clearDetailData() {
        detail_data.store_id            =""
        detail_data.store_name          =""
        detail_data.store_phone         =""
        detail_data.store_website       =""
        detail_data.store_open          =""
        detail_data.store_description   =""
        detail_data.store_open_time     =""
        detail_data.store_picture       =""
        detail_data.store_address       =""
        detail_data.store_latitude      =""
        detail_data.store_longitude     =""
    }
}
object generateImg {
    fun SetImage(urlString: String, dstShowImg: ImageView) {
        // 使用 Coil 加载图像
        val imageLoader = ImageLoader.Builder(Common.activity)
            .crossfade(true) // 设置淡入淡出效果
            .build()

        val imageRequest = ImageRequest.Builder(Common.activity)
            .data(urlString)
            .target(dstShowImg)
            .build()

        imageLoader.enqueue(imageRequest)
    }
}