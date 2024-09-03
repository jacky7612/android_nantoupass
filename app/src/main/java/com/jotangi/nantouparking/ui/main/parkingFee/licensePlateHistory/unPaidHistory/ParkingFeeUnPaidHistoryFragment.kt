package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.config.AppConfig
import com.jotangi.nantouparking.databinding.FragmentParkingFeeUnPaidHistoryBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.ParkingGarageFeeUnPaidResponse
import com.jotangi.nantouparking.model.ParkingGarageFeeUnPaidVO
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidResponse
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.jotangi.nantouparking.utility.DateUtility

class ParkingFeeUnPaidHistoryFragment :
    BaseFragment(),
    ParkingFeeUnPaidClickListener,
    ParkingGarageFeeUnPaidClickListener {
    private var _binding: FragmentParkingFeeUnPaidHistoryBinding? = null
    private val binding get() = _binding
    private var data = mutableListOf<ParkingRoadFeeUnPaidVO>()
    private var garageData = mutableListOf<ParkingGarageFeeUnPaidVO>()
    private var payData: ParkingRoadFeeUnPaidVO? = null
    private var payGarageData: ParkingGarageFeeUnPaidVO? = null
    private lateinit var parkingFeeUnPaidAdapter: ParkingFeeUnPaidAdapter
    private lateinit var parkingGarageFeeUnPaidAdapter: ParkingGarageFeeUnPaidAdapter
    private var plateNo: String = ""
    private var parkingId: String = ""
    private var parkingName: String = ""
    private var parkingAddress: String = ""

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkingFeeUnPaidHistoryBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onResume() {
        super.onResume()

        initData()
    }

    override fun onPause() {
        super.onPause()

        mainViewModel.hasData = false
    }

    private fun init() {
        setupParkingHistoryUnPaidTitle()
        initObserver()
        initBundle()
        initView()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.parkingRoadFeeUnPaidData.observe(viewLifecycleOwner) { result ->
            if (result?.unPaidItems != null) {
                if (result.unPaidItems.isNotEmpty()) {
                    updateRoadListView(result)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "目前沒有符合的紀錄唷！",
                        Toast.LENGTH_SHORT
                    ).show()

                    onBackPressed()
                }
            }
        }

        mainViewModel.parkingGarageFeeUnPaidData.observe(viewLifecycleOwner) { result ->
            if (result?.unPaidItems != null) {
                if (result.unPaidItems.isNotEmpty()) {
                    updateGarageListView(result)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "目前沒有符合的紀錄唷！",
                        Toast.LENGTH_SHORT
                    ).show()

                    onBackPressed()
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    result.responseMessage,
                    Toast.LENGTH_SHORT
                ).show()

                onBackPressed()
            }
        }

        mainViewModel.billIsLock.observe(viewLifecycleOwner) { lockResponse ->
            if (lockResponse != null) {
                showPayMethodDialogView()
            }
        }
    }

    private fun initBundle() {
        if (plateNo == "") {
            plateNo = arguments?.getString("plateNo").toString()
        }

        if (parkingId == "") {
            parkingId = arguments?.getString("parkingId").toString() ?: ""
            parkingName  = arguments?.getString("parkingName").toString() ?: ""
            parkingAddress = arguments?.getString("parkingAddress").toString() ?: ""
        }
    }

    private fun initData() {
        if (parkingId == "") {
            mainViewModel.getParkingRoadFeeUnPaidList(
                requireContext(),
                plateNo
            )
        } else {
            mainViewModel.getParkingGarageFeeUnPaidList(
                requireContext(),
                plateNo,
                parkingId
            )
        }
    }

    private fun initView() {
        updatePlateNo()

        if (parkingId == "") {
            initRecyclerView()
        } else {
            initGarageRecyclerView()
        }
    }

    private fun updatePlateNo() {
        binding?.apply {
            parkingHistorySearchPlateNoTitleTextView.text =
                "${parkingHistorySearchPlateNoTitleTextView.text}$plateNo"
        }
    }

    private fun initRecyclerView() {
        binding?.parkingHistoryUnPaidRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingFeeUnPaidAdapter = ParkingFeeUnPaidAdapter(
                data,
                requireContext(),
                this@ParkingFeeUnPaidHistoryFragment
            )
            this.adapter = parkingFeeUnPaidAdapter
        }
    }

    private fun initGarageRecyclerView() {
        binding?.parkingHistoryUnPaidRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingGarageFeeUnPaidAdapter = ParkingGarageFeeUnPaidAdapter(
                garageData,
                requireContext(),
                this@ParkingFeeUnPaidHistoryFragment
            )
            this.adapter = parkingGarageFeeUnPaidAdapter
        }
    }

    override fun onParkingFeeUnPaidItemClick(
        position: Int,
        vo: ParkingRoadFeeUnPaidVO
    ) {
        data.forEach {
            it.isSelected = false
        }

        data[position].isSelected = true

        payData = vo
        parkingFeeUnPaidAdapter.updateDataSource(data)
    }

    override fun onParkingGarageFeeUnPaidItemClick(
        position: Int,
        vo: ParkingGarageFeeUnPaidVO
    ) {
        garageData.forEach {
            it.isSelected = false
        }

        garageData[position].isSelected = true

        payGarageData = vo
        parkingGarageFeeUnPaidAdapter.updateDataSource(garageData)
    }

    private fun initAction() {
        binding?.apply {
            unPaidPayButton.setOnClickListener {
                isShowDialog()
            }
        }
    }

    private fun isShowDialog() {
        if (
            (payData != null && payData!!.isSelected) ||
            (payGarageData != null && payGarageData!!.isSelected)
        ) {
            showPayMethodDialogView()
        } else {
            AppUtility.showPopDialog(
                requireContext(),
                null,
                "請先選擇待繳停車費唷"
            )
        }
    }

    private fun updateRoadListView(result: ParkingRoadFeeUnPaidResponse) {
        data = result.unPaidItems.toMutableList()
        parkingFeeUnPaidAdapter.updateDataSource(data)
    }

    private fun updateGarageListView(result: ParkingGarageFeeUnPaidResponse) {
        garageData = result.unPaidItems.toMutableList()
        parkingGarageFeeUnPaidAdapter.updateDataSource(garageData)
    }

    private fun showPayMethodDialogView() {
        try {
            launchUri(getAppUrl())
        } catch (e: PackageManager.NameNotFoundException) {
            confirmLineInstall(AppConfig.LINE_URL)
            e.printStackTrace()
        }
    }

    private fun getAppUrl(): String {
        val appUrl = if (parkingId.isNotEmpty()) {
            ApiConfig.URL_HOST +
                    ApiConfig.PAYMENT_URL +
                    "?" +
                    "member_id=${AppUtility.getLoginId(requireContext())}" +
                    "&" +
                    "amount=${payGarageData!!.billAmount}" +
                    "&" +
                    "bill_no=${payGarageData!!.billNo}" +
                    "&" +
                    "plate_number=$plateNo" +
                    "&" +
                    "plateId=$parkingId" +
                    "&" +
                    "description=$parkingName" +
                    " " +
                    parkingAddress +
                    "&" +
                    "parkTime=${payGarageData!!.billStartTime}" +
                    "&" +
                    "searchTime=${payGarageData!!.billSearchTime}"
        } else {
            ApiConfig.URL_HOST +
                    ApiConfig.PAYMENT_URL +
                    "?" +
                    "member_id=${AppUtility.getLoginId(requireContext())}" +
                    "&" +
                    "amount=${payData!!.billAmount}" +
                    "&" +
                    "bill_no=${payData!!.billNo}" +
                    "&" +
                    "plate_number=$plateNo" +
                    "&" +
                    "description=${payData!!.billRoad}" +
                    " " +
                    "${payData!!.billCell}" +
                    "&" +
                    "parkTime=${payData!!.billStartTime}" +
                    "&" +
                    "searchTime=${payData!!.billSearchTime}"
        }
        return appUrl
    }

    private fun launchUri(uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )

        startActivity(intent)
    }

    private fun confirmLineInstall(webUrl: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("LINE Pay")
            .setMessage("請先安裝Line App或使用網頁進行付款")
            .setCancelable(false)
            .setPositiveButton("安裝") { dialog, which ->
//                launchUri("market://details?id=jp.naver.line.android")
                launchUri(AppConfig.LINE_URL)
            }
            .setNegativeButton("開啟網頁") { dialog, which -> //開啟網頁
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(webUrl)
                startActivity(i)
            }
            .show()
    }
}