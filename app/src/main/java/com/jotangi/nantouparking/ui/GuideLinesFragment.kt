package com.jotangi.nantouparking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentGuideLinesBinding
import com.jotangi.nantouparking.databinding.FragmentMainBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.StoreVO
import com.jotangi.nantouparking.ui.main.StoreAdapter
import com.jotangi.nantouparking.ui.main.StoreClickListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GuideLinesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GuideLinesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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