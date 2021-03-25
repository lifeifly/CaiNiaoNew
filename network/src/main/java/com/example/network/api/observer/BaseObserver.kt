package com.example.network.api.observer

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T>:Observer<T> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        onFailure(e)
    }

    //只暴露成功或失败的方法给用户实现
    abstract fun onSuccess(t: T)

    abstract fun onFailure(e: Throwable)
}