package com.jotangi.nantouparking.ui.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.nantouparking.databinding.FragmentQNABinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment

class QNAFragment : BaseFragment() {
    private var _binding: FragmentQNABinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQNABinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        setupQNATitle()
        initAction()
    }

    private fun initAction() {

    }

    companion object {

    }
}