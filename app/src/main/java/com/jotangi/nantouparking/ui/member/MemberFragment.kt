package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.zxing.client.android.BuildConfig
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentMemberBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.LogoutResponse
import com.jotangi.nantouparking.model.MemberInfoVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class MemberFragment : BaseFragment() {
    private var _binding: FragmentMemberBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
var call = false
    var id:String =""
    var pwd:String =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMemberBinding.inflate(inflater, container, false)
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
        setupMemberTitle()
        initObserver()
        initView()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.memberInfoData.observe(viewLifecycleOwner) { result ->

                if (!result.isNullOrEmpty()) {
                    Log.d("micCheckZX1", result[0].memberName)
                    updateMemberInfo4Login(result)
                } else {
                    Log.d("micCheckZX2", "zx")

                }
        }

        mainViewModel.logoutData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                updateMemberInfo(result)
                mainViewModel.clearData()
                Glob.clearMemberInfo(requireContext())
            }
        }

        mainViewModel.isDelete.observe(viewLifecycleOwner) { result ->
//            if (result != null) {
            if (result != null) {
                if (result) {
                    mainViewModel.clearDeleteAccount()
                    mainViewModel.clearData()
                    Glob.clearMemberInfo(requireContext())
                    findNavController().navigate(R.id.action_member_fragment_to_main_fragment)
                } else {

                    showPrivateDialog("刪除資料異常，請重試", null, true)
                }
            }
        }


    }

    private fun initView() {
        binding?.apply {
            id =AppUtility.getLoginId(requireContext())!!
            pwd =AppUtility.getLoginPassword(requireContext())!!
call = true
            mainViewModel.getMemberInfo(
                requireContext(),
                id,
                pwd,
                null
            )

            memberIdTextView.text = AppUtility.getLoginId(requireContext())
            memberNameTextView.text = AppUtility.getLoginName(requireContext())
            memberAppVersionTextView.text = getString(R.string.member_app_version_title) +
                    " ${BuildConfig.VERSION_NAME}"
        }
    }

    private fun initAction() {
        binding?.apply {
            memberDataConstraintLayout.setOnClickListener {
                findNavController().navigate(R.id.action_to_member_data)
            }

            memberPasswordConstraintLayout.setOnClickListener {
                findNavController().navigate(R.id.action_to_password_update)
            }

//            memberCouponConstraintLayout.setOnClickListener {
//                findNavController().navigate(R.id.action_to_coupon)
//            }

            memberFeeRecordConstraintLayout.setOnClickListener {
                findNavController().navigate(R.id.action_to_parking_history_paid)
            }

            memberParkingIntroConstraintLayout.setOnClickListener {
                findNavController().navigate(R.id.action_to_parking_notice)
            }

            memberQnaConstraintLayout.setOnClickListener {
                findNavController().navigate(R.id.action_to_q_n_a)
            }

            memberDeleteConstraintLayout.setOnClickListener {
                showDeleteDialog(
                    "將刪除帳號！是否確定？",
                    null
                )
            }

            memberLogoutButton.setOnClickListener {
                mainViewModel.logout(requireContext())
            }
        }
    }
    private fun updateMemberInfo4Login(result: List<MemberInfoVO>) {
//        if (result.code == ApiConfig.API_CODE_SUCCESS.toString()) {
        AppUtility.updateLoginId(
            requireContext(),
            id
        )
Log.d("micCheckAQ",result[0].memberName )
//        AppUtility.updateLoginName(
//            requireContext(),
//            result[0].memberName!!
//        )
        binding?.memberNameTextView?.text = result[0].memberName

        AppUtility.updateLoginPassword(
            requireContext(),
            pwd
        )
//        }
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

            AppUtility.updateWriteOffCouponNo(
                requireContext(),
                ""
            )

            showPrivateDialog(
//                result.responseMessage,
                "登出成功！",
                null,
                false
            )
        } else {
            AppUtility.showPopDialog(
                requireContext(),
                result.code,
                result.responseMessage
            )
        }
    }

    private fun showPrivateDialog(
        message: String,
        curUI: Any?,
        skipChangePage: Boolean
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = "提醒！"

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
//            findNavController().navigate(R.id.member_action_to_login)
            if (!skipChangePage) {
                findNavController().navigate(R.id.action_member_fragment_to_main_fragment)
            }
        }

        alert.show()
    }

    private fun showDeleteDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = "提醒！"

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            mainViewModel.deleteAccount(requireContext())
        }
        alert.setNegativeButton("取消") { _, _ -> }

        alert.show()
    }
}