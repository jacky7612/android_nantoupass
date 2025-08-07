package com.jotangi.nantoupass.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jotangi.nantoupass.config.ApiConfig
import com.jotangi.nantoupass.databinding.ItemStoreBinding
import com.jotangi.nantoupass.model.StoreVO

class StoreViewHolder(val binding: ItemStoreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: StoreVO,
        listener: StoreClickListener?
    ) {
        binding.apply {
            if (!data.storeImg.isNullOrEmpty()) {
                Glide.with(itemStoreImageView)
                    .load(ApiConfig.URL_HOST + data.storeImg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(itemStoreImageView)
            }

            itemStoreNameTextView.text = data.storeName ?: ""
            itemStoreOpenTimeTextView.text = data.storeOpenTime ?: ""

            itemStoreConstraintLayout.setOnClickListener {
                listener?.onStoreItemClick(data)
            }
        }
    }
}