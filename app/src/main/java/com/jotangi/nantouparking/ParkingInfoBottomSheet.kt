package com.jotangi.nantouparking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ParkingInfoBottomSheet : BottomSheetDialogFragment() {

    private var status: String? = null
    private var address: String? = null
    private var code: String? = null
    private var updateTime: String? = null

    fun init(status: String, address: String, code: String, updateTime: String) {
        this.status = status
        this.address = address
        this.code = code
        this.updateTime = updateTime
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parking_info2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var space:String = ""
if(status.equals("0")) {
    space = "空位"
} else {
    space = "沒空位"
}
        view.findViewById<TextView>(R.id.tv_status).text = space
        view.findViewById<TextView>(R.id.tv_address).text = address
        view.findViewById<TextView>(R.id.tv_code).text = code
        view.findViewById<TextView>(R.id.tv_update_time).text = "更新時間：$updateTime"
    }
}
