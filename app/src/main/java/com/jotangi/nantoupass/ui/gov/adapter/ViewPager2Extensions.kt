package com.jotangi.nantoupass.ui.gov.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.setSpeed(scrollSpeed: Float) {
    try {
        // 取出內部 RecyclerView
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val linearLayoutManager = recyclerView.layoutManager ?: return

        // 自定義 SmoothScroller
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun calculateTimeForScrolling(dx: Int): Int {
                // dx: 滑動距離 px
                // scrollSpeed: 每 1000px 所需時間
                return (scrollSpeed * dx / 1000).toInt()
            }
        }

        // 設定 RecyclerView 的 LinearSmoothScroller（這裡示範單次使用）
        recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}