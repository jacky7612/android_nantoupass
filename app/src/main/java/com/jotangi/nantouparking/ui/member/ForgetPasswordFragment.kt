package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.view.LayoutInflater
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
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupForgetPasswordTitle()
        initObserver()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.forgetPasswordData.observe(viewLifecycleOwner) { result ->
            if (result.code == ApiConfig.API_CODE_SUCCESS.toString()) {
                showPrivateDialog(
                    result.responseMessage,
                    null
                )
            } else {
                AppUtility.showPopDialog(
                    requireContext(),
                    result.code,
                    result.responseMessage
                )
            }
        }
    }

    private fun initAction() {
        binding?.apply {
            resetPassWordButton.setOnClickListener {
                forgetPassword()
            }
        }
    }



    private fun forgetPassword() {
        if (binding?.forgetPasswordPhoneContentTextInputEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "帳號為必填！",
                binding?.forgetPasswordPhoneContentTextInputEditText
            )

            return
        }

        if (binding?.forgetPasswordNewInputContentTextInputEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "密碼為必填！",
                binding?.forgetPasswordNewInputContentTextInputEditText
            )

            return
        }

        if (binding?.forgetPasswordNewInputContentTextInputEditText?.text.toString() !=
            binding?.forgetPasswordNewConfirmContentTextInputEditText?.text.toString()
        ) {
            showPrivateDialog(
                "密碼不一致！",
                binding?.forgetPasswordNewInputContentTextInputEditText
            )

            return
        }

        mainViewModel.forgetPassword(
            requireContext(),
            binding?.forgetPasswordPhoneContentTextInputEditText?.text.toString(),
            binding?.forgetPasswordNewInputContentTextInputEditText?.text.toString()
        )
    }

    private fun showPrivateDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = "提醒！"

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.forgetPasswordPhoneContentTextInputEditText,
                binding?.forgetPasswordNewInputContentTextInputEditText -> {
                    return@setPositiveButton
                }

                null -> {
                    findNavController().navigate(R.id.action_to_login)
                }
            }
        }

        alert.show()
    }

    companion object {

    }
}