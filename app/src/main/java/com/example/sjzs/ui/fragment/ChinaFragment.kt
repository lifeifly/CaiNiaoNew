package com.example.sjzs.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.banner.BannerPagerAdapter
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.ChinaBannerItemBinding
import com.example.sjzs.databinding.FragmentChinaBinding
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.adapter.ChinaPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.click.IItemClick
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.ChinaDataViewModel
import com.example.sjzs.viewmodel.ChinaRequestViewModel
import kotlinx.coroutines.launch

class ChinaFragment : AbsBaseLazyFragment() {

    private lateinit var chinaPagingAdapter: ChinaPagingAdapter
    private lateinit var bind: FragmentChinaBinding
    private val dataViewModel: ChinaDataViewModel
            by lazy {
                ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
                ).get(
                    ChinaDataViewModel::class.java
                )
            }

    private val requestViewModel: ChinaRequestViewModel
            by lazy {
                ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
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
        bind = FragmentChinaBinding.bind(rootView)
        setRecyclerViewData()
        initBanner()
    }



    /**
     * 监听recyclerview的数据变化
     */
    fun setRecyclerViewData() {
        //设置头部底部刷新
        chinaPagingAdapter = ChinaPagingAdapter(
            activity as Context
        )

        bind.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bind.recyclerView.adapter = chinaPagingAdapter

        //添加分割线
        bind.recyclerView.addItemDecoration(RvDivider())
        //监听数据发生变化，刷新RecyclerView
        dataViewModel.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                chinaPagingAdapter.submitData(it)
            }
        })
    }


    /**
     * 设置banner的监听
     */
    fun initBanner() {
        //监听数据变化，刷新BannerView
        dataViewModel.bannerData.observe(this, object : Observer<List<CommomBanner>> {
            override fun onChanged(t: List<CommomBanner>?) {
                bind.bannerView.setAdapter(object : BannerPagerAdapter(), IItemClick<CommomBanner> {
                    override fun getView(position: Int, convertView: View?): View {
                        val bannerDataBinding: ChinaBannerItemBinding =
                            DataBindingUtil.inflate(
                                LayoutInflater.from(activity),
                                R.layout.china_banner_item, bind.bannerView, false
                            )
                        //设置shimmerlayout
                        bannerDataBinding.shimmerLayout.apply {
                            //系统默认红色，设置自己想要的颜色
                            setShimmerColor(0x55FFFFFF)
                            //闪动角度
                            setShimmerAngle(15)
                            startShimmerAnimation()
                        }
                        //为glide设置listener，加载完成后取消shimmer的闪动
                        bannerDataBinding.listener1 = object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false.also { bannerDataBinding.shimmerLayout.stopShimmerAnimation() }
                            }
                        }
                        //设置点击事件
                        bannerDataBinding.itemClick = this
                        bannerDataBinding.setVariable(BR.comonbanner, t?.get(position))
                        return bannerDataBinding.root
                    }

                    override fun getCount(): Int {
                        if (t != null) {
                            return t.size
                        }
                        return 0
                    }

                    override fun onClick(view: View, t: CommomBanner) {
                        val intent: Intent = Intent()
                        intent.putExtra("data", CollectBean(t.href,t.title,t.srcHref,t.href,0))
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

    override fun showError(text: String) {

    }




}