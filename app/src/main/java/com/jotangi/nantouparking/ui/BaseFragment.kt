package com.jotangi.nantouparking.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.MainActivity
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.utility.AppUtility

abstract class BaseFragment : Fragment() {
    abstract fun getToolBar(): ToolbarIncludeBinding?
    var mActivity: MainActivity? = null
    lateinit var mainViewModel: MainViewModel

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
    }

    open fun onBackPressed() {
        val navigationController = findNavController()
        if (
            AppUtility.getLoginType(requireContext()).equals("1") ||
            AppUtility.getLoginType(requireContext()).equals("")
        ) {
            if (navigationController.currentDestination?.id == R.id.action_to_main) {
                requireActivity().finish()
            } else {
                mActivity?.onBackPressed()
            }
        } else {
            requireActivity().finish()
        }
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
                onBackPressed()
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
                onBackPressed()
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
}