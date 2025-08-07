package com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory.record

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemGovPaidFeeBinding
import com.jotangi.payStation.Model.ApiModel.DataGovParkingFeePaidVO

class ParkingHistoryPlatePaidItemViewHolder(val binding: ItemGovPaidFeeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: DataGovParkingFeePaidVO,
        listener: ParkingFeePaidClickListener?
    ) {
        binding.apply {
//            itemParkingFeePaidConstraintLayout.setOnClickListener {
//                listener?.onParkingFeePaidItemClick(data)
//            }

//            paidDetailMaterialButton.setOnClickListener {
//                listener?.onParkingFeePaidItemClick(data)
//            }

            tvTicket .text = data.ticket ?: ""
            tvArea.text = data.area ?: ""
            tvParkDate.text = data.parkDate ?: ""
            tvPayAmount.text = data.payAmount ?: ""
            tvPayDate.text = data.payDate ?: ""
            tvPaySource.text = data.paySource ?: ""
//            paidStatusContentTextView.text = data.orderStatus

//            paidStatusContentTextView.text = when (data.billPayStatus) {
//                "1" -> {
//                    "已繳費"
//                }
//                "9" -> {
//                    "已退費"
//                }
//                else -> {
//                    "請洽客服"
//                }
//            }
        }
    }
}