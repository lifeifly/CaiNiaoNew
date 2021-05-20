package com.example.sjzs.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.sjzs.R
import com.example.sjzs.databinding.ActivitySkinBinding
import com.example.sjzs.model.datastore.datastore
import com.example.sjzs.model.datastore.key
import com.example.sjzs.ui.base.BaseSkinActivity
import com.example.sjzs.ui.scrollview.SkinCardView
import com.example.skin.SkinManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SkinActivity : BaseSkinActivity(), View.OnClickListener {
    //上次的换肤类型
    private lateinit var mType: SkinCardView
    private var mBasePath = ""
    private lateinit var mBinding: ActivitySkinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBasePath =
            getExternalFilesDir(null)?.absolutePath + File.separator + "skin" + File.separator + "plugin"

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_skin)
        initView()
        //获取上次的type
        lifecycleScope.launch(Dispatchers.Main) {
            datastore.data.map {
                it[key] ?: ""
            }.collect {
                mType = getType(it)
                //添加边框
                mType.addFrame()
            }
        }
    }

    fun initView() {
        //支持Toolbar
        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.title="换肤"
        //启用返回键
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.skinItem1.setOnClickListener(this)
        mBinding.skinItem2.setOnClickListener(this)
        mBinding.skinItem3.setOnClickListener(this)
        mBinding.skinItem4.setOnClickListener(this)
        mBinding.skinItem5.setOnClickListener(this)
        mBinding.skinItem6.setOnClickListener(this)
        mBinding.skinItem7.setOnClickListener(this)
        mBinding.skinItem8.setOnClickListener(this)
        mBinding.skinItem9.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //点击的必须和上次不一样才会执行代码
        when (v?.id) {
            R.id.skin_item1 -> {
                if (mType.id != R.id.skin_item1) {
                    loadNewRes(mBinding.skinItem1, "")
                }
            }
            R.id.skin_item2 -> {
                if (mType.id != R.id.skin_item2) {
                    val path = mergePath("1")
                    loadNewRes(mBinding.skinItem2, path)
                }
            }
            R.id.skin_item3 -> {
                if (mType.id != R.id.skin_item3) {
                    val path = mergePath("2")
                    loadNewRes(mBinding.skinItem3, path)
                }
            }
            R.id.skin_item4 -> {
                if (mType.id != R.id.skin_item4) {
                    val path = mergePath("3")
                    loadNewRes(mBinding.skinItem4, path)
                }
            }
            R.id.skin_item5 -> {
                if (mType.id != R.id.skin_item5) {
                    val path = mergePath("4")
                    loadNewRes(mBinding.skinItem5, path)
                }
            }
            R.id.skin_item6 -> {
                if (mType.id != R.id.skin_item6) {
                    val path = mergePath("5")
                    loadNewRes(mBinding.skinItem6, path)
                }
            }
            R.id.skin_item7 -> {
                if (mType.id != R.id.skin_item7) {
                    val path = mergePath("6")
                    loadNewRes(mBinding.skinItem7, path)
                }
            }
            R.id.skin_item8 -> {
                if (mType.id != R.id.skin_item8) {
                    val path = mergePath("7")
                    loadNewRes(mBinding.skinItem8, path)
                }
            }
            R.id.skin_item9 -> {
                if (mType.id != R.id.skin_item9) {
                    val path = mergePath("8")
                    loadNewRes(mBinding.skinItem9, path)
                }
            }
        }
    }

    /**
     * 开始加载新的资源
     */
    private fun loadNewRes(newSkinCardView: SkinCardView, path: String?) {
        //去除上个SkinCardView边框
        mType.deleteFrame()
        //添加边框给新的SkinCardView，并开启加载动画
        newSkinCardView.startAnimation()
        //删除datastore的数据，并更新新的数据
        lifecycleScope.launch(Dispatchers.Main) {
            //切换到Io执行
            withContext(Dispatchers.IO) {
                datastore.edit {
                    val currentValue = it[key] ?: ""
                    if (TextUtils.isEmpty(path) || path == null) {
                        it[key] = ""
                    } else {
                        it[key] = path
                    }
                }
            }
            //切换到主线程
            //加载资源并切换皮肤
            SkinManager.loadSkinApk(path)
            //更新当前mType
            mType = newSkinCardView
            //关闭animation
            newSkinCardView.stopAnimation()
        }
    }

    /**
     * 拼接path
     */
    private fun mergePath(id: String): String {
        return mBasePath + id + ".apk"
    }

    private fun getType(path: Any?): SkinCardView {
        if (TextUtils.isEmpty(path as CharSequence?)) {

            return mBinding.skinItem1
        }
        val s = path as String
        if (s.contains("1")) {
            return mBinding.skinItem2
        } else if (s.contains("2")) {
            return mBinding.skinItem3
        } else if (s.contains("3")) {
            return mBinding.skinItem4
        } else if (s.contains("4")) {
            return mBinding.skinItem5
        } else if (s.contains("5")) {
            return mBinding.skinItem6
        } else if (s.contains("6")) {
            return mBinding.skinItem7
        } else if (s.contains("7")) {
            return mBinding.skinItem8
        }
        return mBinding.skinItem9

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