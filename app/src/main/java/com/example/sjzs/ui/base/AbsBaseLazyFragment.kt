package com.example.sjzs.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * 主要做懒加载
 */
abstract class AbsBaseLazyFragment : Fragment(), IBaseView {


    private var mRootView: View? = null//fragment跟布局

    private var isFirstLoad = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutRes(), container, false)
        }
        initView(mRootView!!)

        return mRootView
    }

    abstract fun getLayoutRes(): Int

    abstract fun initView(rootView: View)


    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            requestData()
            isFirstLoad = true
        }
        Log.d("LAZYFRAGMENT", "onResume: ")
    }



    override fun onDestroyView() {
        mRootView=null
        isFirstLoad=true
        super.onDestroyView()

    }
}