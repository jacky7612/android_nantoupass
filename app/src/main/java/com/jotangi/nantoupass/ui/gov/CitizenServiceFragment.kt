package com.jotangi.nantoupass.ui.gov

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantoupass.MainActivity
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.Response4PassAgencyContent
import com.jotangi.nantoupass.config.Response4PassAgencyUnitContent
import com.jotangi.nantoupass.databinding.FragmentCitizenServiceBinding
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.ui.BaseFragment
import com.jotangi.nantoupass.ui.gov.adapter.AgencyDetailAdapter

class CitizenServiceFragment : BaseFragment() {
    private var _binding: FragmentCitizenServiceBinding? = null
    private val binding get() = _binding!!
    private var data_agency : List<Response4PassAgencyContent>? = null
    private var data_agencyunit : List<Response4PassAgencyUnitContent>? = null
    private lateinit var agencyDetailAdapter: AgencyDetailAdapter

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

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

        initDetailAdapter()

        initObserver()
        init_data()
        setupCitizenTitle("市民服務")
    }

    private fun setupSpinners(datas : List<Response4PassAgencyContent>?) {
        var institutions: MutableList<String> = (datas?.mapNotNull { it.name } ?: emptyList()).toMutableList()

        // 在第一個位置插入提示文字
        institutions.add(0, "下拉選擇機關")

        // Create and set the adapter for institutions
        val institutionAdapter = createSpinnerAdapter(institutions)
        binding.spinnerInstitution.adapter = institutionAdapter

        // Handle institution selection
        binding.spinnerInstitution.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (data_agency != null) {
                    binding.tvInstitution.text = if (position > 0) institutions[position] else ""
                    val angency_sid = if (position - 1 >= 0) data_agency!![position - 1].sid else ""
                    if (!angency_sid.isNullOrEmpty()) {
                        passViewModel.getAgencyunit(requireContext(), angency_sid)
                    } else {
                        clearSpinnersUnit()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSpinnersUnits(datas : List<Response4PassAgencyUnitContent>?) {
        var units: MutableList<String> = (datas?.mapNotNull { it.name } ?: emptyList()).toMutableList()

        // 在第一個位置插入提示文字
        units.add(0, "下拉選擇單位")
        binding.spinnerUnit.adapter = createSpinnerAdapter(units.toList())

        // Handle institution selection
        binding.spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvUnit.text = if (position > 0) units[position] else ""
                // 如果選到第一個提示文字，就清空 RecyclerView
                if (position == 0) {
                    agencyDetailAdapter.updateData(emptyList())
                } else {
                    val selectedUnit = datas?.get(position - 1)
                    agencyDetailAdapter.updateData(selectedUnit?.services ?: emptyList())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

    /****************
     * api response *
     ****************/
    private fun initObserver() {
        passViewModel.agency.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true" && result.code == "0x0200") {
                    data_agency = result.data?.data
                    setupSpinners(data_agency)
                    clearSpinnersUnit()
                }
            } else {
            }
        }
        passViewModel.agencyunit.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true" && result.code == "0x0200") {
                    data_agencyunit = result.data?.data
                    setupSpinnersUnits(data_agencyunit)
                }
            } else {
            }
        }
    }

    /***************
     * 清除科室單位  *
     ***************/
    private fun clearSpinnersUnit() {
        passViewModel.clearAgencyUnit()
        data_agencyunit = emptyList()
        setupSpinnersUnits(data_agencyunit)
    }
    private fun initDetailAdapter() {
        // 初始化 RecyclerView
        agencyDetailAdapter = AgencyDetailAdapter(emptyList())
        binding.rvResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResult.adapter = agencyDetailAdapter
    }

    /***************
     * 載入清單資料  *
     ***************/
    private fun init_data() {
        passViewModel.getAgency(requireContext())
    }
}
