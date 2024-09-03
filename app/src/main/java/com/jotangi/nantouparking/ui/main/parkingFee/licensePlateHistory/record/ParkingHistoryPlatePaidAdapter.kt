package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.record

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemGovPaidFeeBinding
import com.jotangi.nantouparking.databinding.ItemPaidFeeBinding
import com.jotangi.nantouparking.model.ParkingFeePaidVO
import com.jotangi.payStation.Model.ApiModel.DataGovParkingFeePaidVO

interface ParkingFeePaidClickListener {
    fun onParkingFeePaidItemClick(vo: DataGovParkingFeePaidVO)
}

class ParkingHistoryPlatePaidAdapter(
    private var data: List<DataGovParkingFeePaidVO>,
    val context: Context,
    private val listener: ParkingFeePaidClickListener?
) : RecyclerView.Adapter<ParkingHistoryPlatePaidItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingHistoryPlatePaidItemViewHolder {
        val binding = ItemGovPaidFeeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ParkingHistoryPlatePaidItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParkingHistoryPlatePaidItemViewHolder,
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

    fun updateDataSource(dataSource: List<DataGovParkingFeePaidVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}