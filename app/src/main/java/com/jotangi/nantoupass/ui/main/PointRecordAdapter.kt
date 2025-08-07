package com.jotangi.nantoupass.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.model.PointRecord2Response

class PointRecordAdapter(private val dataList: List<PointRecord2Response>) :
    RecyclerView.Adapter<PointRecordAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val tvStoreName: TextView = itemView.findViewById(R.id.tvStoreName)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_point_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = dataList[position]
        holder.tvDateTime.text = record.point_created_at
        holder.tvStoreName.text = record.store_name
        holder.tvPoints.text = record.point + "點"
    }

    override fun getItemCount(): Int = dataList.size
}
