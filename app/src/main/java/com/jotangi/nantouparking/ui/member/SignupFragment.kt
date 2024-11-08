package com.jotangi.nantouparking.ui.member

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentSignupBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.CouponVO
import com.jotangi.nantouparking.model.SignupResponse
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.squareup.picasso.Picasso

class SignupFragment : BaseFragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
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
        setupSignupTitle()
        initObserver()
        initAction()
    }

    private fun initObserver() {
        mainViewModel.signupData.observe(viewLifecycleOwner) { result ->
            if (result != null)
                updateMemberInfo(result)
        }

        mainViewModel.exchangeableCouponData.observe(viewLifecycleOwner) { result ->
            if (result != null && result.isNotEmpty()) {
                AppUtility.updateWriteOffCouponNo(
                    requireContext(),
                    result[0].couponNo
                )
                showCustomDialog(result)
            }
//            } else {
//                AppUtility.showPopDialog(
//                    requireContext(),
//                    null,
//                    "資料異常~請洽客服！"
//                )
//            }
        }

    }

    private fun initAction() {
        binding?.apply {
            signupPlateTextEditText.addTextChangedListener(object : TextWatcher {
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
                            signupPlateTextEditText.setText(upperCaseText)
                            signupPlateTextEditText.setSelection(upperCaseText.length) // 將光標移到最後
                        }
                    }
                }
            })

            signupPlateNumberEditText.addTextChangedListener(object : TextWatcher {
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
                            signupPlateNumberEditText.setText(upperCaseText)
                            signupPlateNumberEditText.setSelection(upperCaseText.length) // 將光標移到最後
                        }
                    }
                }
            })

            sighupConfirmButton.setOnClickListener {
                signup()
            }

            signupCheckbox.setOnCheckedChangeListener { _, _ -> }
        }
    }

    private fun updateMemberInfo(result: SignupResponse) {
        if (result.code == ApiConfig.API_CODE_SUCCESS) {
            AppUtility.updateLoginStatus(
                requireContext(),
                true
            )

            AppUtility.updateLoginId(
                requireContext(),
                binding?.signupPhoneContentTextInputEditText?.text.toString()
            )

            AppUtility.updateLoginName(
                requireContext(),
                binding?.signupNameContentTextInputEditText?.text.toString()
            )

            AppUtility.updateLoginPassword(
                requireContext(),
                binding?.signupPasswordContentTextInputEditText?.text.toString()
            )



            show4SignupDialog(
                "註冊",
                "註冊成功",
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

    private fun showCustomDialog(result: List<CouponVO>) {
        if (!result[0].equals(null)) {
            val customDialog = Dialog(requireContext())

            customDialog.setContentView(R.layout.dialog_gift)
            customDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.setCanceledOnTouchOutside(true)

            val dialogContentTextView: TextView = customDialog.findViewById(R.id.dialog_gift_discount_content_textView)!!
            val dialogContentImageView: ImageView = customDialog.findViewById(R.id.dialog_gift_discount_content_imageView)!!
            val dialogDateTextView: TextView = customDialog.findViewById(R.id.dialog_gift_discount_endDate_textView)!!
            val dialogCloseButton = customDialog.findViewById<Button>(R.id.dialog_gift_close_button)
            val dialogEnterButton = customDialog.findViewById<Button>(R.id.dialog_gift_enter)

            dialogCloseButton.setOnClickListener {
                customDialog.dismiss()
                findNavController().navigate(R.id.action_to_main)
            }

            dialogEnterButton.setOnClickListener {
                customDialog.dismiss()
                findNavController().navigate(R.id.action_to_coupon)
            }

            if (!result[0].couponName.isNullOrEmpty()) {
                dialogContentTextView.text = result[0].couponName
            }

            if (!result[0].couponPicture.isNullOrEmpty()) {
                Picasso.with(requireActivity())
                    .load(ApiConfig.URL_HOST + result[0].couponPicture)
//                    .load(ApiConfig.TEST_URL_HOST + result[0].couponPicture)
                    .into(dialogContentImageView)
            }

            if (!result[0].couponEndDate.isNullOrEmpty()) {
                dialogDateTextView.text = "兌換期限：${result[0].couponEndDate}"
            }

            customDialog.show()
        }
    }

    private fun signup() {
        if (binding?.signupPhoneContentTextInputEditText?.text.toString().isNullOrEmpty()) {
            showPrivateDialog(
                "電話為必填！",
                binding?.signupPhoneContentTextInputEditText
            )

            return
        }

        if (binding?.signupPasswordContentTextInputEditText?.text.toString().isNullOrEmpty()) {
            showPrivateDialog(
                "密碼為必填！",
                binding?.signupPasswordContentTextInputEditText
            )

            return
        }

        if (!binding?.signupCheckbox?.isChecked!!) {
            showPrivateDialog(
                "請同意條款！",
                binding?.signupPasswordContentTextInputEditText
            )

            return
        }

        var PlateNO = ""
        if (!binding?.signupPlateTextEditText?.text.isNullOrEmpty() && !binding?.signupPlateNumberEditText?.text.isNullOrEmpty()) {
            PlateNO =
                "${binding?.signupPlateTextEditText?.text.toString()}-${binding?.signupPlateNumberEditText?.text.toString()}"
        }
        mainViewModel.signup(
            requireContext(),
            binding?.signupNameContentTextInputEditText?.text.toString(),
            binding?.signupEmailContentTextInputEditText?.text.toString(),
            binding?.signupPhoneContentTextInputEditText?.text.toString(),
            binding?.signupPasswordContentTextInputEditText?.text.toString(),
            PlateNO
        )
    }

    private fun show4SignupDialog(
        title: String,
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.signupPhoneContentTextInputEditText,
                binding?.signupPasswordContentTextInputEditText,
                binding?.signupCheckbox -> {
                    return@setPositiveButton
                }

                null -> {
                    getMyCoupon()
                }
            }
        }

        alert.show()
        findNavController().navigate(R.id.action_to_main)
    }
    private fun showPrivateDialog(
        message: String,
        curUI: Any?
    ) {
        val alert = AlertDialog.Builder(requireContext())
        val title = if (curUI == null) {
            "註冊"
        } else {
            "提醒！"
        }

        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("確定") { _, _ ->
            when (curUI) {
                binding?.signupPhoneContentTextInputEditText,
                binding?.signupPasswordContentTextInputEditText,
                binding?.signupCheckbox -> {
                    return@setPositiveButton
                }

                null -> {
                    getMyCoupon()
                }
            }
        }

        alert.show()
    }

    private fun getMyCoupon() {
        mainViewModel.getMyCoupon(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!
        )
    }

    companion object {

    }
}