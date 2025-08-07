package com.jotangi.nantoupass.ui.charge

import com.jotangi.miaoliparking.ui.dialog.NearbyStationAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.databinding.DialogNearbyStationsBinding
import com.jotangi.nantoupass.jackyModels.map.JChargeMapData
import com.jotangi.nantoupass.model.charge.DataChargeStatusInfo

class NearbyStationsBottomSheetFragment(
    private val stationList: List<JChargeMapData>,
    private val currentLocation: LatLng
) : BottomSheetDialogFragment() {

    private var _binding: DialogNearbyStationsBinding? = null
    private val binding get() = _binding!!
companion object {
    var position = ""
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNearbyStationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NearbyStationAdapter(stationList, currentLocation) { title, list, position ->
            val index = stationList.indexOfFirst { it.position == position }
            Log.d("micCheckGG0", "0")
            if (index >= 0) {
                val result = Bundle().apply {
                    putParcelable("selected_station", stationList[index])
                }
                Log.d("micCheckGG1", "1")
                requireActivity().supportFragmentManager.setFragmentResult("station_selected", result)
            }
Log.d("micCheckGG2", "2")
            showChargerDetails(title, list, position)
        }
        binding.recyclerViewNearbyStations.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewNearbyStations.adapter = adapter

    }

    private fun putParcelable(s: String, jChargeMapDataV2: JChargeMapData) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showChargerDetails(stationTitle: String, list: List<DataChargeStatusInfo>, position:LatLng) {
//        val bottomSheet = NearbyStationsBottomSheetFragment(emptyList(), currentLocation)
//        bottomSheet.show(childFragmentManager, "NearbyStationsDialog")
//        val bottomSheet = ChargerDetailsBottomSheetFragment(stationTitle,list)
//        bottomSheet.show(childFragmentManager, "NearbyStationsDialog")
        showBottomSheetSafely(stationTitle, list, position)


    }
    private fun showBottomSheetSafely(title: String, list: List<DataChargeStatusInfo>, position: LatLng) {
        val activity = activity ?: return // ✅ Cache activity safely

        // Dismiss current bottom sheet
        dismiss()

        Handler(Looper.getMainLooper()).postDelayed({
            if (!activity.isFinishing) {
                val tag = ChargeMarkInfo2Fragment::class.java.simpleName
                if (activity.supportFragmentManager.findFragmentByTag(tag) == null) {
                    val bottomSheet = ChargeMarkInfo2Fragment()
                    bottomSheet.init(title, "", position, list)
                    bottomSheet.show(activity.supportFragmentManager, tag)
                    Glob.CurChargeMarkerInfo = null
                }
            }
        }, 200) // Delay to wait for previous fragment to finish dismissing
    }

}

