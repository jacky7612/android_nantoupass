package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.record

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.JackyVariant.GovpaidfeeAdapter
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentParkingHistoryPlatePaidBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.ParkingFeePaidVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.jotangi.payStation.Model.ApiModel.ApiEntry
import com.jotangi.payStation.Model.ApiModel.DataGovParkingFeePaidVO
import com.jotangi.payStation.Model.ApiModel.DataStoreInfo4SimpleShow

class ParkingHistoryPlatePaidFragment :
    BaseFragment(),
    ParkingFeePaidClickListener {
    private var _binding: FragmentParkingHistoryPlatePaidBinding? = null
    private val binding get() = _binding!!
    private var data = mutableListOf<DataGovParkingFeePaidVO>()
    private lateinit var parkingHistoryPlatePaidAdapter: ParkingHistoryPlatePaidAdapter
    private var parkingId: String = ""
    private var plateNo: String = ""

    private var govlistAdapter: GovpaidfeeAdapter? =null
    private lateinit var list: MutableList<DataGovParkingFeePaidVO>

    override fun getToolBar(): ToolbarIncludeBinding = binding.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkingHistoryPlatePaidBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
Log.d("micCheckCC", "CC")
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupParkingHistoryTitle()
//        initObserver(
        initView()
        initData()
        initAction()
    }
//
//    private fun initObserver() {
//        mainViewModel.parkingFeePaidData.observe(viewLifecycleOwner) { result ->
//            if (result != null) {
//                if (result.isNotEmpty()) {
//                    binding.apply {
//                        parkingPlatePaidRecyclerView.visibility = View.VISIBLE
//                        parkingPlatePaidDefaultTitleTextView.visibility = View.GONE
//                    }
//
//                    updateListView(result)
//                } else {
//                    binding.apply {
//                        parkingPlatePaidRecyclerView.visibility = View.GONE
//                        parkingPlatePaidDefaultTitleTextView.visibility = View.VISIBLE
//                    }
//                }
//            }
//        }
//    }

    private fun initView() {
//        initPaidListRecyclerView()
    }

    private fun initData() {
        if (plateNo == "") {
            plateNo = arguments?.getString("plateNo").toString() ?: ""
        }

        if (parkingId == "") {
            parkingId = arguments?.getString("parkingId").toString() ?: ""
        }
        val apiEntry =ApiEntry()
        apiEntry.getGovPlateList(plateNo) { responseBody, exception ->
//                        ${Glob.apiRespOrder.data!![0].plate_no}
            Log.d(TAG, "呼叫API尋找車號 :${plateNo}, response :$responseBody")
            if (responseBody != null) {
                Log.d("micCheckZA1", responseBody.toString())
                apiEntry.parseGovPlateList(responseBody) // if (apiResponse is ApiRespCarNoOK) {}
                if (Glob.apiGovPayList!!.status.lowercase() == "true") {
                    activity?.runOnUiThread {
                        binding.parkingPlatePaidRecyclerView.visibility =View.VISIBLE
                        binding.parkingPlatePaidDefaultTitleTextView.visibility =View.GONE
                        data =Glob.apiGovPayList.data!!
                        Log.d("micCheckZA2", data.toString())
                        initGovListRecyclerView()
//                        Glob.platNo = tvEntryNumber.text.toString()
//                        findNavController().navigate(R.id.selectPictureFragment)
                    }
                } else {
                    Log.d("micCheckZA", "4")
                    binding.parkingPlatePaidRecyclerView.visibility =View.GONE
                    binding.parkingPlatePaidDefaultTitleTextView.visibility =View.VISIBLE
//                    activity?.runOnUiThread {
//                        btnEnter.isEnabled = true
//                    }
//                    showMessage("車號:${tvEntryNumber.text}\n\n${Glob.apiRespEN?.responseMessage}")
                }
            } else {
                Log.d("micCheckZA", "2")
//                activity?.runOnUiThread {
//                    btnEnter.isEnabled = true
//                }
//                val msg =
//                    "呼叫API尋找車號後台系統異常 Exception :${exception?.message}"
//                if (LogMessageCtrl.chgApiMsg(msg)) Common.traceUI(msg)
//
//                showMessage("呼叫車號:${tvEntryNumber.text}\n\n尋找車號後台系統異常\n\n請聯絡停車場管理員")
            }
        }

//        mainViewModel.getParkingFeePaidList(
//            requireContext(),
//            "",
//            "",
//            plateNo,
//            parkingId
//        )
    }

    private fun initAction() {}

    private fun initPaidListRecyclerView() {
        binding.parkingPlatePaidRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingHistoryPlatePaidAdapter = ParkingHistoryPlatePaidAdapter(
                data,
                requireContext(),
                this@ParkingHistoryPlatePaidFragment
            )
            this.adapter = parkingHistoryPlatePaidAdapter
        }
    }

    override fun onParkingFeePaidItemClick(vo: DataGovParkingFeePaidVO) {
//        val bundle = bundleOf("billNo" to vo.orderNo)

//        findNavController().navigate(
//            R.id.action_to_parking_plate_paid_detail,
//            bundle
//        )
    }

    private fun updateListView(result: List<DataGovParkingFeePaidVO>) {
        parkingHistoryPlatePaidAdapter.updateDataSource(result)
    }

    private fun initGovListRecyclerView() {
        list = ArrayList()
        Log.d("micCheckJ", data.size.toString())
        if (data.size > 0) {
            for (i in 1 until data.size) {
                var curData =data!![i]

                var item = DataGovParkingFeePaidVO(
                    curData.ticket,
                    curData.area,
                    curData.parkDate,
                    curData.payAmount,
                    curData.payDate,
                    curData.paySource
                )
                list.add(item)
            }
        }
        govlistAdapter = GovpaidfeeAdapter(list)

        binding.parkingPlatePaidRecyclerView.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                1,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =govlistAdapter
        }

        if (govlistAdapter != null) {
            binding.apply {
                govlistAdapter!!.itemClick = {
                }
            }
        }
    }
}