package com.jotangi.nantouparking.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentCurrentPointBinding
import com.jotangi.nantouparking.databinding.FragmentMarketChangeBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentPointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentPointFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCurrentPointBinding? = null
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private val binding get() = _binding
    private lateinit var adapter: PointRecordAdapter

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
        _binding = FragmentCurrentPointBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCurrentPointTitle()
        val recyclerView = binding?.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
binding?.noData?.visibility = View.GONE
        // Observe LiveData from MainViewModel
        mainViewModel.pointRecords.observe(viewLifecycleOwner) { records ->
            if(records.isNullOrEmpty()) {
                binding?.noData?.visibility = View.GONE
            } else {
            adapter = PointRecordAdapter(records)
            recyclerView?.adapter = adapter
                }
        }
        // Fetch data
        val memberId =  AppUtility.getLoginId(requireContext())!!// Replace with actual value
        val memberPwd = AppUtility.getLoginPassword(requireContext())!! // Replace with actual value
        val pointType = "0" // Replace with desired point type
        mainViewModel.fetchPointRecords(memberId, memberPwd, pointType)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentPointFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CurrentPointFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}