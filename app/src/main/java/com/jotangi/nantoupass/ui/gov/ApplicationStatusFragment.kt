package com.jotangi.nantoupass.ui.gov

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.jotangi.nantoupass.databinding.FragmentApplicationStatusBinding
import com.jotangi.nantoupass.databinding.ItemApplicationBinding
import com.jotangi.nantoupass.ui.gov.ApplicationItem
import com.google.android.material.tabs.TabLayout

class ApplicationStatusFragment : Fragment() {

    private lateinit var binding: FragmentApplicationStatusBinding
    private lateinit var adapter: ApplicationAdapter

    private val allApplications = listOf(
        ApplicationItem("低收入戶申請", "2025.02.21"),
        ApplicationItem("生育補助申請", "2025.02.21")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplicationStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ApplicationAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
binding.back.setOnClickListener {
    requireActivity().onBackPressedDispatcher.onBackPressed()
}
        // Add tabs dynamically (Fixing TabItem issue)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("依日期"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("申請中"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("處理中"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("已通過"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("未通過"))

        // Initially display "申請中" (Pending)
        updateStatus(0)

        // Handle Tab Selection
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { updateStatus(it.position) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
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

