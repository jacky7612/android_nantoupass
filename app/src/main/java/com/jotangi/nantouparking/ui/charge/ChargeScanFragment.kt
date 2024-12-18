package com.jotangi.nantouparking.ui.charge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.jotangi.nantouparking.JackyVariant.ConvertText
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentChargeScanBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.lang.Thread.sleep

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChargeScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChargeScanFragment : BaseFragment() {
    private var _binding: FragmentChargeScanBinding? = null
    private val binding get() = _binding

    private lateinit var barcodeScannerView: DecoratedBarcodeView

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    // TODO: Rename and change types of parameters

    private var isToNext =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var myQRcode :String =""
    private var myGunNum :String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargeScanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myQRcode =""
        myGunNum =""
        setToolbarTitle("開始使用")

        initObserver()

        binding?.apply {
            btNext.setOnClickListener {
                triggerGetData(myQRcode, myGunNum)
            }
        }

        barcodeScannerView = binding!!.barcodeScanner
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                result.text?.let { barcode ->
                    Log.i("BarcodeScannerFragment", "Scanned Barcode: $barcode")
                    // Handle the scanned barcode result here
                    myQRcode =barcode
                    Glob.curChargeInfo?.QRCode =myQRcode
                    myGunNum ="1"
                    val fruits = myQRcode.split("&port=") // Split by comma
                    if (fruits.size >= 2) {
                        myQRcode = fruits[0]
                        myGunNum = fruits[1]
                    }
                    binding?.apply {
                        Glob.curChargeInfo?.MaskQRCode =maskText(myQRcode)
                        if (!Glob.QRcodeAutoNext) {
                            etDeviceId.setText(Glob.curChargeInfo?.MaskQRCode)
                        }
                        if (Glob.QRcodeAutoNext) {
                            triggerGetData(myQRcode, myGunNum)
                        }
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {
                // Handle possible result points if needed
            }
        })
    }

    override fun onPause() {
        super.onPause()
        if (this::barcodeScannerView.isInitialized) {
            barcodeScannerView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::barcodeScannerView.isInitialized) {
            barcodeScannerView.resume()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startScanning()
        } else {
            Log.e("BarcodeScannerFragment", "Camera permission is needed to scan barcodes")
        }
    }
    // =============================================================================================
    private fun initObserver() {
        chargeViewModel.chargeQrcode.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    Glob.curChargeGunData =result.data
                    Glob.curChargeInfo!!.gunDeviceId =result.data!!.ID
                    Glob.curChargeInfo!!.gunNumber="1"
                    if (!isToNext) {
                        isToNext =true
                        findNavController().navigate(R.id.action_chargeScanFragment_to_chargeStartFragment) // action_chargeScanFragment_to_chargePayFragment
                    }
                } else {
                    if (result.code        == "0x0201") {
                        showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
                    } else if (result.code == "0x0202") {
                        showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeEntryFragment_to_main_fragment)
                    } else {
                        showCustomDialog(requireContext(), result.responseMessage, -1)
                    }
                }
            }
        }
    }
    private fun triggerGetData(QRcode: String, gun_num: String) {
        chargeViewModel.sendQRcode(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            QRcode,
            gun_num
        )
    }

    private fun maskText(input: String): String {
        val maskLen =input.length / 2
//        val maskedText = ConvertText.maskRandomWords(input, maskLen) // 选择要遮蔽的单词数量
        val maskedText = ConvertText.maskWords(input, 5, 12) // 选择要遮蔽的单词数量

        return maskedText.toString()
    }
}