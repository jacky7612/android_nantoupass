package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.ItemUnPaidFeeBinding
import com.jotangi.nantouparking.model.ParkingGarageFeeUnPaidVO
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO

class ParkingGarageFeeUnPaidItemViewHolder(val binding: ItemUnPaidFeeBinding) : RecyclerView.ViewHolder(binding.root) {
    var checked:Boolean = false
    fun bind(
        position: Int,
        data: ParkingGarageFeeUnPaidVO,
        listener: ParkingGarageFeeUnPaidClickListener?
    ) {
        binding.apply {
            unPaidItemCheckBox.setOnCheckedChangeListener(null)

            unPaidItemCheckBox.isChecked = data.isSelected
            unPaidItemContentConstraintLayout.setBackgroundResource(
                if (data.isSelected)
                    R.drawable.rounded_blue_background
                else
                    R.drawable.round_black_empty
            )
            unPaidItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                data.isSelected = isChecked
                checked = isChecked
                listener?.onParkingGarageFeeUnPaidItemClick(
                    position,
                    data,
                    isChecked
                )
            }
            unPaidItemContentConstraintLayout.setOnClickListener {
                data.isSelected = checked
                listener?.onParkingGarageFeeUnPaidItemClick(
                    position,
                    data,
                    checked
                )
                checked = !checked
            }

            unPaidNoContentTextView.text = data.billNo

            val filterDate = getDateFromDateTime(data.billStartTime)
            unPaidDateContentTextView.text = filterDate
            unPaidFeeContentTextView.text = data.billAmount
        }
    }

    private fun getDateFromDateTime(dateTime: String): String {
        var dateTimeString = dateTime
        if (dateTime.contains("T")) {
            dateTimeString = dateTime.replace(
                "T",
                " "
            )
        }

//        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//
//        val localDateTime = LocalDateTime.parse(
//            dateTimeString,
//            inputFormatter
//        )

//        return localDateTime.format(outputFormatter)
        return dateTimeString
    }
}