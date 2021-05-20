package com.example.sjzs.ui.scrollview

import android.content.Context
import android.view.ViewConfiguration

class FlingHelper(private val context: Context) {
    init {
        mPhysicalCoeff = context.resources.displayMetrics.density * 160F * 386.0878F * 0.84F
    }

    companion object {
        private val DECELERATION_RATE = (Math.log(0.78) / Math.log(0.9)).toFloat()
        private val mFlingFriction = ViewConfiguration.getScrollFriction()
        private var mPhysicalCoeff: Float = 0F
    }

     fun getSplineDeceleration(i: Int): Double {
        return Math.log((0.35F * Math.abs(i)).toDouble()) / (mFlingFriction * mPhysicalCoeff)
    }

     fun getSplineDecelerationByDistance(d:Double): Double {
        return ((DECELERATION_RATE-1.0F).toDouble()*Math.log(d/(mFlingFriction* mPhysicalCoeff)))/ DECELERATION_RATE
    }

     fun getSplineDistance(i: Int): Double {
        return Math.exp(getSplineDeceleration(i)*(DECELERATION_RATE/ (DECELERATION_RATE-1.0F)))*(mFlingFriction * mPhysicalCoeff)
    }

     fun getVelocityDistance(d:Double): Double {
        return Math.abs((Math.exp(getSplineDecelerationByDistance(d)* mFlingFriction)* mPhysicalCoeff)/0.349999940395355)
    }



}