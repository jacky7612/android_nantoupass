package com.jotangi.nantouparking.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.jotangi.nantouparking.JackyVariant.Glob
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentMarket2Binding
import com.jotangi.nantouparking.databinding.FragmentMarket4ActivityBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.ui.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Market4ActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Market4ActivityFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentMarket4ActivityBinding? = null
    private val binding get() = _binding
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarket4ActivityBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 模擬從 API 回傳的資料
        val activityItem = Glob.lstActivity.getOrNull(0)
        if (activityItem == null) {
            // 沒有資料，處理錯誤
            return
        }
        val linkUrl = activityItem.link_url

        addImageView(ApiConfig.URL_HOST + activityItem.body_picture1, linkUrl)
        addImageView(ApiConfig.URL_HOST + activityItem.body_picture2, linkUrl)
        addImageView(ApiConfig.URL_HOST + activityItem.body_picture3, linkUrl)
    }

    private fun addImageView(imageUrl: String, linkUrl: String) {
        val imageView = ImageView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
        }

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        imageView.setOnClickListener {
            if (linkUrl.isNotBlank()) {
                openWeb(linkUrl)
            }
        }

        binding?.imageContainer?.addView(imageView)
    }
    private fun openWeb(webUri: String) {
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = Uri.parse(webUri)
        startActivity(intent)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Market4ActivityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Market4ActivityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}