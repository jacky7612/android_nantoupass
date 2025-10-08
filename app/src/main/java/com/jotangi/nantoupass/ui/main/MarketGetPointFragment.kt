package com.jotangi.nantoupass.ui.main

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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.FragmentMarketGetPointBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.ui.BaseFragment
import com.jotangi.nantoupass.utility.AppUtility
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
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
    private lateinit var previewView: DecoratedBarcodeView
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

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera() // Permission is already granted
        } else {
            requestPermissions(
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

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun startCamera() {
        previewView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                result.text?.let { barcode ->
                    Log.i("BarcodeScannerFragment", "Scanned Barcode: $barcode")
                    barcode?.let {
                        requireActivity().runOnUiThread {
                            showQRCodeContent(it)
                        }
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {
                // Handle possible result points if needed
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) { // Match the requestCode used in requestPermissions
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MarketGetPointFragment", "Camera permission granted")
                startCamera() // Start the camera immediately
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
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