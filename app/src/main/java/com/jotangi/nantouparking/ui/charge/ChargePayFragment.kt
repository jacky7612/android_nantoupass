package com.jotangi.nantouparking.ui.charge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentChargePayBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChargePayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChargePayFragment : BaseFragment() {
    private var _binding: FragmentChargePayBinding? = null
    private val binding get() = _binding

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
//    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargePayBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle("付款")
//        init()
//        initEvent()
        binding?.apply {
            btNext.setOnClickListener {
                findNavController().navigate(R.id.action_chargePayFragment_to_chargeStartFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}