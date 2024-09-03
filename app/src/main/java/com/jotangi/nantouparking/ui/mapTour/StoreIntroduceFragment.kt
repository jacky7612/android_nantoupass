package com.jotangi.nantouparking.ui.mapTour

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentStoreIntroduceBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment
import com.squareup.picasso.Picasso


class StoreIntroduceFragment : BaseFragment() {

    private var _binding: FragmentStoreIntroduceBinding? = null
    private val binding get() = _binding!!

    override fun getToolBar(): ToolbarIncludeBinding = binding.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreIntroduceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        setupStoreIntroduceTitle()
        initView()
    }

    private fun initView() {
        binding.apply {
            storeNameTextView.text = arguments?.getString("storeName")
            storeIntroduceTextView.text = arguments?.getString("storeDescript")
            storeAddressTextView.text = "地址\n" + arguments?.getString("storeAddress")
            storeAddressMapImageView.setOnClickListener {
                val map =
                    "http://maps.google.com/maps?" + "daddr=" + arguments?.getString("storeLatitude") + "," + arguments?.getString(
                        "storeLongitude"
                    )
                Log.d("TAG", "storeLatitude: " + arguments?.getString("storeLatitude"))
                val uri = Uri.parse(map)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            storePhoneTextView.text = "電話\n" + arguments?.getString("storePhone")
            storePhoneImageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + arguments?.getString("storePhone")))
                startActivity(intent)
            }
            Picasso.with(requireActivity())
                .load(ApiConfig.URL_HOST + arguments?.getString("storePicture"))
//                .load(ApiConfig.TEST_URL_HOST + arguments?.getString("storePicture"))
                .into(storeIntroducePicture)

        }
    }

    companion object {
    }
}