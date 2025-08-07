package com.jotangi.nantoupass.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.jotangi.nantoupass.databinding.FragmentCouponDetailBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding

class CouponDetailFragment : BaseFragment() {

    private var _binding: FragmentCouponDetailBinding? = null
    private val binding get() = _binding!!
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCouponListTitle()

        // Use the couponPass object from CouponListFragment
        val coupon = CouponListFragment.couponPass
        binding.tvCouponName.text = coupon.coupon_name
        binding.tvDiscountAmount.text = "折抵 ${coupon.discount_amount} 元"
        binding.tvCouponDescription.text = coupon.coupon_body
        binding.tvCouponDate.text = "使用期限: ${coupon.coupon_startdate} ~ ${coupon.coupon_enddate}"
binding.tvCouponTerms.text = coupon.coupon_description
        generateQRCode(coupon.coupon_id)
    }

    private fun generateQRCode(couponId: String) {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(couponId, BarcodeFormat.QR_CODE, 300, 300)
            val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565)
            for (x in 0 until 300) {
                for (y in 0 until 300) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    )
                }
            }
            binding.ivQRCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QRCode", "Error generating QR Code", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
