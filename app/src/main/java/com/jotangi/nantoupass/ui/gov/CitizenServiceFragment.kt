package com.jotangi.nantoupass.ui.gov

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.jotangi.nantoupass.MainActivity
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.FragmentCitizenServiceBinding

class CitizenServiceFragment : Fragment() {
    private var _binding: FragmentCitizenServiceBinding? = null
    private val binding get() = _binding!!

    // Map of institutions and their respective units
    private val institutionMap = mapOf(
        "下拉選擇機關" to emptyArray(),
        "南投市公所" to arrayOf("下拉選擇單位", "農經課", "民政課", "財政課", "工務課", "社會課"),
        "圖書館" to arrayOf("下拉選擇單位", "兒童閱讀課", "成人閱讀課", "書籍管理課"),
        "幼兒園" to arrayOf("下拉選擇單位", "學前教育課", "行政管理課"),
        "清潔隊" to arrayOf("下拉選擇單位", "垃圾處理課", "環保課"),
        "市場管理所" to arrayOf("下拉選擇單位", "市場規劃課", "商戶管理課")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitizenServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()

        // Handle back button press
        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSpinners() {
        val institutions = institutionMap.keys.toList()

        // Create and set the adapter for institutions
        val institutionAdapter = createSpinnerAdapter(institutions)
        binding.spinnerInstitution.adapter = institutionAdapter

        // Handle institution selection
        binding.spinnerInstitution.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedInstitution = institutions[position]
                val units = institutionMap[selectedInstitution] ?: emptyArray()

                // Update unit spinner based on selected institution
                binding.spinnerUnit.adapter = createSpinnerAdapter(units.toList())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Initialize spinnerUnit with empty values
        binding.spinnerUnit.adapter = createSpinnerAdapter(emptyList())
    }

    private fun createSpinnerAdapter(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.spinnerText, items).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
