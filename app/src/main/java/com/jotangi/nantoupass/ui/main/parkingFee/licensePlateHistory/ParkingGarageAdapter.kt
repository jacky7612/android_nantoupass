package com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemParkingGarageBinding
import com.jotangi.nantoupass.model.ParkingGarageVO

interface ParkingGarageClickListener {
    fun onParkingGarageItemClick(garageData: ParkingGarageVO)
}

class ParkingGarageAdapter(
    private var data: List<ParkingGarageVO>,
    val context: Context,
    private val listener: ParkingGarageClickListener?
) : RecyclerView.Adapter<ParkingGarageViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingGarageViewHolder {
        val binding = ItemParkingGarageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ParkingGarageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParkingGarageViewHolder,
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

    fun updateDataSource(dataSource: List<ParkingGarageVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}