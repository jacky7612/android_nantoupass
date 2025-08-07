package com.jotangi.nantoupass.ui.main.parkingInfo

import ParkingInfoClickListener
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemParkingInfoBinding
import com.jotangi.nantoupass.model.ParkingInfoBuildingVO

class ParkingInfoViewHolder(val binding: ItemParkingInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: ParkingInfoBuildingVO,
        listener: ParkingInfoClickListener?
    ) {
        binding.apply {
            itemParkingInfoTitleTextView.text = data.road
            itemParkingInfoCountTextView.text = "${data.emptyCount}格"

//            itemParkingInfoCountTextView.text = data.plateNo
        }
    }
}