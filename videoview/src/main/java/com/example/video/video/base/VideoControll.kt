package com.example.video.video.base

interface VideoControll {
    fun play(position:Int)
    fun pause()
    fun fullScreen()
    fun hideProgress()
    fun showProgress(progress: Int)
    //把拖拽状态传给VideoView
    fun setIsDrag(isDrag:Boolean)
}