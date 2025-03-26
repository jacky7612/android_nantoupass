package com.jotangi.nantouparking.ui.charge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentChargeStartBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChargeStartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChargeStartFragment : BaseFragment() {
    private var _binding: FragmentChargeStartBinding? = null
    private val binding get() = _binding
    private var status_code:String = ""

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
//    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet

    private var myDialog: Dialog? =null
    private var myThread: Thread? =null
    private var isInitThread =false
    private var paused            =false
    private var running           =true
    private val MAX_SEC           =10
    private var iIdx              =0
    private val MAX_DOT           =5
    private var iRetry            =0
    private val MAX_RETRY         =6
    private var jumbNextPage =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDialog = Dialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargeStartBinding.inflate(inflater, container, false)
        return binding?.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle("開始充電")

        initObserver()

//        initEvent()
        // Create a new thread
        var s ="."
        myThread = Thread {
            while (running) {
                if (!paused) {
                    Thread.sleep(1000) // Sleep for 1 seconds

                    if (++iIdx >= MAX_SEC) {
                        iIdx = 0
                        iRetry++
                        // Code to run in the background
                        triggerCheckStatus()
                    }

                    activity?.runOnUiThread {
                        binding?.apply {
                            if (s.length < MAX_DOT) {
                                s +="."
                            } else {
                                s ="."
                            }
                            if (iRetry >= MAX_RETRY) {
                                iRetry =0
                                paused =true
                                myDialog?.dismiss()
                                showCustomDialog(requireContext(), "操作逾時，請重新啟動")
                            } else {
                                if (mMessageTextView != null) {
//                                    mMessageTextView?.text = "充電啟動中 $s"
                                    mMessageTextView?.text = "充電啟動中 [$status_code] $s"
                                }
                            }
                        }
                    }
                }
            }
        }


        binding?.apply {
            tvDeviceName.text =Glob.curChargeGunData!!.charge_name
            linearlayoutAreaChargeStart2.visibility =View.GONE
//            if (Glob.QRcodeAutoNext) {
//                linearlayoutAreaChargeStart2.visibility =View.VISIBLE
//                tvDeviceId.text = Glob.curChargeInfo!!.MaskQRCode
//            } else {
//                linearlayoutAreaChargeStart2.visibility =View.GONE
//            }
            btStart.setOnClickListener {
                btStart.isEnabled =false
                showCustomYesNoDialog(requireContext(), "按下確認後，充電槍即開始通電，並開始計算", -1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        stopThread()
        _binding = null
    }
    // =============================================================================================
    private fun stopThread() {
        running = false // Set running to false to stop the loop
        myThread?.join() // Optionally wait for the thread to finish
        myThread = null // Clean up the reference
    }
    private fun initObserver() {
        chargeViewModel.checkChargeStatus.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                } else {

                }
            }
        }
        chargeViewModel.chargePowerON.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.code == "0x0200" ||
                    result.code == "0x0206" ||
                    result.code == "0x020E") {
                    if (result.code == "0x0200" && result.data!!.ID != null) {
                        Glob.curChargeInfo!!.controlId = result.data!!.ID
                        if (!jumbNextPage) {
                            paused =true
                            jumbNextPage =true
                            findNavController().navigate(R.id.action_chargeStartFragment_to_chargingFragment)
                        }
                    }

                    if (Glob.ChargeStartDirect2NextPage) {
                        if (!jumbNextPage) {
                            paused =true
                            jumbNextPage =true
                            findNavController().navigate(R.id.action_chargeStartFragment_to_chargingFragment)
                        }
                    } else {

                        if (!jumbNextPage) {
                            triggerCheckStatus()

                            if (mMessageTextView == null) {
                                showHintDialog("充電啟動中")
                            }

                            if (!isInitThread) {
                                // Start the thread
                                isInitThread = true
                                myThread?.start()
                            } else {
                                paused = false
                            }
                        }
                    }
                } else {
                    showCustomDialog(requireContext(), result.responseMessage, -1)
                }
            }
            binding?.apply {
                btStart.isEnabled =true
            }
        }

        chargeViewModel.chargeCheck.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (!result.code.isNullOrEmpty()) this.status_code =result.code
                if (result.status == "true") {

                } else {
                    when (result.code) {
                        "0x0201" -> { // 目前正在充電中
                            myDialog?.dismiss()
                            findNavController().navigate(R.id.action_chargeStartFragment_to_chargingFragment)
                        }
                        "0x0202" -> { // 有未繳費充電帳單
//                            if (messageTextView == null) {
//                                showHintDialog("充電啟動中")
//                            }
//                            val msg ="尚有充電未繳款項目\n請先完成繳款\n方能使用充電服務"
//                            showCustomDialog(requireContext(), msg, R.id.action_chargeNoticeFragment2_to_chargeHistoryDetailFragment, "前往繳費")
                        }
                        else -> {
//                            if (messageTextView == null) {
//                                showHintDialog("充電啟動中")
//                            }
//                            showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeNoticeFragment2_to_main_fragment)
                        }
                    }
                }
            }
        }
    }
    private fun triggerGetData() {
        var control ="1"
        paused =false
        if (mMessageTextView != null) {
            // Show the dialog
            myDialog?.show()
        }
        chargeViewModel.clearPowerON()
        chargeViewModel.chargePowerON.value?.clear()
        chargeViewModel.setPowerON(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            Glob.curChargeInfo!!.gunDeviceId,
            Glob.curChargeInfo!!.gunNumber,
            control
        )
    }
    private fun triggerCheckStatus() {
//        chargeViewModel.clearCheckChargeStatus()
//
//        if (chargeViewModel.checkChargeStatus.value != null)
//            chargeViewModel.checkChargeStatus.value!!.clear()

//        chargeViewModel.curChargeStatus(
//            requireContext(),
//            AppUtility.getLoginId(requireContext())!!,
//            AppUtility.getLoginPassword(requireContext())!!,
//            Glob.curChargeInfo!!.controlId
//        )
        chargeViewModel.clearCheckData()
        chargeViewModel.checkCharge(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!)
    }


    private fun showCustomYesNoDialog(context: Context, message: String, targetFragmentID: Int = -1, buttonText: String ="確定") {
        if (message.isNullOrEmpty()) return

        // Create a new Dialog
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.msgbox_ok_cancel_custom)

        // Prevent closing the dialog by clicking outside
        dialog.setCancelable(false)

        // Set the message in the TextView
        val messageTextView: TextView = dialog.findViewById(R.id.tv_message)
        messageTextView.text = message

        // Set up the OK button
        val okButton: Button = dialog.findViewById(R.id.bt_ok)
        okButton.text =buttonText
        okButton.setOnClickListener {

            if (!Glob.ChargeStartSkipWait) {
                if (mMessageTextView == null) {
                    showHintDialog("充電啟動中")
                }
            }

            // Navigate to the desired fragment
            triggerGetData()
            dialog.dismiss() // Close the dialog
        }
        // Set up the OK button
        val btCancel: Button = dialog.findViewById(R.id.bt_cancel)
//        btCancel.text =buttonText
        btCancel.setOnClickListener {
            // Navigate to the desired fragmenttriggerGetData()
            binding?.btStart?.isEnabled =true
            dialog.dismiss() // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }

    private var mMessageTextView:TextView? =null
    private fun showHintDialog(message: String) {
        if (message.isNullOrEmpty()) return

        // Create a new Dialog
        myDialog?.setContentView(R.layout.hintbox_custom)

        // Prevent closing the dialog by clicking outside
        myDialog?.setCancelable(false)

        // Set the message in the TextView
        mMessageTextView = myDialog!!.findViewById(R.id.tv_message)
        mMessageTextView?.text = message

        // Show the dialog
        myDialog?.show()
    }
}