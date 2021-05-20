package com.example.sjzs.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banner.BannerPagerAdapter
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.FragmentPictureBinding
import com.example.sjzs.databinding.PictureBannerItemBinding
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.bean.RollData
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.activity.SearchActivity
import com.example.sjzs.ui.adapter.PicturePagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.click.IItemClick
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.PictureDataViewModel
import com.example.sjzs.viewmodel.PictureRequestViewModel
import kotlinx.coroutines.launch

class PictureFragment : AbsBaseFragment() {

    private lateinit var picturePagingAdapter: PicturePagingAdapter
    private lateinit var bind: FragmentPictureBinding
    private val dataViewModel: PictureDataViewModel
            by lazy {
                ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
                ).get(
                    PictureDataViewModel::class.java
                )
            }

    private val requestViewModel: PictureRequestViewModel
            by lazy {
                ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
                ).get(PictureRequestViewModel::class.java)
            }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_picture
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestViewModel.setDataViewModel(dataViewModel)
    }

    override fun initView(rootView: View) {
        bind = FragmentPictureBinding.bind(rootView)
        bind.searchTv.setOnClickListener { v ->
            val intent=Intent(activity as Context,SearchActivity::class.java)
            (activity as Context).startActivity(intent)
        }
        setRecyclerViewData()
        initBanner()
    }

    /**
     * 监听recyclerview的数据变化
     */
    fun setRecyclerViewData() {
        //设置头部底部刷新
        picturePagingAdapter = PicturePagingAdapter(
            activity as Context
        )

        bind.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bind.recyclerView.adapter = picturePagingAdapter

        //添加分割线
        bind.recyclerView.addItemDecoration(RvDivider())
        //监听数据发生变化，刷新RecyclerView
        dataViewModel.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                picturePagingAdapter.submitData(it)
            }
        })
    }


    /**
     * 设置banner的监听
     */
    fun initBanner() {
        dataViewModel.bannerList.observe(this, Observer {
            bind.bannerView.setAdapter(object : BannerPagerAdapter(){
                override fun getView(position: Int, convertView: View?): View {
                    val bannerDataBinding: PictureBannerItemBinding =
                        DataBindingUtil.inflate(
                            LayoutInflater.from(activity),
                            R.layout.picture_banner_item, bind.bannerView, false
                        )

                    //设置点击事件
                    bannerDataBinding.itemClick = object : IItemClick<RollData> {
                        override fun onClick(view: View, t: RollData) {
                            val intent = Intent()
                            intent.putExtra("data", CollectBean(t.id,t.title,t.image,t.url,0))
                            if (t.type.contains("ARTI")) {
                                intent.setClass(view.context, ArticleActivity::class.java)
                            } else if (t.type.contains("PHOA")) {
                                intent.setClass(view.context, PhotoActivity::class.java)
                            }
                            view.context.startActivity(intent)
                        }
                    }

                    bannerDataBinding.setVariable(BR.picturebanner, it?.get(position))
                    return bannerDataBinding.root
                }

                override fun getCount(): Int {
                    if (it != null) {
                        return it.size
                    }
                    return 0
                }
            })
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

    override fun showError(text: String) {

    }



}