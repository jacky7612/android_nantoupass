package com.jotangi. nantouparking.ui.charge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.JackyVariant.ConvertText
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiChargeConfig
import com.jotangi.nantouparking.databinding.FragmentChargingBinding
import com.jotangi.nantouparking.databinding.ToolbarFeetBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseWithBottomBarFragment
import com.jotangi.nantouparking.utility.AppUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
class ChargingFragment : BaseWithBottomBarFragment() {
    private var _binding: FragmentChargingBinding? = null
    private val binding get() = _binding

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    // TODO: Rename and change types of parameters
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet

    private var myThread: Thread?   =null
    private val MAX_SEC             =30
    private var iIdx                =0
    private var paused              =false
    private var running             =true

    private var HadOrder                    =false
    private var isInitThread4Stop           =false
    private var myThread4Stop   : Thread?   =null
    private var myDialog4Stop   : Dialog?   =null
    private val MAX_SEC4Stop                =10
    private var iIdx4Stop                   =0
    private val MAX_DOT4Stop                =5
    private var iRetry4Stop                 =0
    private val MAX_RETRY4Stop              =6
    private var paused4Stop                 =false
    private var running4Stop                =true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myDialog4Stop = Dialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargingBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle("充電服務", false)
        paused              =false
        HadOrder            =false
        running             =true
        isInitThread4Stop   =false
        paused4Stop         =false
        running4Stop        =true
        chargeViewModel.clearCheckData()
        chargeViewModel.clearHistoryData()
        chargeViewModel.clearChargingData()
        initObserver()
//        init()
        initEvent()

        triggerGetChargingData()
        binding?.apply {
            btStop.setOnClickListener {
                showCustomYesNoDialog(requireContext(), "即將停止充電\n並跳轉至繳費頁面", -1)
            }
        }

        // 顯示充電或停車位置資訊
        // Create a new thread
        myThread = Thread {
            while (running) {
                if (!paused) {
                    Thread.sleep(1000) // Sleep for 1 seconds

                    if (++iIdx >= MAX_SEC) {
                        iIdx = 0
                        // Code to run in the background
                        triggerGetChargingData()
                    }

                    activity?.runOnUiThread {
                        binding?.apply {
                            tvUpdateSec.text = "[${(MAX_SEC - iIdx)}]"
                        }
                    }
                }
            }
        }

        // Start the thread
        myThread?.start()


