package com.jotangi.nantoupass.ui.charge.ChargeDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.nantoupass.JackyVariant.ConvertText
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.ApiChargeConfig
import com.jotangi.nantoupass.databinding.FragmentChargeHistoryDetailBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.charge.DataChargeHistory
import com.jotangi.nantoupass.ui.BaseFragment
import com.jotangi.nantoupass.utility.AppUtility

class ChargeHistoryDetailFragment : BaseFragment() {
    private var _binding: FragmentChargeHistoryDetailBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private lateinit var billNo: String

    private var cur_data: DataChargeHistory? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChargeHistoryDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        chargeViewModel.clearCheckData()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupParkingHistoryTitle()
        initObserver()
        initData()
        initAction()
        chargeViewModel.clearHistoryData()
        if (Glob.curChargeHistoryData != null) {
            updateView(Glob.curChargeHistoryData!!)
        } else {
            triggerGetOrderData()
        }
    }
    private fun triggerGetOrderData() {
        chargeViewModel.clearHistoryData()
        chargeViewModel.getHistory(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            ConvertText.getFormattedDate("") + " 00:00:00",
            ConvertText.getFormattedDate("") + " 23:59:59",
            "false",
            "9999",
            "0"
        )
    }

    private fun initObserver() {
        chargeViewModel.chargeHistory.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    updateView(chargeViewModel.chargeHistory.value!!.data!![0])
                } else {
                    if (result.code        == "0x0201") {
                        showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
                    } else if (result.code == "0x0202") {
                        showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
                    }
                }
            }
        }
    }

    private fun initData() {
        billNo = arguments?.getString("billNo").toString()
        mainViewModel.getParkingFeePaidDetail(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            billNo
        )
    }

    private fun initAction() {
        binding?.apply {
            btNavigation.setOnClickListener {
                showPayMethodDialogView()
            }
        }
    }

    private fun updateView(data: DataChargeHistory) {
        binding?.apply {
            tvHint.visibility =View.INVISIBLE
            clChargeDetail.visibility =View.VISIBLE

            cur_data =data
            val kWh =data.kwh
            tvTime.text                 =data.start_time
            tvAmount.text               =data.price
            if (data.pay_status != null) {
                tvStatus.text = if (data.pay_status.contains("付款成功") || data.pay_status == "1") "已付款" else "待付款"
            }
            if (data.price.toFloat().toInt() == 0) {
                tvStatus.text ="無需付款"
            }
            btNavigation.visibility     =if (data.price.toFloat().toInt() == 0 || tvStatus.text == "已付款") View.GONE else View.VISIBLE
            tvTotalTime.text            =data.charge_time

            tvStationName.text          =data.station_name
            tvChargeDeviceId.text       =data.charge_point_id
            tvStationDescription.text   =data.charge_name
            tvChargePowerContent.text   ="${kWh}kWh"
        }
    }

    companion object {

    }
    private fun showPayMethodDialogView() {
        try {
            val dstUrl =getAppUrl()
            launchUri(dstUrl)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun getAppUrl(): String {
        val appUrl = ApiChargeConfig.PAYMENT_URL +
                "?" +
                "member_id=${AppUtility.getLoginId(requireContext())}" +
                "&" +
                "amount=${cur_data?.price}" +
                "&" +
                "bill_no=${cur_data?.order_id}" +
                "&" +
                "description=${cur_data?.station_name} ${cur_data?.charge_name}" +
                "&" +
                "start_time=${cur_data?.start_time}" +
                "&" +
                "stop_time=${cur_data?.stop_time}" +
                "&" +
                "charge_time=${cur_data?.charge_time}"
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
}