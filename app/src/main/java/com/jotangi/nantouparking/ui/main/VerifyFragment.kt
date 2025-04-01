package com.jotangi.nantouparking.ui.main

import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentStoreManager3Binding
import com.jotangi.nantouparking.databinding.FragmentVerifyBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.ui.charge.ChargeNoticeFragment
import com.jotangi.nantouparking.utility.AppUtility

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VerifyFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    var verifyCodeValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVerifyTitle()
        initObserver()
        ChargeNoticeFragment.plateNoValue.let {
            val parts = it.split("-")
            if (parts.size == 2) {
                binding?.editTextPlateAlpha?.setText(parts[0])
                binding?.editTextPlateNumber?.setText(parts[1])
            }
        }
        binding?.textPhoneNumber?.setText(ChargeNoticeFragment.idValue)
        binding?.textResend?.visibility = View.GONE
        binding?.btnSend?.visibility = View.VISIBLE
        binding?.btnVerify?.setOnClickListener {
            val code = binding?.editTextVerification?.text?.toString()
            if(code.equals(verifyCodeValue)) {
                    editMemberData("1")
            } else {
                showVerificationErrorDialog()
            }
        }
        binding?.btnSend?.setOnClickListener { verifyCodeValue = generateSixDigitCode().toString()
        mainViewModel.sendVerify(requireContext(), ChargeNoticeFragment.idValue, verifyCodeValue)
        }
        binding?.textResendOption?.setOnClickListener { verifyCodeValue = generateSixDigitCode().toString()
            mainViewModel.sendVerify(requireContext(), ChargeNoticeFragment.idValue, verifyCodeValue)
        }
    }
    fun generateSixDigitCode(): Int {
        return (100000..999999).random()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)
        return binding?.root    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun editMemberData(verifyStatus:String) {
        binding!!.apply {
            mainViewModel.editMemberInfo(
                requireContext(),
               ChargeNoticeFragment.nameValue,
                ChargeNoticeFragment.emailValue,
                ChargeNoticeFragment.idValue,
                binding?.editTextPlateAlpha?.text.toString() +"-" + binding?.editTextPlateNumber?.text.toString() +"-",
                AppUtility.getLoginPassword(requireContext())!!,
                ChargeNoticeFragment.carrierValue,
                verifyStatus
            )
        }
    }

    private fun initObserver() {
        mainViewModel.loginData.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.code) {
                    "0x0200" -> {
                        binding?.textResend?.visibility = View.VISIBLE
                        binding?.btnSend?.visibility = View.GONE
                        Toast.makeText(requireContext(), "傳送驗證碼成功", Toast.LENGTH_SHORT)
                            .show()
                    }

                    "0x0201" -> {
                        Toast.makeText(requireContext(), "查無會員資料", Toast.LENGTH_SHORT).show()
                    }

                    "0x0202" -> {
                        Toast.makeText(requireContext(), "系統異常", Toast.LENGTH_SHORT).show()
                    }

                    "0x0203" -> {
                        Toast.makeText(requireContext(), "缺少必要參數", Toast.LENGTH_SHORT).show()
                    }

                    "0x0204" -> {
                        Toast.makeText(requireContext(), "資料庫錯誤", Toast.LENGTH_SHORT).show()
                    }
                    "0x0205" -> {
                        Toast.makeText(requireContext(), "帳號或密碼錯誤", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "未知錯誤代碼: ${result.code}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        mainViewModel.memberInfoEditData.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.code) {
                    "0x0200" -> {
                        findNavController().navigate(
                            R.id.action_verifyFragment_to_mapChargeParkingFragment
                        )
                        Toast.makeText(requireContext(),"驗證成功", Toast.LENGTH_SHORT ).show()
                    }
                    "0x0201" -> {
                        Toast.makeText(requireContext(),"查無會員資料", Toast.LENGTH_SHORT ).show()
                    }
                    "0x0202" -> {
                        Toast.makeText(requireContext(),"系統異常", Toast.LENGTH_SHORT ).show()
                    }
                    "0x0203" -> {
                        Toast.makeText(requireContext(),"缺少必要參數", Toast.LENGTH_SHORT ).show()
                    }
                    "0x0204" -> {
                        Toast.makeText(requireContext(),"資料庫錯誤", Toast.LENGTH_SHORT ).show()
                    }
                    "0x0205" -> {
                        Toast.makeText(requireContext(),"帳號或密碼錯誤", Toast.LENGTH_SHORT ).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(),"未知錯誤代碼: ${result.code}", Toast.LENGTH_SHORT ).show()
                    }
                }
            }
        }
    }

    fun showVerificationErrorDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_verification_error, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val confirmButton = dialogView.findViewById<TextView>(R.id.buttonDialogConfirm)
        confirmButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            (Resources.getSystem().displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}