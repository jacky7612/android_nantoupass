package com.jotangi.nantouparking.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.R

class MemberRecordAdapter(private val records: List<MemberRecord>) :
    RecyclerView.Adapter<MemberRecordAdapter.MemberRecordViewHolder>() {

    class MemberRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDateTime: TextView = view.findViewById(R.id.tv_date_time)
        val tvAccountNumber: TextView = view.findViewById(R.id.tv_account_number)
        val tvPointCount: TextView = view.findViewById(R.id.tv_point_count)
        val tvTotalPrice: TextView = view.findViewById(R.id.tv_total_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberRecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member_record, parent, false)
        return MemberRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberRecordViewHolder, position: Int) {
        val record = records[position]
        holder.tvDateTime.text = "時間：${record.dateTime}"
        holder.tvAccountNumber.text = "會員帳號：${record.accountNumber}"
        holder.tvPointCount.text = "核銷點數：${record.pointCount}"
        holder.tvTotalPrice.text = "商品總價：${record.totalPrice}"
    }

    override fun getItemCount(): Int = records.size
}
