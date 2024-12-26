package com.jotangi.nantouparking.ui.main

import CouponListResponse
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.R

class CouponAdapter(
    private var coupons: List<CouponListResponse>,
    private var tabId: Int,
    private val onItemClick: (CouponListResponse) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coupon_list, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]
        holder.bind(coupon)

        // Change content and background based on tabId
        when (tabId) {
            1 -> {
                holder.itemView.setBackgroundResource(R.color.white)
                holder.couponDate.text = "兌換時間：${coupon.coupon_startdate}"
            }
            2 -> {
                holder.itemView.setBackgroundResource(R.color.lightGray)
                holder.couponDate.text = "過期時間：${coupon.coupon_enddate}"
            }
            else -> {
                holder.itemView.setBackgroundResource(android.R.color.white)
                holder.couponDate.text = "期限：${coupon.coupon_startdate} - ${coupon.coupon_enddate}"
            }
        }
        holder.itemView.setOnClickListener {
            onItemClick(coupon)
        }
    }

    override fun getItemCount(): Int = coupons.size

    fun updateData(newCoupons: List<CouponListResponse>, newTabId: Int) {
        coupons = newCoupons
        tabId = newTabId
        notifyDataSetChanged()
    }

    inner class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val couponTitle: TextView = itemView.findViewById(R.id.tv_title)

        private val couponName: TextView = itemView.findViewById(R.id.tv_coupon_name)
        private val discountAmount: TextView = itemView.findViewById(R.id.tv_discount_amount)
        val couponDate: TextView = itemView.findViewById(R.id.tv_coupon_date)

        fun bind(coupon: CouponListResponse) {
            couponName.text = coupon.coupon_name
            discountAmount.text = "$${coupon.discount_amount} 元"
            couponTitle.text = coupon.coupon_body
        }
    }
}
