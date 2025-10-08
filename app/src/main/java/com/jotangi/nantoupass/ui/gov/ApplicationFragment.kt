package com.jotangi.nantoupass.ui.gov

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import com.jotangi.nantoupass.databinding.FragmentApplicationBinding
import com.jotangi.nantoupass.databinding.ToolbarFeetBinding
import com.jotangi.nantoupass.databinding.ToolbarIncludeBinding
import com.jotangi.nantoupass.model.StoreVO
import com.jotangi.nantoupass.ui.BaseWithBottomBarFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ApplicationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApplicationFragment : BaseWithBottomBarFragment() {

    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding
    //    private lateinit var storeAdapter: StoreAdapter
    private var data = mutableListOf<StoreVO>()

    override fun getToolBar(): ToolbarIncludeBinding = binding!!.toolbarInclude
    override fun getToolBarFeet(): ToolbarFeetBinding = binding!!.toolbarFeet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentApplicationBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupApplyTitle()
        initEvent4apply()
        binding?.apply {
            wvApply.settings.javaScriptEnabled = true
            wvApply.settings.domStorageEnabled = true
            wvApply.settings.setSupportZoom(true)
            wvApply.settings.builtInZoomControls = true
            wvApply.settings.displayZoomControls = false
            wvApply.isFocusable = true
            wvApply.isFocusableInTouchMode = true
            wvApply.requestFocus(View.FOCUS_DOWN)

            // 使用內部 WebView 開啟連結
            wvApply.webViewClient = WebViewClient()

            // 🔹 設定下載監聽器來處理 PDF 或其他檔案下載
            wvApply.setDownloadListener { url, _, _, _, _ ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "無法開啟檔案", Toast.LENGTH_SHORT).show()
                }
            }

            // Load the URL
            wvApply.loadUrl("https://miaoparking.jotangi.net/nantoupass/ui/apply4app/apply.php")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApplicationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplicationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}