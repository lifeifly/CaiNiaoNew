package com.example.sjzs.ui.activity

import android.animation.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sjzs.R
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.jar.Manifest

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()

        setContentView(R.layout.activity_welcome)
        val animatorX = ObjectAnimator.ofFloat(welcome_iv, "scaleX", 1F, 2F)
        val animatorY = ObjectAnimator.ofFloat(welcome_iv, "scaleY", 1F, 2F)
        val animator = AnimatorSet()
        animator.playTogether(animatorX, animatorY)
        animator.duration = 2000
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                //动画结束申请权限
                checkPermissionRequest()
            }
        })
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //使用WindowInsetContoller
            val controller=ViewCompat.getWindowInsetsController(window.decorView)
            //隐藏系统bar，导航栏和状态栏
            controller?.show(WindowInsetsCompat.Type.systemBars())
        } else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //解决水滴屏全屏
                val lp = window.attributes
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                lp.flags= lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                window.attributes = lp

            }else{
                //解决水滴屏全屏
                val lp = window.attributes
                lp.flags= lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                window.attributes = lp
            }
    }

    /**
     * 申请权限
     */
    private fun checkPermissionRequest() {
        val permission = RxPermissions(this)
        //申请文件读写权限
        permission.request(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
            .subscribe(object : Consumer<Boolean> {
                override fun accept(t: Boolean?) {
                    if (t != null && t) {
                        //申请成功，开始进入主界面
                        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@WelcomeActivity, "未赋予权限，不能正常使用", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            })


    }
}