package com.jotangi.nantouparking.ui.member

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentPasswordUpdateBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class PasswordUpdateFragment : BaseFragment() {
    private var _binding: FragmentPasswordUpdateBinding? = null
    private val binding get() = _binding

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPasswordUpdateBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initEditText()
    }

    override fun onPause() {
        super.onPause()

        mainViewModel.clearUpdatePasswordData()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initEditText() {
        binding?.apply {
            seePWD1.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Show the password when the finger touches the eye icon
                        passwordUpdateCurrentPasswordContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Hide the password when the finger is lifted
                        passwordUpdateCurrentPasswordContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                true
            }
            seePWD2.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Show the password when the finger touches the eye icon
                        passwordUpdateNewInputContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Hide the password when the finger is lifted
                        passwordUpdateNewInputContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                true
            }

            seePWD3.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Show the password when the finger touches the eye icon
                        passwordUpdateNewConfirmContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Hide the password when the finger is lifted
                        passwordUpdateNewConfirmContentEditText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupMemberPasswordUpdateTitle()
        initObserver()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.updatePasswordData.observe(viewLifecycleOwner) { result ->
            if (
                result != null &&
                result.code == ApiConfig.API_CODE_SUCCESS
            ) {


                showPrivateDialog(
                    "修改完成",
                    null
                )
            }
        }
    }

    private fun initAction() {
        binding?.apply {
            memberUpdatePasswordConfirmButton.setOnClickListener {
                changePassword()
            }

            memberUpdatePasswordCancelButton.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun showPrivateDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = ""

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.passwordUpdateCurrentPasswordContentEditText,
                binding?.passwordUpdateNewInputContentEditText,
                binding?.passwordUpdateNewConfirmContentEditText -> {
                    return@setPositiveButton
                }

                null -> {
                    onBackPressed()
                }
            }
        }

        alert.show()
    }

    private fun changePassword() {
        binding?.apply {
            if (
                checkPasswordValid(
                    passwordUpdateCurrentPasswordContentEditText.text.toString(),
                    passwordUpdateNewInputContentEditText.text.toString(),
                    passwordUpdateNewConfirmContentEditText.text.toString()
                )
            ) {
                AppUtility.updateLoginPassword(
                    requireContext(),
                    passwordUpdateNewInputContentEditText.text.toString()
                )

                mainViewModel.updatePassword(
                    requireContext(),
                    AppUtility.getLoginId(requireContext())!!,
                    passwordUpdateCurrentPasswordContentEditText.text.toString(),
                    passwordUpdateNewInputContentEditText.text.toString()
                )
            } else {
                AppUtility.showPopDialog(
                    requireContext(), null, "原密碼或新密碼錯誤！"
                )
            }
        }
    }

    private fun checkPasswordValid(
        curPassword: String,
        newPassword: String,
        newPasswordConfirm: String
    ): Boolean {
        return curPassword == AppUtility.getLoginPassword(requireContext()) &&
                AppUtility.passwordValid(newPassword) &&
                AppUtility.passwordValid(newPasswordConfirm) &&
                newPassword == newPasswordConfirm
    }


}