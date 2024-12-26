package com.jotangi.nantouparking.ui

import CouponListResponse
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.jotangi.nantouparking.databinding.FragmentCouponListBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.main.CouponAdapter
import com.jotangi.nantouparking.utility.AppUtility

class CouponListFragment : BaseFragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentCouponListBinding? = null
    private val binding get() = _binding!!
    private lateinit var couponAdapter: CouponAdapter
    private var selectedTabId = 0 // Default to "可兌換"
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
setupCouponListTitle()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupTabLayout()
        setupRecyclerView()

        // Fetch initial data for the default tab
        fetchCouponList(selectedTabId)
    }

    private fun setupTabLayout() {
        binding.tabLayout.apply {
            addTab(this.newTab().setText("可兌換")) // Add "可兌換" tab
            addTab(this.newTab().setText("已兌換")) // Add "已兌換" tab
            addTab(this.newTab().setText("已過期")) // Add "已過期" tab

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selectedTabId = when (tab?.position) {
                        1 -> 1 // "已兌換"
                        2 -> 2 // "已過期"
                        else -> 0 // "可兌換"
                    }
                    fetchCouponList(selectedTabId)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun setupRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        couponAdapter = CouponAdapter(emptyList(), selectedTabId) { coupon ->
            // Handle item click
            onCouponClicked(coupon)
        }
        binding.rv.adapter = couponAdapter
    }

    private fun fetchCouponList(tabId: Int) {
        viewModel.fetchCouponList(
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            "",
            tabId.toString()
        )
        observeCouponList()
    }

    private fun observeCouponList() {
        viewModel.couponListData.observe(viewLifecycleOwner) { couponList ->
            if (couponList.isNotEmpty()) {
                couponAdapter.updateData(couponList, selectedTabId)
                Log.d("CouponList", "Coupons: $couponList")
            } else {
                Log.d("CouponList", "No data available for tabId: $selectedTabId")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var couponPass: CouponListResponse = CouponListResponse(
            cid = "",
            coupon_id = "",
            member_id = "",
            coupon_name = "",
            using_flag = "",
            using_time = null,
            coupon_body = "",
            coupon_description = "",
            coupon_startdate = "",
            coupon_enddate = "",
            coupon_status = "",
            coupon_rule = "",
            discount_amount = "",
            coupon_storeid = null,
            coupon_picture = null,
            coupon_created_at = "",
            coupon_updated_at = null,
            coupon_trash = ""
        )
    }


    private fun onCouponClicked(coupon: CouponListResponse) {
        if(selectedTabId == 0) {
            couponPass = coupon
            val action = CouponListFragmentDirections
                .actionCouponListFragmentToCouponDetailFragment()
            findNavController().navigate(action)
        }
    }
}
