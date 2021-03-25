package com.example.sjzs.model.observer

abstract class ResponseObserver<T> {

    abstract fun onSuccess(t: T)
    abstract fun onFailure(e: Throwable)
}