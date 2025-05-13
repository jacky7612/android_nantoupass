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
import com.google.android.material.tabs.TabLayout
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
    private var currentPage = 0
    private val pageSize = 10
    private var currentList: List<ParkingRoadFeeUnPaidVO> = listOf()
    private var isAllSelected = false
    private val REQUEST_CODE_BROWSER = 1001
    private var _binding: FragmentParkingFeeUnPaidHistoryBinding? = null
    private val binding get() = _binding
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
    private  var canton: ParkingRoadFeeUnPaidResponse? = null
    private  var nantou: ParkingRoadFeeUnPaidResponse? = null
var current = ""
    private var nantouList = mutableListOf<ParkingRoadFeeUnPaidVO>()
    private var cantonList = mutableListOf<ParkingRoadFeeUnPaidVO>()
    var call = false
    var call2 = false
    var call3 = false
    companion object {
        private var selectPayData = mutableListOf<ParkingRoadFeeUnPaidVO>()
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
        initTab()
        init()
        current = "南投市"
        binding?.pageContainer?.visibility = View.GONE
        binding?.forwardActive?.setOnClickListener {
            val totalItems = currentList.size
           currentList.forEach { it.isSelected = false }

            val totalPages = (totalItems + pageSize - 1) / pageSize
            if (currentPage < totalPages - 1) {
                currentPage++
                selectPayData.clear()
                binding?.selectAll?.isChecked = false
                updatePagination()
            }
        }

        binding?.backActive?.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                selectPayData.clear()
                binding?.selectAll?.isChecked = false
                currentList.forEach { it.isSelected = false }
                updatePagination()
            }
        }

        binding?.toolbarInclude?.toolBackImageButton?.setOnClickListener {
            Log.d("micCheckHG", "HG1")
            findNavController().navigate(R.id.action_to_parking_license_plate_fragment)
        }
