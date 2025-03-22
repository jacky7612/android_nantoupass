package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentParkingLicensePlateHistoryBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.ParkingGarageVO
import com.jotangi.nantouparking.model.ParkingRoadFeeUnPaidVO
import com.jotangi.nantouparking.model.PlateNumberVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.unPaidHistory.ParkingFeeUnPaidHistoryFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.jotangi.payStation.Model.ApiModel.ApiEntry
import org.json.JSONException
import org.json.JSONObject


class ParkingLicensePlateHistoryFragment :
    BaseFragment(),
    PlateNumberClickListener,
    ParkingGarageClickListener {
    private var _binding: FragmentParkingLicensePlateHistoryBinding? = null
    private val binding get() = _binding
    private var data = mutableListOf<PlateNumberVO>()
    private var parkingGarageData = mutableListOf<ParkingGarageVO>()
    private lateinit var plateNumberAdapter: PlateNumberAdapter
    private lateinit var parkingGarageAdapter: ParkingGarageAdapter
    private var mPlateNo: String = ""
    private var mPId: String = ""
    private var mPName: String = ""
    private var mPAddress: String = ""
    var isEditing = false
    private var itemClickType = ""
    private val PARKING_TYPE_ROAD = "1"
    private val PARKING_TYPE_GARAGE = "2"
    private val PARKING_TYPE_GARAGE_LIST = "3"
    var combineRoadUnPaidDataList:MutableList<ParkingRoadFeeUnPaidVO> = mutableListOf()
    var combinePlateNumberDataList:MutableList<PlateNumberVO> = mutableListOf()
    companion object {
        var parkingCurPage = "1"
        var parkingName = ""
    }
var call = false
    var call2 = false
    var call3 = false
    var call4 = false
    var call5 = false
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkingLicensePlateHistoryBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mainViewModel.clearParkingGarageList()
        mainViewModel.clearPlateNoList()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.progressBar?.bringToFront()

        init()
        initEditText2()
        initEditText1()
    }

    fun initEditText1() {
        binding?.apply {
            plateTextEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Do nothing
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val upperCaseText = it.toString().uppercase()
                        if (it.toString() != upperCaseText) {
                            plateTextEditText.setText(upperCaseText)
                            plateTextEditText.setSelection(upperCaseText.length) // 將光標移到最後
                        }
                    }
                }
            })
            plateNumberEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Do nothing
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val upperCaseText = it.toString().uppercase()
                        if (it.toString() != upperCaseText) {
                            plateNumberEditText.setText(upperCaseText)
                            plateNumberEditText.setSelection(upperCaseText.length) // 將光標移到最後
                        }
                    }
                }
            })
        }
    }

    private fun init() {
        setupLicensePlateHistoryTitle()
        binding?.toolbarInclude?.toolBackImageButton?.setOnClickListener {
            findNavController().navigate(R.id.action_parking_license_plate_fragment_to_main_fragment)
        }
        initBackListener()
        initObserver()
        initData()
        initView()
        initAction()
        updateTabView()
        updateContentView()
    }

    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            mPlateNo = ""
            when (parkingCurPage) {
                PARKING_TYPE_GARAGE_LIST -> {
                    parkingCurPage = PARKING_TYPE_GARAGE
                    updateContentView()
                    call3 = true
                    mainViewModel.getPlateNumber(
                        requireContext(),
                        AppUtility.getLoginId(requireContext())!!,
                        AppUtility.getLoginPassword(requireContext())!!,
                        parkingCurPage
                    )
                }

                PARKING_TYPE_GARAGE -> {
                    parkingCurPage = PARKING_TYPE_ROAD
                    updateTabView()
                    updateContentView()
                    call3 = true
                    mainViewModel.getPlateNumber(
                        requireContext(),
                        AppUtility.getLoginId(requireContext())!!,
                        AppUtility.getLoginPassword(requireContext())!!,
                        parkingCurPage
                    )
                }

                PARKING_TYPE_ROAD -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun initObserver() {

        mainViewModel.plateNumberDataCanton.observe(viewLifecycleOwner) { result ->
            Log.d("micCheckJJ", result.toString())
            if(call4) {
                if(result == null) {
                    combinePlateNumberDataList.addAll(emptyList())

                } else {
                    combinePlateNumberDataList.addAll(result)
                }
                    Log.d("micCheckJJ2", combinePlateNumberDataList.toString())

                    combinePlateNumberDataList = combinePlateNumberDataList.distinctBy { it.plateNo }.toMutableList()
                    Log.d("micCheckMB2", combinePlateNumberDataList.toString())
                    updateListView(combinePlateNumberDataList)

                    if (mPlateNo.isNotEmpty()) {
                        if (call) {
                            getPlateUnPaidList()
                        }
                    }

            }
            call4 = false
        }
        mainViewModel.plateNumberData.observe(viewLifecycleOwner) { result ->
            if(call3) {
//                if (result != null) {
                    call4 = true
                    combinePlateNumberDataList.clear()
                if(!result.isNullOrEmpty()) {
                    combinePlateNumberDataList.addAll(result)
                    updateListView(combinePlateNumberDataList)
                    if (mPlateNo.isNotEmpty()) {
                        if (call) {
                            getPlateUnPaidList()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "目前沒有查詢紀錄唷！",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.d("micCheckMB1", AppUtility.getLoginId(requireContext())!!.toString())
                Log.d("micCheckMB2", AppUtility.getLoginPassword(requireContext())!!.toString())

//                mainViewModel.getPlateNumberCanton(
//                        requireContext(),
//                        AppUtility.getLoginId(requireContext())!!,
//                        AppUtility.getLoginPassword(requireContext())!!,
//                        parkingCurPage
//                    )
//                    updateListView(result)
//
//                    if (mPlateNo.isNotEmpty()) {
//                        if (call) {
//                            getPlateUnPaidList()
//                        }
//                    }
//                }
            }
            call3 = false
        }

        mainViewModel.addPlateNoData.observe(viewLifecycleOwner) { result ->
            Log.d("micCheckZ", "2")
            mainViewModel.clearPlateNoList()
            call3 = true
            call = true
            mainViewModel.getPlateNumber(
                requireContext(),
                AppUtility.getLoginId(requireContext())!!,
                AppUtility.getLoginPassword(requireContext())!!,
                parkingCurPage
            )
        }

        mainViewModel.parkingRoadFeeUnPaidData.observe(viewLifecycleOwner) { result ->

            if(call) {
//                if (result != null && mainViewModel.hasData) {
//                    if (result.unPaidItems.isNullOrEmpty()) {
//                        Log.d("micCheckZ1", "1")
//                        mPlateNo = ""
//                        Toast.makeText(
//                            requireActivity(),
//                            "目前沒有符合的紀錄唷！",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        requireActivity().runOnUiThread {
//                            binding?.progressBar?.visibility = View.GONE
//                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
//                        }
//                        binding?.plateTextEditText?.setText("")
//                        binding?.plateNumberEditText?.setText("")
                combineRoadUnPaidDataList.clear()
                if(!result.unPaidItems.isNullOrEmpty()) {
                    combineRoadUnPaidDataList.addAll(result.unPaidItems)
                }
                Log.d("micCheckMB3", combineRoadUnPaidDataList.toString())
                if (!mPlateNo.isNullOrEmpty()) {
                call5 = true
                mainViewModel.getParkingRoadFeeUnPaidListCanton1(
                    requireContext(),
                    mPlateNo
                )
            }
//                    } else if (result.unPaidItems.isNotEmpty()) {
//                        requireActivity().runOnUiThread {
//                            binding?.progressBar?.visibility = View.GONE
//                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
//                        }
//                        Log.d("micCheckZ2", result.toString())
//                        binding?.plateTextEditText?.setText("")
//                        binding?.plateNumberEditText?.setText("")
//                        Log.d("micCheckAAZ7", "7")
//                        Log.d("micCheckHG", "1")
//                        navigateToUnPaidHistory()
//                    }
//                }
            }
            isEditing = false
            call = false
        }

        mainViewModel.parkingRoadFeeUnPaidDataCanton1.observe(viewLifecycleOwner) { result ->

            if(call5) {
//                if (result != null && mainViewModel.hasData) {
                if(!result.unPaidItems.isNullOrEmpty()) {
                    combineRoadUnPaidDataList.addAll(result.unPaidItems)
                }
                Log.d("micCheckMB4", combineRoadUnPaidDataList.toString())

                if (combineRoadUnPaidDataList.isNullOrEmpty()) {
                        Log.d("micCheckZ1", "1")
                        mPlateNo = ""
                        Toast.makeText(
                            requireActivity(),
                            "目前沒有符合的紀錄唷！",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().runOnUiThread {
                            binding?.progressBar?.visibility = View.GONE
                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                        }
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                    } else if (combineRoadUnPaidDataList.isNotEmpty()) {
                        requireActivity().runOnUiThread {
                            binding?.progressBar?.visibility = View.GONE
                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                        }
                        Log.d("micCheckZ2", result.toString())
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                        Log.d("micCheckAAZ7", "7")
                        Log.d("micCheckHG", "1")
                        navigateToUnPaidHistory()
                    }
//                }
            }
            isEditing = false
            call5 = false
        }

        mainViewModel.parkingGarageFeeUnPaidData.observe(viewLifecycleOwner) { result ->
            Log.d("micCheckZ", "4")
            if(call) {
                if (result != null && mainViewModel.hasData) {
                    if (result.unPaidItems.isNullOrEmpty()) {
                        mPlateNo = ""
                        Toast.makeText(
                            requireActivity(),
                            "目前沒有符合的紀錄唷！",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().runOnUiThread {
                            binding?.progressBar?.visibility = View.GONE
                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                        }
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                    } else if (result.unPaidItems.isNotEmpty()) {
                        requireActivity().runOnUiThread {
                            binding?.progressBar?.visibility = View.GONE
                            Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                        }
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                        Log.d("micCheckAAZ5", "5")
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                        navigateToUnPaidHistory()
                        Log.d("micCheckHG", "2")

                    }
                }
            }
            isEditing = false
            call = false
        }

        mainViewModel.deletePlateNumberData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                AppUtility.showPopDialog(
                    requireContext(),
                    result.code,
//                    result.responseMessage
                    "刪除成功！"
                )

                mainViewModel.clearDeletePlateData()
                mPlateNo = ""
                initData()
            }
        }

        mainViewModel.parkingGarageData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                Log.d("micCheckJH", result.toString())
                updateParkingGarageListView(result)
            }
        }


        mainViewModel.parkingFeePaidData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.isNotEmpty()) {

                } else {

                }
            }
        }


        mainViewModel.navigateToPaidHistory.observe(viewLifecycleOwner) { shouldNavigate ->
            Log.d("micCheckAAZ3", shouldNavigate.toString())
            if (shouldNavigate == null) {
                // Handle the case where the value is null
                Log.d("NavigationDebug", "Value is null, resetting or ignoring navigation")
                return@observe
            }
            if (call2) {
                Log.d("micCheckAAZ4", "AD1")
                if (shouldNavigate == true ) {
                    Log.d("micCheckAAZ4", "AD2")
                    requireActivity().runOnUiThread {
                        if (findNavController().currentDestination?.id == R.id.parking_license_plate_fragment) {
                            Log.d("NavigationDebug", "1")
                            requireActivity().runOnUiThread {
                                binding?.progressBar?.visibility = View.GONE
                                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                            }
                            navigateToPaidHistory()
                        } else {
                            Log.d("NavigationDebug", "2")
                        }
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "目前沒有符合的紀錄唷！",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                    call2 = false

            }
        }

    }

    private fun initData() {
        if (AppUtility.getLoginStatus(requireContext())) {
//            parkingCurPage = PARKING_TYPE_ROAD
            call3 = true
            mainViewModel.getPlateNumber(
                requireContext(),
                AppUtility.getLoginId(requireContext())!!,
                AppUtility.getLoginPassword(requireContext())!!,
                parkingCurPage
            )
        }

        mainViewModel.getParkingGarage(requireContext())
    }

    private fun initView() {
        initEditInputType()
        initPlateRecyclerView()
        initParkingGarageRecyclerView()
    }

    private fun initEditInputType() {
        binding?.apply {
            plateTextEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
            plateNumberEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        }
    }

    private fun initPlateRecyclerView() {
        binding?.plateNumberRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            plateNumberAdapter = PlateNumberAdapter(
                data,
                requireContext(),
                this@ParkingLicensePlateHistoryFragment
            )
            this.adapter = plateNumberAdapter
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initParkingGarageRecyclerView() {
        binding?.parkingGarageRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            parkingGarageAdapter = ParkingGarageAdapter(
                parkingGarageData,
                requireContext(),
                this@ParkingLicensePlateHistoryFragment
            )
            this.adapter = parkingGarageAdapter
        }
        binding?.root?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val rect = Rect()
                binding?.parkingGarageRecyclerView?.getGlobalVisibleRect(rect)
                if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    // Click is outside the RecyclerView bounds
                    binding?.parkingGarageConstraintLayout?.visibility = View.GONE
                    true // Return true to indicate the touch event is handled
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun initAction() {
        binding?.apply {
            searchPlateNumberButton.setOnClickListener {
                ParkingFeeUnPaidHistoryFragment.back = false
                addNewPlateNo()
            }

            plateHistoryEditTextView.setOnClickListener {
                when (isEditing) {
                    true -> {
                        isEditing = false
                        plateHistoryEditTextView.text = "編輯"
                    }

                    false -> {
                        isEditing = true
                        plateHistoryEditTextView.text = "完成"
                    }
                }

                plateNumberAdapter.updateEditStatus(isEditing)
                plateNumberAdapter.notifyDataSetChanged()
            }

            parkingRoadTextView.setOnClickListener {
                parkingCurPage = PARKING_TYPE_ROAD
                updateTabView()
                updateContentView()

                if (AppUtility.getLoginStatus(requireContext())) {
                    call3 = true
                    mainViewModel.getPlateNumber(
                        requireContext(),
                        AppUtility.getLoginId(requireContext())!!,
                        AppUtility.getLoginPassword(requireContext())!!,
                        parkingCurPage
                    )
                }
            }

            parkingGarageTextView.setOnClickListener {
                parkingCurPage = PARKING_TYPE_GARAGE_LIST
                updateTabView()
                updateContentView()
                parkingCurPage = PARKING_TYPE_GARAGE

                if (AppUtility.getLoginStatus(requireContext())) {
                    call3 = true
                    mainViewModel.getPlateNumber(
                        requireContext(),
                        AppUtility.getLoginId(requireContext())!!,
                        AppUtility.getLoginPassword(requireContext())!!,
                        parkingCurPage
                    )
                }
            }
        }
    }

    private fun addNewPlateNo() {
        if (binding?.plateTextEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "此為必填！",
                binding?.plateTextEditText
            )

            return
        }

        if (binding?.plateNumberEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "此為必填！",
                binding?.plateNumberEditText
            )

            return
        }

        mPlateNo =
            "${binding?.plateTextEditText?.text.toString()}-${binding?.plateNumberEditText?.text.toString()}"

        if (AppUtility.getLoginStatus(requireContext())) {
//            if (parkingCurPage.equals(PARKING_TYPE_GARAGE)) {
//                parkingCurPage = PARKING_TYPE_GARAGE_LIST
//                updateContentView()
//            } else {
                mainViewModel.addPlateNo(
                    requireContext(),
                    AppUtility.getLoginId(requireContext())!!,
                    AppUtility.getLoginPassword(requireContext())!!,
                    mPlateNo,
                    parkingCurPage
                )
//            }

        } else {
            getPlateUnPaidList()
//            when (parkingCurPage) {
//                PARKING_TYPE_ROAD -> {
//                    navigateToUnPaidHistory()
//                }
//
//                PARKING_TYPE_GARAGE -> {
//                    navigateToUnPaidHistory()
//                }

//                PARKING_TYPE_GARAGE_LIST -> {
//                    parkingCurPage = PARKING_TYPE_GARAGE_LIST
//                    updateContentView()
//                }
//            }
        }
    }

    override fun onPlateNumberItemClick(
        type: String,
        vo: PlateNumberVO
    ) {
        ParkingFeeUnPaidHistoryFragment.back = false
        itemClickType = type
        mPlateNo = vo.plateNo

        when (isEditing) {
            true -> {
                showCustomDialog(vo.plateNo)
            }

            false -> {
                if (parkingCurPage.equals(PARKING_TYPE_ROAD)) {
                    when (itemClickType) {
                        "record" -> {
                            Log.d("micCheckC1", mPId)

//                            navigateToPaidHistory()
                            Log.d("micCheckJH", mPlateNo)
                            call2 = true
                        mainViewModel.paid(mPlateNo)
                            requireActivity().runOnUiThread {
                                binding?.progressBar?.visibility = View.VISIBLE
                                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                            }
                        }

                        else -> {
                            getPlateUnPaidList()
                        }
                    }
                } else {
                    when (itemClickType) {
                        "record" -> {
                            Log.d("micCheckC2", mPId)
//                            navigateToPaidHistory()
                            call2 = true
                            mainViewModel.paid(mPlateNo)
                            requireActivity().runOnUiThread {
                                binding?.progressBar?.visibility = View.VISIBLE
                                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
                            }
                        }

                        else -> {
                            getPlateUnPaidList()
                        }
                    }
                }
            }
        }
    }

    private fun getPlateUnPaidList() {
        if (parkingCurPage.equals(PARKING_TYPE_ROAD)) {
            call = true
            requireActivity().runOnUiThread {
                binding?.progressBar?.visibility = View.VISIBLE
                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
            }

            Log.d("micCheckZ6", mPlateNo)
            mainViewModel.getParkingRoadFeeUnPaidList(
                requireContext(),
                mPlateNo
            )
            Log.d("micCheckSS", "1")
        } else {
            requireActivity().runOnUiThread {
                binding?.progressBar?.visibility = View.VISIBLE
                Log.d("ProgressBarDebug", "ProgressBar set to VISIBLE")
            }
            Log.d("micCheckAAZ10", "10")
            call = true
            mainViewModel.getParkingGarageFeeUnPaidList(
                requireContext(),
                mPlateNo,
                mPId
            )
            Log.d("micCheckSS", "2")
        }
    }

    override fun onParkingGarageItemClick(garageData: ParkingGarageVO) {
        parkingName = garageData.parkingGarageName
        mPId = garageData.parkingGarageId
        mPName = garageData.parkingGarageName
        mPAddress = garageData.parkingGarageAddress
        parkingCurPage = PARKING_TYPE_GARAGE

        binding?.parkingGarageTextView?.text = mPName

//        if (AppUtility.getLoginStatus(requireContext())) {
//            mainViewModel.addPlateNo(
//                requireContext(),
//                AppUtility.getLoginId(requireContext())!!,
//                AppUtility.getLoginPassword(requireContext())!!,
//                mPlateNo,
//                parkingCurPage
//            )
//        }

        updateContentView()
    }

    private fun navigateToPaidHistory() {
        val bundle = bundleOf(
            "plateNo" to mPlateNo,
            "parkingId" to mPId,
        )
call = false
        mPlateNo = ""
        mPId = ""
        itemClickType = ""

        findNavController().navigate(
            R.id.action_to_parking_history_plate_paid,
            bundle
        )
    }

    private fun navigateToUnPaidHistory() {
        Log.d("micCheckAAZ6", "6")
        if(!ParkingFeeUnPaidHistoryFragment.back) {
            if (parkingCurPage.equals(PARKING_TYPE_GARAGE) && mPId.isNullOrEmpty()) {
                AppUtility.showPopDialog(
                    requireContext(),
                    null,
                    "請先選擇停車場！"
                )
            } else {
                if (parkingCurPage.equals(PARKING_TYPE_ROAD)) {
                    Log.d("micCheckBC", mPId)
                    mPId = ""
                }
                Log.d("micCheckKK", mPId.toString())
                val bundle = bundleOf(
                    "plateNo" to mPlateNo,
                    "parkingId" to mPId,
                    "parkingName" to mPName,
                    "parkingAddress" to mPAddress
                )
//            mPlateNo = ""
//            mPId = ""
//            mPName = ""
//            mPAddress = ""

                findNavController().navigate(
                    R.id.action_to_parking_history_unpaid,
                    bundle
                )
            }
        } else {
//            ParkingFeeUnPaidHistoryFragment.back = false
        }
    }

    private fun updateListView(result: List<PlateNumberVO>) {
        data = result.toMutableList()
        plateNumberAdapter.updateDataSource(data)
    }

    private fun updateParkingGarageListView(result: List<ParkingGarageVO>) {
        parkingGarageData = result.toMutableList()
        parkingGarageAdapter.updateDataSource(parkingGarageData)
    }

    private fun updateTabView() {
        binding?.apply {
            if (parkingCurPage.equals(PARKING_TYPE_ROAD)) {
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
    @SuppressLint("ClickableViewAccessibility")
    fun initEditText2(){
        binding?.apply {
            val inputFilter = InputFilter { source, _, _, _, _, _ ->
                if (source.matches(Regex("[\\u4E00-\\u9FA5a-zA-Z0-9]+"))) {
                    source // Allow Chinese, both cases, and digits
                } else {
                    "" // Block other characters
                }
            }

// Remove maxLength restriction and set the input filter
            plateNumberEditText.filters = arrayOf(inputFilter)
            plateNumberEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if (s.isEmpty()) {
//                            plateNumberEditText.error =
//                                "此欄位不可為空" // This field cannot be empty
                        }
                    }
                }
            })


// Input filter to allow Chinese characters, uppercase letters, and digits only
            val inputFilter2 = InputFilter { source, _, _, _, _, _ ->
                if (source.matches(Regex("[\\u4E00-\\u9FA5a-zA-Z0-9]+"))) {
                    source // Allow Chinese, both cases, and digits
                } else {
                    "" // Block other characters
                }
            }

// Set the input filter
            if (plateTextEditText != null) {
                plateTextEditText .filters = arrayOf(inputFilter2)
            }

// Add a text watcher to ensure the field is not empty
            if (plateTextEditText  != null) {
                plateTextEditText .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        s?.let {
                            if (s.isNullOrEmpty()) {
//                                plateTextEditText .error = "此欄位不可為空"
                            }
                        }
                    }
                })
            }
        }
        }

    private fun updateContentView() {
        binding?.apply {
            when (parkingCurPage) {
                PARKING_TYPE_ROAD, PARKING_TYPE_GARAGE -> {
                    parkingRoadConstraintLayout.visibility = View.VISIBLE
                    parkingGarageConstraintLayout.visibility = View.GONE
                }

                PARKING_TYPE_GARAGE_LIST -> {
                    parkingRoadConstraintLayout.visibility = View.GONE
                    parkingGarageConstraintLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showPrivateDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = "提醒！"

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.plateTextEditText,
                binding?.plateNumberEditText -> {
                    return@setPositiveButton
                }

                null -> {
                    initData()
                }
            }
        }

        alert.show()
    }

    private fun showCustomDialog(plateNo: String) {
        val alert = AlertDialog.Builder(requireContext())
        val title = "提醒！"

        alert.setTitle(title)
        alert.setMessage("將刪除此車牌！是否確定?!")
        alert.setPositiveButton("確定") { _, _ ->
            mainViewModel.deleteParkingFeeUnPaidPlate(
                requireContext(),
                AppUtility.getLoginId(requireContext())!!,
                AppUtility.getLoginPassword(requireContext())!!,
                plateNo,
                parkingCurPage
            )
        }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }

        alert.show()
    }

    override fun onResume() {
        super.onResume()
        call = false
call3 = true
    }

}