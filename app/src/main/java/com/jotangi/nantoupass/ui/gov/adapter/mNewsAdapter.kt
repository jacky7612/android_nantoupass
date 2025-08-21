package com.jotangi.nantoupass.ui.gov.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.Response4PassNewsContent

class mNewsAdapter(
    private var data: List<Response4PassNewsContent>
) : RecyclerView.Adapter<mNewsAdapter.mNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mNewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return mNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: mNewsViewHolder, position: Int) {
        val news = data[position]
        holder.title.text = news.title
        holder.date.text = news.content
    }

    override fun getItemCount(): Int = data.size

    fun updateDataSource(dataSource: List<Response4PassNewsContent>?) {
        if (dataSource != null) {
            this.data = dataSource
        }
        this.notifyDataSetChanged()
    }

    class mNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.news_text)
        val date: TextView = itemView.findViewById(R.id.news_date)
    }
}