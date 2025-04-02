package com.jotangi.nantouparking.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
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
    var call = false
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
        binding?.btnCancel?.setOnClickListener {
            findNavController().navigate(
                R.id.action_verifyFragment_to_mainFragment
            )
        }
        binding?.editTextPlateAlpha?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val upperCaseText = it.toString().uppercase()
                    if (it.toString() != upperCaseText) {
                        binding?.editTextPlateAlpha?.setText(upperCaseText)
                        binding?.editTextPlateAlpha?.setSelection(upperCaseText.length) // 將光標移到最後
                    }
                }
            }
        })
        binding?.editTextPlateNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val upperCaseText = it.toString().uppercase()
                    if (it.toString() != upperCaseText) {
                        binding?.editTextPlateNumber?.setText(upperCaseText)
                        binding?.editTextPlateNumber?.setSelection(upperCaseText.length) // 將光標移到最後
                    }
                }
            }
        })
        binding?.textPhoneNumber?.setText(ChargeNoticeFragment.idValue)
        binding?.textResend?.visibility = View.GONE
        binding?.btnSend?.visibility = View.VISIBLE
        binding?.btnVerify?.setOnClickListener {
            if(binding?.editTextPlateAlpha?.text.isNullOrEmpty() && binding?.editTextPlateNumber?.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "請填寫車牌號碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding?.editTextVerification?.text.isNullOrEmpty() ) {
                Toast.makeText(requireContext(), "請填寫驗證碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val code = binding?.editTextVerification?.text?.toString()
            if(code.equals(verifyCodeValue)) {
                    editMemberData("1")
            } else {
                showVerificationErrorDialog()
            }
        }

        binding?.btnSend?.setOnClickListener {
call = true
            verifyCodeValue = generateSixDigitCode().toString()
        mainViewModel.sendVerify(requireContext(), ChargeNoticeFragment.idValue, verifyCodeValue)
        }
        binding?.textResendOption2?.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        binding?.textResendOption2?.setOnClickListener {
            call = true
            verifyCodeValue = generateSixDigitCode().toString()
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
                binding?.editTextPlateAlpha?.text.toString() +"-" + binding?.editTextPlateNumber?.text.toString(),
                AppUtility.getLoginPassword(requireContext())!!,
                ChargeNoticeFragment.carrierValue,
                verifyStatus
            )
        }
    }

    private fun initObserver() {
        mainViewModel.loginData.observe(viewLifecycleOwner) { result ->
            if(call) {
                result?.let {
                    when (result.code) {
                        "0x0200" -> {
                            binding?.textResend?.visibility = View.VISIBLE
                            binding?.btnSend?.visibility = View.GONE
                            object :
                                CountDownTimer(30_000, 1000) { // 30 seconds, tick every 1 second
                                override fun onTick(millisUntilFinished: Long) {

                                }

                                override fun onFinish() {
                                    binding?.textResend?.visibility = View.GONE
                                    binding?.btnSend?.visibility = View.VISIBLE
                                }
                            }.start()
                            Toast.makeText(requireContext(), "傳送驗證碼成功", Toast.LENGTH_SHORT)
                                .show()
                        }

                        "0x0201" -> {
                            Toast.makeText(requireContext(), "查無會員資料", Toast.LENGTH_SHORT)
                                .show()
                        }

                        "0x0202" -> {
                            Toast.makeText(requireContext(), "系統異常", Toast.LENGTH_SHORT).show()
                        }

                        "0x0203" -> {
                            Toast.makeText(requireContext(), "缺少必要參數", Toast.LENGTH_SHORT)
                                .show()
                        }

                        "0x0204" -> {
                            Toast.makeText(requireContext(), "資料庫錯誤", Toast.LENGTH_SHORT)
                                .show()
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
                call = false
            }
        }
        mainViewModel.memberInfoEditData.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.code) {
                    "0x0200" -> {
                        Toast.makeText(requireContext(),"驗證成功", Toast.LENGTH_SHORT ).show()
                        findNavController().navigate(
                            R.id.action_verifyFragment_to_mapChargeParkingFragment
                        )
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