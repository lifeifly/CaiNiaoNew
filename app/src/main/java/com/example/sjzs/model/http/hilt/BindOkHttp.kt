package com.example.sjzs.model.http.hilt

import javax.inject.Qualifier

/**
 * 用来区分hilt的哪个方法提供对象，多个方法就需要多个这届
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class BindOkHttp {
}