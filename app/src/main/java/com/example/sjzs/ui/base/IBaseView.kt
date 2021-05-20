package com.example.sjzs.ui.base

interface IBaseView {
    //显示Loading页面
    fun showLoading()

    //刷新成功，取消Laoding
    fun dismissLoading()

    //请求数据
    fun requestData()

    //刷新数据
    fun complete()

    //请求失败
    fun showError(text:String)
}