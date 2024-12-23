package com.jotangi.nantouparking.ui.member

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentForgetPasswordBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class ForgetPasswordFragment : BaseFragment() {
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initEditText()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initEditText() {
        binding?.apply {
            seePWD1.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Show the password when the finger touches the eye icon
                        forgetPasswordNewInputContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Hide the password when the finger is lifted
                        forgetPasswordNewInputContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                true
            }
            seePWD2.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Show the password when the finger touches the eye icon
                        forgetPasswordNewConfirmContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Hide the password when the finger is lifted
                        forgetPasswordNewConfirmContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                true
            }
        }
    }

    private fun init() {
        mainViewModel.clearForgetPwdData()
        setupForgetPasswordTitle()
        initObserver()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.forgetPasswordData.observe(viewLifecycleOwner) { result ->
            if (result.code == ApiConfig.API_CODE_SUCCESS.toString()) {
                showPrivateDialog(
                    "更新成功",
                    null
                )
            } else {
                if (!result.responseMessage.isNullOrEmpty()) {
                    AppUtility.showPopDialog(
                        requireContext(),
                        result.code,
                        result.responseMessage
                    )
                }
            }
        }
    }

    private fun initAction() {
        binding?.apply {
            resetPassWordButton.setOnClickListener {
                forgetPassword()
            }
            memberUpdatePasswordCancelButton.setOnClickListener{
                onBackPressed()
            }
        }
    }



    private fun forgetPassword() {
        if (binding?.forgetPasswordPhoneContentEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "帳號為必填！",
                binding?.forgetPasswordPhoneContentEditText
            )

            return
        }

        if (binding?.forgetPasswordNewInputContentEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "密碼為必填！",
                binding?.forgetPasswordNewInputContentEditText
            )

            return
        }

        if (binding?.forgetPasswordNewInputContentEditText?.text.toString() !=
            binding?.forgetPasswordNewConfirmContentEditText?.text.toString()
        ) {
            showPrivateDialog(
                "密碼不一致！",
                binding?.forgetPasswordNewInputContentEditText
            )

            return
        }

        mainViewModel.forgetPassword(
            requireContext(),
            binding?.forgetPasswordPhoneContentEditText?.text.toString(),
            binding?.forgetPasswordNewInputContentEditText?.text.toString()
        )
    }

    private fun showPrivateDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = "提醒！"

        // 需加此段才會正確
        if (message == "更新成功") {
            AppUtility.updateLoginPassword(
                requireContext(),
                binding?.forgetPasswordNewInputContentEditText?.text.toString()
            )
        }

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.forgetPasswordPhoneContentEditText,
                binding?.forgetPasswordNewInputContentEditText -> {
                    return@setPositiveButton
                }

                null -> {
//                    findNavController().navigate(R.id.action_to_login)
                    onBackPressed()
                }
            }
        }

        alert.show()
    }

    companion object {

    }
}