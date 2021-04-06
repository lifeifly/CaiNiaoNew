package com.example.video.banner

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap

class DotIndicatorView : View {
    var mDrawable: Drawable? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
//            mDrawable?.setBounds(0,0,measuredWidth,measuredHeight)
//            mDrawable?.draw(canvas)
            val bitmap=drawableToBitmap(mDrawable)
            val circleBitmap=getCircleBitmap(bitmap)
            canvas.drawBitmap(circleBitmap,0F,0F,null)


        }
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        //创建一个bitmap
        val circleBitmap=Bitmap.createBitmap(measuredWidth,measuredHeight,Bitmap.Config.ARGB_8888)
        val circleCanvas=Canvas(circleBitmap)
        val paint=Paint()
        paint.isAntiAlias=true
        paint.isFilterBitmap=true
        paint.isDither=true
        //在画布上画圆
        circleCanvas.drawCircle((measuredWidth/2).toFloat(), (measuredHeight/2).toFloat(),(measuredWidth/2).toFloat(),paint)
        //取圆和矩形的交集
        paint.xfermode=PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //在把bitmap绘制到新的上面
        circleCanvas.drawBitmap(bitmap,0F,0F,paint)
        //内存泄漏，需回收bitmap
        bitmap.recycle()

        return circleBitmap
    }

    private fun drawableToBitmap(mDrawable: Drawable?):Bitmap {
        //BitmapDrawablel类型
        if (mDrawable is BitmapDrawable){
            return (mDrawable as BitmapDrawable).bitmap
        }

        //其他时ColorDrawable类型
        //创建一个什么也没有的bitmap
        val outBitmap=Bitmap.createBitmap(measuredWidth,measuredHeight,Bitmap.Config.ARGB_8888)

        //创建画布
        val outCanvas=Canvas(outBitmap)
        //把drawable画到bitmap上
        mDrawable?.setBounds(0,0,measuredWidth,measuredHeight)
        mDrawable?.draw(outCanvas)
        return outBitmap
    }

    //设置drawable
    fun setDrawable(drawable: Drawable?) {
        this.mDrawable = drawable
        invalidate()
    }

}