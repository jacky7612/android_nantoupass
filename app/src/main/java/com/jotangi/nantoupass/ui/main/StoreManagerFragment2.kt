package com.jotangi.nantoupass.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.ApiConfig
import com.jotangi.nantoupass.databinding.FragmentStoreManager2Binding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.LogoutResponse
import com.jotangi.nantoupass.ui.BaseFragment
import com.jotangi.nantoupass.utility.AppUtility

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreManagerFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreManagerFragment2 : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentStoreManager2Binding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
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
        _binding = FragmentStoreManager2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        mainViewModel.getMemberInfo2(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            null
        )
setupStoreManagerTitle()
        mainViewModel.logoutData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                updateMemberInfo(result)
                mainViewModel.clearData()
                Glob.clearMemberInfo(requireContext())
            }
        }

        binding?.apply {
            recordConfirm.setOnClickListener {
                findNavController().navigate(R.id.action_to_store_manager3)
            }

            logout.setOnClickListener {
                // Update login status
                AppUtility.updateLoginStatus(requireContext(), false)

                // Show the private dialog
                showPrivateDialog(
                    message = "登出成功！",
                    curUI = null,
                    skipChangePage = false
                )

                // Save preference
                saveStorePreference("Main")
            }

        }
    }

    private fun saveStorePreference(storeType:String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("key_store", storeType)
        editor.apply() // Apply changes asynchronously
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoreManagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreManagerFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
                findNavController().navigate(R.id.action_to_main)
            }
        }

        alert.show()
    }

    private fun initObserver() {
        mainViewModel.memberInfoData.observe(viewLifecycleOwner) { result ->

            if (!result.isNullOrEmpty()) {
                binding?.SOSACowBoy?.text = result[0].memberName
            } else {
                Log.d("micCheckZX2", "zx")

            }
        }
        }
}