package com.jotangi.nantouparking.ui.main.parkingInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemParkingInfoBinding
import com.jotangi.nantouparking.model.ParkingInfoBuildingVO

interface ParkingInfoClickListener {
    fun onParkingInfoItemClick()
}

class ParkingInfoAdapter(
    private var data: List<ParkingInfoBuildingVO>,
    val context: Context,
    private val listener: ParkingInfoClickListener?
) : RecyclerView.Adapter<ParkingInfoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingInfoViewHolder {
        val binding = ItemParkingInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ParkingInfoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParkingInfoViewHolder,
        position: Int
    ) {
        holder.bind(
            data[position],
            listener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<ParkingInfoBuildingVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}