package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemUnPaidFeeBinding
import com.jotangi.nantouparking.model.ParkingGarageFeeUnPaidVO

interface ParkingGarageFeeUnPaidClickListener {
    fun onParkingGarageFeeUnPaidItemClick(
        position: Int,
        vo: ParkingGarageFeeUnPaidVO
    )
}

class ParkingGarageFeeUnPaidAdapter(
    private var data: List<ParkingGarageFeeUnPaidVO>,
    val context: Context,
    private val listener: ParkingGarageFeeUnPaidClickListener?
) : RecyclerView.Adapter<ParkingGarageFeeUnPaidItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingGarageFeeUnPaidItemViewHolder {
        val binding = ItemUnPaidFeeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ParkingGarageFeeUnPaidItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParkingGarageFeeUnPaidItemViewHolder,
        position: Int
    ) {
        holder.bind(
            position,
            data[position],
            listener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<ParkingGarageFeeUnPaidVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}