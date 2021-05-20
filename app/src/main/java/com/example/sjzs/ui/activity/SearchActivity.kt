package com.example.sjzs.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sjzs.R
import com.example.sjzs.databinding.ActivitySearchBinding
import com.example.sjzs.ui.adapter.SearchPagingAdapter
import com.example.sjzs.ui.base.BaseSkinActivity
import com.example.sjzs.ui.recyclerview.SearchRvDivider
import com.example.sjzs.viewmodel.SearchActivityDataViewModel
import kotlinx.coroutines.launch

class SearchActivity : BaseSkinActivity(), View.OnClickListener {
    private lateinit var mBind:ActivitySearchBinding

    private lateinit var mAdapter:SearchPagingAdapter
    //当前的text
    private var mCurText=""
    private val mDataVM:SearchActivityDataViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application))
            .get(SearchActivityDataViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind=DataBindingUtil.setContentView(this,R.layout.activity_search)
        mBind.searchBtn.setOnClickListener(this)
        mBind.btnBack.setOnClickListener(this)
        mBind.editSearch.setOnEditorActionListener{v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //关闭软键盘
                shutownKeyBoard();
                search(mBind.editSearch.text.toString())
                true;
            }
             false
        }
        setRecyclerView()

        initHistory()
    }

    private fun initHistory() {
        mDataVM.historiesData.observe(this, Observer {
            mBind.mFlowContainer.addTexts(it)
        })
    }

    private fun setRecyclerView() {
        mAdapter= SearchPagingAdapter()

        mBind.mRecycler.layoutManager=LinearLayoutManager(this)
        mBind.mRecycler.addItemDecoration(SearchRvDivider())
        mBind.mRecycler.adapter=mAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_back->{
                finish()
            }
            R.id.search_btn->{
                //开始搜索
                shutownKeyBoard()
                if (mBind.editSearch.text!=null&&!TextUtils.isEmpty(mBind.editSearch.text)){
                    search(mBind.editSearch.text.toString())
                }
            }
        }
    }

    /**
     * 搜索
     */
    fun search(keyword:String){
        if (mCurText==keyword)return
        mDataVM.search(keyword)
        //添加到FlowLayout
        mBind.mFlowContainer.addText(keyword)
        mDataVM.insertHistory(keyword)
        mBind.mRecycler.removeAllViews()
        mDataVM.listData?.observe(this, Observer {
            lifecycleScope.launch {
                mAdapter.submitData(it)
            }
        })
        mAdapter.refresh()
        mCurText=keyword
    }
    /**
     * 关闭软键盘
     */
    private fun shutownKeyBoard(){
        val imm=(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        if (imm.isActive()&&currentFocus!=null){
            imm.hideSoftInputFromWindow(currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}