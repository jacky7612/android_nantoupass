package com.jotangi.nantoupass.ui.gov

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.jotangi.nantoupass.databinding.FragmentApplicationStatusBinding
import com.jotangi.nantoupass.databinding.ItemApplicationBinding
import com.jotangi.nantoupass.ui.gov.models.ApplicationItem
import com.google.android.material.tabs.TabLayout
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.Response4PassApplyDetailContent
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.ui.BaseWithBottomBarFragment

class ApplicationStatusFragment : BaseWithBottomBarFragment() {

    private var _binding: FragmentApplicationStatusBinding? = null
    private val binding get() = _binding

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet
    private var data_detail : List<Response4PassApplyDetailContent>? = null
    private lateinit var adapter: ApplicationAdapter

    private val allApplications = listOf(
        ApplicationItem("低收入戶申請", "2025.02.21"),
        ApplicationItem("生育補助申請", "2025.02.21")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        _binding = FragmentApplicationStatusBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupApplyTitle()
        initObserver()
        initEvent()
        getData()

        adapter = ApplicationAdapter()
        binding?.apply {
            rvData.layoutManager = LinearLayoutManager(requireContext())
            rvData.adapter = adapter
            // Add tabs dynamically (Fixing TabItem issue)
            tabLayout.addTab(tabLayout.newTab().setText("依日期"))
            tabLayout.addTab(tabLayout.newTab().setText("申請中"))
            tabLayout.addTab(tabLayout.newTab().setText("處理中"))
            tabLayout.addTab(tabLayout.newTab().setText("已通過"))
            tabLayout.addTab(tabLayout.newTab().setText("未通過"))

            // Initially display "申請中" (Pending)
            updateStatus(0)

            // Handle Tab Selection
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let { updateStatus(it.position) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun updateStatus(status: Int) {
        val newData = when (status) {
            0 -> allApplications // "申請中"
            1 -> emptyList() // "處理中" - No mock data
            2 -> emptyList() // "已通過" - No mock data
            3 -> emptyList() // "未通過" - No mock data
            else -> allApplications
        }
        adapter.submitList(newData)
    }
    /****************
     * api response *
     ****************/
    private fun initObserver() {
        passViewModel.ApplyDetail.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true" && result.code == "0x0200") {
                    data_detail = result.data?.data
                    val lstApplyDetail = data_detail?.map { it.title?: "" }  // 取 head_img
                    if (lstApplyDetail != null) updateRecycleView(lstApplyDetail)
                }
            } else {
            }
        }
    }

    private fun getData() {
        passViewModel.clearApplyDetail()
        passViewModel.getApplyDetail(requireContext(),Glob.ItemApply.sid)
    }
    private fun updateRecycleView(input:List<String>) {
        binding?.apply {
            rvData.layoutManager = GridLayoutManager(requireContext(), 2)
            rvData.addItemDecoration(SpaceItemDecoration(2)) // Reduce spacing to match the provided image
            rvData.adapter = CategoryAdapter(input) { category, position ->

//                findNavController().navigate(R.id.action_application_services_fragment_to_low_income_fragment)

                Toast.makeText(requireContext(), "Clicked: $category", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class ApplicationAdapter : ListAdapter<ApplicationItem, ApplicationAdapter.ApplicationViewHolder>(
    object : DiffUtil.ItemCallback<ApplicationItem>() {
        override fun areItemsTheSame(oldItem: ApplicationItem, newItem: ApplicationItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ApplicationItem, newItem: ApplicationItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val binding = ItemApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ApplicationViewHolder(private val binding: ItemApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ApplicationItem) {
            binding.tvTitle.text = item.name
            binding.tvDate.text = "申請日期 ${item.date}"

            binding.btnViewView.setOnClickListener {
                Toast.makeText(binding.root.context, "${item.name} PDF 檢視", Toast.LENGTH_SHORT).show()
            }
            binding.btnViewPdf.setOnClickListener {
                Toast.makeText(binding.root.context, "${item.name} PDF 檢視", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

