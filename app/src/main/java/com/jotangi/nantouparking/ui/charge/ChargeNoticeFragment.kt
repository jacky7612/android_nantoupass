package com.jotangi.nantouparking.ui.charge

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentChargeNoticeBinding
import com.jotangi.nantouparking.databinding.FragmentParkingNoticeBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class ChargeNoticeFragment : BaseFragment() {
    private var _binding: FragmentChargeNoticeBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private var isBottom: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChargeNoticeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chargeViewModel.clear()
        Glob.clear()
        init()
    }

    private fun init() {
        setupChargeNoticeTitle()

        initObserver()
        triggerGetData()
        initAction()
    }

    private fun initAction() {
        binding?.apply {
            btAgree.setOnClickListener {
                findNavController().navigate(
                    R.id.action_chargeNoticeFragment2_to_mapChargeParkingFragment
                )
            }
        }
    }

    // 檢查 ScrollView 是否已經滑到底部的方法
    private fun isScrollViewAtBottom(): Boolean {
        val scrollViewHeight = binding?.svCharge?.height
        val scrollContentHeight = binding?.svCharge?.getChildAt(0)?.height
        val scrollY = binding?.svCharge?.scrollY
        if (scrollY != null) {
            return scrollY + scrollViewHeight!! >= scrollContentHeight!!
        }

        return false
    }
    companion object {

    }
    // ---------------------------------------------------------------------------------------------
    private fun initObserver() {
        chargeViewModel.chargeCheck.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    // 已經滾動到最底部
                    binding?.apply {
                        btAgree.isEnabled = true
                        btAgree.setBackgroundResource(R.drawable.round_primary)
                        btAgree.setTextColor(Color.WHITE)
                    }
                    Glob.curChargeInfo!!.gunDeviceId =result.charge_id
                    Glob.curChargeInfo!!.gunNumber   =result.gun_no
                } else {
                    when (result.code) {
                        "0x0201" -> { // 目前正在充電中
                            Glob.curChargeInfo!!.gunDeviceId =result.charge_id
                            Glob.curChargeInfo!!.gunNumber   =result.gun_no
                            showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeNoticeFragment2_to_chargingFragment)
                        }
                        "0x0202" -> { // 有未繳費充電帳單
                            Glob.curChargeInfo!!.gunDeviceId =result.charge_id
                            Glob.curChargeInfo!!.gunNumber   =result.gun_no
                            val msg ="尚有充電未繳款項目\n請先完成繳款\n方能使用充電服務"
                            showCustomDialog(requireContext(), msg, R.id.action_chargeNoticeFragment2_to_chargeHistoryDetailFragment, "前往繳費")
                        }
                        else -> {
                            Glob.curChargeInfo!!.gunDeviceId =result.charge_id
                            Glob.curChargeInfo!!.gunNumber   =result.gun_no
                            showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeNoticeFragment2_to_main_fragment)
                        }
                    }
                }
            }
        }
    }
    private fun triggerGetData() {
        chargeViewModel.checkCharge(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!)
    }
}