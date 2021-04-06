package com.example.video.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

abstract class VideoLifeCycleCallback : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    abstract fun onCreate()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    abstract fun onStart()


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    abstract fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    abstract fun onPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    abstract fun onStop()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    abstract fun onDestroy()
}