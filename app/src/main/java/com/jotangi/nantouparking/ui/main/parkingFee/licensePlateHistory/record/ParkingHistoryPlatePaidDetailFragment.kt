package com.jotangi.nantouparking.ui.main.parkingFee.licensePlateHistory.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.nantouparking.databinding.FragmentParkingHistoryPlatePaidDetailBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.ParkingFeePaidDetailVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class ParkingHistoryPlatePaidDetailFragment : BaseFragment() {
    private var _binding: FragmentParkingHistoryPlatePaidDetailBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private lateinit var billNo: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkingHistoryPlatePaidDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
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
    }

    private fun initObserver() {
        mainViewModel.parkingFeePaidDetailData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
//                val data = result.filter { it.billPayStatus == "1" }
                updateView(result)
            }
        }
    }

    private fun initData() {
        billNo = arguments?.getString("billNo").toString()
        mainViewModel.getParkingFeePaidDetail(
            requireContext(),
            billNo,
            AppUtility.getLoginPassword(requireContext())!!,
            billNo
        )
    }

    private fun initAction() {

    }

    private fun updateView(result: List<ParkingFeePaidDetailVO>) {
        binding?.apply {
            parkingHistoryPaidPlateContentTextView.text = result[0].plateNo ?: ""
            parkingHistoryPaidNoContentTextView.text = result[0].billNo ?: ""
            parkingHistoryPaidDateContentTextView.text = result[0].billPayDate ?: ""
            parkingHistoryPaidFeeContentTextView.text = result[0].billAmount ?: ""
            parkingHistoryPaidTypeContentTextView.text = when (result[0].billPayType) {
                "1" -> {
                    "Linepay"
                }

                "2" -> {
                    "街口"
                }

                "3" -> {
                    "信用卡"
                }

                else -> {
                    "未繳費"
                }
            }

            parkingHistoryPaidStatusContentTextView.text = when (result[0].billPayStatus) {
                "1" -> "已付款"
                else -> "未付款"
            }
            parkingHistoryAddressTextView.text = result[0].billAddress ?: ""
            parkingHistoryPaidDateTextView.text = result[0].billDescription ?: ""
            parkingHistoryPaidAmountTextView.text = "NT$${result[0].billAmount}" ?: ""
        }
    }
}