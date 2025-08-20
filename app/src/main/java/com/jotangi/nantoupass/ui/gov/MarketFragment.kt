package com.jotangi.nantoupass.ui.gov

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.MainActivity
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.FragmentMainBinding
import com.jotangi.nantoupass.databinding.FragmentMarketBinding
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.StoreVO
import com.jotangi.nantoupass.ui.BaseWithBottomBarFragment
import com.jotangi.nantoupass.ui.gov.NewsAdapter
import com.jotangi.nantoupass.ui.gov.NewsItem
import com.jotangi.nantoupass.utility.AppUtility


class MarketFragment : BaseWithBottomBarFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding
    //    private lateinit var storeAdapter: StoreAdapter
    private var data = mutableListOf<StoreVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet

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
        initEvent()

        recyclerView = view.findViewById(R.id.recyclerViewNews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val newsList = listOf(
            NewsItem("南投市最新消息南投市最新消息南投市最新消息", "2025.02.14 13:00"),
            NewsItem("南投市最新消息南投市最新消息南投市最新消息", "2025.02.14 13:00")
        )

        newsAdapter = NewsAdapter(newsList)
        recyclerView.adapter = newsAdapter
    }
}