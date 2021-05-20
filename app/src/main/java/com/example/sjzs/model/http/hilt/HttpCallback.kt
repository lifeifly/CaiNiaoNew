package com.example.sjzs.model.http.hilt

import java.lang.reflect.ParameterizedType

abstract class HttpCallback<T>:IHttpCallback<T> {

    override fun onSuccess(result: T) {
        onResult(result)
    }

    override fun onError(message: Throwable) {
        onFailure(message)
    }
    /**
     * 把结果返回出去
     */
    abstract fun onResult(result: T)

    /**
     * 把错误返回
     */
    abstract fun onFailure(message: Throwable)
    /**
     * 获取输入参数的实际类型
     */
    private fun getParamterType():Class<T>{
        //获取泛型的类型
        val type=javaClass.genericSuperclass
        //获取真实类型参数
        val params=(type as ParameterizedType).actualTypeArguments
        return params[0] as Class<T>//因为就传了一个泛型，所以时第0个
    }
}