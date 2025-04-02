package com.jotangi.nantouparking.ui.charge

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
var call = false
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
        binding?.svCharge?.post {
            if (!isScrollViewScrollable()) {
                // Content doesn't need scrolling; enable the button
                isBottom = true
                binding?.btAgree?.apply {
                    isEnabled = true
                    setBackgroundResource(R.drawable.round_primary)
                    setTextColor(Color.WHITE)
                }
            }
        }
        binding?.svCharge?.viewTreeObserver?.addOnScrollChangedListener {
            // 判斷是否已經滑到底部
            isBottom = isScrollViewAtBottom()
//                agreeContractCheckBox.isEnabled = isBottom
            if (isBottom) {
                // 已經滾動到最底部
                binding?.btAgree?.isEnabled = true
                binding?.btAgree?.setBackgroundResource(R.drawable.round_primary)
                binding?.btAgree?.setTextColor(Color.WHITE)
            }
        }
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
                call = true
                mainViewModel.getMemberInfo(
                    requireContext(),
                    AppUtility.getLoginId(requireContext())!!,
                    AppUtility.getLoginPassword(requireContext())!!,
                    null
                )
            }
        }
    }
    private fun isScrollViewScrollable(): Boolean {
        val scrollView = binding?.svCharge ?: return false
        val child = scrollView.getChildAt(0) ?: return false
        return child.height > scrollView.height
    }
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
        var nameValue = ""
        var emailValue = ""
        var idValue = ""
        var plateNoValue = ""
        var pwdValue = ""
        var carrierValue = ""
        var verifyStatusValue = ""
    }
    private fun initObserver() {
        mainViewModel.memberInfoData.observe(viewLifecycleOwner) { result ->
            if(call) {
                Log.d("micCheck11", (result.firstOrNull()?.verifyStatus == null).toString())
                Log.d("micCheck11", (result.firstOrNull()?.verifyStatus).toString())

                if (result != null) {
                    if (result.firstOrNull()?.verifyStatus == null) {
                        Toast.makeText(requireContext(), "獲取會員資料失敗", Toast.LENGTH_SHORT)
                            .show()
                        return@observe
                    }
                    if (result.firstOrNull()?.verifyStatus.equals("0")) {
                        nameValue = result.firstOrNull()?.memberName ?: ""
                        emailValue = result.firstOrNull()?.memberEmail ?: ""
                        idValue = result.firstOrNull()?.memberId ?: ""
                        plateNoValue = result.firstOrNull()?.memberPlate ?: ""
                        Log.d("micCheckHJ", plateNoValue)
                        pwdValue = result.firstOrNull()?.memberPassword ?: ""
                        carrierValue = result.firstOrNull()?.memberCarrier ?: ""
                        verifyStatusValue = result.firstOrNull()?.verifyStatus ?: "0"
                        findNavController().navigate(
                            R.id.action_chargeNoticeFragment2_to_verifyFragment
                        )
                    } else {
                        Log.d("micCheckHJ", result.firstOrNull()?.memberId.toString())
                        findNavController().navigate(
                            R.id.action_chargeNoticeFragment2_to_mapChargeParkingFragment
                        )
                    }
                } else {
                    Toast.makeText(requireContext(), "獲取會員資料失敗", Toast.LENGTH_SHORT).show()
                }
                call = false
            }
        }
            chargeViewModel.chargeCheck.observe(viewLifecycleOwner) { result ->
                Log.d("micCheckLKJ", "LKJ")
                if (result != null) {
                    if (result.status == "true") {
                        // 已經滾動到最底部
                        Glob.curChargeInfo!!.gunDeviceId = result.charge_id
                        Glob.curChargeInfo!!.gunNumber = result.gun_no
                    } else {
                        when (result.code) {
                            "0x0201" -> { // 目前正在充電中
                                Glob.curChargeInfo!!.gunDeviceId = result.charge_id
                                Glob.curChargeInfo!!.gunNumber = result.gun_no
                                showCustomDialog(
                                    requireContext(),
                                    result.responseMessage,
                                    R.id.action_chargeNoticeFragment2_to_chargingFragment
                                )
                            }
                            "0x0202" -> { // 有未繳費充電帳單
                                Glob.curChargeInfo!!.gunDeviceId = result.charge_id
                                Glob.curChargeInfo!!.gunNumber = result.gun_no
                                val msg = "尚有充電未繳款項目\n請先完成繳款\n方能使用充電服務"
                                showCustomDialog(
                                    requireContext(),
                                    msg,
                                    R.id.action_chargeNoticeFragment2_to_chargeHistoryDetailFragment,
                                    "前往繳費"
                                )
                            }
                            else -> {
                                Glob.curChargeInfo!!.gunDeviceId = result.charge_id
                                Glob.curChargeInfo!!.gunNumber = result.gun_no
                                showCustomDialog(
                                    requireContext(),
                                    result.responseMessage,
                                    R.id.action_chargeNoticeFragment2_to_main_fragment
                                )
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