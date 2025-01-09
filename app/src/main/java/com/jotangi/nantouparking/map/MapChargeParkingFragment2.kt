package com.jotangi.nantouparking.map


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.ParkingInfoBottomSheet
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMapChargeParkingBinding
import com.jotangi.nantouparking.databinding.ToolbarFeetBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.jackyModels.map.JChargeMapData
import com.jotangi.nantouparking.jackyModels.map.JMapCharge
import com.jotangi.nantouparking.ui.BaseWithBottomBarFragment
import com.jotangi.nantouparking.ui.charge.MarkerInfoBottomSheet
import com.jotangi.nantouparking.utility.AppUtility
import com.jotangi.zhudongparking.jackyModels.map.JMapCharge2
import com.jotangi.zhudongparking.jackyModels.map.JMapCharge3
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MapChargeParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class MapChargeParkingFragment2 : BaseWithBottomBarFragment(), JMapCharge2.MarkerDialogCallback  {
    private var _binding: FragmentMapChargeParkingBinding? = null
    private val binding get() = _binding!!
    private var jmap: JMapCharge2? =null
    private var jmap3: JMapCharge3? =null
    private var myview: View? = null
    private var mysavedInstanceState: Bundle? = null
    override fun getToolBar(): ToolbarIncludeBinding? {
        TODO("Not yet implemented")
    }
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapChargeParkingBinding.inflate(inflater, container, false)

        val view: View = inflater.inflate(R.layout.fragment_map_charge_parking, container, false)
        myview =view
        mysavedInstanceState =savedInstanceState

        initObserver()
        triggerGetData()

        // feet button
        initEvent(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
Log.d("micCheckAQ", "onViewCreated")

        // feet button
        initEvent()

//        mapChargeParkingTitle()

        var btBack: ImageButton =view.findViewById<ImageButton>(R.id.bt_back)
        btBack.setOnClickListener {
            findNavController().navigate(R.id.action_mapChargeParkingFragment_to_main_fragment)
        }

        // 顯示充電或停車位置資訊
        Thread {
            // Simulate some time-consuming operation
            while (true) {
                Thread.sleep(100)
                activity?.runOnUiThread {
                    Glob.CurChargeMarkerInfo?.let {
                        showBottomSheetSafely(it.title ?: "", it.descript ?: "")
                    }
                }
            }
        }.start()

    }
    private fun showBottomSheetSafely(title: String, description: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            println("jacky isAdded :${isAdded}, isStateSaved :${isStateSaved}")
            if (isAdded && !isStateSaved) { // 生命週期保護
                Glob.CurChargeMarkerInfo?.let {
                    val bottomSheet = MarkerInfoBottomSheet()
                    bottomSheet.init(it.title ?: "", it.descript ?: "", it.position ?: LatLng(-999.0, -999.0))
                    bottomSheet.show(
                        childFragmentManager,
                        MarkerInfoBottomSheet::class.java.simpleName
                    )
                    Glob.CurChargeMarkerInfo = null
                }
            }
        }
    }
    private fun initObserver() {
//        chargeViewModel.chargeStation.observe(viewLifecycleOwner) { result ->
//            Log.d("micCheckPOP", result.toString())
//            if (result != null) {
//                initMap(myview!!, mysavedInstanceState)
//            } else {
//            }
//        }

        chargeViewModel.roadParkStatus.observe(viewLifecycleOwner) { response ->
            viewLifecycleOwner.lifecycleScope.launch {
                initMapRoad(myview!!, mysavedInstanceState)
                Log.d("micCheckPU", response.toString())
            }
        }

        chargeViewModel.parkStatus.observe(viewLifecycleOwner) { response ->
//                            initMapPark(myview!!, mysavedInstanceState)

            Log.d("micCheckUK", response.toString())
        }
    }
    private fun triggerGetData() {
        Log.d("micCheckPOP1", AppUtility.getLoginId(requireContext())!!)
        Log.d("micCheckPOP1", AppUtility.getLoginPassword(requireContext())!!)
//        chargeViewModel.getNearStation(
//            requireContext(),
//            AppUtility.getLoginId(requireContext())!!,
//            AppUtility.getLoginPassword(requireContext())!!,
//            "10", "0")
        chargeViewModel.fetchAllParkStatus()
        chargeViewModel.fetchRoadParkStatus()

    }
    private fun initMapRoad(view: View, savedInstanceState: Bundle?) {
        // Get parking spot data from roadParkStatus
        val stationData = chargeViewModel.roadParkStatus.value?.data
        // Initialize mutable list to hold mapped data
        val parkingSpots = mutableListOf<JChargeMapData>()
        parkingSpots.clear()
        // Map station data to JChargeMapData format
        stationData?.forEach { station ->
            val latitude = station.Latitude.replace(",", "").toDouble()
            val longitude = station.Longitude.replace(",", "").toDouble()

            val jMapData = JChargeMapData(
                station.ParkingSpaceCode,    // station_id -> ParkingSpaceCode
                station.BillSegmentName,     // station_name -> BillSegmentName
                station.BillSegmentName, // address -> BillSegmentName
                LatLng(latitude, longitude),
                station.Status,// Cleaned LatLng
                station.update_time
            )
            parkingSpots.add(jMapData)
        }
        // Example static charge spots (unchanged)
        val chargeSpots = listOf(
            JChargeMapData(
                "1",
                "第一充電站",
                "空位:80",
                LatLng(23.4771319, 120.4120205),
                "0",
                "2024-1-12"
            ),
            JChargeMapData(
                "2",
                "第二充電站",
                "空位:40",
                LatLng(23.4791319, 120.4140205),
                "0",
                "2024-1-12"
            ),
            JChargeMapData(
                "3",
                "第三充電站",
                "空位:10",
                LatLng(23.4811319, 120.4160205),
                "0",
                "2024-1-12"
            )
        )
        // Initialize map with the transformed parking spots
        jmap = JMapCharge2(
            view, resources, savedInstanceState,
            parkingSpots, 0, Glob.MapMode, true
        )
        jmap?.setMarkerDialogCallback(this)

    }

    private fun initMapPark(view: View, savedInstanceState: Bundle?) {
        // Get parking spot data from roadParkStatus
        val stationData = chargeViewModel.parkStatus.value?.data
        // Initialize mutable list to hold mapped data
        val parkingSpots = mutableListOf<JChargeMapData.JChargeMapData2>()
        parkingSpots.clear()
        // Map station data to JChargeMapData format
        stationData?.forEach { station ->
            val latitude = station.lat.replace(",", "").toDouble()
            val longitude = station.lng.replace(",", "").toDouble()

            val jMapData = JChargeMapData.JChargeMapData2(
                station.road,    // station_id -> ParkingSpaceCode
                station.address,     // station_name -> BillSegmentName // address -> BillSegmentName
                LatLng(latitude, longitude),
                station.emptyCount.toString(),// Cleaned LatLng
                station.update_time
            )
            parkingSpots.add(jMapData)
        }
        // Example static charge spots (unchanged)
        // Initialize map with the transformed parking spots
        jmap3 = JMapCharge3(
            view, resources, savedInstanceState,
            parkingSpots, 0, Glob.MapMode, true
        )
        jmap?.setMarkerDialogCallback(this)

    }

    override fun showMarkerDialog(data: JChargeMapData) {
        Log.d("micCheckAAS", "AAS")
        binding.apply {
            mapNotification.visibility = View.VISIBLE
            var space = ""
            if(data.status.equals("0")) {
                space = "空位"
            } else {
                space = "沒空位"
            }
            statusTxt.text = space
            location.text = data.title
            code.text = data.StationUID
            updateTime.text = "更新時間：$data.updateTime"
        }
    }
}