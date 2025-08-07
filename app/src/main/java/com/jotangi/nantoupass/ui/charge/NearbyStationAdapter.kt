package com.jotangi.miaoliparking.ui.dialog

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.jotangi.nantoupass.databinding.ItemNearbyStationBinding
import com.jotangi.nantoupass.jackyModels.map.JChargeMapData
import com.jotangi.nantoupass.model.charge.DataChargeStatusInfo
import com.jotangi.nantoupass.ui.charge.ChargerStatusAdapter

class NearbyStationAdapter(
    private val stationList: List<JChargeMapData>,
    private val currentLocation: LatLng,
    private val onShowDetails: (String, List<DataChargeStatusInfo>, LatLng) -> Unit
) : RecyclerView.Adapter<NearbyStationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNearbyStationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(station: JChargeMapData) {
            binding.tvStationName.text = station.title

            val results = FloatArray(1)
            station.position?.latitude?.let {
                android.location.Location.distanceBetween(
                    currentLocation.latitude, currentLocation.longitude,
                    it, station.position!!.longitude,
                    results
                )
            }
            val distanceKm = results[0] / 1000
            binding.tvDistance.text = "距所在位置: %.1f km".format(distanceKm)

            val acCharger = station.chargeDetail.find { it.type.equals("AC", true) }

            val availableCount =
                acCharger?.detail?.avalible_count?.toString()?.trim()?.toIntOrNull() ?: 0

            val chargerName = if (acCharger != null) "${acCharger.type} 樁" else "AC 樁"

//            binding.tvAC.text = chargerName +" "+ acCharger?.detail?.name
//            binding.tvACStatus.text = "可用 $availableCount"
            Log.d(
                "NearbyStationAdapter",
                "Available count: $availableCount → Background = ${if (availableCount == 0) "RED" else "GREEN"}"
            )
            binding.btnDetails.paintFlags =
                binding.btnDetails.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            binding.recyclerChargerStatus.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = ChargerStatusAdapter(station.chargeDetail)
                isNestedScrollingEnabled = false
            }
            binding.btnDetails.setOnClickListener {

                station.position?.let { it1 ->
                    onShowDetails(station.title ?: "無標題", station.chargeDetail,
                        it1
                    )
                }
            }
        }



    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNearbyStationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = stationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stationList[position])
    }
}
