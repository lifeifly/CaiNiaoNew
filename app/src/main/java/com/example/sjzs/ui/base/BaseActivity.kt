package com.example.sjzs.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.sjzs.R
import com.example.video.loadview.LoadView

abstract class BaseActivity : BaseSkinActivity(), IBaseView {
    //数据绑定
    protected lateinit var binding: ViewDataBinding
    //根布局
    protected lateinit var mRoot:View
    //内容的容器
    protected lateinit var mContentContainer:View
    //加载的容器
    protected lateinit var mLoadContainer:ViewGroup
    //加载的view
    protected lateinit var mLoadView:LoadView
    //错误的textView
    protected lateinit var mTvError:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayout())
        //将加载的布局添加进来
        mRoot=LayoutInflater.from(this).inflate(R.layout.error_loading_layout,binding.root as ViewGroup)
        mContentContainer=findViewById(R.id.content_container)
        mLoadContainer=mRoot.findViewById(R.id.load_container)
        mLoadView=mRoot.findViewById(R.id.load_view)
        mTvError=mRoot.findViewById(R.id.tv_error)
        initView()
        bind()
        requestData()
    }

    override fun showError(text:String) {
        dismissLoading()
        mTvError.visibility=View.VISIBLE
        mTvError.setText(text)
        //为tvError设置重试监听器
        mTvError.setOnClickListener{v ->
            requestData()
        }
    }

    /**
     * 显示加载页面
     */
    override fun showLoading() {

        mLoadView.start()
        mTvError.visibility=View.GONE
    }

    /**
     * 加载完成，显示内容
     */
    override fun complete() {

        //移除loadview
        mLoadView.clear()
        dismissLoading()
        (mRoot as ViewGroup).removeView(mLoadContainer)

        mContentContainer.visibility=View.VISIBLE
    }

    /**
     * 取消加载
     */
    override fun dismissLoading() {
        mLoadView.stop()
    }


    abstract fun initView()
    abstract fun getLayout(): Int
    abstract fun bind()
}