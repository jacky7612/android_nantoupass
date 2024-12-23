package com.jotangi.nantouparking.ui.main.parkingInfo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jotangi.nantouparking.databinding.FragmentParkingInfoBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.ParkingInfoBuildingVO
import com.jotangi.nantouparking.ui.BaseFragment


class ParkingInfoFragment :
    BaseFragment(),
    ParkingInfoClickListener {
    private var _binding: FragmentParkingInfoBinding? = null
    private val binding get() = _binding
    private var handler: Handler? = null
    private var startY: Float = 0f

    private var data = mutableListOf<ParkingInfoBuildingVO>()
    private lateinit var parkingInfoAdapter: ParkingInfoAdapter

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    private val listener = SwipeRefreshLayout.OnRefreshListener {
        updateRefreshScrollView()
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            initData()
            Log.d("TAG", "run: ${"跑"}")
            handler!!.postDelayed(this, 10000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkingInfoBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mainViewModel.clearParkingInfoList()
        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        setupParkingInfoTitle()
        initObserver()
        initData()
        initView()
        initAction()
        startAutoUpdateTimer()
    }

    private fun initObserver() {
        mainViewModel.buildingParkingSpaceData.observe(viewLifecycleOwner) { result ->
//            binding?.parkingInfoSwipe?.isRefreshing = false
Log.d("micCheckMNM", result.toString())
            if (result != null) {
                parkingInfoAdapter.updateDataSource(result.parkingData)
//                updateLeftListView(result.parkingData)
//                updateScrollViewPosition()
            }
        }
    }

    private fun initData() {
        mainViewModel.getParkingInfoLeftList(requireContext())
    }

    private fun initView() {
//        updateScrollViewPosition()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding?.parkingInfoRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingInfoAdapter = ParkingInfoAdapter(
                data,
                requireContext(),
                this@ParkingInfoFragment
            )
            this.adapter = parkingInfoAdapter
        }
    }

    private fun updateScrollViewPosition() {
        binding?.apply {
//            parkingInfoScrollView.smoothScrollTo(
//                0,
//                60
//            )
        }
    }

    private fun initAction() {
        binding?.apply {
//            parkingInfoNavigationButton.setOnClickListener {
//                openMap()
//            }

//            parkingInfoSwipe.setOnRefreshListener(listener)

//            parkingInfoScrollView.setOnTouchListener { view, motionEvent ->
//                when (motionEvent.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        startY = motionEvent.y
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        val endY = motionEvent.y
//                        if (endY - startY > 100) {
//                            // 偵測到向下滑動手勢
//                            // 在這裡觸發刷新操作
//                            parkingInfoSwipe.isRefreshing = true
//                            updateRefreshScrollView()
//                        }
//                    }
//                }

//                false
//            }
        }
    }

    private fun updateRefreshScrollView() {
//        requireActivity().runOnUiThread {
//            val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                binding?.apply {
//                    parkingInfoSwipe.isRefreshing = false
//                }
//                initData()
//            }, 1500)
//        }
    }

    private fun startAutoUpdateTimer() {
        handler = Handler()
        handler?.post(runnable)
    }

    private fun openMap() {
//        val parkingInfoGPS = "http://maps.google.com/maps?daddr=${AppConfig.ZHU_DONG_BUILDING_OFFICE_LONGITUDE},${AppConfig.ZHU_DONG_BUILDING_OFFICE_LATITUDE}"
//        val gmmIntentUri: Uri =
//            Uri.parse(parkingInfoGPS)
//        val mapIntent = Intent(
//            Intent.ACTION_VIEW,
//            gmmIntentUri
//        )
//
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)
    }

    private fun updateLeftListView(result: List<ParkingInfoBuildingVO>) {
        binding?.apply {
//            val upSpace = result.upSpace ?: "0"
//            val upFreeSpace = result.upFreeSpace ?: "0"
//            val downSpace = result.downSpace ?: "0"
//            val downFreeSpace = result.downFreeSpace ?: "0"
//
//            parkingInfoLeftLayerOneTotalContentTextView.text = "${upSpace}格"
//            parkingInfoLeftLayerOneCurrentContentTextView.text = "${upFreeSpace}格"
//            parkingInfoLeftLayerTwoTotalContentTextView.text = "${downSpace}格"
//            parkingInfoLeftLayerTwoCurrentContentTextView.text = "${downFreeSpace}格"
        }
    }

    override fun onParkingInfoItemClick() {

    }

    override fun onPause() {
        super.onPause()

        handler?.removeCallbacks(runnable)
    }
}