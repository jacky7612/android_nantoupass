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
import com.jotangi.nantouparking.databinding.FragmentMarketChangeBinding
import com.jotangi.nantouparking.databinding.FragmentMarketGetPointBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
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

class MarketChangeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private lateinit var previewView: PreviewView
    private lateinit var barcodeScanner: BarcodeScanner
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentMarketChangeBinding? = null
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private val binding get() = _binding

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
        _binding = FragmentMarketChangeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMarketChangePointTitle()
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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(requireContext()))
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
        content?.let {
            Log.d("micCheckBV", it)
            storeId = it
            val parts = storeId.split(",")

// Assign the parts to separate variables
            storeNumber = parts[0] // "1229102"
            storeName = parts[1]
            if (findNavController().currentDestination?.id != R.id.point_amount_fragment) {
                findNavController().navigate(R.id.action_to_point_amount)
            } else {
                Log.d("NavigationDebug", "Already in point_amount_fragment")
            }
        }
    }

    companion object {
var storeName = ""
        var storeNumber = ""
        var storeId = "0"

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