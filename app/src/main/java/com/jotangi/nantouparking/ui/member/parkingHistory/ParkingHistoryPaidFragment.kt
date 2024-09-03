package com.jotangi.nantouparking.ui.member.parkingHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentParkingHistoryPaidBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.ParkingFeePaidVO
import com.jotangi.nantouparking.model.ParkingGarageVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.ParkingGarageAdapter
import com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.ParkingGarageClickListener
import com.jotangi.nantouparking.utility.AppUtility

class ParkingHistoryPaidFragment :
    BaseFragment(),
    ParkingFeePaidClickListener,
    ParkingGarageClickListener {
    private var _binding: FragmentParkingHistoryPaidBinding? = null
    private val binding get() = _binding!!
    private var data = mutableListOf<ParkingFeePaidVO>()
    private var parkingGarageData = mutableListOf<ParkingGarageVO>()
    private lateinit var parkingFeePaidAdapter: ParkingFeePaidAdapter
    private lateinit var parkingGarageAdapter: ParkingGarageAdapter
    private var mPId: String = ""
    private val PARKING_TYPE_ROAD = "1"
    private val PARKING_TYPE_GARAGE = "2"
    private val PARKING_TYPE_GARAGE_LIST = "3"
    private var parkingCurPage = "1"

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkingHistoryPaidBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupParkingHistoryTitle()
        initBackListener()
        initObserver()
        initView()
        initData()
        initAction()
    }

    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            mPId = ""
            when (parkingCurPage) {
                PARKING_TYPE_GARAGE_LIST -> {
                    parkingCurPage = PARKING_TYPE_GARAGE
                    updateContentView()
                }

                PARKING_TYPE_GARAGE -> {
                    parkingCurPage = PARKING_TYPE_ROAD
                    updateTabView()
                    updateContentView()
                    mainViewModel.getParkingFeePaidList(
                        requireContext(),
                        AppUtility.getLoginId(requireContext())!!,
                        AppUtility.getLoginPassword(requireContext())!!,
                        "",
                        mPId
                    )
                }

                PARKING_TYPE_ROAD -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun initObserver() {
        mainViewModel.parkingFeePaidData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.isNotEmpty()) {

//                    val data = result.filter {
//                        it.billPayStatus == "1" || it.billPayStatus == "9"
//                    }

                    binding.apply {
                        parkingFeeRecyclerView.visibility = View.VISIBLE
                        parkingFeeGarageRecyclerView.visibility = View.GONE
                        parkingFeeDefaultTitleTextView.visibility = View.GONE
                    }

                    updateListView(result)
                } else {
                    binding.apply {
                        parkingFeeRecyclerView.visibility = View.GONE
                        parkingFeeGarageRecyclerView.visibility = View.GONE
                        parkingFeeDefaultTitleTextView.visibility = View.VISIBLE
                    }
                }
            }
        }

        mainViewModel.parkingGarageData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                parkingGarageData = result.toMutableList()
                updateParkingGarageListView(result)
            }
        }
    }

    private fun initView() {
        initPaidListRecyclerView()
        initParkingGarageRecyclerView()
    }

    private fun initData() {
        mainViewModel.getParkingFeePaidList(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            "",
            mPId
        )

        mainViewModel.getParkingGarage(requireContext())
    }

    private fun initAction() {
        binding.apply {
            parkingRoadTextView.setOnClickListener {
                parkingCurPage = PARKING_TYPE_ROAD
                updateTabView()
                updateContentView()
                mPId = ""

                if (AppUtility.getLoginStatus(requireContext())) {
                    mainViewModel.getParkingFeePaidList(
                        requireContext(),
                        AppUtility.getLoginId(requireContext())!!,
                        AppUtility.getLoginPassword(requireContext())!!,
                        "",
                        mPId
                    )
                }
            }

            parkingGarageTextView.setOnClickListener {
                parkingCurPage = PARKING_TYPE_GARAGE
                updateTabView()
                updateContentView()
            }
        }
    }

    private fun initPaidListRecyclerView() {
        binding.parkingFeeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingFeePaidAdapter = ParkingFeePaidAdapter(
                data,
                requireContext(),
                this@ParkingHistoryPaidFragment
            )
            this.adapter = parkingFeePaidAdapter
        }
    }

    private fun initParkingGarageRecyclerView() {
        binding.parkingFeeGarageRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingGarageAdapter = ParkingGarageAdapter(
                parkingGarageData,
                requireContext(),
                this@ParkingHistoryPaidFragment
            )
            this.adapter = parkingGarageAdapter
        }
    }

    override fun onParkingFeePaidItemClick(vo: ParkingFeePaidVO) {
        val bundle = bundleOf("billNo" to vo.orderNo)

        findNavController().navigate(
            R.id.action_to_parking_history_paid_detail,
            bundle
        )
    }

    override fun onParkingGarageItemClick(garageData: ParkingGarageVO) {
        mPId = garageData.parkingGarageId
        parkingCurPage = PARKING_TYPE_GARAGE_LIST

        updateContentView()
        mainViewModel.getParkingFeePaidList(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            "",
            mPId
        )
    }

    private fun updateListView(result: List<ParkingFeePaidVO>) {
        parkingFeePaidAdapter.updateDataSource(result)
    }

    private fun updateParkingGarageListView(result: List<ParkingGarageVO>) {
        parkingGarageData = result.toMutableList()
        parkingGarageAdapter.updateDataSource(parkingGarageData)
    }

    private fun updateTabView() {
        binding.apply {
            if (parkingCurPage.equals(PARKING_TYPE_ROAD)) {
                parkingRoadTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue_600
                    )
                )

                parkingGarageTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                parkingRoadTabView.visibility = View.VISIBLE
                parkingGarageTabView.visibility = View.GONE
            } else {
                parkingRoadTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                parkingGarageTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue_600
                    )
                )

                parkingRoadTabView.visibility = View.GONE
                parkingGarageTabView.visibility = View.VISIBLE
            }
        }
    }

    private fun updateContentView() {
        binding.apply {
            when (parkingCurPage) {
                PARKING_TYPE_ROAD, PARKING_TYPE_GARAGE_LIST -> {
                    parkingFeeRecyclerView.visibility = View.VISIBLE
                    parkingFeeGarageRecyclerView.visibility = View.GONE
                }

                PARKING_TYPE_GARAGE -> {
                    parkingFeeRecyclerView.visibility = View.GONE
                    parkingFeeGarageRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }
}