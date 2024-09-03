package com.jotangi.nantouparking.ui.member.parkingHistory

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemPaidFeeBinding
import com.jotangi.nantouparking.model.ParkingFeePaidVO

class ParkingFeePaidItemViewHolder(val binding: ItemPaidFeeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: ParkingFeePaidVO,
        listener: ParkingFeePaidClickListener?
    ) {
        binding.apply {
            itemParkingFeePaidConstraintLayout.setOnClickListener {
                listener?.onParkingFeePaidItemClick(data)
            }

            paidDetailMaterialButton.setOnClickListener {
                listener?.onParkingFeePaidItemClick(data)
            }

            paidBillNoContentTextView.text = data.orderNo ?: ""
            paidPlateContentTextView.text = data.plateNo ?: ""
            paidDateContentTextView.text = data.orderPayDate ?: ""
            paidFeeContentTextView.text = data.orderPayAmount ?: ""
//            paidStatusContentTextView.text = data.orderStatus

            paidStatusContentTextView.text = when (data.billPayStatus) {
                "1" -> {
                    "已繳費"
                }
                "9" -> {
                    "已退費"
                }
                else -> {
                    "請洽客服"
                }
            }
        }
    }
}