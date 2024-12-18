package com.jotangi.nantouparking.jackyModels.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.ToolbarFeetBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.ui.BaseWithBottomBarFragment
import com.jotangi.nantouparking.ui.charge.MarkerInfoBottomSheet

// <!-- 使用 google 地圖 start 必需指定至該 fragment -->
// 建立模組於 2024-07-13 21:37:00
// 此處原繼承 MapChargeParkingFragment 改至通用 BaseFragment
class JMap(view :View, val feetBinding: ToolbarFeetBinding, val resource: Resources,
           val savedInstanceState: Bundle?, val jmap_charges: List<JMapData>,
           val jmap_parkings: List<JMapData>, val default_latlng_pos: Int,
           val mode: String,
           val ShowInfoInStart: Boolean):
    BaseWithBottomBarFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private var mapView: MapView =view.findViewById<MapView>(R.id.mv_main)
    private var selectedMarker: Marker? = null
    private var myBuilder: LatLngBounds.Builder? = null
    private var markers: MutableList<Marker> = mutableListOf()
    private var btLocation: ImageView =view.findViewById(R.id.bt_Local)
    private var btCharge: ImageView =view.findViewById(R.id.bt_charge)
    private var btParking: ImageView =view.findViewById(R.id.bt_parking)
    var fShowCharge: Boolean =false
    var fShowParking: Boolean =true
    var jmap_cur: List<JMapData>

    private var fusedLocationClient: FusedLocationProviderClient
    init {
        fShowCharge  =false
        fShowParking =false
        fShowCharge  =(mode == "charge" )
        fShowParking =(mode == "parking")
        jmap_cur =  if (fShowCharge) jmap_charges
        else jmap_parkings

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        myBuilder =LatLngBounds.Builder()
        fusedLocationClient =LocationServices.getFusedLocationProviderClient(view.context)
        btLocation.setOnClickListener {
            checkLocationPermissionAndMoveToCurrentLocation()
        }
        btCharge.setOnClickListener {
            fShowCharge = !fShowCharge
            refreshMarkerAtLocation(jmap_charges, jmap_parkings)
            if (fShowCharge) {
                Glob.setImageViaDrawable(btCharge, R.drawable.ic_l_green)
            } else {
                Glob.setImageViaDrawable(btCharge, R.drawable.ic_l_gray)
            }
        }
        btParking.setOnClickListener {
            fShowParking = !fShowParking
            refreshMarkerAtLocation(jmap_charges, jmap_parkings)
            if (fShowParking) {
                Glob.setImageViaDrawable(btParking, R.drawable.ic_p_blue)
            } else {
                Glob.setImageViaDrawable(btParking, R.drawable.ic_p_gray)
            }
        }

        if (fShowParking) {
            Glob.setImageViaDrawable(btParking, R.drawable.ic_p_blue)
        } else {
            Glob.setImageViaDrawable(btParking, R.drawable.ic_p_gray)
        }
        if (fShowCharge) {
            Glob.setImageViaDrawable(btCharge, R.drawable.ic_l_green)
        } else {
            Glob.setImageViaDrawable(btCharge, R.drawable.ic_l_gray)
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add parking spots
        addParkingSpots()

        // Set the map click listener
//        mMap.setOnMapClickListener { latLng ->
//            onMapClick(latLng)
//        }

        // Set the marker click listener
        mMap.setOnMarkerClickListener(this)

        if (Glob.DoLocaleCurrentLatLng) {
            checkLocationPermissionAndMoveToCurrentLocation()
        }
    }

    private fun onMapClick(latLng: LatLng) {
        // Remove the previous marker, if any
        selectedMarker?.remove()

        // Add a new marker at the clicked location
        selectedMarker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_orange))))

        // Optionally, move the camera to the selected location
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
//        Log.d("MapFragment", "Marker clicked: ${marker.title}")

        // Reset icon of previously selected marker (if any)
        if (fShowCharge)
            selectedMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_blue)))
        if (fShowParking)
            selectedMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_red)))

        // Do something with the selected marker
