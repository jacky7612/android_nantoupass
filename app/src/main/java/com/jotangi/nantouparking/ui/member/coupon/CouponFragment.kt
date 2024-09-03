package com.jotangi.nantouparking.ui.member.coupon

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentCouponBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.CouponVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.journeyapps.barcodescanner.BarcodeEncoder


class CouponFragment : BaseFragment(), CouponClickListener {
    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private var data = mutableListOf<CouponVO>()
    private lateinit var couponAdapter: CouponAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
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
        setupCouponTitle()
        initObserver()
        initView()
        initData()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.exchangeableCouponData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                updateListView(result)
            } else {
                updateListView(null)
                AppUtility.showPopDialog(
                    requireContext(),
                    null,
                    "目前沒有符合的優惠券唷"
                )
            }
        }

        mainViewModel.exchangedCouponData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                updateListView(result)
            } else {
                updateListView(null)
                AppUtility.showPopDialog(
                    requireContext(),
                    null,
                    "目前沒有符合的優惠券唷"
                )
            }
        }
    }

    private fun initView() {
        initRecyclerView()
    }

    private fun initData() {
        mainViewModel.getMyCoupon(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!
        )
    }

    private fun initAction() {
        binding?.apply {
            couponMaterialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                updateTabButton(
                    checkedId,
                    isChecked
                )
            }
        }
    }

    private fun initRecyclerView() {
        binding?.couponRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            couponAdapter = CouponAdapter(
                data,
                requireContext(),
                this@CouponFragment
            )
            this.adapter = couponAdapter
        }
    }

    override fun onCouponItemClick(vo: CouponVO) {
        showCustomDialog(vo)
    }

    private fun showCustomDialog(result: CouponVO) {
        val customDialog = Dialog(requireContext())

        customDialog.setContentView(R.layout.dialog_coupon)
        customDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog.setCanceledOnTouchOutside(true)

        val dialogTitleTextView: TextView =
            customDialog.findViewById(R.id.dialog_coupon_discount_title_textView)!!
        val dialogContentImageView: ImageView =
            customDialog.findViewById(R.id.dialog_coupon_discount_content_imageView)!!
        val dialogDateTextView: TextView =
            customDialog.findViewById(R.id.dialog_coupon_discount_endDate_textView)!!
        val dialogConfirmButton =
            customDialog.findViewById<Button>(R.id.dialog_coupon_confirm_button)

        dialogConfirmButton.setOnClickListener {
            customDialog.dismiss()
        }

        if (result.couponName.isNotEmpty()) {
            dialogTitleTextView.text = result.couponName
        }

        if (result.couponNo.isNotEmpty()) {
            val couponQRCodeImageBitmap = BarcodeEncoder().encodeBitmap(
                result.couponNo,
                BarcodeFormat.QR_CODE,
                250,
                250
            )

            dialogContentImageView.setImageBitmap(couponQRCodeImageBitmap)
        }

        if (result.couponEndDate.isNotEmpty()) {
            dialogDateTextView.text = "使用期限：${result.couponEndDate}"
        }

        customDialog.show()
    }

    private fun updateListView(result: List<CouponVO>?) {
        if (result != null) {
            couponAdapter.updateDataSource(result)
        } else {
            couponAdapter.clearDataSource()
        }
    }

    private fun updateTabButton(
        checkedId: Int,
        isChecked: Boolean
    ) {
        binding?.apply {
            when (checkedId) {
                R.id.coupon_exchangeable_materialButton -> {
                    if (isChecked) {
                        resetTabButtonColor()
                        updateTabButtonColor(couponExchangeableMaterialButton)
                        mainViewModel.getExchangeableCoupon()
                    }
                }

                R.id.coupon_exchanged_materialButton -> {
                    if (isChecked) {
                        resetTabButtonColor()
                        updateTabButtonColor(couponExchangedMaterialButton)
                        mainViewModel.getExchangedCoupon()
                    }
                }

                R.id.coupon_expired_materialButton -> {
                    if (isChecked) {
                        resetTabButtonColor()
                        updateTabButtonColor(couponExpiredMaterialButton)
                        mainViewModel.getExpiredCoupon()
                    }
                }
            }
        }
    }

    private fun resetTabButtonColor() {
        binding?.apply {
            couponExchangeableMaterialButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )

            couponExchangedMaterialButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )

            couponExpiredMaterialButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
        }
    }

    private fun updateTabButtonColor(selectedButton: MaterialButton) {
        selectedButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_blue_A400
            )
        )
    }
}