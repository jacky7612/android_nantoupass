package com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemParkingGarageBinding
import com.jotangi.nantoupass.model.ParkingGarageVO

class ParkingGarageViewHolder(val binding: ItemParkingGarageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: ParkingGarageVO,
        listener: ParkingGarageClickListener?
    ) {
        binding.apply {
            itemParkingGarageTitleTextView.text = data.parkingGarageName ?: ""
//            itemParkingGarageAddressTextView.text = data.parkingGarageAddress ?: ""

            itemParkingGarageConstraintLayout.setOnClickListener {
                listener?.onParkingGarageItemClick(data)
            }
        }
    }
}