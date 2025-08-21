package com.jotangi.nantoupass.ui.gov

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.Response4PassNewsContent
import com.jotangi.nantoupass.config.Response4PassSightseeingContent
import com.jotangi.nantoupass.databinding.FragmentMarketBinding
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.StoreVO
import com.jotangi.nantoupass.ui.BaseWithBottomBarFragment
import com.jotangi.nantoupass.ui.gov.adapter.NewsAdapter
import com.jotangi.nantoupass.ui.gov.adapter.SightseeingBannerAdapter
import com.jotangi.nantoupass.ui.gov.adapter.mNewsAdapter
import com.jotangi.nantoupass.ui.gov.adapter.setSpeed
import com.jotangi.nantoupass.ui.gov.models.NewsItem


class MarketFragment : BaseWithBottomBarFragment() {
    private var changeBannerSec = 5

    private lateinit var recyclerView: RecyclerView

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding
    //    private lateinit var storeAdapter: StoreAdapter
    private var data = mutableListOf<StoreVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet


    // Banner 自動輪播
    private var data_sightseeing : List<Response4PassSightseeingContent>? = null
    private val indicators = mutableListOf<ImageView>()
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    private lateinit var newsAdapter: mNewsAdapter
    private var data_news = mutableListOf<Response4PassNewsContent>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMarketTitle()
        initObserver()
        initEvent()
        initNewsRecyclerView()
        getData()

//        recyclerView = view.findViewById(R.id.rvNews)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        val newsList = listOf(
//            NewsItem("南投市最新消息南投市最新消息南投市最新消息", "2025.02.14 13:00"),
//            NewsItem("南投市最新消息南投市最新消息南投市最新消息", "2025.02.14 13:00")
//        )
//
//        newsAdapter = NewsAdapter(newsList)
//        recyclerView.adapter = newsAdapter
    }

    override fun onResume() {
        super.onResume()
        slideHandler.postDelayed(slideRunnable, (changeBannerSec * 1000).toLong())
    }

    override fun onPause() {
        super.onPause()
        slideHandler.removeCallbacks(slideRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (runnable != null) handler.removeCallbacks(runnable!!)
        _binding = null
    }

    private val slideHandler = Handler()
    private val slideRunnable = object : Runnable {
        override fun run() {
            binding?.apply {
                val itemCount = vpSightseeing.adapter?.itemCount ?: 0
                if (itemCount == 0) return

                val nextItem = (vpSightseeing.currentItem + 1) % itemCount
                vpSightseeing.setSpeed(2500f)  // 每次切換前呼叫
                vpSightseeing.setCurrentItem(nextItem, true)
            }
            slideHandler.postDelayed(this, (changeBannerSec * 1000).toLong()) // 3 秒換下一張
        }
    }

    private fun initNewsRecyclerView() {
        binding?.rvNews?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            newsAdapter = mNewsAdapter(
                data_news
            )
            this.adapter = newsAdapter
        }
    }
    /****************
     * api response *
     ****************/
    private fun initObserver() {
        passViewModel.sightseeing.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true" && result.code == "0x0200") {
                    data_sightseeing = result.data?.data
                    setupSightSeeingBanner()
                }
            } else {
            }
        }

        passViewModel.news.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true" && result.code == "0x0200") {
                    binding?.rvNews?.visibility = View.VISIBLE
                    newsAdapter.updateDataSource(result.data?.data)
                }
            } else {
                binding?.rvNews?.visibility = View.GONE
            }
        }
    }
    private fun getData() {
        passViewModel.getSightseeing(requireContext())
        passViewModel.clearNews()
        passViewModel.getNews(requireContext(), "0")
    }

    private fun setupSightSeeingBanner() {
        data_sightseeing?.let { list ->
            val imgList = list.map { it.head_img?: "" }  // 取 head_img
            val adapter = SightseeingBannerAdapter(imgList) { position, url ->
                // 👉 點擊圖片後要做的事
                // 例如：開新頁面 or Toast
                openWeb(list[position].web_url)
                Toast.makeText(requireContext(), "點擊第 $position 張圖: $url", Toast.LENGTH_SHORT).show()
            }
            binding?.vpSightseeing?.adapter = adapter
            binding?.vpSightseeing?.setSpeed(2500f) // 每 1000px 滑動 1500ms

            // 設置小圓點指示器
            setupIndicator(imgList.size)
        }
    }
    private fun setupIndicator(count: Int) {
        binding?.bannerIndicator?.removeAllViews()
        val indicators = arrayOfNulls<ImageView>(count)
        for (i in 0 until count) {
            indicators[i] = ImageView(requireContext()).apply {
                setImageResource(R.drawable.indicator_inactive) // 預設小圓點
                val params = LinearLayout.LayoutParams(16, 16)
                params.setMargins(4, 0, 4, 0)
                layoutParams = params
            }
            binding?.bannerIndicator?.addView(indicators[i])
        }

        binding?.vpSightseeing?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until count) {
                    indicators[i]?.setImageResource(if (i == position) R.drawable.indicator_active else R.drawable.indicator_inactive)
                }
            }
        })
    }

    private fun openWeb(webUri: String?) {
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = Uri.parse(webUri)
        startActivity(intent)
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context?.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open the link.", Toast.LENGTH_SHORT).show()
        }
    }
}