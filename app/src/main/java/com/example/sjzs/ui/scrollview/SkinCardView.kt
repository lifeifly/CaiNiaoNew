package com.example.sjzs.ui.scrollview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.sjzs.R
import com.example.sjzs.helper.Utils
import com.example.video.video.utils.ToolUtils

class SkinCardView:FrameLayout {

    private lateinit var mProgressIv:ImageView
    private lateinit var mCardView:CardView
    private lateinit var mTv:TextView

    //选中的边框
    private  var bgColor:Int= Color.BLACK
    private  var cornerRadius:Float= 0F
    private  var elevation1:Float= 0F
    private  var colorName:String= ""
    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        //将布局文件加载到该view中
        View.inflate(context, R.layout.skin_item_layout,this)
        //获取自定义的color和text
        val typeArray=context.obtainStyledAttributes(attrs,R.styleable.SkinCardView)
        bgColor=typeArray.getColor(R.styleable.SkinCardView_bgColor,bgColor)
        cornerRadius=typeArray.getDimension(R.styleable.SkinCardView_cornerRadius,
            Utils.dp2Px(10F,context)
        )
        colorName= typeArray.getString(R.styleable.SkinCardView_colorName).toString()
        elevation1=typeArray.getDimension(R.styleable.SkinCardView_elevation,
            Utils.dp2Px(5F,context)
        )

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mProgressIv=findViewById(R.id.card_progress)
        mCardView=findViewById(R.id.card_container)
        mTv=findViewById(R.id.card_tv)
        initView()
    }

    private fun initView() {
        mTv.setText(colorName)
        mCardView.setCardBackgroundColor(bgColor)
        mCardView.cardElevation=elevation1
        mCardView.radius=cornerRadius

    }


    /**
     * 开启加载动画
     */
    fun startAnimation(){
        addFrame()
        mProgressIv.visibility=View.VISIBLE
        (mProgressIv.drawable as AnimationDrawable).start()
    }
    /**
     * 关闭加载动画,加载完成
     */
    fun stopAnimation(){
        (mProgressIv.drawable as AnimationDrawable).stop()
        mProgressIv.visibility=View.GONE

    }

    /**
     * 添加边框
     */
    fun addFrame(){
        setBackgroundResource(R.drawable.checked_frame)
    }
    /**
     * 取消边框
     */
    fun deleteFrame(){
        setBackgroundColor(Color.TRANSPARENT)
    }
}