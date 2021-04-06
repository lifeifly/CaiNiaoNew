package com.example.video.network

import android.util.Log
import android.util.SparseArray
import com.example.ffmpeg07.VideoMerge
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.internal.threadFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.*

class DownloadManager(baseUrl: String) {
    private val mBaseUrl: String = baseUrl

    //当前的状态0是运行，1是停止
    private var mStatus = 0

    @Volatile
    private var count = 0
    private val executor: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(
            0, 3, 30, TimeUnit.SECONDS,
            LinkedBlockingDeque(), threadFactory("Download Dispatcher", false)
        )
    }


    //保存视频文件的集合
    private val videoMap: SparseArray<File> by lazy { SparseArray<File>() }
    fun downloadVideos(path: String, completeCallback: VideoMerge.CompleteCallback) {
        //在创建一个子线程去监控线程池是否执行完毕
        try {
            Observable.just(executor)
                .map {
                    var isEnd = false
                    while (!isEnd && !executor.isShutdown) {
                        val s = count++
                        val id = "$s.ts"
                        executor.execute(
                            DownloadRunnable(
                                mBaseUrl,
                                id,
                                path,
                                s,
                                object : DownCallback {
                                    override fun onSuccess(code: Int, file: File?) {
                                        //下载成功
                                        if (code == 404) {//未找到资源，停止添加runnable
                                            isEnd = true
                                            count = 0
                                            executor.shutdown()
                                        } else if (code == 200) {
                                            if (file != null) {
                                                videoMap.put(s, file)
                                                Log.d("TAG", "onSuccess: $s")
                                            }
                                        }
                                    }

                                    override fun onError(e: Exception) {
                                        //请求失败
                                        executor.shutdownNow()
                                        //链接链接错误
                                        completeCallback.onError(e)
                                    }
                                })
                        )
                        Thread.sleep(100)
                    }
                    return@map monitor(executor)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Boolean) {
                        //执行完毕，进行合成
                        Log.d("meger", "onNext: ")
                        if (mStatus == 0) {
                            val vm = VideoMerge()
                            vm.merge(completeCallback, videoMap, path)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (completeCallback != null) {
                            completeCallback.onError(e as Exception?)
                        }
                    }
                })
        } catch (e: Exception) {
            completeCallback.onError(e)
        }
    }

    /**
     * 停止下载和合并视频
     */
    fun stopDownloadAndMerge() {
        executor.shutdownNow()
        //改变状态
        mStatus = 1
    }

    //监控线程的执行完毕
    fun monitor(executor: ThreadPoolExecutor): Boolean {
        while (!isTerminated(executor)) {
            val queueSize = executor.getQueue().size
            val activeCount = executor.getActiveCount()
            val completedTaskCount = executor.getCompletedTaskCount()
            val taskCount = executor.getTaskCount()
            Log.d("TIME", "monitor: ")
            Thread.sleep(1000)
        }
        return true
    }

    /**
     * 判断线程池是否执行完毕
     */
    fun isTerminated(executor: ThreadPoolExecutor): Boolean {
        Log.d("TERMINATED", "isTerminated: " + executor.getQueue().size + executor.getActiveCount())
        return executor.getQueue().size == 0 && executor.getActiveCount() == 0
    }

    class DownloadRunnable(
        url: String,
        id: String,
        path: String,
        count: Int,
        callback: DownCallback
    ) : Runnable {
        private val mUrl = url
        private val mId = id
        private val mPath = path
        private val mCount = count
        private val mCallback = callback
        override fun run() {
            val url = mUrl + File.separator + mId
            OkHttpManager.async(url).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mCallback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    var file: File? = null
                    if (response.code == 200) {
                        //下载
                        file = File(mPath, mId)
                        val bs = response.body?.byteStream()
                        val fos = FileOutputStream(file)

                        val byteArray = ByteArray(2048)
                        var len = bs?.read(byteArray)
                        while (len != -1) {
                            fos.write(byteArray, 0, len!!)
                            len = bs?.read(byteArray)
                        }
                    }
                    mCallback.onSuccess(response.code, file)
                }
            })
        }
    }

    interface DownCallback {
        fun onSuccess(code: Int, file: File?)
        fun onError(e: Exception)
    }
}

