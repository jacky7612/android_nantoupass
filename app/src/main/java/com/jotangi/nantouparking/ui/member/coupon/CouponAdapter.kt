package com.jotangi.nantouparking.ui.member.coupon

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemCouponBinding
import com.jotangi.nantouparking.model.CouponVO

interface CouponClickListener {
    fun onCouponItemClick(vo: CouponVO)
}

class CouponAdapter(
    private var data: List<CouponVO>,
    val context: Context,
    private val listener: CouponClickListener?
) : RecyclerView.Adapter<CouponItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CouponItemViewHolder {
        val binding = ItemCouponBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CouponItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CouponItemViewHolder,
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

    fun updateDataSource(dataSource: List<CouponVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }

    fun clearDataSource() {
        val emptyList: List<CouponVO> = emptyList()
        this.data = emptyList

        this.notifyDataSetChanged()
    }
}