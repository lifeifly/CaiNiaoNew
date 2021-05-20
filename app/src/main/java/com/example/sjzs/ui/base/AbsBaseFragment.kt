package com.example.sjzs.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * 主要做懒加载
 */
abstract class AbsBaseFragment : Fragment(), IBaseView{


    private var mRootView: View? = null//fragment跟布局

    private var isViewCreated = false//跟布局是否创建

    private var currentVisibleState = false//当前View是否可见


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutRes(), container, false)
        }
        initView(mRootView!!)
        //标记跟布局已经创建
        isViewCreated = true
        //根据是否可见判断是否请求数据
        if (isVisible) {
            //当前可见，请求可见数据
            dispatchUserVisibleHint(true)
        }
        return mRootView
    }

    abstract fun getLayoutRes(): Int

    abstract fun initView(rootView: View)

    //判断fragmnet是否可见
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false)
            }
        }
    }

    //分发Fragment可见事件
    private fun dispatchUserVisibleHint(isVisible: Boolean) {
        //如果上次状态跟当前状态一样，不用操作
        if (currentVisibleState == isVisible) return

        currentVisibleState = isVisible

        if (isVisible) {
            //可见事件
            requestData()
        } else {
            //停止加载
            onFragmentLoadStop()
        }
    }

    protected fun onFragmentLoadStop() {

    }


    override fun onResume() {
        super.onResume()
        if (!currentVisibleState && isVisible) {
            dispatchUserVisibleHint(true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (currentVisibleState && isVisible) {
            dispatchUserVisibleHint(false)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        currentVisibleState = false

    }
}