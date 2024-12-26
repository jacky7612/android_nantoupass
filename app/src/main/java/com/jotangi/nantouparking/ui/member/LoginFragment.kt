package com.jotangi.nantouparking.ui.member

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentLoginBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.LoginResponse
import com.jotangi.nantouparking.model.MemberInfoVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
                Log.d("micCheckMNB", result.responseMessage)
                if(result.responseMessage.contains("Store")) {
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

    @SuppressLint("ClickableViewAccessibility")
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

            seePWD1.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Show the password when the finger touches the eye icon
                        loginPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Hide the password when the finger is lifted
                        loginPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                true
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

        lifecycleScope.launch {
            val fcmToken = fetchFcmToken() // Call the suspend function
            if (fcmToken != null) {
                mainViewModel.login(
                    requireContext(),
                    binding?.loginIdEditText?.text.toString(),
                    binding?.loginPasswordEditText?.text.toString(),
                    fcmToken
                )
                Log.d("micCheckFCM", fcmToken)
            } else {
                Log.w("FCM", "FCM token is null")
                showPrivateDialog("無法取得 FCM token！請稍後再試", null)
            }
        }
    }

    suspend fun fetchFcmToken(): String? = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
                continuation.resume(null) // Resume with null in case of failure
            } else {
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
                continuation.resume(token) // Resume with the token
            }
        }
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