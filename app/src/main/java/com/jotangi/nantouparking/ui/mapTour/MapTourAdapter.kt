package com.jotangi.nantouparking.ui.mapTour

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemMapTourBinding
import com.jotangi.nantouparking.model.AttractionVO

class MapTourAdapter(
    private var data: List<AttractionVO>,
    val context: Context
) : RecyclerView.Adapter<AttractionViewHolder>() {

    var ItemClick: (AttractionVO) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttractionViewHolder {
        val binding = ItemMapTourBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AttractionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val index = position + 1
        holder.bind(index, data[position])
        holder.itemView.setOnClickListener {
            data[position].let(ItemClick)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<AttractionVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }
}