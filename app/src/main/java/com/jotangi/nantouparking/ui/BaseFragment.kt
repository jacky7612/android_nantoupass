package com.jotangi.nantouparking.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.MainActivity
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.charge.ChargeViewModel
import com.jotangi.nantouparking.utility.AppUtility

abstract class BaseFragment : Fragment() {
    abstract fun getToolBar(): ToolbarIncludeBinding?
    var mActivity: MainActivity? = null
    lateinit var mainViewModel: MainViewModel
    lateinit var chargeViewModel: ChargeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        try {
            mActivity = (activity as MainActivity)
        } catch (e: ClassCastException) {

        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        chargeViewModel = ViewModelProvider(requireActivity())[ChargeViewModel::class.java]
    }

    open fun onBackPressed() {
        val currentFragmentId = findNavController().currentDestination?.id

        var action_id: Int = -1
        when (currentFragmentId) {
//            R.id.parking_license_plate_fragment -> action_id =
//                R.id.action_parking_license_plate_fragment_to_main_fragment

            R.id.chargeEntryFragment -> action_id = R.id.action_chargeEntryFragment_to_main_fragment
            R.id.mapChargeParkingFragment -> action_id =
                R.id.action_mapChargeParkingFragment_to_main_fragment

            R.id.chargingFragment -> {
                chargeViewModel.clearPowerON()
                chargeViewModel.clearCheckData()
                chargeViewModel.clearQRcode()
                action_id = R.id.action_chargingFragment_to_main_fragment2
            }

            R.id.member_fragment -> {
                action_id = R.id.action_member_fragment_to_main_fragment
                mainViewModel.clearDeleteAccount()
            }

//            R.id.member_data_fragment -> action_id =
//                R.id.action_member_data_fragment_to_member_fragment

//            R.id.monthly_payment_plate_fragment -> action_id =
//                R.id.action_monthly_payment_plate_fragment_to_main_fragment

//            R.id.parking_history_unpaid_fragment -> action_id =
//                R.id.action_parking_history_unpaid_fragment_to_parking_license_plate_fragment

//            R.id.parking_license_plate_fragment -> {
//                if (Glob.Back2Home) action_id =
//                    R.id.action_parking_license_plate_fragment_to_main_fragment
//            }

            R.id.chargeHistoryDetailFragment -> {
                action_id = R.id.action_chargeHistoryDetailFragment_to_chargeEntryFragment
            }

            R.id.chargeStartFragment -> {
                chargeViewModel.clearPowerON()
                chargeViewModel.clearCheckData()
                chargeViewModel.clearQRcode()
                action_id = R.id.action_chargeStartFragment_to_chargeScanFragment
            }

            R.id.chargeScanFragment -> {
                chargeViewModel.clearPowerON()
                chargeViewModel.clearCheckData()
                chargeViewModel.clearQRcode()
                Glob.isScanPage =false
                action_id = R.id.action_chargeScanFragment_to_chargeEntryFragment
            }
//            R.id.member_data_fragment -> action_id =R.id.action_chargingFragment_to_main_fragment2
        }
        if (action_id != -1) {
            findNavController().navigate(action_id)
            return
        }
        if (currentFragmentId == R.id.action_to_main) {
            requireActivity().finish()
        } else {
            mActivity?.onBackPressed()
        }
        Glob.Back2Home = true
    }

    private fun setupToolBarBtn(
        imageButton: ImageButton?,
        res: Int?,
        onClick: () -> Unit
    ) {
        imageButton?.apply {

            res?.let {
                setImageResource(it)
            }

            setOnClickListener {
                onClick()
            }
        }
    }

