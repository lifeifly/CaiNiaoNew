package com.example.test

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log


class VideoUtils(val videoInformations: VideoInformations) {
    private val handler: Handler = Handler(Looper.getMainLooper())
    //获取视频的宽高,和时长
    fun getVideoWidthAndHeightAndVideoTimes(videoUrl: String) {
        val mediaMetadataRetriever = MediaMetadataRetriever()

        object : Thread() {
            override fun run() {
                var totalTime=0f
                var videoTimes = 0f
                var videoWidth = 0f
                var videoHeight = 0f
                super.run()
                for (i in 1..50) {
                    val realUrl=videoUrl+i+".ts"
                    mediaMetadataRetriever.setDataSource(realUrl, HashMap())

                    videoTimes = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toFloat()
                    if (i==1){
                        videoWidth = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!!.toFloat()
                        videoHeight = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!!.toFloat()
                        handler.post {
                            videoInformations.dealWithVideoInformation(videoWidth, videoHeight, videoTimes)
                        }
                    }
                    Log.i("zzm", "视频的长度：  $videoTimes")
                    totalTime+=videoTimes

                }
                Log.i("zzm", "视频的宽：  $videoWidth")
                Log.i("zzm", "视频的高：  $videoHeight")
                Log.i("zzm", "视频的长度：  $totalTime")
                mediaMetadataRetriever.release()
            }
        }.start()
    }

    interface VideoInformations {
        fun dealWithVideoInformation(w: Float, h: Float, vt: Float)
    }

}
