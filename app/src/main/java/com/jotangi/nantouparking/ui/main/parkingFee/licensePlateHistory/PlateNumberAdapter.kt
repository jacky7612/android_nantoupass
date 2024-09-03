package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemLicensePlateBinding
import com.jotangi.nantouparking.model.PlateNumberVO

interface PlateNumberClickListener {
    fun onPlateNumberItemClick(
        type: String,
        vo: PlateNumberVO
    )
}

class PlateNumberAdapter(
    private var data: List<PlateNumberVO>,
    val context: Context,
    private val listener: PlateNumberClickListener?
) : RecyclerView.Adapter<LicensePlateViewHolder>() {
    var isEditing = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LicensePlateViewHolder {
        val binding = ItemLicensePlateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LicensePlateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LicensePlateViewHolder,
        position: Int
    ) {
        holder.bind(
            context,
            data[position],
            isEditing,
            listener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<PlateNumberVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }

    fun updateEditStatus(editStatus: Boolean) {
        isEditing = editStatus
    }
}