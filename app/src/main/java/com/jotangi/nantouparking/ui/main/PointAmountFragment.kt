package com.jotangi.nantouparking.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMarketBinding
import com.jotangi.nantouparking.databinding.FragmentMarketChangeBinding
import com.jotangi.nantouparking.databinding.FragmentPointAmountBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PointAmountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PointAmountFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
var call = false
    private var _binding: FragmentPointAmountBinding? = null
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPointAmountBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMarketChangePointTitle()
binding?.noData?.visibility = View.GONE
binding?.tvStoreName?.text = MarketChangeFragment.storeName
        binding?.confirm?.setOnClickListener {
            call = true
            mainViewModel.useUserPoints(
                memberId =  AppUtility.getLoginId(requireContext())!!,
                storeId = MarketChangeFragment.storeNumber,
                pointNum = binding?.etInputPoints?.text.toString(),
                productPrice = "0"
            )
        }
        mainViewModel.memberInfo.observe(viewLifecycleOwner) { members ->
            if(members.isNullOrEmpty()) {
                binding?.tvCurrentPoints?.text = "目前點數：" + "未知" + " 點"
                binding?.noData?.visibility = View.VISIBLE
            } else {
                binding?.tvCurrentPoints?.text = "目前點數：" + members[0].member_totalpoints + " 點"
            }
        }
        mainViewModel.usePointResponse.observe(viewLifecycleOwner) { response ->
            if(call) {
                if (response.status == "true") {
                    // Handle success
                    showChangeSuccessDialog(
                        MarketChangeFragment.storeName,
                        binding?.etInputPoints?.text.toString()
                    )
                } else {
                    Toast.makeText(requireContext(), response.responseMessage, Toast.LENGTH_LONG)
                        .show()
                    println("Error: ${response.responseMessage}")
                }
            }
            call = false
        }
        // Observe error response
        mainViewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            binding?.tvCurrentPoints?.text = "目前點數：" + "未知" + " 點"
            binding?.noData?.visibility = View.VISIBLE
            binding?.noData?.text = "後端錯誤, 無法得知點數"
            println("Error: ${error.responseMessage}")
        }

        // Fetch member info
        mainViewModel.fetchMemberInfo(AppUtility.getLoginId(requireContext())!!, AppUtility.getLoginPassword(requireContext())!!)
    }

    private fun showChangeSuccessDialog(storeName: String, pointsUsed: String) {
        // Inflate the custom layout
        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.dialog_change_success, null)

        // Find the TextView elements and set their text
        val tvMessage1 = dialogView.findViewById<TextView>(R.id.tvDuplicateMessage1)
        val tvMessage2 = dialogView.findViewById<TextView>(R.id.tvDuplicateMessage2)
        val tvMessage3 = dialogView.findViewById<TextView>(R.id.tvDuplicateMessage3)

        tvMessage1.text = "折抵成功" // Static text
        tvMessage2.text = storeName  // Dynamic store name
        tvMessage3.text = "折抵 $pointsUsed 點" // Dynamic points used

        // Initialize the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Find the confirm button and set a click listener
        val btnConfirm = dialogView.findViewById<TextView>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            view?.let { it1 -> hideKeyboard(requireContext(), it1) }
            onBackPressed()
            dialog.dismiss()  // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PointAmountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PointAmountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}