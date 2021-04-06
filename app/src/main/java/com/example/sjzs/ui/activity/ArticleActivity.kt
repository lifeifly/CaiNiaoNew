package com.example.sjzs.ui.activity


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.sjzs.R
import com.example.sjzs.databinding.ActivityArticleBinding
import com.example.sjzs.ui.base.BaseActivity
import com.example.sjzs.viewmodel.ArticleDataViewModel
import com.example.sjzs.viewmodel.ArticleRequestViewModel


class ArticleActivity : BaseActivity() {
    private val dataVm: ArticleDataViewModel by lazy {
        ViewModelProvider(this).get(
            ArticleDataViewModel::class.java
        )
    }
    private val requestVm: ArticleRequestViewModel by lazy {
        ViewModelProvider(this).get(
            ArticleRequestViewModel::class.java
        )
    }

    private  lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG1", "onCreate: ")
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_article)
        initView()
        binding.dataVM = dataVm
        binding.setLifecycleOwner(this)
        lifecycle.addObserver(binding.surfaceView.mLifeCycleCallback)
        requestVm.setDataViewModel(dataVm)
        requestData()
    }

    private fun initView() {
        //给toolbar设置返回键
        val toolbar = binding.toolBar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)//设置返回键可用

    }


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun requestData() {
        val intent = intent
        val url = intent.getStringExtra("data")

        if (url != null) {
            requestVm.requestData(url)
        }
    }

    override fun complete() {

    }


    /**
     * 响应返回键
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}