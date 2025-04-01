package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
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
    var verifyStatus = "0"

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
                verifyStatus = result.firstOrNull()?.verifyStatus ?: "0"
                Log.d("micCheckAQ", result.toString())
                updateViewData(result)
            }
        }

        mainViewModel.memberInfoEditData.observe(viewLifecycleOwner) { result ->
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
            Log.d("micCheckAQ3", mPlateNo)
            mainViewModel.editMemberInfo(
                requireContext(),
                memberDataNameContentEditText?.text.toString(),
                memberDataEmailContentEditText?.text.toString(),
                memberDataPhoneContentEditText?.text.toString(),
                mPlateNo,
                AppUtility.getLoginPassword(requireContext())!!,
                vehicleEditText.text.toString(),
                verifyStatus
            )
        }
    }

    private fun updateViewData(result: List<MemberInfoVO>) {
        val member = result.firstOrNull() ?: run {
            Log.e("micCheckZ", "Result list is empty or null")
            return
        }

        binding?.apply {
            memberDataNameContentEditText.setText(member.memberName ?: "")
            memberDataPhoneContentEditText.setText(member.memberId ?: "")

            member.memberPlate?.let {
                val parts = it.split("-")
                if (parts.size == 2) {
                    plateTextEditText.setText(parts[0])
                    plateNumberEditText.setText(parts[1])
                } else {
                    Log.w("micCheckZ", "Invalid plate format: $it")
                }
            }

            memberDataEmailContentEditText.setText(member.memberEmail ?: "")
            Log.d("micCheckZ", member.memberCarrier ?: "carrier is null")
            vehicleEditText.setText(member.memberCarrier ?: "")
        }
    }

    private fun updateViewEditData(result: MemberInfoEditResponse) {
        if (result.code == ApiConfig.API_CODE_SUCCESS) {
            AppUtility.updateLoginName(
                requireContext(),
                binding?.memberDataNameContentEditText?.text.toString()
            )
Log.d("micCheckZX", "1")
            showPrivateDialog(
//                result.responseMessage,
                "修改成功",
                null
            )
        } else {
            Log.d("micCheckZX", "2")
            showPrivateDialog(
//                result.responseMessage,
                "姓名為必填",
                "")
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
                    memberDataNameContentEditText.isEnabled = true
                    memberDataEmailContentEditText.isEnabled = true
//                    memberDataPhoneContentTextInputEditText.isEnabled = true
                    plateTextEditText.isEnabled = true
                    plateNumberEditText.isEnabled = true
                }

                false -> {
                    memberDataNameContentEditText.isEnabled =false
                    memberDataEmailContentEditText.isEnabled =false
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