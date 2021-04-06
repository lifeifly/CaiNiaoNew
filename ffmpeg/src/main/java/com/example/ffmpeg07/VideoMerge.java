package com.example.ffmpeg07;

import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VideoMerge {

    // Used to load the 'native-lib' library on application startup.
    static {

        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avcodec");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");
        System.loadLibrary("native-lib");

    }


    public void merge(CompleteCallback mCallback, SparseArray<File> videomap, String path) {

        //输出文件
        File targetFile = new File(path, "output.mp4");
        File transFile = new File(path, "output1.mp4");
        File fileList = new File(path, "filelist.txt");
        try {
            if (!transFile.exists()) {
                transFile.createNewFile();
            }
            if (!fileList.exists()) {
                fileList.createNewFile();
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            //把要合并的写入文件FileList,并执行命令，由于耗时操作，需要在子线程完成
            writeOutput(path, mCallback, videomap, fileList, targetFile, transFile);

        } catch (IOException e) {
            e.printStackTrace();
            if (mCallback!=null){
                mCallback.onError(e);
            }
        }
    }

    private void writeOutput(String headPath, CompleteCallback callback, SparseArray<File> videomap, File fileList, File targetFile, File transFile) {
        Observable.just(videomap)
                .map(new Function<SparseArray<File>, String[]>() {
                    @Override
                    public String[] apply(SparseArray<File> fileSparseArray) throws Throwable {
                        //写入
                        FileUtils.writeFileList(fileList, videomap);
                        //组成命令
                        String str = "ffmpeg -f concat -safe 0 -i %s -c copy %s";
                        String s = String.format(str, fileList.getAbsolutePath(), targetFile.getAbsolutePath());
                        String[] commands = s.split("\\s+");
                        Log.d("path", "command->" + Arrays.toString(commands));
                        Log.d("path", "target->" + targetFile.getAbsolutePath());
                        return commands;
                    }
                })
                .map(command -> {
                    mergeVideo(command);
                    Log.d("TAG", "writeOutput: ");
                    return 0;
                })
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        //帧内编码
                        //组成命令
//                        String str = "ffmpeg -i %s -strict -2 -qscale 0 -intra %s";
                        String str = "ffmpeg -i %s -g 5 %s";
                        String s = String.format(str, targetFile.getAbsolutePath(), transFile.getAbsolutePath());
                        String[] commands = s.split("\\s+");
                        Log.d("path1", "command->" + Arrays.toString(commands));
                        Log.d("path1", "target->" + targetFile.getAbsolutePath());
                        intraVideo(commands);
                        return 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Integer>) o -> {
                    Log.d("path1", "writeOutput: ");
                    //删除散的视频
                    for (int i = 0; i < videomap.size(); i++) {
                        videomap.valueAt(i).delete();
                    }
                    //删除FileList
                    fileList.delete();
                    //删除targetFile
                    targetFile.delete();
                    //合成视频完毕,回调
                    callback.onComplete(transFile);
                });
    }

    public interface CompleteCallback {

        public void onComplete(File targetFile);

        public void onError(Exception e);
    }

    private native int mergeVideo(String[] commands);

    private native int intraVideo(String[] commands);
}
