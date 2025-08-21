package com.jotangi.nantoupass.ui.gov.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.config.Response4PassAgencyUnitServices
import com.jotangi.nantoupass.databinding.ItemAgencyServiceBinding

class AgencyDetailAdapter(
    private var data: List<Response4PassAgencyUnitServices>
) : RecyclerView.Adapter<AgencyDetailAdapter.ItemViewHolder>() {

    // Item ViewHolder
    class ItemViewHolder(private val binding: ItemAgencyServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Response4PassAgencyUnitServices) {
            binding.tvBusinessDetails.text = item.service
            binding.tvPhoneNumber.text = item.contact
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemAgencyServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return   ItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<Response4PassAgencyUnitServices>) {
        data = newData
        notifyDataSetChanged()
    }
}