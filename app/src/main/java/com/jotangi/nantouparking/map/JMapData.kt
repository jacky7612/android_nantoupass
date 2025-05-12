package com.jotangi.nantouparking.jackyModels.map

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantouparking.model.charge.DataChargeStatusInfo

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
    var position: LatLng?,
    var status:String,
    var updateTime:String,
    var chargeDetail: List<DataChargeStatusInfo>
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("chargeDetail")
    ) {
    }
    fun clear() {
        StationUID = ""
        title = ""
        descript = ""
        position = LatLng(-999.0, -999.0)
    }

    data class JChargeMapData2(
        var road: String="",
        var address: String="",
        var position: LatLng,
        var emptyCount:String,
        var updateTime:String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(StationUID)
        parcel.writeString(title)
        parcel.writeString(descript)
        parcel.writeParcelable(position, flags)
        parcel.writeString(status)
        parcel.writeString(updateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JChargeMapData> {
        override fun createFromParcel(parcel: Parcel): JChargeMapData {
            return JChargeMapData(parcel)
        }

        override fun newArray(size: Int): Array<JChargeMapData?> {
            return arrayOfNulls(size)
        }
    }
}