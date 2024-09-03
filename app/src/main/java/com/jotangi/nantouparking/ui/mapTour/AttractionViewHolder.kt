package com.jotangi.nantouparking.ui.mapTour

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemMapTourBinding
import com.jotangi.nantouparking.model.AttractionVO

class AttractionViewHolder(val binding: ItemMapTourBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        index: Int,
        data: AttractionVO
    ) {
        binding.apply {
            itemMapTourNoTextView.text = index.toString() //data.sid.substring(0)
            itemMapTourContentTextView.text = data.storeName
        }
    }
}