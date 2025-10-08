package com.jotangi.nantoupass.ui.gov

import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.jotangi.nantoupass.JackyVariant.Glob
import com.jotangi.nantoupass.MainActivity
import com.jotangi.nantoupass.R
import com.jotangi.nantoupass.config.Response4PassApplyitem
import com.jotangi.nantoupass.config.Response4PassApplyitemContent
import com.jotangi.nantoupass.config.Response4PassSightseeingContent
import com.jotangi.nantoupass.databinding.FragmentApplicationServicesBinding
import com.jotangi.nantoupass.databinding.FragmentMarketBinding
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.StoreVO
import com.jotangi.nantoupass.ui.BaseWithBottomBarFragment

class ApplicationServicesFragment : BaseWithBottomBarFragment() {

    private var _binding: FragmentApplicationServicesBinding? = null
    private val binding get() = _binding
    //    private lateinit var storeAdapter: StoreAdapter
    private var data = mutableListOf<StoreVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet
    private var data_applyitem : List<Response4PassApplyitemContent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicationServicesBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupApplyTitle()
        initObserver()
        initEvent()
        getData()

        binding?.textViewRecordStatus?.setOnClickListener {
            findNavController().navigate(R.id.action_application_services_fragment_to_application_status_fragment)

        }
    }
    /****************
     * api response *
     ****************/
    private fun initObserver() {
        passViewModel.ApplyItem.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.status == "true" && result.code == "0x0200") {
                    data_applyitem = result.data?.data
                    val lstApplyitem = data_applyitem?.map { it.name?: "" }  // 取 head_img
                    if (lstApplyitem != null) updateRecycleView(lstApplyitem)
                }
            } else {
            }
        }
    }

    private fun getData() {
        passViewModel.clearApply()
        passViewModel.getApplyItems(requireContext())
    }
    private fun updateRecycleView(input:List<String>) {
        binding?.apply {
            rvApply.layoutManager = GridLayoutManager(requireContext(), 2)
            rvApply.addItemDecoration(SpaceItemDecoration(2)) // Reduce spacing to match the provided image
            rvApply.adapter = CategoryAdapter(input) { category, position ->
                if (data_applyitem != null) {
                    Glob.ItemApply.sid = data_applyitem!!.get(position).sid.toString()
                }
                findNavController().navigate(R.id.action_application_services_fragment_to_low_income_fragment)

                Toast.makeText(requireContext(), "Clicked: $category", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class CategoryAdapter(private val categories: List<String>, private val onItemClick: (String, Int) -> Unit) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textViewCategory)
        val cardView: CardView = view.findViewById(R.id.cardViewCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.textView.text = category
        holder.cardView.setOnClickListener {
            onItemClick(category, position)
        }
    }

    override fun getItemCount(): Int = categories.size
}

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position >= 2) { // Apply spacing only to rows after the first row
            outRect.top = space
        }
    }
}