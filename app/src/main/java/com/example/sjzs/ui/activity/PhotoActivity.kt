package com.example.sjzs.ui.activity

import android.app.Dialog
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.withTransaction
import com.example.sjzs.R
import com.example.sjzs.databinding.ActivityPhotoBinding
import com.example.sjzs.helper.ShareUtils
import com.example.sjzs.helper.Utils
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.room.CollectDao
import com.example.sjzs.model.room.ListBeanDatabase
import com.example.sjzs.ui.base.BaseActivity
import com.example.sjzs.viewmodel.PhotoActivityDataViewModel
import com.example.sjzs.viewmodel.PhotoActivityRequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhotoActivity : BaseActivity(), View.OnClickListener {
    private val mPhotoDataVm: PhotoActivityDataViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(PhotoActivityDataViewModel::class.java)
    }
    private val mPhotoRequestVm: PhotoActivityRequestViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(PhotoActivityRequestViewModel::class.java)
    }

    //记录是否被收藏
    private var isCollected = false

    //存储收藏的数据库
    private lateinit var mDataBase: ListBeanDatabase
    private lateinit var mCollectDao:CollectDao
    private var passedCollectBean:CollectBean?=null

    private lateinit var mIvCollect:ImageView
    private lateinit var mIvShare:ImageView

    private lateinit var dialog: Dialog
    override fun initView() {
        val intent = intent
        passedCollectBean = intent.getParcelableExtra("data")

        //给toolbar设置返回键
        val toolbar = (binding as ActivityPhotoBinding).toolBar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)//设置返回键可用
        initTool()

        initDialog()

        passedCollectBean?.let { it.url?.let { it1 -> isCollected(it1) } }

    }
    fun initTool(){
        mIvCollect=binding.root.findViewById(R.id.iv_collect)
        mIvShare=binding.root.findViewById(R.id.iv_share)
        mIvCollect.setOnClickListener(this)
        mIvShare.setOnClickListener(this)
    }
    fun isCollected(url:String){
        //查寻
        mDataBase=ListBeanDatabase.getDatabase(this.applicationContext)
        mCollectDao=mDataBase.collectDao()
        GlobalScope.launch (Dispatchers.Main){
            val collectBean=mCollectDao.queryByUrl(url)
            if (collectBean!=null){
                isCollected=true
                mIvCollect.setImageResource(R.drawable.ic_collect_selected)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_photo
    }

    override fun bind() {
        (binding as ActivityPhotoBinding).dataVM = mPhotoDataVm
        binding.setLifecycleOwner(this)
        mPhotoRequestVm.setDataViewModel(mPhotoDataVm)
        mPhotoRequestVm.mHostActivity = this
        mPhotoDataVm.data.observe(this, Observer {
            dismissLoading()
            complete()
        })
    }



    override fun requestData() {
        //显示加载动画
        showLoading()
        //检测网络
        if (Utils.isNetworkConnected(context = this)) {

            if (passedCollectBean?.url != null) {
                Log.d("ARTICLE", "requestData: " + passedCollectBean?.url)
                mPhotoRequestVm.requestData(passedCollectBean?.url!!)
            }
        } else {
            showError("网络异常，点击重试")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.iv_share->{
                dialog.show()
            }
            R.id.iv_collect->{
                isCollected=!isCollected
                if (isCollected){
                    //把收藏存入数据库
                    GlobalScope.launch {
                        mDataBase.withTransaction {
                            if (passedCollectBean!=null){
                                passedCollectBean?.date=System.currentTimeMillis()
                                mCollectDao.insertSingleBean(passedCollectBean!!)
                            }
                        }
                    }
                    //设置收藏的图片
                    mIvCollect.setImageResource(R.drawable.ic_collect_selected)
                }
                else{
                    //数据库中删除
                    GlobalScope.launch {
                        mDataBase.withTransaction {
                            mCollectDao.deleteSingleByUrl(passedCollectBean?.url!!)
                        }
                    }
                    //设置正常的图片
                    mIvCollect.setImageResource(R.drawable.ic_collect)
                }
            }
            R.id.share_qq->{
                if (passedCollectBean!=null){
                    val shareUtils= ShareUtils(this)
                    shareUtils.shareToQQ(passedCollectBean?.title!!,passedCollectBean?.url!!)
                }
            }
            R.id.share_wechat->{
                if (passedCollectBean!=null){
                    val shareUtils= ShareUtils(this)
                    shareUtils.shareToWeChat(passedCollectBean?.title!!,passedCollectBean?.url!!)
                }
            }
            R.id.cancle_btn->{
                if(dialog.isShowing){
                    dialog.dismiss()
                }
            }
        }

    }
    /**
     * 显示分享dialog
     */
    fun initDialog(){
        //初始化dialog
        val view = LayoutInflater.from(this).inflate(R.layout.share_dialog_layout,null)
        val ivQQ=view.findViewById<ImageView>(R.id.share_qq)
        ivQQ.setOnClickListener(this)
        val ivWecaht=view.findViewById<ImageView>(R.id.share_wechat)
        ivWecaht.setOnClickListener(this)
        val cancelBtn=view.findViewById<Button>(R.id.cancle_btn)
        cancelBtn.setOnClickListener(this)
        dialog= Dialog(this)
        dialog.setContentView(view)
        //点击外部取消
        dialog.setCanceledOnTouchOutside(true)
        val window=dialog.window
        //下部对齐
        window?.setGravity(Gravity.BOTTOM)
        val params=window?.attributes
        //设置宽高
        params?.width=(resources.displayMetrics.widthPixels*0.9).toInt()
        params?.height= WindowManager.LayoutParams.WRAP_CONTENT

        window?.attributes=params
        window?.setBackgroundDrawableResource(R.drawable.search_bg)
        //设置动画
        window?.setWindowAnimations(R.style.ActionSheetDialog)
    }
}