package com.jotangi.nantoupass.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jotangi.nantoupass.MainActivity
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.utility.AppUtility

abstract class BaseWithBottomBarFragment : BaseFragment() {
    abstract fun getToolBarFeet(): ToolbarFeetBinding?
    val msgStoreValue ="\n\n儲值功能即將上線\n\n敬請期待!\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        try {
            mActivity = (activity as MainActivity)
        } catch (e: ClassCastException) {

        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }
    fun initEvent() {
        // 商圈
        getToolBarFeet()?.toolbarBtnMap?.setOnClickListener {
            toMarketFragment(R.id.action_main_fragment_to_marketFragment)
        }

        // 首頁
        getToolBarFeet()?.toolbarBtnCharge?.setOnClickListener {
            toHomeFagment(R.id.main_fragment)
        }

        // 工程
        getToolBarFeet()?.toolbarBtnStoreValue?.setOnClickListener {
//            AppUtility.showPopDialog(
//                requireContext(),
//                "",
//                msgStoreValue
//            )
//            toChargeFagment(R.id.chargeEntryFragment)
            toProjectFagment(R.id.action_main_fragment_to_mapProjectFragment)
        }
    }
    fun initEvent4apply() {
        // 商圈
        getToolBarFeet()?.toolbarBtnMap?.setOnClickListener {
            toMarketFragment(R.id.action_applicationFragment_to_marketFragment)
        }

        // 首頁
        getToolBarFeet()?.toolbarBtnCharge?.setOnClickListener {
            toHomeFagment(R.id.main_fragment)
        }

        // 工程
        getToolBarFeet()?.toolbarBtnStoreValue?.setOnClickListener {
            toProjectFagment(R.id.action_applicationFragment_to_mapProjectFragment)
        }
    }

    private var ToolBarBtMap : ImageButton? =null
    private var ToolBarLayoutMap : LinearLayout? =null
    private var ToolBarBtCharge : ImageButton? =null
    private var ToolBarLayoutCharge : LinearLayout? =null
    private var ToolBarBtStorevalue : ImageButton? =null
    private var ToolBarLayoutStorevalue : LinearLayout? =null
    fun initEvent(view: View) {

        // 商圈
        if (ToolBarLayoutMap == null) ToolBarLayoutMap =view.findViewById(R.id.layout_feet_map)
        ToolBarLayoutMap!!.setOnClickListener {
            chargeViewModel.clearPowerON()
            chargeViewModel.clearPowerOFF()
            chargeViewModel.clearCheckData()
            chargeViewModel.clearQRcode()
            toMarketFragment(R.id.mapChargeParkingFragment)
        }
        if (ToolBarBtMap == null) ToolBarBtMap =view.findViewById(R.id.toolbar_btn_map)
        ToolBarBtMap!!.setOnClickListener {
            toMarketFragment(R.id.mapChargeParkingFragment)
        }

        // 首頁
        if (ToolBarLayoutCharge == null) ToolBarLayoutCharge =view.findViewById(R.id.layout_feet_charge)
        ToolBarLayoutCharge!!.setOnClickListener {
            toHomeFagment(R.id.main_fragment)
        }
        if (ToolBarBtCharge == null) ToolBarBtCharge =view.findViewById(R.id.toolbar_btn_charge)
        ToolBarBtCharge!!.setOnClickListener {
            toHomeFagment(R.id.main_fragment)
        }

        // 工程
        if (ToolBarLayoutStorevalue == null) ToolBarLayoutStorevalue =view.findViewById(R.id.layout_feet_store_value)
        ToolBarLayoutStorevalue!!.setOnClickListener {
//            AppUtility.showPopDialog(
//                requireContext(),
//                "",
//                msgStoreValue
//            )
            toProjectFagment(R.id.action_main_fragment_to_mapProjectFragment)
        }
        if (ToolBarBtStorevalue == null) ToolBarBtStorevalue =view.findViewById(R.id.toolbar_btn_store_value)
        ToolBarBtStorevalue!!.setOnClickListener {
//            AppUtility.showPopDialog(
//                requireContext(),
//                "",
//                msgStoreValue
//            )
            toProjectFagment(R.id.action_main_fragment_to_mapProjectFragment)
        }

    }
    private fun toChargeFagment(id: Int)
    {
        val currentFragmentId = findNavController().currentDestination?.id
        if (currentFragmentId != id) {
            var action_id: Int =-1
            when (currentFragmentId) {
                R.id.mapChargeParkingFragment -> action_id =R.id.action_mapChargeParkingFragment_to_chargeEntryFragment
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_main_fragment
//                R.id.parking_info_fragment -> action_id =R.id.action_parking_info_fragment_to_main_fragment
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_mapChargeParkingFragment
//                R.id.main_fragment -> action_id =R.id.action_mapChargeParkingFragment_to_chargeEntryFragment
//                R.id.chargeEntryFragment -> action_id =R.id.action_chargeEntryFragment_to_mapChargeParkingFragment
//                R.id.chargeScanFragment -> action_id =R.id.action_chargeScanFragment_to_main_fragment
//                R.id.parking_history_paid_fragment -> action_id =R.id.action_parking_history_paid_fragment_to_main_fragment
//                R.id.chargingFragment -> action_id =R.id.action_chargingFragment_to_mapChargeParkingFragment
            }
            if (action_id != -1) findNavController().navigate(action_id)
        }
    }
    private fun toMarketFragment(id: Int)
    {
        val currentFragmentId = findNavController().currentDestination?.id
        if (currentFragmentId != id) {
            var action_id: Int =-1
            when (currentFragmentId) {
                R.id.marketFragment-> return
//                R.id.parking_info_fragment -> action_id =R.id.action_parking_info_fragment_to_mapChargeParkingFragment
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_mapChargeParkingFragment
//                R.id.main_fragment -> action_id =R.id.action_main_fragment_to_mapChargeParkingFragment
                R.id.chargeEntryFragment -> action_id =R.id.action_chargeEntryFragment_to_mapChargeParkingFragment
//                R.id.parking_history_paid_fragment -> action_id =R.id.action_parking_history_paid_fragment_to_mapChargeParkingFragment
//                R.id.chargeScanFragment -> action_id =R.id.action_chargeScanFragment_to_mapChargeParkingFragment
                R.id.chargingFragment -> action_id =R.id.action_chargingFragment_to_mapChargeParkingFragment
            }
            if (action_id != -1)
                findNavController().navigate(action_id)
            else
                findNavController().navigate(id)
        } else {
//            findNavController().navigate(R.id.action_mapChargeParkingFragment_self)
        }
    }
    private fun toHomeFagment(id: Int)
    {
        val currentFragmentId = findNavController().currentDestination?.id
        if (currentFragmentId != id) {
            var action_id: Int =-1
            when (currentFragmentId) {
                R.id.marketFragment -> action_id =R.id.action_marketFragment_to_main_fragment
                R.id.mapChargeParkingFragment -> action_id =R.id.action_mapChargeParkingFragment_to_main_fragment
                R.id.chargingFragment -> action_id =R.id.action_chargingFragment_to_main_fragment2
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_main_fragment
//                R.id.parking_info_fragment -> action_id =R.id.action_parking_info_fragment_to_main_fragment
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_mapChargeParkingFragment
//                R.id.main_fragment -> action_id =R.id.action_main_fragment_to_mapChargeParkingFragment
//                R.id.chargeEntryFragment -> action_id =R.id.action_chargeEntryFragment_to_main_fragment
//                R.id.chargeScanFragment -> action_id =R.id.action_chargeScanFragment_to_main_fragment
//                R.id.parking_history_paid_fragment -> action_id =R.id.action_parking_history_paid_fragment_to_main_fragment
//                R.id.chargingFragment -> action_id =R.id.action_chargingFragment_to_main_fragment
            }
            if (action_id != -1)
                findNavController().navigate(action_id)
            else
                findNavController().navigate(id)
        }
    }
    private fun toProjectFagment(id: Int)
    {
        val currentFragmentId = findNavController().currentDestination?.id
        if (currentFragmentId != id) {
            var action_id: Int =-1
            when (currentFragmentId) {
                R.id.marketFragment -> action_id =R.id.action_marketFragment_to_main_fragment
                R.id.mapChargeParkingFragment -> action_id =R.id.action_mapChargeParkingFragment_to_main_fragment
                R.id.chargingFragment -> action_id =R.id.action_chargingFragment_to_main_fragment2
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_main_fragment
//                R.id.parking_info_fragment -> action_id =R.id.action_parking_info_fragment_to_main_fragment
//                R.id.parking_license_plate_fragment -> action_id =R.id.action_parking_license_plate_fragment_to_mapChargeParkingFragment
//                R.id.main_fragment -> action_id =R.id.action_main_fragment_to_mapChargeParkingFragment
//                R.id.chargeEntryFragment -> action_id =R.id.action_chargeEntryFragment_to_main_fragment
//                R.id.chargeScanFragment -> action_id =R.id.action_chargeScanFragment_to_main_fragment
//                R.id.parking_history_paid_fragment -> action_id =R.id.action_parking_history_paid_fragment_to_main_fragment
//                R.id.chargingFragment -> action_id =R.id.action_chargingFragment_to_main_fragment
            }
            if (action_id != -1)
                findNavController().navigate(action_id)
            else
                findNavController().navigate(id)
        }
    }
}