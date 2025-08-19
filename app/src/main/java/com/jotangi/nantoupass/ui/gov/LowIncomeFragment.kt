package com.jotangi.nantoupass.ui.gov

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.databinding.FragmentLowIncomeBinding
import com.jotangi.nantoupass.databinding.ItemLowIncomeBinding
import com.jotangi.nantoupass.ui.gov.LowIncomeItem

class LowIncomeFragment : Fragment() {

    private lateinit var binding: FragmentLowIncomeBinding
    private lateinit var adapter: LowIncomeAdapter

    private val mockData = listOf(
        LowIncomeItem("中低收入戶申請表", "線上申辦", "下載", "2025.02.21", true),
        LowIncomeItem("自願放棄社福津貼切結書", "臨櫃辦理", "下載", "2025.02.21", false),
        LowIncomeItem("南投縣政府辦理社會救助審核作業規定", "線上申辦", "下載", "2025.02.21", true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLowIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        adapter = LowIncomeAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        adapter.submitList(mockData)
    }
}

class LowIncomeAdapter :
    ListAdapter<LowIncomeItem, LowIncomeAdapter.LowIncomeViewHolder>(
        object : DiffUtil.ItemCallback<LowIncomeItem>() {
            override fun areItemsTheSame(oldItem: LowIncomeItem, newItem: LowIncomeItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: LowIncomeItem, newItem: LowIncomeItem): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LowIncomeViewHolder {
        val binding = ItemLowIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LowIncomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LowIncomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LowIncomeViewHolder(private val binding: ItemLowIncomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LowIncomeItem) {
            binding.tvTitle.text = item.title
            binding.btnApply.text = item.applyText
            binding.btnDownload.text = item.downloadText
            binding.tvUpdateTime.text = "更新時間：${item.updateTime}"
            binding.tvTitle.paintFlags = binding.tvTitle.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            // Change button background dynamically
            val applyBackground = if (item.isOnlineApply) {
                R.drawable.bg_button_online
            } else {
                R.drawable.bg_button_offline
            }

            binding.btnApply.setBackgroundResource(applyBackground)

            binding.btnApply.setOnClickListener {
                Toast.makeText(binding.root.context, "${item.title} - ${item.applyText}", Toast.LENGTH_SHORT).show()
            }

            binding.btnDownload.setOnClickListener {
                Toast.makeText(binding.root.context, "${item.title} - ${item.downloadText}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
