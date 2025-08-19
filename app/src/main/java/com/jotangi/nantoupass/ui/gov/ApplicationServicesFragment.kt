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
import com.jotangi.nantoupass.MainActivity
import com.jotangi.nantoupass.R

class ApplicationServicesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_application_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back: ImageView = view.findViewById(R.id.back)
        val textViewRecordStatus: TextView = view.findViewById(R.id.textViewRecordStatus)
        textViewRecordStatus.paintFlags = textViewRecordStatus.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        textViewRecordStatus.setOnClickListener {
            findNavController().navigate(R.id.action_application_services_fragment_to_application_status_fragment)

        }

      back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val categoryList = listOf(
            "低收入戶", "中低收入戶",
            "老人福利", "求職就業",
            "求學進修", "生育補助"
        )


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.addItemDecoration(SpaceItemDecoration(2)) // Reduce spacing to match the provided image
        recyclerView.adapter = CategoryAdapter(categoryList) { category ->
            findNavController().navigate(R.id.action_application_services_fragment_to_low_income_fragment)

            Toast.makeText(requireContext(), "Clicked: $category", Toast.LENGTH_SHORT).show()
        }
    }
}

class CategoryAdapter(private val categories: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
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
            onItemClick(category)
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