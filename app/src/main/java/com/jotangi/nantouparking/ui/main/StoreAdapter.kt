package com.jotangi.nantouparking.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemParkingGarageBinding
import com.jotangi.nantouparking.databinding.ItemStoreBinding
import com.jotangi.nantouparking.model.ParkingGarageVO
import com.jotangi.nantouparking.model.StoreVO

interface StoreClickListener {
    fun onStoreItemClick(storeData: StoreVO)
}

class StoreAdapter(
    private var data: List<StoreVO>,
    val context: Context,
    private val listener: StoreClickListener?
) : RecyclerView.Adapter<StoreViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoreViewHolder {
        val binding = ItemStoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StoreViewHolder,
        position: Int
    ) {
        holder.bind(
            data[position],
            listener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<StoreVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}