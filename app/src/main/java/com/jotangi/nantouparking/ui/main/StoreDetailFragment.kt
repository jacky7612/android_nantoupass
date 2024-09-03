package com.jotangi.nantouparking.ui.main

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.databinding.FragmentStoreDetailBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.StoreVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility

class StoreDetailFragment : BaseFragment() {
    private var _binding: FragmentStoreDetailBinding? = null
    private val binding get() = _binding
    private lateinit var data: StoreVO
    private var isExpanded = false
    private val isClose = "展開 v"
    private val isShow = "收合 ^"
    private val collapsedHeight = 150
    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreDetailBinding.inflate(inflater, container, false)

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
        setupStoreIntroduceTitle()
        initData()
        initView()
        initAction()
    }

    private fun initData() {
        val bundle = arguments
        data = bundle?.getParcelable("storeVO")!!
    }

    private fun initView() {
        if (::data.isInitialized) {
            binding?.apply {
                if (!data.storeImg.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(ApiConfig.URL_HOST + data.storeImg)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                        .into(storeDetailImageView)
                }

                storeDetailNameTextView.text = data.storeName ?: ""
                storeDetailDesTextView.text = data.storeDes ?: ""
                storeDetailShowTextView.text = isClose
                item1ContentTextView.text = data.storeOpenTime ?: ""
                item2ContentTextView.text = data.storeOpen ?: ""
                item3ContentTextView.text = data.storeAddress ?: ""
                item4ContentTextView.text = data.storePhone ?: ""
            }
        }
    }

    private fun initAction() {
        binding?.apply {
            // line 1
            storeDetailNavigationConstraintLayout.setOnClickListener {
                if (
                    !data.storeLat.isNullOrEmpty() &&
                    !data.storeLong.isNullOrEmpty()
                ) {
                    openMap(
                        data.storeLat,
                        data.storeLong
                    )
                } else {
                    AppUtility.showPopDialog(
                        requireContext(),
                        null,
                        "尚未開放！\n敬請期待！"
                    )
                }
            }

            storeDetailPhoneConstraintLayout.setOnClickListener {
                if (!data.storePhone.isNullOrEmpty()) {
                    makePhoneCall(data.storePhone)
                } else {
                    AppUtility.showPopDialog(
                        requireContext(),
                        null,
                        "尚未開放！\n敬請期待！"
                    )
                }
            }

            storeDetailOfficialWebConstraintLayout.setOnClickListener {
                if (!data.storeWebsite.isNullOrEmpty()) {
                    openWeb(data.storeWebsite)
                } else {
                    AppUtility.showPopDialog(
                        requireContext(),
                        null,
                        "尚未開放！\n敬請期待！"
                    )
                }
            }

            storeDetailShowTextView.setOnClickListener {
                isExpanded = !isExpanded
                storeDetailShowTextView.text = if (isExpanded) {
                    isShow
                } else {
                    isClose
                }

                val newHeight =
                    if (isExpanded) {
                        getExpandedHeight(storeDetailDesTextView)
                    } else {
                        collapsedHeight
                    }
                animateTextViewHeight(storeDetailDesTextView, newHeight)
            }
        }
    }

    private fun openWeb(webUri: String) {
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = Uri.parse(webUri)
        startActivity(intent)
    }

    private fun openMap(
        lat: String,
        long: String
    ) {
        val parkingInfoGPS = "http://maps.google.com/maps?daddr=$lat,$long"
        val gmmIntentUri: Uri =
            Uri.parse(parkingInfoGPS)
        val mapIntent = Intent(
            Intent.ACTION_VIEW,
            gmmIntentUri
        )

        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        startActivity(intent)
    }

    private fun getExpandedHeight(view: View): Int {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                view.width,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        )
        return view.measuredHeight
    }

    private fun animateTextViewHeight(textView: TextView, newHeight: Int) {
        val anim = ValueAnimator.ofInt(textView.height, newHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = textView.layoutParams
            layoutParams.height = value
            textView.layoutParams = layoutParams
        }
        anim.duration = 200 // 調整動畫時間
        anim.start()
    }
}