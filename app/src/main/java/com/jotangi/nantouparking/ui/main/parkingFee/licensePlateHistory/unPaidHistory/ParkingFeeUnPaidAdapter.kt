package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemUnPaidFeeBinding
import com.jotangi.nantouparking.databinding.ItemUnPaidFeeRoadBinding
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO2

interface ParkingFeeUnPaidClickListener {
    fun onParkingFeeUnPaidItemClick(
        position: Int,
        vo: ParkingRoadFeeUnPaidVO,
        isChecked:Boolean
    )
}

class ParkingFeeUnPaidAdapter(
    private var data: List<ParkingRoadFeeUnPaidVO>,
    val context: Context,
    private val listener: ParkingFeeUnPaidClickListener?
) : RecyclerView.Adapter<ParkingFeeUnPaidItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingFeeUnPaidItemViewHolder {
        val binding = ItemUnPaidFeeRoadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ParkingFeeUnPaidItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParkingFeeUnPaidItemViewHolder,
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

    fun updateDataSource(dataSource: List<ParkingRoadFeeUnPaidVO>) {
        this.data = dataSource
        this.notifyDataSetChanged()
    }
    fun selectAllItems(selectAll: Boolean) {
        data.forEachIndexed { index, item ->
            item.isSelected = selectAll
        }
        notifyDataSetChanged()
    }
}