initListener()

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
        mainViewModel.parkingRoadFeeUnPaidDataCanton.observe(viewLifecycleOwner) { result ->
            if (call3) {
                val convert = ParkingRoadFeeUnPaidResponse(
                    unPaidItems = result?.unPaidItems?.map { vo2 ->
                        ParkingRoadFeeUnPaidVO(
                            billNo = vo2.order_no,
                            billPlateNo = "",
                            billStartTime = vo2.enterTime,
                            billLeaveTime = vo2.exitTime,
                            billAmount = vo2.amount,
                            billRoad = vo2.road,
                            billCell = vo2.cell,
                            billImagePath = vo2.enteredImage,
                            billSearchTime = vo2.searchTime,
                            isSelected = vo2.isSelected,
                            isLocked = vo2.isLocked,
                            lockDueTime = vo2.lockDueTime
                        )
                    } ?: emptyList(), // <-- safe
                    status = "",
                    code = "",
                    responseMessage = result?.responseMessage ?: "" // <-- safe
                )


                Log.d("micCheckLJL", result.toString())

                    canton = convert // Initialize nantou for the first time

                Log.d("micCheckYOU", canton.toString())

                if(nantou?.unPaidItems.isNullOrEmpty() && canton?.unPaidItems.isNullOrEmpty()) {
                    requireActivity().runOnUiThread {
                        binding?.progressBar?.visibility = View.GONE
                        Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                    }
                    if(nantou?.status.equals("false") || canton?.status.equals("false")) {
                        Log.d("micCheckPOP", "1")
                        Toast.makeText(
                            requireActivity(),
                            result.responseMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.d("micCheckPOP", "1")
                        Toast.makeText(
                            requireActivity(),
                            "目前沒有符合的紀錄唷！",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    binding?.tabLayout?.apply {
                        selectTab(getTabAt(0))
                    }
                    Log.d("micCheckMB", "1")
                } else if (nantou?.unPaidItems.isNullOrEmpty() && !canton?.unPaidItems.isNullOrEmpty()) {
                    canton?.let { updateRoadListView(it) }

                    binding?.tabLayout?.apply {
                        selectTab(getTabAt(1))
                    }
                    requireActivity().runOnUiThread {
                        binding?.progressBar?.visibility = View.GONE
                        Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                    }
                    Log.d("micCheckMB", "2")
                } else if (!nantou?.unPaidItems.isNullOrEmpty() && canton?.unPaidItems.isNullOrEmpty()) {
                    nantou?.let { updateRoadListView(it) }
                    binding?.tabLayout?.apply {
                        selectTab(getTabAt(0))
                    }
                    requireActivity().runOnUiThread {
                        binding?.progressBar?.visibility = View.GONE
                        Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                    }
                    Log.d("micCheckMB", "3")
                } else if (!nantou?.unPaidItems.isNullOrEmpty() && !canton?.unPaidItems.isNullOrEmpty()) {
                    nantou?.let { updateRoadListView(it) }
                    binding?.tabLayout?.apply {
                        selectTab(getTabAt(0))
                    }
                    requireActivity().runOnUiThread {
                        binding?.progressBar?.visibility = View.GONE
                        Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                    }
                    Log.d("micCheckMB", "4")
                }
//               if (result?.unPaidItems != null) {
//                   Log.d("micCheckZ3", result.toString())
//                   if (result.unPaidItems.isNotEmpty()) {
//                       updateRoadListView(result)
//                       requireActivity().runOnUiThread {
//                           binding?.progressBar?.visibility = View.GONE
//                           Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
//                       }
//                   } else {
//                       Log.d("micCheckZ4", result.toString())
//                       requireActivity().runOnUiThread {
//                           binding?.progressBar?.visibility = View.GONE
//                           Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
//                       }
//                       Toast.makeText(
//                           requireActivity(),
//                           "目前沒有符合的紀錄唷！",
//                           Toast.LENGTH_SHORT
//                       ).show()
////                    onBackPressed()
//                   }
//               }
            }
                call3 = false
        }
        mainViewModel.parkingRoadFeeUnPaidData.observe(viewLifecycleOwner) { result ->
           if(call) {
               call3 = true

                   nantou = result // Initialize nantou for the first time

               mainViewModel.getParkingRoadFeeUnPaidListCanton(
                   requireContext(),
                   plateNo
               )
//               if (result?.unPaidItems != null) {
//                   Log.d("micCheckZ3", result.toString())
//                   if (result.unPaidItems.isNotEmpty()) {
//                       updateRoadListView(result)
//                       requireActivity().runOnUiThread {
//                           binding?.progressBar?.visibility = View.GONE
//                           Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
//                       }
//                   } else {
//                       Log.d("micCheckZ4", result.toString())
//                       requireActivity().runOnUiThread {
//                           binding?.progressBar?.visibility = View.GONE
//                           Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
//                       }
//                       Toast.makeText(
//                           requireActivity(),
//                           "目前沒有符合的紀錄唷！",
//                           Toast.LENGTH_SHORT
//                       ).show()
////                    onBackPressed()
//                   }
//               }
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
                        Log.d("micCheckPOP", "2")

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
                        "目前沒有符合的紀錄唷！",
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
            plateNo = arguments?.getString("plateNo").toString() ?: ""
        }

        if (parkingId == "") {
            parkingId = arguments?.getString("parkingId").toString() ?: ""
            parkingName  = arguments?.getString("parkingName").toString() ?: ""
            parkingAddress = arguments?.getString("parkingAddress").toString() ?: ""
        }
    }

    fun initTab() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout?.apply {
            addTab(newTab().setText("南投市")) // Nantou City
            addTab(newTab().setText("草屯鎮")) // Caotun Township

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> { // 南投市
                            current = "南投市"
                            selectPayData.clear() // Clear selection list
                            isAllSelected = false
                            binding?.selectAll?.isChecked = false

                            // ✅ Reset checkbox in data
                            nantouList.forEach { it.isSelected = false }

                            // ✅ Show updated list
                            currentList = nantouList
                            currentPage = 0
                            updatePagination()

                            if (nantou?.unPaidItems.isNullOrEmpty()) {
                                val message = nantou?.responseMessage ?: "目前沒有符合的紀錄唷！"
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }
                            Log.d("TabSelection", "Selected: 南投市")
                        }

                        1 -> { // 草屯鎮
                            current = "草屯鎮"
                            selectPayData.clear()
                            isAllSelected = false
                            binding?.selectAll?.isChecked = false

                            // ✅ Reset checkbox in data
                            cantonList.forEach { it.isSelected = false }

                            // ✅ Show updated list
                            currentList = cantonList
                            currentPage = 0
                            updatePagination()
                            if (canton?.unPaidItems.isNullOrEmpty()) {
                                val message = canton?.responseMessage ?: "目前沒有符合的紀錄唷！"
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }
                            Log.d("TabSelection", "Selected: 草屯鎮")
                        }
                    }

            }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Optional: Handle tab unselected state if needed
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Optional: Handle tab reselected state if needed
                }
            })

            // Set default selected tab
            getTabAt(0)?.select()
        }
    }


    private fun initData() {
        Log.d("micCheckHG", (parkingId.equals("")).toString())
        if (parkingId == "") {
            binding?.tabLayout?.visibility = View.VISIBLE
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
            binding?.tabLayout?.visibility = View.GONE
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
        if (parkingId == "") {
            // Use current page's data from currentList instead of `data`
            val totalItems = currentList.size
            val startIndex = currentPage * pageSize
            val endIndex = minOf(startIndex + pageSize, totalItems)
            val currentPageList = currentList.subList(startIndex, endIndex)

            if (position < currentPageList.size) {
                currentPageList[position].isSelected = isChecked
                if (isChecked) {
                    if (!selectPayData.any { it.billNo == vo.billNo }) {
                        selectPayData.add(vo)
                    }
                } else {
                    selectPayData.removeAll { it.billNo == vo.billNo }
                }
                payData = vo
                parkingFeeUnPaidAdapter.updateDataSource(currentPageList)
            }
        }
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

    private fun updatePagination() {
        val totalItems = currentList.size
        val totalPages = (totalItems + pageSize - 1) / pageSize

        val startIndex = currentPage * pageSize
        val endIndex = minOf(startIndex + pageSize, totalItems)
        val pageItems = currentList.subList(startIndex, endIndex)

        binding?.pageNumber?.text = "${startIndex + 1}-$endIndex 筆(共 $totalItems 筆)"
        parkingFeeUnPaidAdapter.updateDataSource(pageItems)

        // Handle pagination button visibility
        binding?.backActive?.visibility = if (currentPage > 0) View.VISIBLE else View.GONE
        binding?.backInactive?.visibility = if (currentPage > 0) View.GONE else View.VISIBLE

        binding?.forwardActive?.visibility = if (currentPage < totalPages - 1) View.VISIBLE else View.GONE
        binding?.forwardInactive?.visibility = if (currentPage < totalPages - 1) View.GONE else View.VISIBLE
//        binding?.selectAll?.isChecked = false
//        isAllSelected = false
    }

    private fun updateRoadListView(result: ParkingRoadFeeUnPaidResponse) {
        binding?.pageContainer?.visibility = View.VISIBLE

        if (!::parkingFeeUnPaidAdapter.isInitialized) {
            initRecyclerView()
        }
        if(result.status.equals("false")) {
            Toast.makeText(requireActivity(), result.responseMessage, Toast.LENGTH_SHORT).show()
        } else {
            if (result.unPaidItems.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "目前沒有符合的紀錄唷！", Toast.LENGTH_SHORT)
                    .show()
                parkingFeeUnPaidAdapter.updateDataSource(emptyList())
                return
            }
        }

        if (current == "南投市") {
            nantouList = result.unPaidItems.toMutableList()
            currentList = nantouList
        } else {
            cantonList = result.unPaidItems.toMutableList()
            currentList = cantonList
        }
        currentPage = 0
        updatePagination()
    }


//    private fun updateRoadListView(result: ParkingRoadFeeUnPaidResponse) {
//        if (!::parkingFeeUnPaidAdapter.isInitialized) {
//            initRecyclerView()
//        }
//        if(result.unPaidItems.isNullOrEmpty()) {
//            Toast.makeText(
//                requireActivity(),
//                "目前沒有符合的紀錄唷！",
//                Toast.LENGTH_SHORT
//            ).show()
//            parkingFeeUnPaidAdapter.updateDataSource(emptyList())
//            return
//        } else {
//            data = result.unPaidItems.toMutableList()
//            parkingFeeUnPaidAdapter.updateDataSource(data)
//        }
//    }

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
var url = ""
            if(current.equals("南投市")) {
                url = ApiConfig.URL_HOST
            } else {
                url = ApiConfig.URL_HOST_CANTON
            }
            url +
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
            var url = ""
            if(current.equals("南投市")) {
                url = ApiConfig.URL_HOST
            } else {
                url = ApiConfig.URL_HOST_CANTON
            }
            url +
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
            isAllSelected = isChecked
            selectPayData.clear()
            selectGaragePayData.clear()

            if (parkingId == "") {
                // ROAD MODE
                val targetList = if (current == "南投市") nantouList else cantonList
                val totalItems = targetList.size
                val startIndex = currentPage * pageSize
                val endIndex = minOf(startIndex + pageSize, totalItems)
                val currentPageList = targetList.subList(startIndex, endIndex)

                currentPageList.forEach { it.isSelected = isChecked }
                if (isChecked) {
                    selectPayData.addAll(currentPageList)
                } else {
                    selectPayData.removeAll { it in currentPageList }
                }
                parkingFeeUnPaidAdapter.updateDataSource(currentPageList)
            } else {
                // GARAGE MODE (assumes no pagination here, but you can adapt similarly if needed)
                garageData.forEach { it.isSelected = isChecked }
                if (isChecked) {
                    selectGaragePayData.addAll(garageData)
                }
                parkingGarageFeeUnPaidAdapter.updateDataSource(garageData)
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