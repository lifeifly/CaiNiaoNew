package com.example.thumbupsample

import android.content.Context
import android.util.TypedValue
import android.view.View

/**
 * dp转px
 */
fun dp2Px(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

fun sp2Px(context: Context, sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics
    )
}

fun getOtherDefaultSize(size: Int, measureSpec: Int): Int {
    var result = size
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)
    when (specMode) {
        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.AT_MOST -> {

        }
        View.MeasureSpec.EXACTLY -> {
            result = specSize
            result = Math.max(result, size)
        }
    }
    return result
}

fun evaluate(fraction: Float, startColor: Int, endColor: Int): Int {
    val startInt = startColor
    var startA = ((startInt shr 24) and 0xff) / 255.0F
    var startR = ((startInt shr 16) and 0xff) / 255.0F
    var startG = ((startInt shr 8) and 0xff) / 255.0F
    var startB = (startInt and 0xff) / 255.0F

    val endInt = endColor
    var endA = ((endInt shr 24) and 0xff) / 255.0F
    var endR = ((endInt shr 16) and 0xff) / 255.0F
    var endG = ((endInt shr 8) and 0xff) / 255.0F
    var endB = (endInt and 0xff) / 255.0F

    //转化成线性
    startR = Math.pow(startR.toDouble(), 2.2).toFloat()
    startG = Math.pow(startG.toDouble(), 2.2).toFloat()
    startB = Math.pow(startB.toDouble(), 2.2).toFloat()

    endR = Math.pow(endR.toDouble(), 2.2).toFloat()
    endG = Math.pow(endG.toDouble(), 2.2).toFloat()
    endB = Math.pow(endB.toDouble(), 2.2).toFloat()

    //计算在线性控件的插值颜色
    var a = startA + fraction * (endA - startA)
    var r = startR + fraction * (endR - startR)
    var g = startG + fraction * (endG - startG)
    var b = startB + fraction * (endB - startB)

    // convert back to sRGB in the [0..255] range

    // convert back to sRGB in the [0..255] range
    a = a * 255.0f
    r = Math.pow(r.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
    g = Math.pow(g.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
    b = Math.pow(b.toDouble(), 1.0 / 2.2).toFloat() * 255.0f

    return (Math.round(a) shl 24) or (Math.round(r) shl 16) or (Math.round(g) shl 8) or (Math.round(
        b
    ))
}