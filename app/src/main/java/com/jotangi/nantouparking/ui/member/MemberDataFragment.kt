package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentMemberDataBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.MemberInfoEditResponse
import com.jotangi.nantouparking.model.MemberInfoVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class MemberDataFragment : BaseFragment() {
    private var _binding: FragmentMemberDataBinding? = null
    private val binding get() = _binding
    private var isEdit = false
    private var mPlateNo: String = ""

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMemberDataBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mainViewModel.clearMemberData()
        _binding = null
    }

    private fun init() {
        setupMemberDataTitle()
        initObserver()
        initEditInputType()
        initData()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.memberInfoData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                updateViewData(result)
            }
        }

        mainViewModel.memberInfoEditData.observe(viewLifecycleOwner) { result ->
//            updateEditStatus()
            if (result != null) {
                updateView()
                updateViewEditData(result)
            }
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
//            memberDataEditButton.setOnClickListener {
//                updateEditStatus()
//                updateView()
//            }

            memberDataCancelButton.setOnClickListener {
//                updateEditStatus()
                updateView()
                onBackPressed()
            }

            memberDataConfirmButton.setOnClickListener {
                editMemberData()
            }
        }
    }

    private fun initEditInputType() {
        binding?.apply {
            plateTextEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
            plateNumberEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        }
    }

    private fun editMemberData() {
        binding?.apply {
            mPlateNo =
                "${plateTextEditText.text}-${plateNumberEditText.text}"

            mainViewModel.editMemberInfo(
                requireContext(),
                binding?.memberDataNameContentTextInputEditText?.text.toString(),
                binding?.memberDataEmailContentTextInputEditText?.text.toString(),
                binding?.memberDataPhoneContentTextInputEditText?.text.toString(),
                mPlateNo,
                AppUtility.getLoginPassword(requireContext())!!
            )
        }

    }

    private fun updateViewData(result: List<MemberInfoVO>) {
        binding?.apply {
            memberDataNameContentTextInputEditText.setText(result[0].memberName)
            memberDataEmailContentTextInputEditText.setText(result[0].memberEmail)
            memberDataPhoneContentTextInputEditText.setText(result[0].memberId)
            val parts = result[0].memberPlate.split("-")

            if (parts.size > 1) {
                plateTextEditText.setText(parts[0])
                plateNumberEditText.setText(parts[1])
            }
        }
    }

    private fun updateViewEditData(result: MemberInfoEditResponse) {
        if (result.code == ApiConfig.API_CODE_SUCCESS) {
            AppUtility.updateLoginName(
                requireContext(),
                binding?.memberDataNameContentTextInputEditText?.text.toString()
            )

            showPrivateDialog(
//                result.responseMessage,
                "修改成功",
                null
            )
        }
    }

    private fun updateView() {
//        updateEditTextStatus()
//        updateButtonVisible()
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
        }

        alert.show()
    }

}