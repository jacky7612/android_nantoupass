package com.jotangi.nantoupass.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.databinding.ItemStoreBinding
import com.jotangi.nantoupass.model.StoreVO

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