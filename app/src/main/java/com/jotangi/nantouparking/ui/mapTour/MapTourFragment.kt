package com.jotangi.nantouparking.ui.mapTour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMapTourBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.AttractionVO
import com.jotangi.nantouparking.ui.BaseFragment

class MapTourFragment : BaseFragment() {
    private var _binding: FragmentMapTourBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapTourAdapter: MapTourAdapter
    private var data = mutableListOf<AttractionVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapTourBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        setupMapTourTitle()
        initObserver()
        initView()
        initData()
    }

    private fun initObserver() {
        mainViewModel.attractions.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                mapTourAdapter.updateDataSource(result)
            }
        }
    }

    private fun initView() {
        initMapView()
        initRecyclerView()
    }

    private fun initData() {
        mainViewModel.getAttractions(requireContext())
    }

    private fun initMapView() {
        binding.mapTourWebView.apply {
            val webViewClient = WebViewClient()

            this.webViewClient = webViewClient
            this.settings.javaScriptEnabled = true
            this.settings.builtInZoomControls = true
            this.settings.displayZoomControls = false
            this.settings.setSupportZoom(true)
            this.settings.loadWithOverviewMode = true
            this.settings.useWideViewPort = true
            this.settings.allowFileAccessFromFileURLs = true

            this.loadDataWithBaseURL(
                "file:///android_res/drawable/",
                "<img src='zhu_dong_map.png' />",
                "text/html",
                "utf-8",
                null
            )
        }
    }

    private fun initRecyclerView() {
        binding.attractionRecyclerView.apply {
            mapTourAdapter = MapTourAdapter(
                data,
                requireContext()
            )
            this.adapter = mapTourAdapter
        }
        viewContext()

    }

    private fun viewContext() {

        mapTourAdapter.ItemClick = {
            val bundle = bundleOf(
                "storeName" to it.storeName,
                "storeDescript" to it.storeDescript,
                "storeAddress" to it.storeAddress,
                "storeLatitude" to it.storeLatitude,
                "storeLongitude" to it.storeLongitude,
                "storePhone" to it.storePhone,
                "storePicture" to it.storePicture
            )
            findNavController().navigate(R.id.action_map_tour_fragment_to_storeIntroduceFragment, bundle)
        }

    }

    companion object {

    }
}