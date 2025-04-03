package com.jotangi.nantouparking.ui.charge

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.JackyVariant.ConvertText
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentChargeEntryBinding
import com.jotangi.nantouparking.databinding.ToolbarFeetBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.charge.DataChargeHistory
import com.jotangi.nantouparking.ui.BaseWithBottomBarFragment
import com.jotangi.nantouparking.ui.charge.ChargeMain.ChargeHistoryAdapter
import com.jotangi.nantouparking.utility.AppUtility

// TODO: Rename parameter arguments, choose names that match
class ChargeEntryFragment : BaseWithBottomBarFragment() {
    private var _binding: FragmentChargeEntryBinding? = null
    private val binding get() = _binding

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    // TODO: Rename and change types of parameters
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet


    private var chargeHistoryData = mutableListOf<DataChargeHistory>()
    private lateinit var chargeHistoryAdapter: ChargeHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargeEntryBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle("充電服務", true)

        initObserver()
        triggerGetData()
        initEvent()

        binding?.apply {
            btStart.setOnClickListener {
                findNavController().navigate(R.id.action_chargeEntryFragment_to_chargeScanFragment)
            }
            tvChargeTab.setOnClickListener {
                setTabVisible(true)
            }
            tvChargeHistoryTab.setOnClickListener {
                setTabVisible(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    // =============================================================================================
    private fun initObserver() {
        chargeViewModel.chargeHistory.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    initRecyclerView(result.data!!)
                } else {
                    if (result.code        == "0x0201") {
                        showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
                    } else if (result.code == "0x0202") {
                        showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
                    }
                }
            }
        }
        chargeViewModel.chargeCheck.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    triggerHistory()
                } else {
                    when (result.code) {
                        "0x0201" -> { // 目前正在充電中
                            showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_chargingFragment)
                        }
                        "0x0202" -> { // 有未繳費充電帳單
                            val msg ="尚有充電未繳款項目\n請先完成繳款\n方能使用充電服務"
                            showCustomDialog(requireContext(), msg, R.id.action_chargeEntryFragment_to_chargeHistoryDetailFragment, "前往繳費")
                        }
                        else -> {
                            triggerHistory()
                            showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
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
    private fun triggerHistory() {
        chargeViewModel.getHistory(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            ConvertText.getFormattedDate("") + " 00:00:00",
            ConvertText.getFormattedDate("") + " 23:59:59"
        )
    }
    private fun setTabVisible(flag :Boolean)
    {
        binding?.apply {
            clAreaCharge.visibility         =if ( flag) View.VISIBLE else View.GONE
            vwChargeTab.visibility          =if ( flag) View.VISIBLE else View.GONE

            clAreaChargeHistory.visibility  =if (!flag) View.VISIBLE else View.GONE
            vwChargeHistoryTab.visibility   =if (!flag) View.VISIBLE else View.GONE
        }
    }

    private fun initRecyclerView(result: List<DataChargeHistory>) {
        chargeHistoryData = result.toMutableList()
        binding?.apply {
            rvChargeHistory.visibility  =if (chargeHistoryData.size > 0) View.VISIBLE else View.GONE
            tvNoData.visibility         =if (chargeHistoryData.size == 0) View.VISIBLE else View.GONE

            tvHint.visibility  =View.GONE
        }
        binding?.rvChargeHistory?.apply {

            // Initialize the adapter with the click listener
            chargeHistoryAdapter = ChargeHistoryAdapter(chargeHistoryData) { item ->
                // Handle item click
                Glob.curChargeHistoryData =item
                findNavController().navigate(R.id.action_chargeEntryFragment_to_chargeHistoryDetailFragment)
//                Toast.makeText(requireContext(), "Clicked: ${item.start_time}", Toast.LENGTH_SHORT).show()
            }

            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = chargeHistoryAdapter
        }
    }
}