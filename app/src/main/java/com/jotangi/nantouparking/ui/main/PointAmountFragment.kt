package com.jotangi.nantouparking.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        mainViewModel.memberInfo.observe(viewLifecycleOwner) { members ->
            if(members.isNullOrEmpty()) {
                binding?.tvCurrentPoints?.text = "目前點數：" + "未知" + " 點"
                binding?.noData?.visibility = View.VISIBLE
            } else {
                binding?.tvCurrentPoints?.text = "目前點數：" + members[0].member_totalpoints + " 點"
            }
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