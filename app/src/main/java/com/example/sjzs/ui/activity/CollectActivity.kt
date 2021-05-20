package com.example.sjzs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sjzs.R
import com.example.sjzs.model.room.CollectDataBaseHelper
import com.example.sjzs.ui.adapter.CollectPagingAdapter
import com.example.sjzs.ui.base.BaseSkinActivity
import com.example.sjzs.ui.recyclerview.CustomItemTouchCallback
import com.example.sjzs.ui.recyclerview.DataIsNullListener
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.CollectDataViewModel
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.coroutines.launch

class CollectActivity : BaseSkinActivity(), View.OnClickListener {
    private val mDataVM: CollectDataViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(CollectDataViewModel::class.java)
    }
    private lateinit var mAdapter: CollectPagingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
        initView()


        mAdapter = CollectPagingAdapter()

        mAdapter.setListener(object : CollectPagingAdapter.CollectLoadListener {
            override fun loadData() {
                no_data_tv.visibility = View.GONE
                collect_rv.visibility = View.VISIBLE
            }
        })

        collect_rv.layoutManager = LinearLayoutManager(this)
        collect_rv.addItemDecoration(RvDivider())

        val collectDataBaseHelper = CollectDataBaseHelper(this, mAdapter,object : DataIsNullListener{
            override fun dataIsNull() {
                Log.d("TAG", "loadData: ")
                no_data_tv.visibility = View.VISIBLE
                collect_rv.visibility = View.GONE
            }
        })
        collect_rv.setItemTouchStatus(collectDataBaseHelper)
        val itemTouchHelper = ItemTouchHelper(CustomItemTouchCallback(collectDataBaseHelper))
        itemTouchHelper.attachToRecyclerView(collect_rv)

        collect_rv.adapter = mAdapter
        mDataVM.listData.observe(this, Observer {
            lifecycleScope.launch {
                mAdapter.submitData(it)
            }
        })
    }

    private fun initView() {
        //启用toolbar

        setSupportActionBar(tool_bar)
        supportActionBar?.title = ""

        //为toolbar上的控件设置点击事件
        iv_back.setOnClickListener(this)
        tv_select_all.setOnClickListener(this)
        tv_delete.setOnClickListener(this)
        tv_complete.setOnClickListener(this)
        iv_edit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
                overridePendingTransition(
                    R.anim.main_slide_in_animation,
                    R.anim.collect_slide_out_animation
                )
            }
            R.id.iv_edit -> {
                if (collect_rv.childCount>0){//有数据时才显示编辑页面
                    showEditbar()
                    //显示选择框
                    collect_rv.showSelect()
                }
            }
            R.id.tv_select_all -> {
                collect_rv.selectAll()
            }
            R.id.tv_delete -> {
                collect_rv.delete()
                //删除之后隐藏选择框
                collect_rv.hideSelect()
                showBackBar()
            }
            R.id.tv_complete -> {
                //隐藏选择框
                collect_rv.hideSelect()
                showBackBar()
            }
        }
    }

    private fun showEditbar(){
        //隐藏返回按钮和编辑按钮
        iv_edit.visibility = View.GONE
        iv_back.visibility = View.GONE
        //显示编辑文本
        tv_select_all.visibility = View.VISIBLE
        delete_complete_group.visibility = View.VISIBLE
    }

    private fun showBackBar(){
        //隐藏编辑文本
        tv_select_all.visibility = View.GONE
        delete_complete_group.visibility = View.GONE

        //显示返回按钮和编辑按钮
        iv_edit.visibility = View.VISIBLE
        iv_back.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}