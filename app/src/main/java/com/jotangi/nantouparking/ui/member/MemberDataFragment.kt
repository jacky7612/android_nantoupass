package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private var isEdit = false
    private var mPlateNo: String = ""

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

            plateTextEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Do nothing
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val upperCaseText = it.toString().uppercase()
                        if (it.toString() != upperCaseText) {
                            plateTextEditText.setText(upperCaseText)
                            plateTextEditText.setSelection(upperCaseText.length) // 將光標移到最後
                        }
                    }
                }
            })

            plateNumberEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Do nothing
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val upperCaseText = it.toString().uppercase()
                        if (it.toString() != upperCaseText) {
                            plateNumberEditText.setText(upperCaseText)
                            plateNumberEditText.setSelection(upperCaseText.length) // 將光標移到最後
                        }
                    }
                }
            })

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

    private fun editMemberData() {
        binding!!.apply {
            mPlateNo =
                "${plateTextEditText.text}-${plateNumberEditText.text}"

            mainViewModel.editMemberInfo(
                requireContext(),
                memberDataNameContentTextInputEditText?.text.toString(),
                memberDataEmailContentTextInputEditText?.text.toString(),
                memberDataPhoneContentTextInputEditText?.text.toString(),
                mPlateNo,
                AppUtility.getLoginPassword(requireContext())!!
            )
        }
    }

    private fun updateViewData(result: List<MemberInfoVO>) {
        if (result.size == 0) {

            return
        }
        binding?.apply {
            memberDataNameContentTextInputEditText.setText(result[0].memberName)
//            memberDataEmailContentTextInputEditText.setText(result[0].memberEmail)
            memberDataPhoneContentTextInputEditText.setText(result[0].memberId)
//            val parts = result[0].memberPlate!!.split("-")

//            if (parts.size > 1) {
//                plateTextEditText.setText(parts[0])
//                plateNumberEditText.setText(parts[1])
//            }
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

    private fun updateEditTextStatus() {
        binding?.apply {
            when (isEdit) {
                true -> {
                    memberDataNameContentTextInputEditText.isEnabled = true
                    memberDataEmailContentTextInputEditText.isEnabled = true
//                    memberDataPhoneContentTextInputEditText.isEnabled = true
                    plateTextEditText.isEnabled = true
                    plateNumberEditText.isEnabled = true
                }

                false -> {
                    memberDataNameContentTextInputEditText.isEnabled =false
                    memberDataEmailContentTextInputEditText.isEnabled =false
//                    memberDataPhoneContentTextInputEditText.isEnabled = false
                    plateTextEditText.isEnabled =false
                    plateNumberEditText.isEnabled =false
                }
            }
        }
    }

    private fun updateButtonVisible() {
        binding?.apply {
            when (isEdit) {
                true -> {
//                    memberDataEditButton.visibility = View.GONE
                    memberDataCancelButton.visibility = View.VISIBLE
                    memberDataConfirmButton.visibility = View.VISIBLE
                }

                false -> {
//                    memberDataEditButton.visibility = View.VISIBLE
                    memberDataCancelButton.visibility = View.GONE
                    memberDataConfirmButton.visibility = View.GONE
                }
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
            onBackPressed()
        }

        alert.show()
    }

}