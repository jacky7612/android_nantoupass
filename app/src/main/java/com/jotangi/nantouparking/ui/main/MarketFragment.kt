package com.jotangi.nantouparking.ui.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.databinding.FragmentMainBinding
import com.jotangi.nantouparking.databinding.FragmentMarketBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentMarketBinding? = null
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    private val binding get() = _binding
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
        _binding = FragmentMarketBinding.inflate(inflater, container, false)

        return binding?.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMarketTitle()

        binding?.apply {
            getPoint.setOnClickListener{
                if(!AppUtility.getLoginStatus(requireContext())){
                    showLogoutDialog()
                } else {
                    findNavController().navigate(R.id.action_to_market_get_point)
                }
            }
            usePoint.setOnClickListener{
                findNavController().navigate(R.id.action_to_marekt_change)

            }
            recordPoint.setOnClickListener{
                findNavController().navigate(R.id.action_to_current_point_fragment)
            }

            lottory.setOnClickListener {
                    findNavController().navigate(R.id.action_to_market_lottory)
            }

        }
    }

    private fun showLogoutDialog() {
        // Inflate the custom layout
        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.dialog_not_login, null)

        // Initialize the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Find the confirm button and set a click listener
        val btnConfirm = dialogView.findViewById<TextView>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MarketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onBackPressed() {
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.market_fragment) {
            // Navigate back to MainFragment
            navController.popBackStack(R.id.main_fragment, false)
        } else {
            super.onBackPressed()
        }
    }
}