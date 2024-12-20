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
import com.jotangi.nantouparking.model.PointRecordResponse
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupStoreManager2Title()
        setupRecyclerView()
        observeViewModel()

        val buttons = listOf(binding!!.btnOneDay, binding!!.btnSevenDays, binding!!.btnOneMonth)

        // Set the initial selection
        binding!!.btnOneDay.isSelected = true
binding!!.noData.visibility = View.GONE
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

                when (button.id) {
                    R.id.btn_one_day -> {
                        val currentDate = getCurrentDate()
                        fetchStorePoints(currentDate, currentDate) // Fetch points for the same day
                    }

                    R.id.btn_seven_days -> {
                        val currentDate = getCurrentDate()
                        val sevenDaysAgo = getDateBeforeDays(7)
                        fetchStorePoints(
                            sevenDaysAgo,
                            currentDate
                        ) // Fetch points for the last 7 days
                    }

                    R.id.btn_one_month -> {
                        val currentDate = getCurrentDate()
                        val oneMonthAgo = getDateBeforeDays(30)
                        fetchStorePoints(
                            oneMonthAgo,
                            currentDate
                        ) // Fetch points for the last 30 days
                    }
                }
            }
        }
    }


    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getDateBeforeDays(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.adapter = MemberRecordAdapter(emptyList())
    }

    private fun observeViewModel() {
        mainViewModel.pointRecordsData.observe(viewLifecycleOwner) { pointRecords ->
            if (pointRecords.isNullOrEmpty()) {
                binding!!.noData.visibility = View.VISIBLE
            } else {
                binding!!.noData.visibility = View.GONE
                binding?.recyclerView?.adapter = MemberRecordAdapter(pointRecords ?: emptyList())
            }
        }
    }
    private fun fetchStorePoints(startDate: String, endDate: String) {
        mainViewModel.getStorePointRecords(
            requireContext(),
            AppUtility.getLoginId(requireContext())!!,
            AppUtility.getLoginPassword(requireContext())!!,
            startDate,
            endDate
        )
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