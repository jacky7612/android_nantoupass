package com.jotangi.nantoupass.ui.member.parkingHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemPaidFeeBinding
import com.jotangi.nantoupass.model.ParkingFeePaidVO

interface ParkingFeePaidClickListener {
    fun onParkingFeePaidItemClick(vo: ParkingFeePaidVO)
}

class ParkingFeePaidAdapter(
    private var data: List<ParkingFeePaidVO>,
    val context: Context,
    private val listener: ParkingFeePaidClickListener?
) : RecyclerView.Adapter<ParkingFeePaidItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingFeePaidItemViewHolder {
        val binding = ItemPaidFeeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ParkingFeePaidItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParkingFeePaidItemViewHolder,
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

    fun updateDataSource(dataSource: List<ParkingFeePaidVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}