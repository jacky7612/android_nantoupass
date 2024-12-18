package com.jotangi.nantouparking.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentLoginBinding
import com.jotangi.nantouparking.databinding.FragmentStoreManager3Binding
import com.jotangi.nantouparking.databinding.FragmentStoreManagerBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreManager3Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreManager3Fragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentStoreManager3Binding? = null
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
        _binding = FragmentStoreManager3Binding.inflate(inflater, container, false)
        return binding?.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
setupStoreManager2Title()
        val btnOneDay: TextView = view.findViewById(R.id.btn_one_day)
        val btnSevenDays: TextView = view.findViewById(R.id.btn_seven_days)
        val btnOneMonth: TextView = view.findViewById(R.id.btn_one_month)
binding!!.btnOneDay.isSelected = true
        val buttons = listOf(btnOneDay, btnSevenDays, btnOneMonth)



        buttons.forEach { button ->
            button.setOnClickListener {
                // Reset all buttons
                buttons.forEach {
                    it.isSelected = false
                    it.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
                }

                // Set selected button style
                button.isSelected = true
                button.setTextColor(resources.getColor(android.R.color.black))
            }
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // Mock data
        val mockData = listOf(
            MemberRecord("2024-12-05 16:48:42", "0982958675", 120, 120),
            MemberRecord("2024-11-28 15:28:04", "0917890276", 120, 120),
            MemberRecord("2024-11-21 16:22:12", "0928417131", 120, 120)
        )

        // RecyclerView setup
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MemberRecordAdapter(mockData)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoreManager3Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreManager3Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}