package com.jotangi.nantoupass.ui.manager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.jotangi.nantoupass.databinding.FragmentScanCouponBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.ui.BaseFragment
import com.jotangi.nantoupass.utility.AppUtility
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ScanCouponFragment : BaseFragment() {
    private var _binding: FragmentScanCouponBinding? = null
    private val binding get() = _binding!!
    override fun getToolBar(): ToolbarIncludeBinding = binding.toolbarInclude

    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private var scannedValue: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScanCouponTitle()

        barcodeScannerView = binding.barcodeScanner
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                result.text?.let { barcode ->
                    if (scannedValue == null) { // 鎖定只處理一次
                        scannedValue = barcode
                        Log.d("ScanCouponFragment", "Scanned coupon: $barcode")

                        // 呼叫 API
                        AppUtility.updateWriteOffCouponNo(requireContext(), barcode)

                        // 回上一頁
                        findNavController().navigateUp()
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {}
        })
    }

    override fun onResume() {
        super.onResume()
        if (this::barcodeScannerView.isInitialized) {
            barcodeScannerView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::barcodeScannerView.isInitialized) {
            barcodeScannerView.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScanning()
            } else {
                Log.e("ScanCouponFragment", "Camera permission denied")
            }
        }
}