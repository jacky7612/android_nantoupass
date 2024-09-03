package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.view.LayoutInflater
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
    }

    override fun onPause() {
        super.onPause()

        mainViewModel.clearUpdatePasswordData()
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
                binding?.passwordUpdateCurrentPasswordContentTextInputEditText,
                binding?.passwordUpdateNewInputContentTextInputEditText,
                binding?.passwordUpdateNewConfirmContentTextInputEditText -> {
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
                    passwordUpdateCurrentPasswordContentTextInputEditText.text.toString(),
                    passwordUpdateNewInputContentTextInputEditText.text.toString(),
                    passwordUpdateNewConfirmContentTextInputEditText.text.toString()
                )
            ) {
                AppUtility.updateLoginPassword(
                    requireContext(),
                    passwordUpdateNewInputContentTextInputEditText.text.toString()
                )

                mainViewModel.updatePassword(
                    requireContext(),
                    AppUtility.getLoginId(requireContext())!!,
                    passwordUpdateCurrentPasswordContentTextInputEditText.text.toString(),
                    passwordUpdateNewInputContentTextInputEditText.text.toString()
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