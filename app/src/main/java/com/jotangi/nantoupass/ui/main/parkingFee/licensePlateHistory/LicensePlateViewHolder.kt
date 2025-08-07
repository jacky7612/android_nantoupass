package com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.ItemLicensePlateBinding
import com.jotangi.nantoupass.model.PlateNumberVO

class LicensePlateViewHolder(val binding: ItemLicensePlateBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        context: Context,
        data: PlateNumberVO,
        isEditing: Boolean,
        listener: PlateNumberClickListener?,
    ) {
        binding.apply {
            itemPlateNumberPayRecordTextView.setOnClickListener {
                listener?.onPlateNumberItemClick(
                    "record",
                    data
                )
                Glob.queryHistoryPlateNo =data.plateNo
            }

            itemPlateNumberEditTextView.setOnClickListener {
                listener?.onPlateNumberItemClick(
                    "",
                    data
                )
            }

            itemPlateNumberTextView.text = data.plateNo

            when (isEditing) {
                true -> {
                    itemPlateNumberPayRecordTextView.visibility = View.GONE
                    itemPlateNumberEditTextView.text = "刪除"
                    itemPlateNumberEditTextView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorRed
                        )
                    )
                }

                false -> {
                    itemPlateNumberEditTextView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.charge_blue_color
                        )
                    )
                    itemPlateNumberEditTextView.text = "查詢帳單"
                    itemPlateNumberPayRecordTextView.visibility = View.VISIBLE
                }
            }
        }
    }
}