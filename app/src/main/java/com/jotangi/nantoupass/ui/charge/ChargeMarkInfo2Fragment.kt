package com.jotangi.nantoupass.ui.charge

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jotangi.nantoupass.databinding.FragmentChargeMarkInfo2Binding
import com.jotangi.nantoupass.model.charge.DataChargeStatusInfo

/**
 * A simple [Fragment] subclass.
 * Use the [ChargeMarkInfo2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChargeMarkInfo2Fragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChargeMarkInfo2Binding? = null
    private val binding get() = _binding!!
    private var mytitle: String = ""
    private var mysnippet: String = ""
    private var myLatLng: LatLng? = null
    private var charger_status_info: MutableList<DataChargeStatusInfo> = mutableListOf()

    fun init(title: String, snippet: String, latlng: LatLng, charge_info:List<DataChargeStatusInfo>) {
        mytitle = title
        mysnippet = snippet
        myLatLng = latlng
        charger_status_info = charge_info.toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargeMarkInfo2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            markerTitle.text = mytitle
//            markerSnippet.text = mysnippet
            for (i in 0 until charger_status_info.count()) {
                val item = charger_status_info[i]
                // 這裡可以使用 item
                if (i == 0) {
                    tvHead01.text = "${charger_status_info[i].type}樁 ${charger_status_info[i].detail.name} 共 ${charger_status_info[i].detail.gun_count} 槍" // AC樁 J1772  共 10 樁
                    tvAvalible01.text = "${charger_status_info[i].detail.avalible_count} 可用"
                    lay03.visibility = View.VISIBLE
                }
                if (i == 1) {
                    tvHead02.text = "${charger_status_info[i].type}樁 ${charger_status_info[i].detail.name} 共 ${charger_status_info[i].detail.gun_count} 槍" // AC樁 J1772  共 10 樁
                    tvAvalible02.text = "${charger_status_info[i].detail.avalible_count} 可用"
                    lay04.visibility = View.VISIBLE
                }
                if (i == 2) {
                    tvHead03.text = "${charger_status_info[i].type}樁 ${charger_status_info[i].detail.name} 共 ${charger_status_info[i].detail.gun_count} 槍" // AC樁 J1772  共 10 樁
                    tvAvalible03.text = "${charger_status_info[i].detail.avalible_count} 可用"
                    lay05.visibility = View.VISIBLE
                }
            }
            tvGoToMap.setOnClickListener {
                openGoogleMapsNavigation(myLatLng!!.latitude, myLatLng!!.longitude)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openGoogleMapsNavigation(lat: Double, lng: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}