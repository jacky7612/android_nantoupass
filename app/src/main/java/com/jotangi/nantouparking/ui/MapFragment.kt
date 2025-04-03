package com.jotangi.nantouparking.ui.charge


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.JackyVariant.Glob.toLatLng
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMapChargeParkingBinding
import com.jotangi.nantouparking.databinding.ToolbarFeetBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.jackyModels.map.JChargeMapData
import com.jotangi.nantouparking.jackyModels.map.JMap
import com.jotangi.nantouparking.jackyModels.map.JMapCharge
import com.jotangi.nantouparking.ui.BaseWithBottomBarFragment
import com.jotangi.nantouparking.utility.AppUtility

/**
 * A simple [Fragment] subclass.
 * Use the [MapChargeParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class MapFragment : BaseWithBottomBarFragment() {
    private var _binding: FragmentMapChargeParkingBinding? = null
    private val binding get() = _binding!!
    private var jmap: JMapCharge? =null
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

Glob.activity = requireActivity()
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
        chargeViewModel.chargeStation.observe(viewLifecycleOwner) { result ->
            Log.d("micCheckPOP", result.toString())
            if (result != null) {
                initMap(myview!!, mysavedInstanceState)
            } else {
            }
        }
    }
    private fun triggerGetData() {
        Log.d("micCheckPOP1", AppUtility.getLoginId(requireContext())!!)
        Log.d("micCheckPOP1", AppUtility.getLoginPassword(requireContext())!!)
        chargeViewModel.getNearStation(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            "10", "0")
    }
    private fun initMap(view :View, savedInstanceState :Bundle?) {

        // Example parking spots
        var station =chargeViewModel.chargeStation.value!!.data

        var stationsInfo = station
        var parkingSpots = mutableListOf<JChargeMapData>()
        parkingSpots.clear()
        stationsInfo!!.forEach { station ->
            val jMapData = JChargeMapData(station.station_id, station.station_name, station.address, LatLng(station.latLng[1], station.latLng[0]), "0", "2024-1-12")
            parkingSpots.add(jMapData)
        }
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

        jmap = JMapCharge(view, resources, savedInstanceState,
            parkingSpots, 0, Glob.MapMode, true )
    }
}