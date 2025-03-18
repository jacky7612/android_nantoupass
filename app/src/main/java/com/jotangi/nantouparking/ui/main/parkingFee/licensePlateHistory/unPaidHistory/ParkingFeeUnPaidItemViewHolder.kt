package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.ItemUnPaidFeeBinding
import com.jotangi.nantouparking.databinding.ItemUnPaidFeeRoadBinding
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO2
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class ParkingFeeUnPaidItemViewHolder(val binding: ItemUnPaidFeeRoadBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var checked: Boolean = false

    fun bind(
        position: Int,
        data: ParkingRoadFeeUnPaidVO,
        listener: ParkingFeeUnPaidClickListener?
    ) {
        binding.apply {
            unPaidItemCheckBox.setOnCheckedChangeListener(null)

            unPaidItemCheckBox.isChecked = data.isSelected
            unPaidItemContentConstraintLayout.setBackgroundResource(
                if (data.isSelected)
                    R.drawable.rounded_blue_background
                else
                    R.drawable.rounded_border_spinner
            )

            unPaidItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                data.isSelected = isChecked
                checked = isChecked
                listener?.onParkingFeeUnPaidItemClick(
                    position,
                    data,
                    isChecked
                )
            }

            unPaidItemContentConstraintLayout.setOnClickListener {
                data.isSelected = checked

                listener?.onParkingFeeUnPaidItemClick(
                    position,
                    data,
                    checked
                )
                checked = !checked
            }

            unPaidNoContentTextView.text = data.billNo ?: "N/A"

            val filterDate = getDateFromDateTime(data.billStartTime)
            unPaidDateContentTextView.text = filterDate

            sectionTextView.text = data.billRoad ?: "N/A"

            val filterLeaveTime = getDateFromDateTime(data.billLeaveTime)
            unPaidLeaveTimeContentTextView.text = filterLeaveTime

            unPaidFeeContentTextView.text = data.billAmount ?: "0"
        }
    }

    /**
     * Function to convert dateTime string into a properly formatted date string.
     * Handles cases where dateTime contains "T" and ensures a valid format.
     */
    private fun getDateFromDateTime(dateTime: String?): String {
        if (dateTime.isNullOrEmpty()) return "Unknown Date" // Default return value if null or empty

        return try {
            val cleanedDateTime = dateTime.replace("T", " ") // Replace 'T' if present

            // Define possible input formats
            val inputFormatters = listOf(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), // Full datetime format
                DateTimeFormatter.ofPattern("yyyy-MM-dd") // Date only format
            )

            // Attempt to parse using one of the formats
            val localDateTime = inputFormatters.firstNotNullOfOrNull { formatter ->
                try {
                    LocalDateTime.parse(cleanedDateTime, formatter)
                } catch (e: DateTimeParseException) {
                    null // Return null if parsing fails
                }
            }

            // If successfully parsed, return formatted date **with time**
            localDateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ?: cleanedDateTime
        } catch (e: DateTimeParseException) {
            "Invalid Date" // Return fallback if parsing completely fails
        }
    }

}
