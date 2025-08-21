package com.jotangi.nantoupass.ui.gov.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.ApiPassConfig

class SightseeingBannerAdapter(private val data: List<String>?,
                               private val onItemClick: ((position: Int, url: String) -> Unit)? = null
) : RecyclerView.Adapter<SightseeingBannerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_sightseeing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sightseeing_banner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = data!![position]

        // 使用 Glide 或 Picasso 來載入圖片
        Glide.with(holder.itemView.context)
            .load("${ApiPassConfig.URL_HOST}$url")
            .into(holder.imageView)

        // 點擊事件
        holder.imageView.setOnClickListener {
            onItemClick?.invoke(position, url)
        }
    }
}