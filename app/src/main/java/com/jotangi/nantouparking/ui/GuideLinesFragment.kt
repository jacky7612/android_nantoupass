package com.jotangi.nantouparking.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentGuideLinesBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.StoreVO
import com.jotangi.nantouparking.ui.main.StoreAdapter
import com.jotangi.nantouparking.ui.main.StoreClickListener

/**
 * A simple [Fragment] subclass.
 * Use the [GuideLinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuideLinesFragment : BaseFragment(),
    StoreClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentGuideLinesBinding? = null
    private val binding get() = _binding
        private lateinit var storeAdapter: StoreAdapter
    private var data = mutableListOf<StoreVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGuideLinesBinding.inflate(inflater, container, false)

        return binding?.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGuildelinesTitle()
        mainViewModel.storeData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                storeAdapter.updateDataSource(result)
            }
        }
        initStoreRecyclerView()
        initData()
    }

    private fun initData() {
        mainViewModel.getMainBannerData(requireContext())
        mainViewModel.getStoreData(
            requireContext(),
            ""
        )
    }

        private fun initStoreRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            storeAdapter = StoreAdapter(
                data,
                requireContext(),
                this@GuideLinesFragment
            )
            this.adapter = storeAdapter
        }
    }

    companion object {



    }

    override fun onStoreItemClick(storeData: StoreVO) {
        println("點了店家 $storeData")
        println("點了店家 $storeData")
        println("點了店家 $storeData")

        val bundle = Bundle().apply {
            putParcelable("storeVO", storeData)
        }

        findNavController().navigate(
            R.id.action_to_store_detail2,
            bundle
        )
    }
}