package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentLoginBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.LoginResponse
import com.jotangi.nantouparking.model.MemberInfoVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
        setupLoginTitle()
        initObserver()
        initAction()
    }

    private fun initObserver() {
//        mainViewModel.memberInfoData.observe(viewLifecycleOwner) { result ->
//            if (result.isNotEmpty()) {
//                updateMemberInfo(result)
//            }
//        }

        mainViewModel.loginData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                loginSucceed(result)
            }
        }
    }

    private fun updateMemberInfo(result: List<MemberInfoVO>) {
//        if (result.code == ApiConfig.API_CODE_SUCCESS.toString()) {
        AppUtility.updateLoginId(
            requireContext(),
            binding?.loginIdEditText?.text.toString()
        )

        AppUtility.updateLoginName(
            requireContext(),
            result[0].memberName!!
        )

        AppUtility.updateLoginPassword(
            requireContext(),
            binding?.loginPasswordEditText?.text.toString()
        )
//        }
    }

    private fun loginSucceed(result: LoginResponse) {
        Log.d("micCheckMB", result.toString())
        if (result.code == ApiConfig.API_CODE_SUCCESS) {
            AppUtility.updateLoginStatus(
                requireContext(),
                true
            )

            AppUtility.updateLoginId(
                requireContext(),
                binding?.loginIdEditText?.text.toString()
            )

            AppUtility.updateLoginPassword(
                requireContext(),
                binding?.loginPasswordEditText?.text.toString()
            )

            showPrivateDialog(
//                result.responseMessage,
                "登入成功！",
                null
            )
            binding?.apply {
                Log.d("micCheckLL", loginIdEditText.text.toString())
                Log.d("micCheckLL", loginPasswordEditText.text.toString())
                if(loginIdEditText.text.toString().equals("1229G1") && loginPasswordEditText.text.toString().equals("1229G1")) {
                    Log.d("micCheckLL", "1")
                    findNavController().navigate(R.id.action_to_store_manager2_fragment)
                } else {
                    Log.d("micCheckLL", "2")
                    findNavController().navigate(R.id.action_to_member_main)
                }
            }

        } else {
            AppUtility.showPopDialog(
                requireContext(),
                result.code,
                result.responseMessage
            )
        }
    }

    private fun initAction() {
        binding?.apply {
            loginButton.setOnClickListener {
                login()
            }

            signupButton.setOnClickListener {
                findNavController().navigate(R.id.action_to_signup)
            }

            forgetPasswordTextView.setOnClickListener {
                findNavController().navigate(R.id.action_to_forget_password)
            }
        }
    }

    private fun login() {
        if (binding?.loginIdEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "帳號為必填！",
                binding?.loginIdEditText
            )

            return
        }

        if (binding?.loginPasswordEditText?.text.isNullOrEmpty()) {
            showPrivateDialog(
                "密碼為必填！",
                binding?.loginPasswordEditText
            )

            return
        }

        mainViewModel.login(
            requireContext(),
            binding?.loginIdEditText?.text.toString(),
            binding?.loginPasswordEditText?.text.toString()
        )
    }

    private fun showPrivateDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = if (curUI == null) {
            "恭喜您"
        } else {
            "提醒！"
        }

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.loginIdEditText,
                binding?.loginPasswordEditText -> {
                    return@setPositiveButton
                }

                null -> {
                    findNavController().navigate(R.id.action_to_main)
                }
            }
        }

        alert.show()
    }

    companion object {

    }
}