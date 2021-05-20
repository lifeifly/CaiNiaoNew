package com.example.sjzs.model.http.hilt

interface IHttpCallback<T> {

    fun  onSuccess(result:T)
    fun onError(message:Throwable)
}