//        selectedMarker?.remove()

        Glob.map_position =markers.indexOf(marker)

        // Change the marker icon
        if (fShowCharge)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_cyan)))
        if (fShowParking)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_orange)))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17f)) // Zoom level 17

        selectedMarker =marker

        // 顯示充電或停車位置資訊
        if (fShowCharge)
            Glob.CurMarkerInfo =jmap_charges[Glob.map_position]
//            Glob.assign(jmap_charges[Glob.map_position])
        if (fShowParking)
            Glob.CurMarkerInfo =jmap_parkings[Glob.map_position]
//            Glob.assign(jmap_parkings[Glob.map_position])

        marker.showInfoWindow()

        return true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun getToolBarFeet(): ToolbarFeetBinding =feetBinding

    override fun getToolBar(): ToolbarIncludeBinding? {
        TODO("Not yet implemented")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun addParkingSpots() {
        val cameraPosition = CameraPosition.Builder()
            .target(jmap_cur[default_latlng_pos].position)
            .zoom(17f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        var marker_id: Int =-1
        marker_id  =if (fShowCharge) R.drawable.ic_mark_blue
        else R.drawable.ic_mark_red
        for (spot in jmap_cur) {
            val marker =mMap.addMarker(
                MarkerOptions()
                    .position(spot.position)
                    .title("${spot.title}\n${spot.descript}")
                    .title(spot.title)
                    .snippet(spot.descript)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(marker_id))))
            marker?.let { markers.add(it)
                myBuilder?.include(marker.position)}

        }
        if (ShowInfoInStart) markers[default_latlng_pos].showInfoWindow()
    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = resource.getDrawable(drawableId, null)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun checkLocationPermissionAndMoveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                Glob.activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Glob.activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                Glob.activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        getLastKnownLocation()
    }
    private fun refreshMarkerAtLocation(input_charge: List<JMapData>, input_parking: List<JMapData>) {
        mMap.clear()  // Clear existing markers if any
        markers.clear()
        selectedMarker =null
        Glob.CurMarkerInfo?.clear()

        if (fShowCharge ) updateChargeMarkerAtLocation(input_charge )
        if (fShowParking) updateParkingMarkerAtLocation(input_parking)
    }
    private fun updateChargeMarkerAtLocation(input: List<JMapData>)
    {
        for (spot in input) {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(spot.position)
                    .title(spot.title)
                    .snippet(spot.descript)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_blue)))
            )
            marker?.let {
                markers.add(it)
                myBuilder?.include(marker.position)
            }
        }
    }
    private fun updateParkingMarkerAtLocation(input: List<JMapData>)
    {
        for (spot in input) {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(spot.position)
                    .title(spot.title)
                    .snippet(spot.descript)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_mark_red)))
            )
            marker?.let {
                markers.add(it)
                myBuilder?.include(marker.position)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}



class JMapCharge(view :View, val resource: Resources,
                  val savedInstanceState: Bundle?,
                  val jmap_parkings: List<JChargeMapData>, val default_latlng_pos: Int,
                  val mode: String,
                  val ShowInfoInStart: Boolean):
    BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private var mapView: MapView =view.findViewById<MapView>(R.id.mv_main)
    private var selectedMarker: Marker? = null
    private var myBuilder: LatLngBounds.Builder? = null
    private var markers: MutableList<Marker> = mutableListOf()
    private var btLocation: ImageView =view.findViewById(R.id.bt_Local)
    var fShowCharge: Boolean =false
    var fShowParking: Boolean =true
    var jmap_cur: List<JChargeMapData>

    private var fusedLocationClient: FusedLocationProviderClient
    init {
        fShowCharge  =false
        fShowParking =false
        fShowCharge  =(mode == "charge" )
        fShowParking =(mode == "parking")
        jmap_cur =jmap_parkings

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        myBuilder =LatLngBounds.Builder()
        fusedLocationClient =LocationServices.getFusedLocationProviderClient(view.context)
        btLocation.setOnClickListener {
            checkLocationPermissionAndMoveToCurrentLocation()
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add parking spots
        addParkingSpots()

        // Set the map click listener
//        mMap.setOnMapClickListener { latLng ->
//            onMapClick(latLng)
//        }

        // Set the marker click listener
        mMap.setOnMarkerClickListener(this)

        if (Glob.DoLocaleCurrentLatLng) {
            checkLocationPermissionAndMoveToCurrentLocation()
        }
    }

    private fun onMapClick(latLng: LatLng) {
        // Remove the previous marker, if any
        selectedMarker?.remove()

        // Add a new marker at the clicked location
        selectedMarker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_charge_placeholder48)))) // select :ic_mark_cyan

        // Optionally, move the camera to the selected location
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
//        Log.d("MapFragment", "Marker clicked: ${marker.title}")

        // Reset icon of previously selected marker (if any)
        selectedMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_charge_placeholder48))) // unselect :ic_mark_blue

        // Do something with the selected marker
