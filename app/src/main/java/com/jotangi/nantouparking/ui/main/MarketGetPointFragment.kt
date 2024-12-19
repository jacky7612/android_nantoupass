package com.jotangi.nantouparking.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMarketGetPointBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketGetPointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MarketGetPointFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var hasScanned = false
    private lateinit var previewView: PreviewView
    private lateinit var barcodeScanner: BarcodeScanner
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentMarketGetPointBinding? = null
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private val binding get() = _binding
    var call = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarketGetPointBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMarketGetPointTitle()
        previewView = view.findViewById(R.id.previewView)
        barcodeScanner = BarcodeScanning.getClient()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera() // Permission is already granted
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }

        mainViewModel.getPointResponse.observe(viewLifecycleOwner) { response ->
            Log.d("micCheckKK", response.toString())

            if (call) {
                if (response.status == "true") {
                    showScanSuccessDialog()
                } else {
                    if (response.responseMessage.contains("每位會員限兌換1次")) {
                        showScanFailDialog()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            response.responseMessage,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
        call = false
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Set up Preview
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Set up Image Analysis
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()

            // Analyzer
            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                try {
                    processImageProxy(imageProxy)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Camera selector
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Bind camera lifecycle
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        Log.d("QRCodeScanner", "Analyzing frame...")

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    Log.d("QRCodeScanner", "Frames analyzed: ${barcodes.size}")
                    for (barcode in barcodes) {
                        val rawValue = barcode.rawValue
                        Log.d("QRCodeScanner", "Detected QR Code: $rawValue")
                        rawValue?.let {
                            requireActivity().runOnUiThread {
                                showQRCodeContent(it)
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("QRCodeScanner", "Error processing frame", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }


    private fun showQRCodeContent(content: String?) {
        // Handle or display QR code content here
        if (!hasScanned) {
            hasScanned = true
            content?.let {
                call = true
                if(it.equals("user_point_get")) {
                    mainViewModel.fetchUserPoints(
                        memberId = AppUtility.getLoginId(requireContext())!!,
                        memberPwd = AppUtility.getLoginPassword(requireContext())!!,
                        pointNum = "50",
                        pointType = "1"
                    )
                } else {
Toast.makeText(requireContext(), "QRCode 不正確", Toast.LENGTH_LONG).show()
                }
                Log.d("micCheckQRCode", "QR Code Content: $it")
                println("QR Code Content: $it")
            }
        }
    }


    private fun showScanSuccessDialog() {
        // Inflate the custom layout
        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.dialog_get_point_scan_success, null)

        // Initialize the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Find the confirm button and set a click listener
        val btnConfirm = dialogView.findViewById<TextView>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            hasScanned = false
             // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }

    private fun showScanFailDialog() {
        // Inflate the custom layout
        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.dialog_duplicate_change_point, null)

        // Initialize the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Find the confirm button and set a click listener
        val btnConfirm = dialogView.findViewById<TextView>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_to_market_fragment)

            hasScanned = false
            // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MarketGetPointFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarketGetPointFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}