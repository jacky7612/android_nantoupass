package com.jotangi.nantouparking.JackyVariant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.AdapterGovpaidfeeBinding
import com.jotangi.payStation.Model.ApiModel.DataGovParkingFeePaidVO

class GovpaidfeeAdapter(private val list: MutableList<DataGovParkingFeePaidVO>) :
                        RecyclerView.Adapter<GovpaidfeeAdapter.ViewHolder>() {


    // Provide a direct reference to each of the views with data items

    var itemClick: (position: Int) -> Unit = {}

    inner class ViewHolder(private val binding: AdapterGovpaidfeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataGovParkingFeePaidVO) {
            binding.apply {
                tvTicket .text   = data.ticket ?: ""
                tvArea.text      = data.area ?: ""
                tvParkDate.text  = data.parkDate ?: ""
                tvPayAmount.text = data.payAmount ?: ""
                tvPayDate.text   = data.payDate ?: ""
                tvPaySource.text = data.paySource ?: ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AdapterGovpaidfeeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            itemClick(position)
        }
    }

    override fun getItemCount() = list.size
}