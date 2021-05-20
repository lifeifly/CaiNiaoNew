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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.banner.BannerPagerAdapter
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.FragmentPictureBinding
import com.example.sjzs.databinding.FragmentVideoBinding
import com.example.sjzs.databinding.VideoBannerItemBinding
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.bean.VideoList
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.activity.SearchActivity
import com.example.sjzs.ui.adapter.VideoPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.click.IItemClick
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.ui.recyclerview.RvGridDivider
import com.example.sjzs.viewmodel.VideoDataViewModel
import com.example.sjzs.viewmodel.VideoRequestViewModel
import kotlinx.coroutines.launch

class VideoFragment:AbsBaseFragment() {
    private lateinit var mBind: FragmentVideoBinding
    private val dataVM:VideoDataViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(VideoDataViewModel::class.java)
    }
    private val requestVM:VideoRequestViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(VideoRequestViewModel::class.java)
    }
    private lateinit var mAdapter:VideoPagingAdapter
    override fun getLayoutRes(): Int {
        return R.layout.fragment_video
    }

    override fun initView(rootView: View) {
        mBind= FragmentVideoBinding.bind(rootView)
        mBind.searchTv.setOnClickListener { v ->
            val intent=Intent(activity as Context, SearchActivity::class.java)
            (activity as Context).startActivity(intent)
        }
        setRecyclerView()
        initBanner()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestVM.setDataViewModel(dataVM)
    }

    private fun initBanner() {
        dataVM.bannerList.observe(this, Observer {
            mBind.bannerView.setAdapter(object :BannerPagerAdapter(){
                override fun getView(position: Int, convertView: View?): View {
                    val videoBannerItemBinding=DataBindingUtil.inflate<VideoBannerItemBinding>(
                        LayoutInflater.from(activity),R.layout.video_banner_item,mBind.bannerView,false)
                    videoBannerItemBinding.itemClick=object :IItemClick<VideoList>{
                        override fun onClick(view: View, t: VideoList) {
                            val intent: Intent = Intent()
                            intent.putExtra("data", CollectBean(t.id,t.title,t.image,t.url,0 ))
                            if (t.id.contains("ARTI")) {
                                intent.setClass(view.context, ArticleActivity::class.java)
                            } else if (t.id.contains("PHOA")) {
                                intent.setClass(view.context, PhotoActivity::class.java)
                            }
                            view.context.startActivity(intent)
                        }
                    }
                    videoBannerItemBinding.setVariable(BR.bannervideolist,it.get(position))
                    return videoBannerItemBinding.root
                }

                override fun getCount(): Int {
                    return it.size
                }
            })
        })
    }

    private fun setRecyclerView() {
        mAdapter= VideoPagingAdapter(activity as Context)
        mBind.recyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        mBind.recyclerView.addItemDecoration(RvGridDivider(10))
        mBind.recyclerView.adapter=mAdapter
        dataVM.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                mAdapter.submitData(it)
            }
        })
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun requestData() {
        requestVM.requestData()
    }

    override fun complete() {

    }

    override fun showError(text: String) {

    }


}