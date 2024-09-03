package com.jotangi.nantouparking.ui.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentStoreManagerBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.LogoutResponse
import com.jotangi.nantouparking.model.WriteOffTicketResponse
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class StoreManagerFragment : BaseFragment() {
    private var _binding: FragmentStoreManagerBinding? = null
    private val binding get() = _binding
    //    private val REDULT_SUCCESS = 12

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStoreManagerBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onResume() {
        super.onResume()

        var isLoading = AppUtility.getLoadingStatus(requireContext())
        if (
            !AppUtility.getWriteOffCouponNo(requireContext()).isNullOrEmpty() &&
            isLoading == false
        ) {
            AppUtility.updateLoadingStatus(
                requireContext(),true
            )
            mainViewModel.reimburseCoupon(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupStoreMangerTitle()
        initBackListener()
        initObserver()
        initData()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.logoutData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                updateMemberInfo(result)
                mainViewModel.clearData()
            }
        }

        mainViewModel.applyCouponData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                AppUtility.updateLoadingStatus(
                    requireContext(),
                    false
                )
                updateCouponData(result)
                mainViewModel.clearApplyCouponData()
            }
        }
    }

    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPressed()
        }
    }

    private fun initData() {
        mainViewModel.getMemberInfo(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            null
        )
    }

    private fun initAction() {
        binding?.apply {
            storeMangerScanButton.setOnClickListener {
                findNavController().navigate(R.id.action_to_scan)
            }

            storeMangerLogoutButton.setOnClickListener {
                mainViewModel.logout(requireContext())
            }
        }
    }

    private fun updateMemberInfo(result: LogoutResponse) {
        if (result.code == ApiConfig.API_CODE_SUCCESS) {
            AppUtility.updateLoginStatus(
                requireContext(),
                false
            )

            AppUtility.updateLoginId(
                requireContext(),
                ""
            )

            AppUtility.updateLoginPassword(
                requireContext(),
                ""
            )

            AppUtility.updateLoginName(
                requireContext(),
                ""
            )

            AppUtility.updateLoginType(
                requireContext(),
                ""
            )

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

    private fun updateCouponData(result: WriteOffTicketResponse) {
        AppUtility.updateWriteOffCouponNo(
            requireContext(),
            ""
        )

        if (!AppUtility.getLoginType(requireContext()).isNullOrEmpty()) {
            if (result.code == ApiConfig.API_CODE_SUCCESS) {
                AppUtility.showPopDialog(
                    requireContext(),
                    null,
                    "核銷成功"
                )
            } else {
                AppUtility.showPopDialog(
                    requireContext(),
                    result.code,
//                result.responseMessage
                    "核銷失敗"
                )
            }
        }
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
            findNavController().navigate(R.id.action_to_main)
        }

        alert.show()
    }

//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REDULT_SUCCESS) {
//            mainViewModel.reimburseCoupon(requireContext())
//        }
//    }

    companion object {

    }
}