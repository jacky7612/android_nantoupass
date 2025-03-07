package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.R
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ParkingFeeUnPaidHistoryFragment :
    BaseFragment(),
    ParkingFeeUnPaidClickListener,
    ParkingGarageFeeUnPaidClickListener {
    private var isAllSelected = false
    private val REQUEST_CODE_BROWSER = 1001
    private var _binding: FragmentParkingFeeUnPaidHistoryBinding? = null
    private val binding get() = _binding
    private var selectPayData = mutableListOf<ParkingRoadFeeUnPaidVO>()
    private var selectGaragePayData = mutableListOf<ParkingGarageFeeUnPaidVO>()
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
var call = false
    var call2 = false
    companion object {
        var back = false
    }
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
initListener()
        init()
    }

    override fun onResume() {
        super.onResume()

        Log.d("micCheckLL", "LL")
        selectPayData.clear()
        binding?.selectAll?.isChecked = false
        selectGaragePayData.clear()
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
           if(call) {
               if (result?.unPaidItems != null) {
                   Log.d("micCheckZ3", result.toString())
                   if (result.unPaidItems.isNotEmpty()) {
                       updateRoadListView(result)
                       requireActivity().runOnUiThread {
                           binding?.progressBar?.visibility = View.GONE
                           Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                       }
                   } else {
                       Log.d("micCheckZ4", result.toString())
                       requireActivity().runOnUiThread {
                           binding?.progressBar?.visibility = View.GONE
                           Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                       }
                       Toast.makeText(
                           requireActivity(),
                           result.responseMessage,
                           Toast.LENGTH_SHORT
                       ).show()
//                    onBackPressed()
                   }
               }
           }
            call = false
        }

        mainViewModel.parkingGarageFeeUnPaidData.observe(viewLifecycleOwner) { result ->
            if(call2) {
                if (result?.unPaidItems != null) {
                    if (result.unPaidItems.isNotEmpty()) {
                        updateGarageListView(result)
                        requireActivity().runOnUiThread {
                            binding?.progressBar?.visibility = View.GONE
                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            binding?.progressBar?.visibility = View.GONE
                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                        }
                        Toast.makeText(
                            requireActivity(),
                            "目前沒有符合的紀錄唷！",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("micCheckZZ", "ZZ2")
                        onBackPressed()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        binding?.progressBar?.visibility = View.GONE
                        Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                    }
                    Toast.makeText(
                        requireActivity(),
                        result.responseMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("micCheckZZ", "ZZ3")
                    onBackPressed()
                }
            }
            call2 = false
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
        Log.d("micCheckHG", (parkingId.equals("")).toString())
        if (parkingId == "") {
            call = true
            Log.d("micCheckHG", "1")
            Log.d("micCheckZ5", plateNo)
            requireActivity().runOnUiThread {
                binding?.progressBar?.visibility = View.VISIBLE
                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
            }
            lifecycleScope.launch {
                delay(2000) // Delay for 2 seconds
                mainViewModel.getParkingRoadFeeUnPaidList(
                    requireContext(),
                    plateNo
                )
            }
        } else {
            call2 = true
            requireActivity().runOnUiThread {
                binding?.progressBar?.visibility = View.VISIBLE
                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
            }
            Log.d("micCheckHG", "2")
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
        vo: ParkingRoadFeeUnPaidVO,
        isChecked: Boolean
    ) {
//        data.forEach {
//            it.isSelected = false
//        }
        if (isChecked) {
            data[position].isSelected = true
            selectPayData.add(data[position])
        } else {
            data[position].isSelected = false
            if (selectPayData.contains(data[position])) {
                selectPayData.remove(data[position])
            }
        }
        payData = vo
        parkingFeeUnPaidAdapter.updateDataSource(data)
    }

    override fun onParkingGarageFeeUnPaidItemClick(
        position: Int,
        vo: ParkingGarageFeeUnPaidVO,
        isChecked: Boolean
    ) {
//        garageData.forEach {
//            it.isSelected = false
//        }
        if (isChecked) {
            garageData[position].isSelected = true
            selectGaragePayData.add(garageData[position])
        } else {
            garageData[position].isSelected = false
            if (selectGaragePayData.contains(garageData[position])) {
                selectGaragePayData.remove(garageData[position])
            }
        }

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
        Log.d("micCheckUU5", (selectPayData == null).toString())
        if (
            (!selectPayData.isNullOrEmpty() ) ||
            (!selectGaragePayData.isNullOrEmpty() )
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
        if (!::parkingFeeUnPaidAdapter.isInitialized) {
            initRecyclerView()
        }
        data = result.unPaidItems.toMutableList()
        parkingFeeUnPaidAdapter.updateDataSource(data)
    }

    private fun updateGarageListView(result: ParkingGarageFeeUnPaidResponse) {
        if (!::parkingGarageFeeUnPaidAdapter.isInitialized) {
            initGarageRecyclerView()
        }
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

        val currentDateTime = LocalDateTime.now()
        println("Current Date and Time: $currentDateTime")

        // Format the date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        // Function to remove duplicates based on billNo
        fun <T> removeDuplicates(list: List<T>, selector: (T) -> String?): List<T> {
            val seen = mutableSetOf<String?>()
            return list.filter { item ->
                val key = selector(item)
                seen.add(key) // Add returns true if the element was not present, false if it was already there
            }
        }

        return if (parkingId.isNotEmpty()) {
            // Remove duplicates from selectGaragePayData based on billNo
            val uniqueGaragePayData = removeDuplicates(selectGaragePayData) { it?.billNo }

            // Concatenate values after removing duplicates
            val amounts = uniqueGaragePayData.joinToString(",") { it?.billAmount.toString() }
            val billNos = uniqueGaragePayData.joinToString(",") { it?.billNo.toString() }
            val parkTimes = uniqueGaragePayData.joinToString(",") { it?.billStartTime.toString() }

            ApiConfig.URL_HOST +
                    ApiConfig.PAYMENT_URL +
                    "?" +
                    "member_id=${AppUtility.getLoginId(requireContext())}" +
                    "&" +
                    "amount=$amounts" +
                    "&" +
                    "bill_no=$billNos" +
                    "&" +
                    "plate_number=$plateNo" +
                    "&" +
                    "plateId=$parkingId" +
                    "&" +
                    "description=$parkingName $parkingAddress" +
                    "&" +
                    "parkTime=$parkTimes" +
                    "&" +
                    "searchTime=${formattedDateTime}"
        } else {
            selectPayData.forEach { item ->
                Log.d("micCheckKK1", "Order No: ${item.billNo}")
            }

            // Remove duplicates from selectPayData based on billNo
            val uniquePayData = removeDuplicates(selectPayData) { it?.billNo }

            // Concatenate values after removing duplicates
            val amounts = uniquePayData.joinToString(",") { it?.billAmount.toString() }
            val billNos = uniquePayData.joinToString(",") { it?.billNo.toString() }
            val parkTimes = uniquePayData.joinToString(",") { it?.billStartTime.toString() }
            val descriptions = uniquePayData.joinToString(",") { "${it.billRoad} ${it.billCell}" }

            Log.d("micCheckKK2", billNos)

            ApiConfig.URL_HOST +
                    ApiConfig.PAYMENT_URL +
                    "?" +
                    "member_id=${AppUtility.getLoginId(requireContext())}" +
                    "&" +
                    "amount=$amounts" +
                    "&" +
                    "bill_no=$billNos" +
                    "&" +
                    "plate_number=$plateNo" +
                    "&" +
                    "description=$descriptions" +
                    "&" +
                    "parkTime=$parkTimes" +
                    "&" +
                    "searchTime=${formattedDateTime}"
        }
    }


    fun initListener() {

        binding?.selectAll?.setOnCheckedChangeListener { _, isChecked ->
            // Update the selection state
            Log.d("micCheckUU", isChecked.toString())
            isAllSelected = isChecked

            // Add or clear data based on the selection state
            if (isAllSelected) {
                Log.d("micCheckUU", "1")
                selectPayData.clear()
                selectPayData.addAll(data)
                Log.d("micCheckUU4",  (selectPayData == null).toString())
                selectGaragePayData.clear()
                selectGaragePayData.addAll(garageData)
            } else {
                Log.d("micCheckUU", "2")
                selectPayData.clear()
                selectGaragePayData.clear()
            }

            // Select or deselect all items based on the current state
            if (parkingId == "") {
                parkingFeeUnPaidAdapter?.selectAllItems(isAllSelected)
            } else {
                parkingGarageFeeUnPaidAdapter?.selectAllItems(isAllSelected)
            }
        }

    }

    private fun launchUri(uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )

        startActivityForResult(intent, REQUEST_CODE_BROWSER)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_BROWSER) {
            findNavController().navigate(R.id.action_to_main_fragment)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        back = true
    }


}