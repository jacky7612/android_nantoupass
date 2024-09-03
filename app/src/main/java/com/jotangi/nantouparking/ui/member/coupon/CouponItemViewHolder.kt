package com.jotangi.nantouparking.ui.member.coupon

import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemCouponBinding
import com.jotangi.nantouparking.model.CouponVO

class CouponItemViewHolder(val binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: CouponVO,
        listener: CouponClickListener?
    ) {
        binding.itemCouponConstraintLayout.setOnClickListener {
            listener?.onCouponItemClick(data)
        }

        binding.couponItemTitle.text = data.couponName
        binding.couponItemExchangeStatus.text = "註冊期限：${data.couponEndDate}"
    }
}