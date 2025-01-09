package com.jotangi.nantouparking

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jotangi.nantouparking.map.MapChargeParkingFragment2

class ParkingInfoBottomSheet : BottomSheetDialogFragment() {

    private var status: String? = null
    private var address: String? = null
    private var code: String? = null
    private var updateTime: String? = null
    private var position: LatLng? = null

    fun init(status: String, address: String, code: String, updateTime: String, position: LatLng) {
        this.status = status
        this.address = address
        this.code = code
        this.updateTime = updateTime
        this.position = position
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parking_info2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var space: String = ""
        if(status.equals("0")) {
            space = "空位"
        } else {
            space = "沒空位"
        }
        if (MapChargeParkingFragment2.type.equals("Road")) {
            view.findViewById<TextView>(R.id.tv_status).text = space
            view.findViewById<TextView>(R.id.tv_address).text = address
            view.findViewById<TextView>(R.id.tv_address).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

            view.findViewById<TextView>(R.id.tv_code).text = code
            view.findViewById<TextView>(R.id.tv_update_time).text = "更新時間：$updateTime"
            view.findViewById<LinearLayout>(R.id.goMap).setOnClickListener {
                position?.let { latLng ->
                    // Create a URI for Google Maps
                    val uri =
                        "geo:${latLng.latitude},${latLng.longitude}?q=${latLng.latitude},${latLng.longitude}(${address})"
                    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps") // Ensure Google Maps app is used
                    startActivity(intent)
                } ?: run {
                    // Handle null position case
                    context?.let {
                        android.widget.Toast.makeText(
                            it,
                            "位置數據無效",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            val drawable = if (status.equals("0")) { // Replace `someCondition` with your logic
                context?.let { ContextCompat.getDrawable(it, R.drawable.road_parking) }
            } else {
                context?.let { ContextCompat.getDrawable(it, R.drawable.road_parking2) }
            }
            view.findViewById<ImageView>(R.id.status_dot).setImageDrawable(
                drawable
            )
        } else {
            view.findViewById<TextView>(R.id.tv_status).text = "剩餘" + status + "車位"
            view.findViewById<TextView>(R.id.tv_address).text = address
            view.findViewById<TextView>(R.id.tv_code).visibility = View.GONE
            view.findViewById<TextView>(R.id.tv_update_time).text = "更新時間：$updateTime"
            view.findViewById<LinearLayout>(R.id.goMap).setOnClickListener {
                position?.let { latLng ->
                    // Create a URI for Google Maps
                    val uri =
                        "geo:${latLng.latitude},${latLng.longitude}?q=${latLng.latitude},${latLng.longitude}(${address})"
                    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps") // Ensure Google Maps app is used
                    startActivity(intent)
                } ?: run {
                    // Handle null position case
                    context?.let {
                        android.widget.Toast.makeText(
                            it,
                            "位置數據無效",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            val drawable = if (status.equals("0")) { // Replace `someCondition` with your logic
                context?.let { ContextCompat.getDrawable(it, R.drawable.road_parking) }
            } else {
                context?.let { ContextCompat.getDrawable(it, R.drawable.road_parking2) }
            }
            view.findViewById<ImageView>(R.id.status_dot).setImageDrawable(
                drawable
            )
        }
    }

}
