package com.jotangi.nantouparking.ui.main

import CustomerServiceBottomSheet
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jotangi.nantouparking.R
import com.jotangi.nantouparking.config.ApiConfig
import com.jotangi.nantouparking.config.AppConfig
import com.jotangi.nantouparking.databinding.FragmentMainBinding
import com.jotangi.nantouparking.databinding.ToolbarIncludeBinding
import com.jotangi.nantouparking.model.BannerVO
import com.jotangi.nantouparking.model.StoreVO
import com.jotangi.nantouparking.ui.BaseFragment
import com.jotangi.nantouparking.utility.AppUtility
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class MainFragment :
    BaseFragment(),
    StoreClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding
//    private lateinit var storeAdapter: StoreAdapter
    private var data = mutableListOf<StoreVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      if(getStorePreference().equals("Store") && AppUtility.getLoginStatus(requireContext())) {
          findNavController().navigate(R.id.action_to_store_manager2_fragment)
      }

        init()
    }

    private fun getStorePreference(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("key_store", null) // Default to null if not found
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun init() {
        if (
            AppUtility.getLoginType(requireContext()).equals("1") ||
            AppUtility.getLoginType(requireContext()).equals("")
        ) {
            setupMainTitle()
            initObserver()
            initView()
            initData()
            initAction()
        } else {
            findNavController().navigate(R.id.action_to_storeManagerFragment)
        }

    }

    private fun initObserver() {
        mainViewModel.bannerData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                binding?.mainBannerDefaultImageView?.visibility = View.GONE
                updateBanner(result)
            } else {
                binding?.mainBannerDefaultImageView?.visibility = View.VISIBLE
            }
        }

        mainViewModel.storeData.observe(viewLifecycleOwner) { result ->
            if (result != null) {
//                storeAdapter.updateDataSource(result)
            }
        }
    }

    private fun initView() {
//        initStoreRecyclerView()
    }

    private fun initData() {
        mainViewModel.getMainBannerData(requireContext())
        mainViewModel.getStoreData(
            requireContext(),
            ""
        )
    }

    private fun initAction() {
        binding?.apply {

            binding?.apply {

                mainChargeConstraintLayout.setOnClickListener {
                    AppUtility.showPopDialog(
                        requireContext(),
                        "",
                        "\n充電服務即將上線\n\n敬請期待!"
                    )
//                    if (!AppUtility.getLoginStatus(requireContext())) {
//                        AppUtility.showPopDialog(
//                            requireContext(),
//                            "請先登入會員",
//                            "\n請先登入會員\n\n未登入會員無法使用此功能"
//                        )
//                    } else {
//                        chargeViewModel.clear()
//                        findNavController().navigate(R.id.action_main_fragment_to_chargeNoticeFragment2)
//                    }
                }


                mainLineConstraintLayout.setOnClickListener {
                    openUrl("https://line.me/R/ti/p/@sbo5307t")
                }
                mainAttractionsConstraintLayout.setOnClickListener {
                    findNavController().navigate(R.id.action_to_guideline)
                }

                market.setOnClickListener {
//                    AppUtility.showPopDialog(
//                        requireContext(),
//                        "",
//                        "市集籌備中，敬請期待"
//                    )
//                    openUrl("https://www.sunnygo.com.tw/web-front/#/testArea?")

                    findNavController().navigate(R.id.action_to_market_fragment2)
                }
                // line 1
                mainSpaceConstraintLayout.setOnClickListener {
                    findNavController().navigate(R.id.action_to_mapChargeFragment2)
                }


                mainServiceConstraintLayout.setOnClickListener {
//                AppUtility.showPopDialog(
//                    requireContext(),
//                    null,
//                    "尚未開放！\n敬請期待！"
//                )
                    val bottomSheet = CustomerServiceBottomSheet()
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)

//                    makePhoneCall(AppConfig.CUSTOMER_SERVICE_PHONE)
                }

                mainOfficialConstraintLayout.setOnClickListener {
                    openWeb(AppConfig.AREA_MAIN_WEB)
                }

                parkingFbConstraintLayout.setOnClickListener {
                    openWeb(AppConfig.MAYOR_FB)
                }

                // line 2
                parkingPaymentConstraintLayout.setOnClickListener {
                    findNavController().navigate(R.id.action_to_parking_license_plate_history)
                }

                parkingInfoConstraintLayout.setOnClickListener {
                    openWeb("https://parking.nantou.gov.tw/")
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()

        updateView()
    }

//    private fun initStoreRecyclerView() {
//        binding?.mainStoreRecyclerView?.apply {
//            layoutManager = LinearLayoutManager(
//                requireContext(),
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//            storeAdapter = StoreAdapter(
//                data,
//                requireContext(),
//                this@MainFragment
//            )
//            this.adapter = storeAdapter
//        }
//    }

    private fun updateView() {
        binding?.mainBannerDefaultImageView?.visibility = View.GONE
    }

    private fun updateBanner(result: List<BannerVO>) {
        binding?.apply {
            mainBannerLoginBanner.setAdapter(object : BannerImageAdapter<BannerVO?>(result) {
                override fun onBindView(
                    holder: BannerImageHolder?,
                    data: BannerVO?,
                    position: Int,
                    size: Int
                ) {
                    if (holder != null) {
                        Glide.with(holder.itemView)
                            .load(ApiConfig.URL_HOST + data?.bannerPic)
//                            .load(ApiConfig.TEST_URL_HOST + data?.bannerPic)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                            .into(holder.imageView)

                        holder.imageView.setOnClickListener {
                            if (!data?.bannerLink.isNullOrEmpty()) {
                                openBannerLink(data!!.bannerLink)
                            }
                        }
                    }
                }
            })
                .addBannerLifecycleObserver(this@MainFragment) //添加生命周期观察者
                .setIndicator(CircleIndicator(requireContext()))
        }
    }

    private fun openBannerLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        startActivity(intent)
    }

    private fun openWeb(webUri: String) {
        val intent = Intent(Intent.ACTION_VIEW)

        intent.data = Uri.parse(webUri)
        startActivity(intent)
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context?.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open the link.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openApp(appUri: String) {
        val launchIntent: Intent? =
            requireContext().packageManager.getLaunchIntentForPackage(appUri)

        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            val mIntent = Intent(Intent.ACTION_VIEW)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mIntent.data = Uri.parse("market://details?id=$appUri")
            requireContext().startActivity(mIntent)
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
            R.id.action_to_store_detail,
            bundle
        )
    }


}