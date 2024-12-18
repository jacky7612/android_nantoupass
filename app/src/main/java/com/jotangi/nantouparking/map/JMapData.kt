package com.jotangi.nantouparking.jackyModels.map

import com.google.android.gms.maps.model.LatLng

data class JMapData(
    var title : String="",
    var descript   : String="",
    var position: LatLng) {
    fun clear() {
        title = ""
        descript = ""
        position = LatLng(-999.0, -999.0)
    }
}
data class JChargeMapData(
    var StationUID: String="",
    var title: String="",
    var descript: String="",
    var position: LatLng
) {
    fun clear() {
        StationUID = ""
        title = ""
        descript = ""
        position = LatLng(-999.0, -999.0)
    }
}