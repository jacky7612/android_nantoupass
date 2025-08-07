package com.jotangi.nantoupass.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StoreVO(
    @SerializedName("store_id")
    val storeId: String,

    @SerializedName("store_name")
    val storeName: String,

    @SerializedName("store_phone")
    val storePhone: String,

    @SerializedName("store_website")
    val storeWebsite: String,

    @SerializedName("store_open")
    val storeOpen: String,

    @SerializedName("store_description")
    val storeDes: String,

    @SerializedName("store_open_time")
    val storeOpenTime: String,

    @SerializedName("store_picture")
    var storeImg: String = "",

    @SerializedName("store_address")
    var storeAddress: String = "",

    @SerializedName("store_latitude")
    val storeLat: String,

    @SerializedName("store_longitude")
    val storeLong: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storeId ?: "")
        parcel.writeString(storeName ?: "")
        parcel.writeString(storePhone ?: "")
        parcel.writeString(storeWebsite ?: "")
        parcel.writeString(storeOpen ?: "")
        parcel.writeString(storeDes ?: "")
        parcel.writeString(storeOpenTime ?: "")
        parcel.writeString(storeImg ?: "")
        parcel.writeString(storeAddress ?: "")
        parcel.writeString(storeLat ?: "")
        parcel.writeString(storeLong ?: "")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoreVO> {
        override fun createFromParcel(parcel: Parcel): StoreVO {
            return StoreVO(parcel)
        }

        override fun newArray(size: Int): Array<StoreVO?> {
            return arrayOfNulls(size)
        }
    }

    override fun hashCode(): Int {
        var result = storeId?.hashCode() ?: 0
        result = 31 * result + (storeName?.hashCode() ?: 0)
        result = 31 * result + (storePhone?.hashCode() ?: 0)
        result = 31 * result + (storeWebsite?.hashCode() ?: 0)
        result = 31 * result + (storeOpen?.hashCode() ?: 0)
        result = 31 * result + (storeDes?.hashCode() ?: 0)
        result = 31 * result + (storeOpenTime?.hashCode() ?: 0)
        result = 31 * result + (storeImg?.hashCode() ?: 0)
        result = 31 * result + (storeAddress?.hashCode() ?: 0)
        result = 31 * result + (storeLat?.hashCode() ?: 0)
        result = 31 * result + (storeLong?.hashCode() ?: 0)
        return result
    }
}