    fun setupMarket2Title() {
        getToolBar()?.apply {
            toolTitleTextView.text = "活動專區"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupGuildelinesTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "漫步踩點"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMap2Title() {
        getToolBar()?.apply {
            toolTitleTextView.text = "停車資訊"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupLottoryWebViewTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "抽獎活動"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupCouponListTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "優惠卷"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }


    fun setupCurrentPointTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "點數紀錄"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMainTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.main_fragment_title)
            toolAdditionalImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolAdditionalImageButton,
                R.drawable.icon_account_2
            ) {
                when (AppUtility.getLoginStatus(requireContext())) {
                    true -> {
                        findNavController().navigate(R.id.action_to_member_main)
                    }

                    false -> {
                        findNavController().navigate(R.id.action_to_login)
                    }
                }
            }
        }
    }

    fun setupLottoryTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "抽獎專區"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupStoreManagerTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "店長專區"
            toolBackImageButton.visibility = View.GONE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupStoreManager2Title() {
        getToolBar()?.apply {
            toolTitleTextView.text = "點數核銷紀錄"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMemberTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.member_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupLoginTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.login_main_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMarketTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "活動專區"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMarketGetPointTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "獲取點數"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMarketChangePointTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "點數折抵"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMarketCurrentPointTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = "點數紀錄"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupSignupTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.signup_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupForgetPasswordTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.forget_password_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupParkingNoticeTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.parking_notice_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupCouponTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.coupon_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupQNATitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.q_n_a_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMemberDataTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.member_data_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMemberDataEditTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.member_data_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMemberPasswordUpdateTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.member_password_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupParkingHistoryTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.parking_history_title) + " ${Glob.queryHistoryPlateNo}"
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupLicensePlateHistoryTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.main_fragment_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
//                onBackPressed()
            }
        }
    }

    fun setupParkingHistoryUnPaidTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.main_fragment_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
//                findNavController().popBackStack()

            }
        }
    }

    fun setupParkingInfoTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.parking_info_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }

            toolbarConstraintLayout.setOnClickListener {

            }

            setupToolBarBtn(
                toolAdditionalImageButton,
                R.drawable.icon_account_2
            ) {
                when (AppUtility.getLoginStatus(requireContext())) {
                    true -> {
                        findNavController().navigate(R.id.action_to_member_main)
                    }

                    false -> {
                        findNavController().navigate(R.id.action_to_login)
                    }
                }
            }
        }
    }

    fun setupStoreMangerTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.store_manager_title)
            toolBackImageButton.visibility = View.GONE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupScanCouponTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.store_manger_scan_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupMapTourTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.map_tour_default_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }

    fun setupStoreIntroduceTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.store_introduce_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }


    fun showCustomDialog(context: Context, message: String, targetFragmentID: Int = -1, buttonText: String ="確定") {
        if (message.isNullOrEmpty()) return

        // Create a new Dialog
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.msgbox_custom)

        // Prevent closing the dialog by clicking outside
        dialog.setCancelable(false)

        // Set the message in the TextView
        val messageTextView: TextView = dialog.findViewById(R.id.tv_message)
        messageTextView.text = message

        // Set up the OK button
        val okButton: Button = dialog.findViewById(R.id.bt_ok)
        okButton.text =buttonText
        okButton.setOnClickListener {
            // Navigate to the desired fragment
            if (targetFragmentID > -1) findNavController().navigate(targetFragmentID)
            dialog.dismiss() // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }
    fun setToolBarTitle(input: String) {
        getToolBar()?.apply {
            toolTitleTextView.text = input
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }
    fun setToolbarTitle(input: String, back_visible :Boolean =true) {
        getToolBar()?.apply {
            toolTitleTextView.text = input
            toolBackImageButton.visibility = View.VISIBLE

            if (back_visible) {
                toolBackImageButton.visibility = View.VISIBLE
                setupToolBarBtn(
                    toolBackImageButton,
                    R.drawable.icon_back_36
                ) {
                    onBackPressed()
                }
            } else {
                toolBackImageButton.setImageDrawable(null) // Clear image
                toolBackImageButton.visibility = View.INVISIBLE
            }
        }
    }
    fun setupChargeNoticeTitle() {
        getToolBar()?.apply {
            toolTitleTextView.text = getString(R.string.charge_notice_title)
            toolBackImageButton.visibility = View.VISIBLE

            setupToolBarBtn(
                toolBackImageButton,
                R.drawable.icon_back_36
            ) {
                onBackPressed()
            }
        }
    }
}