//        selectedMarker?.remove()

        Glob.map_position =markers.indexOf(marker)

        // Change the marker icon
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_charge_placeholder48))) // unselect :ic_mark_cyan

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17f)) // Zoom level 17

        selectedMarker =marker

        // 顯示充電或停車位置資訊
        Glob.CurChargeMarkerInfo =jmap_parkings[Glob.map_position]
//            Glob.assign(jmap_parkings[Glob.map_position])

        marker.showInfoWindow()

        return true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun getToolBar(): ToolbarIncludeBinding? {
        TODO("Not yet implemented")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun addParkingSpots() {
        val cameraPosition = CameraPosition.Builder()
            .target(jmap_cur[default_latlng_pos].position)
            .zoom(17f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        var marker_id: Int =-1
        marker_id  =if (fShowCharge) R.drawable.ic_charge_placeholder48 // unselect :ic_mark_blue
        else R.drawable.ic_charge_placeholder48 // select :ic_mark_cyan
        var iIdx =0
        jmap_cur.forEach { spot ->
            iIdx++
            val marker =mMap.addMarker(
                MarkerOptions()
                    .position(spot.position)
                    .title("${spot.title}\n${spot.descript}")
                    .title(spot.title)
                    .snippet(spot.descript)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(marker_id))))
            marker?.let { markers.add(it)
                myBuilder?.include(marker.position)}

        }
        if (ShowInfoInStart) markers[default_latlng_pos].showInfoWindow()
    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = resource.getDrawable(drawableId, null)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun checkLocationPermissionAndMoveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                Glob.activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Glob.activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                Glob.activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        getLastKnownLocation()
    }
    private fun refreshMarkerAtLocation(input_charge: List<JMapData>, input_parking: List<JMapData>) {
        mMap.clear()  // Clear existing markers if any
        markers.clear()
        selectedMarker =null
        Glob.CurMarkerInfo?.clear()

        if (fShowCharge ) updateChargeMarkerAtLocation(input_charge )
        if (fShowParking) updateParkingMarkerAtLocation(input_parking)
    }
    private fun updateChargeMarkerAtLocation(input: List<JMapData>)
    {
        for (spot in input) {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(spot.position)
                    .title(spot.title)
                    .snippet(spot.descript)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_charge_placeholder48))) // unselect :ic_mark_blue
            )
            marker?.let {
                markers.add(it)
                myBuilder?.include(marker.position)
            }
        }
    }
    private fun updateParkingMarkerAtLocation(input: List<JMapData>)
    {
        for (spot in input) {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(spot.position)
                    .title(spot.title)
                    .snippet(spot.descript)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_charge_placeholder48))) // unselect :ic_mark_blue
            )
            marker?.let {
                markers.add(it)
                myBuilder?.include(marker.position)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun showBottomSheetSafely() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            println("jacky isAdded :${isAdded}, isStateSaved :${isStateSaved}")
            if (isAdded && !isStateSaved) { // 生命週期保護
                Glob.CurChargeMarkerInfo?.let {
                    val bottomSheet = MarkerInfoBottomSheet()
                    bottomSheet.init(it.StationUID, it.title ?: "", it.position ?: LatLng(
                        -999.0,
                        -999.0
                    )
                    )
                    bottomSheet.show(
                        childFragmentManager,
                        MarkerInfoBottomSheet::class.java.simpleName
                    )
                    Glob.CurChargeMarkerInfo = null
                }
            }
        }
    }
}