        // Create a new thread
        var s ="."
        myThread4Stop = Thread {
            while (running4Stop) {
                if (!paused4Stop) {
                    Thread.sleep(1000) // Sleep for 1 seconds

                    if (++iIdx4Stop >= MAX_SEC4Stop) {
                        iIdx4Stop = 0
                        iRetry4Stop++
                        // Code to run in the background
                        triggerCheckStatus()
                    }

                    activity?.runOnUiThread {
                        binding?.apply {
                            if (s.length < MAX_DOT4Stop) {
                                s +="."
                            } else {
                                s ="."
                            }
                            if (iRetry4Stop >= MAX_RETRY4Stop) {
                                iRetry4Stop =0
                                paused4Stop =true
                                myDialog4Stop?.dismiss()
                                showCustomDialog(requireContext(), "操作逾時，請重新啟動")
                            } else {
                                if (mMessageTextView != null) {
                                    mMessageTextView?.text = "停止充電中 $s"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        paused =false
        paused4Stop =false
    }

    override fun onPause() {
        super.onPause()
        paused =true
        paused4Stop =true
    }

    override fun onDestroyView() {
        super.onDestroyView()

        stopThread()
        stopThread4Stop()
        _binding =null
    }
    // =============================================================================================
    private fun initObserver() {
        chargeViewModel.chargePowerOFF.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.code == "0x0206") {
                    triggerCheckStatus()

                    if (mMessageTextView == null) {
                        showHintDialog("停止充電中")
                    } else {
                        // Show the dialog
                        myDialog4Stop?.show()
                    }

                    if (!isInitThread4Stop) {
                        // Start the thread
                        isInitThread4Stop =true
                        myThread4Stop?.start()
                    } else {
                        paused4Stop =false
                    }
                } else {
                    if (Glob.SkipPowerOFFRealResponse) {
                        triggerCheckStatus()

                        if (mMessageTextView == null) {
                            showHintDialog("停止充電中")
                        } else {
                            // Show the dialog
                            myDialog4Stop?.show()
                        }

                        if (!isInitThread4Stop) {
                            // Start the thread
                            isInitThread4Stop =true
                            myThread4Stop?.start()
                        } else {
                            paused4Stop =false
                        }
                    } else {
                        showCustomDialog(requireContext(), "${result.responseMessage}\n停止充電失敗\n請重試", -1)
                    }
                }
            }
        }
        chargeViewModel.chargeHistory.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    if (!HadOrder) {
                        HadOrder =true
                        val ret_data = result.data!!
                        Glob.curChargeInfo?.start_time  = ret_data[0].start_time
                        Glob.curChargeInfo?.stop_time   = ret_data[0].stop_time
                        Glob.curChargeInfo?.charge_time = ret_data[0].charge_time
                        Glob.curChargeInfo?.amount      = ret_data[0].price
                        Glob.curChargeInfo?.bill_no     = ret_data[0].order_id
                        Glob.curChargeInfo?.description = ret_data[0].station_name + " " + ret_data[0].charge_name

                        showPayMethodDialogView()

                        findNavController().navigate(
                            R.id.action_chargingFragment_to_main_fragment2
                        )
                    }
                } else {
                    showCustomDialog(requireContext(), result.responseMessage, -1)
                }
            }
        }

        chargeViewModel.chargingData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    binding?.apply {
                        tvChargeTime.text   =result.data[0].charge_time
                        tvDeviceId.text     ="充電設備序號 :${result.data[0].charge_point_id} kWh"
                        tvKwh.text          =result.data[0].kwh_total
                        tvStationName.text  =result.data[0].station_name
                        tvStatus.text       ="充電中"
                        tvTime.text         =ConvertText.getFormattedDate("", "yyyy-MM-dd HH:mm:ss")

                        flashTextViewBackground(tvTime, tvUpdateSec)
                        chargeViewModel.clearChargingData()
                    }
                } else {
                    updateNoData()
//                    showCustomDialog(requireContext(), result.responseMessage, -1)
                }
            } else {
            }
        }
        chargeViewModel.chargeCheck.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true") {
                    myDialog4Stop?.dismiss()

                    triggerGetOrderData()
                } else {
                    if (result.code.isNullOrEmpty()) {

                    } else {
                        when (result.code) {
                            "0x0201" -> { // 目前正在充電中
//                            findNavController().navigate(R.id.action_chargeStartFragment_to_chargingFragment)
                            }

                            "0x0202" -> { // 有未繳費充電帳單
                                if (mMessageTextView == null) {
                                    showHintDialog("停止充電中")
                                }
                                myDialog4Stop?.dismiss()

                                triggerGetOrderData()
//                            val msg ="尚有充電未繳款項目\n請先完成繳款\n方能使用充電服務"
//                            showCustomDialog(requireContext(), msg, R.id.action_chargeNoticeFragment2_to_chargeHistoryDetailFragment, "前往繳費")
                            }

                            else -> {
                                if (mMessageTextView == null) {
                                    showHintDialog("停止充電中")
                                }
                                myDialog4Stop?.dismiss()
//                            showCustomDialog(requireContext(), result.responseMessage, R.id.action_chargeNoticeFragment2_to_main_fragment)
                            }
                        }
                    }
                }
            }
        }
    }
    private fun updateNoData() {
        binding?.apply {
            tvChargeTime.text   ="準備中"
            tvDeviceId.text     ="充電設備序號 :準備中"
            tvKwh.text          ="? kWh"
            tvStationName.text  ="準備中"
            tvStatus.text       ="連線中"
            tvTime.text         =ConvertText.getFormattedDate("", "yyyy-MM-dd HH:mm:ss")

            flashTextViewBackground(tvTime, tvUpdateSec)
            chargeViewModel.clearChargingData()
        }
    }
    private fun triggerPowerOFF() {
        var control ="0"
        paused =true
        chargeViewModel.clearPowerOFF()
        chargeViewModel.setPowerOFF(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            "",
            "1",
            control
        )
    }
    private fun triggerGetOrderData() {
        chargeViewModel.clearHistoryData()
        chargeViewModel.getHistory(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            ConvertText.getFormattedDate("") + " 00:00:00",
            ConvertText.getFormattedDate("") + " 23:59:59",
            "false",
            "9999",
            "0"
        )
    }
    private fun triggerGetChargingData() {
        chargeViewModel.clearChargingData()
        if (chargeViewModel.chargingData.value != null)
            chargeViewModel.chargingData.value!!.clear()
        chargeViewModel.getChargingData(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!
        )
    }

    private fun triggerCheckStatus() {
        chargeViewModel.clearCheckData()
        chargeViewModel.checkCharge(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!)
    }
    private fun showPayMethodDialogView() {
        try {
            val dstUrl =getAppUrl()
            launchUri(dstUrl)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun getAppUrl(): String {
        val appUrl =ApiChargeConfig.PAYMENT_URL +
                    "?" +
                    "member_id=${AppUtility.getLoginId(requireContext())}" +
                    "&" +
                    "amount=${Glob.curChargeInfo!!.amount}" +
                    "&" +
                    "bill_no=${Glob.curChargeInfo!!.bill_no}" +
                    "&" +
                    "description=${Glob.curChargeInfo!!.description}" +
                    "&" +
                    "start_time=${Glob.curChargeInfo!!.start_time}" +
                    "&" +
                    "stop_time=${Glob.curChargeInfo!!.stop_time}" +
                    "&" +
                    "charge_time=${Glob.curChargeInfo!!.charge_time}"
        return appUrl
    }

    private fun launchUri(uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )

        startActivity(intent)
    }

    private fun flashTextViewBackground(tv1 : TextView, tv2 : TextView) {
        // 閃爍效果
        CoroutineScope(Dispatchers.Main).launch {
            repeat(1) { // 閃爍次數
                tv1.setTextColor(Color.WHITE) // 閃爍顏色
                tv2.setTextColor(Color.WHITE) // 閃爍顏色
                delay(500) // 等待 500 毫秒
                tv1.setTextColor(Color.GRAY) // 恢復透明
                tv2.setTextColor(Color.GRAY) // 恢復透明
                delay(500) // 等待 500 毫秒
            }
        }
    }
    private fun stopThread4Stop() {
        running4Stop = false // Set running to false to stop the loop
        myThread4Stop?.join() // Optionally wait for the thread to finish
        myThread4Stop = null // Clean up the reference
    }

    private fun stopThread() {
        running = false // Set running to false to stop the loop
        myThread?.join() // Optionally wait for the thread to finish
        myThread = null // Clean up the reference
    }

    private fun showMyDialog(context: Context, message: String, targetFragmentID: Int = -1, buttonText: String ="確定") {
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
            // Navigate to the desired fragment

            stopThread()
            paused =true
            if (Glob.GetOrderSampleResponse) {
                triggerGetOrderData()
            } else {
                if (!Glob.ChargeStartSkipWait) {
                    if (mMessageTextView == null) {
                        showHintDialog("停止充電中")
                    }
                }
                triggerPowerOFF()
            }

            dialog.dismiss() // Close the dialog
        }
        // Set up the OK button
        val btCancel: Button = dialog.findViewById(R.id.bt_cancel)
//        btCancel.text =buttonText
        btCancel.setOnClickListener {
            // Navigate to the desired fragmenttriggerGetData()
            dialog.dismiss() // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }

    private var mMessageTextView:TextView? =null
    private fun showHintDialog(message: String) {
        if (message.isNullOrEmpty()) return

        // Create a new Dialog
        myDialog4Stop?.setContentView(R.layout.hintbox_custom)

        // Prevent closing the dialog by clicking outside
        myDialog4Stop?.setCancelable(false)

        // Set the message in the TextView
        mMessageTextView = myDialog4Stop!!.findViewById(R.id.tv_message)
        mMessageTextView?.text = message

        // Show the dialog
        myDialog4Stop?.show()
    }
}