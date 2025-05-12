package com.jotangi.nantouparking.ui.charge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.ItemChargerStatusBinding
import com.jotangi.nantouparking.model.charge.DataChargeStatusInfo

class ChargerStatusAdapter(
    private val statusList: List<DataChargeStatusInfo>
) : RecyclerView.Adapter<ChargerStatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(val binding: ItemChargerStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataChargeStatusInfo) {
            val count = item.detail?.avalible_count ?: 0
            val typeLabel = "${item.type} 樁" + " " +item.detail.name

            binding.tvChargerType.text = typeLabel
            binding.tvChargerStatus.text = "可用 $count"

            binding.tvChargerStatus.setBackgroundResource(
                if (count.equals("0")) R.drawable.bg_status_red
                else R.drawable.bg_status_green
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemChargerStatusBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StatusViewHolder(binding)
    }

    override fun getItemCount(): Int = statusList.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(statusList[position])
    }
}
