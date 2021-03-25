package com.example.sjzs.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neihanduanzi.banner.BannerItemAdaper
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.BannerItemViewLayoutBinding
import com.example.sjzs.databinding.FragmentChinaBinding
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.ui.adapter.SocietyRvAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.click.IItemClick
import com.example.sjzs.viewmodel.ChinaDataViewModel
import com.example.sjzs.viewmodel.ChinaRequestViewModel

class ChinaFragment : AbsBaseFragment() {
    private lateinit var societyRvAdapter: SocietyRvAdapter

    private val dataViewModel: ChinaDataViewModel
            by lazy {
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                    ChinaDataViewModel::class.java
                )
            }

    private val requestViewModel: ChinaRequestViewModel
            by lazy {
                ViewModelProvider(
                    this,
                    ViewModelProvider.NewInstanceFactory()
                ).get(ChinaRequestViewModel::class.java)
            }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_china
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestViewModel.setDataViewModel(dataViewModel)
    }

    override fun initView(rootView: View) {
        val bind = FragmentChinaBinding.bind(rootView)

        //为bannerview设置适配器
        societyRvAdapter = SocietyRvAdapter(activity as Context)
        bind.recyclerView.adapter = societyRvAdapter
        bind.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        //添加分割线
        bind.recyclerView.addItemDecoration(RvDivider())
        //监听数据发生变化，刷新RecyclerView
        dataViewModel.listData.observe(this, object : Observer<List<ListBean>> {
            override fun onChanged(t: List<ListBean>?) {
                if (t != null) {
                    societyRvAdapter.refreshUI(t)
                }
            }
        })
        //监听数据变化，刷新BannerView
        dataViewModel.bannerData.observe(this, object : Observer<List<CommomBanner>> {
            override fun onChanged(t: List<CommomBanner>?) {
                bind.bannerView.setAdapter(object : BannerItemAdaper(), IItemClick<CommomBanner> {
                    override fun getView(position: Int, convertView: View?): View {
                        val bannerDataBinding: com.example.sjzs.databinding.BannerItemViewLayoutBinding =
                            DataBindingUtil.inflate<BannerItemViewLayoutBinding>(
                                LayoutInflater.from(activity),
                                R.layout.banner_item_view_layout, bind.bannerView, false
                            )
                        //设置点击事件
                        bannerDataBinding.itemClick = this

                        bannerDataBinding.setVariable(BR.coomonbanner, t?.get(position))
                        return bannerDataBinding.root
                    }

                    override fun getCount(): Int {
                        if (t != null && t.size > 0) {
                            if (t.size > 1) {
                                bind.bannerView.startRoll()
                            }
                            return t.size
                        }
                        return 0
                    }

                    override fun onClick(view: View, t: CommomBanner) {
                        val intent: Intent = Intent()
                        intent.putExtra("data", t.href)
                        if (t.href.contains("ARTI")) {
                            intent.setClass(view.context, ArticleActivity::class.java)
                        } else if (t.href.contains("PHOA")) {
                            intent.setClass(view.context, PhotoActivity::class.java)
                        }
                        view.context.startActivity(intent)
                    }
                })
            }
        })
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun requestData() {
        requestViewModel.requestData()
    }

    override fun complete() {

    }




}