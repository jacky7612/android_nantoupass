package com.jotangi.nantoupass.ui.charge.ChargeMain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemChargeHistoryBinding
import com.jotangi.nantoupass.model.charge.DataChargeHistory

//interface ItemChargeHistoryClickListener {
//    fun onChargeHistoryItemClick(
//        vo: DataChargeHistory
//    )
//}

class ChargeHistoryAdapter(
    private val itemList: List<DataChargeHistory>,
    private val itemClickListener: (DataChargeHistory) -> Unit // Click listener as a lambda
) : RecyclerView.Adapter<ChargeHistoryAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val binding: ItemChargeHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataChargeHistory, clickListener: (DataChargeHistory) -> Unit) {
            binding.apply {
                val kWh = item.kwh
                tvTitle.text        =item.start_time
                tvChargeTime.text   =item.charge_time
                tvStatus.text       ="金額 :${item.price} 元"
                tvChargeKw.text     ="${kWh}kWh"

                ivArrowRight.setOnClickListener {
                    clickListener(item)
                }
            }

            // Set click listener on the whole item view
//            binding.root.setOnClickListener { clickListener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemChargeHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}