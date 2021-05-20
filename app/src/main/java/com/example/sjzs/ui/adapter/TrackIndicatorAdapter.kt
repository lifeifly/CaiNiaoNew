package com.example.sjzs.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.sjzs.R
import com.example.video.viewpagerindicator.ColorTrackTextView
import com.example.video.viewpagerindicator.ElongationCompressionView
import com.example.video.viewpagerindicator.IndicatorAdapter

class TrackIndicatorAdapter(private val context: Context) : IndicatorAdapter() {
    private val mDatas = mutableListOf<String>("海内", "国际", "社会", "法治", "文娱", "科技", "生活")
    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getView(position: Int, parent: ViewGroup): View {
        val colorTrackTextView = ColorTrackTextView(context)
        colorTrackTextView.setChangeColor(Color.RED)
        colorTrackTextView.setNormalColor(Color.BLACK)
        colorTrackTextView.setText(mDatas[position])
        colorTrackTextView.textSize = 35F
        return colorTrackTextView
    }


    override fun resetIndicator(view: View) {
        (view as ColorTrackTextView).setCurrentProgress(0F)
    }

    override fun getBottomTrackView(): View {
        val view = ElongationCompressionView(context)
        //设置渐变色
        val colors = intArrayOf(
            Color.parseColor("#EE03A9F4"),
            Color.parseColor("#BB03C4F4"),
            Color.parseColor("#BB9C27B0"),
            Color.parseColor("#BBB02757")
        )
        val bg = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        view.background = bg
        view.layoutParams = FrameLayout.LayoutParams(30, 10)
        return view
    }
}