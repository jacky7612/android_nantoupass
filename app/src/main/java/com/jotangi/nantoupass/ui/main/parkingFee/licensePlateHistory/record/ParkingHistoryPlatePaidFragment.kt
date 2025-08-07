package com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory.record

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.JackyVariant.GovpaidfeeAdapter
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.FragmentParkingHistoryPlatePaidBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.ParkingGarageVO
import com.jotangi.nantoupass.ui.BaseFragment
import com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory.ParkingGarageAdapter
import com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory.ParkingGarageClickListener
import com.jotangi.nantoupass.ui.main.parkingFee.licensePlateHistory.ParkingLicensePlateHistoryFragment
import com.jotangi.payStation.Model.ApiModel.ApiEntry
import com.jotangi.payStation.Model.ApiModel.DataGovParkingFeePaidVO

class ParkingHistoryPlatePaidFragment :
    BaseFragment(),
    ParkingFeePaidClickListener,
    ParkingGarageClickListener {
    private var _binding: FragmentParkingHistoryPlatePaidBinding? = null
    private val binding get() = _binding!!
    private var data = mutableListOf<DataGovParkingFeePaidVO>()
    private lateinit var parkingHistoryPlatePaidAdapter: ParkingHistoryPlatePaidAdapter
    private var parkingId: String = ""
    private var plateNo: String = ""
    var parkingCurPage = "1"
    private var govlistAdapter: GovpaidfeeAdapter? =null
    private lateinit var list: MutableList<DataGovParkingFeePaidVO>
    private val PARKING_TYPE_ROAD = "1"
    private val PARKING_TYPE_GARAGE = "2"
    private val PARKING_TYPE_GARAGE_LIST = "3"
    override fun getToolBar(): ToolbarIncludeBinding = binding.toolbarInclude

    private lateinit var parkingGarageAdapter: ParkingGarageAdapter
    private var parkingGarageData = mutableListOf<ParkingGarageVO>()
    companion object {
        var parkingCurPage2 = "1"
        var parkingName2 = ""
    }
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
        initPaidListRecyclerView()
        initObserver()
        initRecyclerView()
//        mainViewModel.paid(mPlateNo)
        binding.parkingPlatePaidRecyclerView.visibility = View.GONE
        mainViewModel.govPlateList.value?.let { list ->
            Log.d("micCheckNNN", list.toString())
            if (list.isEmpty()) {
                binding.parkingPlatePaidRecyclerView.visibility = View.GONE
                binding.parkingPlatePaidDefaultTitleTextView.visibility = View.VISIBLE
            } else {
                binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE
                binding.parkingPlatePaidDefaultTitleTextView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                updateGovListView(list)
            }
        }
        binding.parkingGarageRecyclerView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.GONE
        binding.parkingPlatePaidDefaultTitleTextView.visibility = View.GONE

    }

    private fun updateTabView() {
        binding.apply {
            if (parkingCurPage2.equals(PARKING_TYPE_ROAD)) {
                parkingRoadTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.charge_blue_color
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
                        R.color.charge_blue_color
                    )
                )
                parkingRoadTabView.visibility = View.GONE
                parkingGarageTabView.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView() {
        binding.parkingGarageRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingGarageAdapter = ParkingGarageAdapter(
                parkingGarageData,
                requireContext(),
                this@ParkingHistoryPlatePaidFragment
            )
            this.adapter = parkingGarageAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupParkingHistoryTitle()
//        initObserver(
        initView()
//        initData()
            plateNo = arguments?.getString("plateNo").toString() ?: ""

        requireActivity().runOnUiThread {
            binding.progressBar.bringToFront()
            binding.progressBar.visibility = View.GONE
            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
        }

        mainViewModel.fetchPaidData(plateNo)
        initAction()
    }
//
    private fun initObserver() {
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
    mainViewModel.parkingGarageData.observe(viewLifecycleOwner) { result ->
        if (result != null) {
            updateParkingGarageListView(result)
        }
    }
//    mainViewModel.govPlateList.observe(viewLifecycleOwner) { list ->
//        Log.d("micCheckNNN", list.toString())
//        binding.progressBar.visibility = View.GONE
//        if (list.isNullOrEmpty()) {
//            binding.parkingPlatePaidRecyclerView.visibility = View.GONE
//            binding.parkingPlatePaidDefaultTitleTextView.visibility = View.VISIBLE
//        } else {
//            binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE
//            binding.parkingPlatePaidDefaultTitleTextView.visibility = View.GONE
//            binding.progressBar.visibility = View.GONE
//            updateGovListView(list)
//        }
//    }


//    mainViewModel.parkingFeePaidResponse.observe(viewLifecycleOwner) { result ->
//        if (!result.isNullOrEmpty()) {
//
//            Log.d("micCheckQA3", result.toString())
//            try {
//                // Directly map the list of ParkingFeePaidVO to DataGovParkingFeePaidVO
//                val convertedData = result.map { item ->
//                    DataGovParkingFeePaidVO(
//                        ticket = item.orderNo ?: "單號",
//                        area = item.plateNo ?: "開單公所",
//                        parkDate = item.orderPayDate?.substringBefore(" ") ?: "停車日期",
//                        payAmount = item.orderPayAmount ?: "繳費金額",
//                        payDate = item.orderPayDate?.substringBefore(" ") ?: "繳費日期",
//                        paySource = "統一 代收繳費" // Default value
//                    )
//                }
//
//                data = convertedData.toMutableList()
//                initGovListRecyclerView()
//                binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE
//                // Log the converted data
//                Log.d("micCheckQA2", convertedData.toString())
//            } catch (e: Exception) {
//                Log.e("micCheckUI2", "Error processing result: ${e.localizedMessage}", e)
//            }
//        } else {
//            Toast.makeText(
//                requireActivity(),
//                "目前沒有符合的紀錄唷！",
//                Toast.LENGTH_SHORT
//            ).show()
//            Log.d("micCheckUI2", "Result is null")
//        }
//    }

}

    private fun updateGovListView(result: List<DataGovParkingFeePaidVO>) {
        val newList = if (result.size > 1) result.drop(0) else emptyList()
        Log.d("micCheckGovList", "newList size = ${newList.size}")

        // ✅ Compare with previous list
        if (newList.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            Log.d("micCheckGovList", "Gov list is empty — skipping UI update")
            binding.parkingPlatePaidRecyclerView.visibility = View.GONE
            binding.parkingPlatePaidDefaultTitleTextView.visibility = View.VISIBLE
            return
        }

        // ✅ Save deep copy to prevent accidental mutation later
        ParkingLicensePlateHistoryFragment.previousList = newList.map { it.copy() }
        Log.d("micCheckGovList", "Gov list changed — updating UI")

        if (govlistAdapter == null) {
            govlistAdapter = GovpaidfeeAdapter(newList.toMutableList())
            binding.parkingPlatePaidRecyclerView.layoutManager = GridLayoutManager(
                requireContext(), 1, LinearLayoutManager.VERTICAL, false
            )
            binding.parkingPlatePaidRecyclerView.adapter = govlistAdapter
        } else {
            govlistAdapter?.updateDataSource(newList)
        }


        binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE
        binding.parkingPlatePaidDefaultTitleTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        govlistAdapter?.itemClick = {
            // Handle item click
        }
    }






    private fun updateParkingGarageListView(result: List<ParkingGarageVO>) {
        parkingGarageData = result.toMutableList()
        parkingGarageAdapter.updateDataSource(parkingGarageData)
    }

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
                Log.d("micCheckUI1", responseBody.toString())
                apiEntry.parseGovPlateList(responseBody) // if (apiResponse is ApiRespCarNoOK) {}
                if (Glob.apiGovPayList!!.status.lowercase() == "true") {
                    activity?.runOnUiThread {
                        binding.parkingPlatePaidRecyclerView.visibility =View.VISIBLE
                        binding.parkingPlatePaidDefaultTitleTextView.visibility =View.GONE
                        data =Glob.apiGovPayList.data!!
                        Log.d("micCheckQA1", data.toString())
                        initGovListRecyclerView()
                        binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE

//                        Glob.platNo = tvEntryNumber.text.toString()
//                        findNavController().navigate(R.id.selectPictureFragment)
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "目前沒有符合的紀錄唷！",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("micCheckZA", "2")
                    Log.d("micCheckZA", "4")
                    binding.parkingPlatePaidRecyclerView.visibility =View.GONE
                    binding.parkingPlatePaidDefaultTitleTextView.visibility =View.VISIBLE
//                    activity?.runOnUiThread {
//                        btnEnter.isEnabled = true
//                    }
//                    showMessage("車號:${tvEntryNumber.text}\n\n${Glob.apiRespEN?.responseMessage}")
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "目前沒有符合的紀錄唷！",
                    Toast.LENGTH_SHORT
                ).show()
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

//        mainViewModel.paid2(plateNo)

//        )
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun initAction() {
        binding.apply {

            root.setOnTouchListener { view, motionEvent ->
                binding.parkingGarageRecyclerView.visibility = View.GONE
                false
            }
            parkingRoadTextView.setOnClickListener {
                parkingCurPage2 = PARKING_TYPE_ROAD
                binding.parkingGarageRecyclerView.visibility = View.GONE
                updateTabView()
//                initData()
                binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE
            }

            parkingGarageTextView.setOnClickListener {
                parkingCurPage2 = PARKING_TYPE_GARAGE
                binding.parkingPlatePaidRecyclerView.visibility = View.GONE
                binding.parkingGarageRecyclerView.visibility = View.VISIBLE
                updateTabView()
            }
        }
    }

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
        val newList = mutableListOf<DataGovParkingFeePaidVO>()

        if (data.size > 1) {
            for (i in 1 until data.size) {
                val curData = data[i]
                val item = DataGovParkingFeePaidVO(
                    ticket = curData.ticket,
                    area = curData.area,
                    parkDate = curData.parkDate,
                    payAmount = curData.payAmount,
                    payDate = curData.payDate,
                    paySource = curData.paySource
                )
                newList.add(item)
            }
        }

        // 🔁 Skip if data is same as previous (deep equals)
        if (ParkingLicensePlateHistoryFragment.previousList == newList) {
            Log.d("micCheckGovList", "Gov list is same as previous — skipping update")
            binding.parkingPlatePaidRecyclerView.visibility = View.GONE
            binding.parkingPlatePaidDefaultTitleTextView.visibility = View.VISIBLE
            return
        }

        // ✅ Update and save new list
        ParkingLicensePlateHistoryFragment.previousList = newList.map { it.copy() } // deep copy to prevent future mutation
        Log.d("micCheckGovList", "Gov list updated: $newList")

        if (govlistAdapter == null) {
            govlistAdapter = GovpaidfeeAdapter(newList.toMutableList())
            binding.parkingPlatePaidRecyclerView.apply {
                layoutManager = GridLayoutManager(
                    requireContext(),
                    1,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = govlistAdapter
            }
        } else {
            govlistAdapter?.updateDataSource(newList)
        }

        binding.parkingPlatePaidRecyclerView.visibility = View.VISIBLE
        binding.parkingPlatePaidDefaultTitleTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

    }



    override fun onParkingGarageItemClick(garageData: ParkingGarageVO) {
        var mPId = garageData.parkingGarageId
        parkingName2 = garageData.parkingGarageName
        parkingCurPage = PARKING_TYPE_GARAGE
        binding.parkingGarageRecyclerView.visibility = View.GONE
        binding?.parkingGarageTextView?.text = parkingName2
Log.d("micCheckUI3", mPId)
        if (plateNo == "") {
            plateNo = arguments?.getString("plateNo").toString() ?: ""
        }
        Log.d("micCheckUI4", plateNo)
        mainViewModel.fetchParkingGarageFeePaidList(plateNo, mPId)
    }
}