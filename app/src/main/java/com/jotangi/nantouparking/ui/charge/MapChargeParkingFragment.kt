package com.jotangi.nantouparking.ui.charge


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantouparking.JackyVariant.ConvertText
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMapChargeParkingBinding
import com.jotangi.nantouparking.databinding.ToolbarFeetBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.jackyModels.map.JChargeMapData
import com.jotangi.nantouparking.jackyModels.map.JMapCharge
import com.jotangi.nantouparking.model.charge.DataStation
import com.jotangi.nantouparking.ui.BaseWithBottomBarFragment
import com.jotangi.nantouparking.utility.AppUtility

/**
 * A simple [Fragment] subclass.
 * Use the [MapChargeParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class MapChargeParkingFragment : BaseWithBottomBarFragment() {
    private var _binding: FragmentMapChargeParkingBinding? = null
    private val binding get() = _binding!!
    private var jmap: JMapCharge? =null
    private var myview: View? = null
    private var mysavedInstanceState: Bundle? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun getToolBar(): ToolbarIncludeBinding? {
        TODO("Not yet implemented")
    }
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener("station_selected", this) { _, bundle ->
            Log.d("micCheckLLL", "LLL")
            val selectedStation = bundle.getParcelable<JChargeMapData>("selected_station")
            selectedStation?.let {
                showSelectedStationOnMap(it)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapChargeParkingBinding.inflate(inflater, container, false)
        myview = _binding!!.root
        mysavedInstanceState = savedInstanceState

        initObserver()
        triggerGetData()

        // feet button
        initEvent(myview!!)
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_mapChargeParkingFragment_to_main_fragment)
            }
        })
        Glob.activity = requireActivity()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // feet button
        initEvent()
        binding.btnNearby.setOnClickListener {
            requestLocationPermissionIfNeeded()

        }
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

    private fun showSelectedStationOnMap(station: JChargeMapData) {
        val allStations = chargeViewModel.chargeStation.value?.data ?: return

        // Convert all stations to JChargeMapData
        val parkingSpots = allStations.map {
            JChargeMapData(
                it.station_id,
                it.station_name,
                it.address,
                LatLng(it.latLng[1], it.latLng[0]),
                "0",
                "2024-1-12",
                it.charger_status_info ?: emptyList()
            )
        }

        // Find the index of the selected station in the full list
        val selIdx = parkingSpots.indexOfFirst { it.StationUID == station.StationUID }

        // Pass full list with selected index
        jmap = JMapCharge(
            myview!!,
            resources,
            mysavedInstanceState,
            parkingSpots,
            if (selIdx != -1) selIdx else 0,
            Glob.MapMode,
            true
        )

        // Optionally move camera to selected station
        Handler(Looper.getMainLooper()).postDelayed({
            station.position?.let { jmap?.moveCameraTo(it) }
        }, 300)
    }

    private fun requestLocationPermissionIfNeeded() {
        if (
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocationAndShowNearbyStations()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocationAndShowNearbyStations()
        } else {
            Log.e("MapCharge", "User denied location permission.")
        }
    }
    private fun showBottomSheetSafely(title: String, description: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            println("jacky isAdded :${isAdded}, isStateSaved :${isStateSaved}")
            if (isAdded && !isStateSaved) { // 生命週期保護
                Glob.CurChargeMarkerInfo?.let {
                    val bottomSheet = MarkerInfoBottomSheet()
                    bottomSheet.init(it.title ?: "", it.descript ?: "", it.position ?: LatLng(-999.0, -999.0),it.chargeDetail )
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
            "0", "0")
    }
    private fun initMap(view :View, savedInstanceState :Bundle?) {

        // Example parking spots
        var station =chargeViewModel.chargeStation.value!!.data
        var stationsInfo = station
        var parkingSpots = mutableListOf<JChargeMapData>()
        parkingSpots.clear()
        var idx = -1
        var sel_idx = -1
        stationsInfo!!.forEach { station ->
            idx++
            if (sel_idx == -1 && station.station_name.contains("南投"))
            {
                sel_idx = idx
            }
            val jMapData = JChargeMapData(station.station_id, station.station_name, station.address, LatLng(station.latLng[1], station.latLng[0]), "0", "2024-1-12",station.charger_status_info?: emptyList())
            parkingSpots.add(jMapData)
        }

        if (sel_idx == -1) sel_idx = 0
        if(parkingSpots.count() == 0) return
        jmap = JMapCharge(view, resources, savedInstanceState,
            parkingSpots, sel_idx, Glob.MapMode, true )
    }

    private fun getCurrentLocationAndShowNearbyStations() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("MapCharge", "Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
//                val miaoliLatLng = LatLng(24.560159, 120.820198)
                val currentLatLng = LatLng(it.latitude, it.longitude)
                Glob.lastKnownLatLng = currentLatLng.toString()
                showNearbyStations(currentLatLng)
            } ?: run {
                Log.e("MapCharge", "Location is null")
            }
        }.addOnFailureListener {
            Log.e("MapCharge", "Failed to get location: ${it.message}")
        }
    }

    private fun showNearbyStations(currentLocation: LatLng) {
        val allStations = chargeViewModel.chargeStation.value?.data ?: return
        Log.d("micCheckKKL1", allStations.toString())
        Log.d("micCheckKKL3", currentLocation.toString())
        val sortedStations = allStations.sortedBy { station ->
            val stationLatLng = LatLng(station.latLng[1], station.latLng[0])
            calculateDistance(currentLocation, stationLatLng)
        }.take(3)

        val nearbySpots = sortedStations.map { station ->
            JChargeMapData(
                station.station_id,
                station.station_name,
                "",
                LatLng(station.latLng[1], station.latLng[0]),
                "0",
                ConvertText.getFormattedDate(""),
                station.charger_status_info
            )
        }
        initNearbyMap(myview!!, mysavedInstanceState, sortedStations)
        Log.d("micCheckKKL2", nearbySpots.toString())

        // ✅ Show the custom dialog with list
        val bottomSheet = NearbyStationsBottomSheetFragment(nearbySpots, currentLocation)
        bottomSheet.show(childFragmentManager, "NearbyStationsDialog")
    }
    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            results
        )
        return results[0].toDouble() // in meters
    }

    private fun initNearbyMap(view :View, savedInstanceState :Bundle?, nearbyStation:List<DataStation>) {

        // Example parking spots
        var stationsInfo = nearbyStation
        var parkingSpots = mutableListOf<JChargeMapData>()
        parkingSpots.clear()
        stationsInfo!!.forEach { station ->
            val jMapData = JChargeMapData(station.station_id, station.station_name,
                "", LatLng(station.latLng[1], station.latLng[0]), "0", ConvertText.getFormattedDate(""), station.charger_status_info)
            parkingSpots.add(jMapData)
        }

        if(parkingSpots.count() == 0) return
        jmap = JMapCharge(view, resources, savedInstanceState,
            parkingSpots, 0, Glob.MapMode, true )
    }
}