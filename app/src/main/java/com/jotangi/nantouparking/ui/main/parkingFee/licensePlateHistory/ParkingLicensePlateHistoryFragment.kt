package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Rect
import android.os.Bundle
import android.text.InputType
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
    companion object {
        var parkingCurPage = "1"
    }
var call = false
    var call2 = false
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

        init()
    }

    private fun init() {
        setupLicensePlateHistoryTitle()
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
        mainViewModel.plateNumberData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                Log.d("micCheckZ", result.toString())
                updateListView(result)

                if (mPlateNo.isNotEmpty()) {
                    getPlateUnPaidList()
                }
            }
        }

        mainViewModel.addPlateNoData.observe(viewLifecycleOwner) { result ->
            Log.d("micCheckZ", "2")
            mainViewModel.clearPlateNoList()
            mainViewModel.getPlateNumber(
                requireContext(),
                AppUtility.getLoginId(requireContext())!!,
                AppUtility.getLoginPassword(requireContext())!!,
                parkingCurPage
            )
        }

        mainViewModel.parkingRoadFeeUnPaidData.observe(viewLifecycleOwner) { result ->
            Log.d("micCheckZ", "3")
            if(call) {
                if (result != null && mainViewModel.hasData) {
                    if (result.unPaidItems.isNullOrEmpty()) {
                        mPlateNo = ""
                        Toast.makeText(
                            requireActivity(),
                            "目前沒有符合的紀錄唷！",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (result.unPaidItems.isNotEmpty()) {
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                        navigateToUnPaidHistory()
                    }
                }
            }
            isEditing = false
            call = false
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
                    } else if (result.unPaidItems.isNotEmpty()) {
                        binding?.plateTextEditText?.setText("")
                        binding?.plateNumberEditText?.setText("")
                        navigateToUnPaidHistory()
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
            Log.d("NavigationDebug", shouldNavigate.toString())
            if (call2) {
                if (shouldNavigate == true) {

                    requireActivity().runOnUiThread {
                        if (findNavController().currentDestination?.id == R.id.parking_license_plate_fragment) {
                            Log.d("NavigationDebug", "1")
                            navigateToPaidHistory()
                        } else {
                            Log.d("NavigationDebug", "2")
                        }
                    }
                }
            }
        }
        call2 = false
    }

    private fun initData() {
        if (AppUtility.getLoginStatus(requireContext())) {
//            parkingCurPage = PARKING_TYPE_ROAD
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
            mainViewModel.getParkingRoadFeeUnPaidList(
                requireContext(),
                mPlateNo
            )
            Log.d("micCheckSS", "1")
        } else {
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
        mPId = garageData.parkingGarageId
        mPName = garageData.parkingGarageName
        mPAddress = garageData.parkingGarageAddress
        parkingCurPage = PARKING_TYPE_GARAGE

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

}