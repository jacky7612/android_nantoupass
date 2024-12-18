package com.jotangi.nantouparking.ui.charge

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jotangi.nantouparking.databinding.FragmentMarkerInfoBottomSheetBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarkerInfoBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarkerInfoBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentMarkerInfoBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var mytitle: String =""
    private var mysnippet: String =""
    private var myLatLng: LatLng? =null

    fun init(title: String, snippet: String, latlng: LatLng) {
        mytitle   =title
        mysnippet =snippet
        myLatLng  =latlng
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkerInfoBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            markerTitle.text = mytitle
            markerSnippet.text = mysnippet
            btNavigation.setOnClickListener{
                openGoogleMapsNavigation(myLatLng!!.latitude, myLatLng!!.longitude)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openGoogleMapsNavigation(lat: Double, lng: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}