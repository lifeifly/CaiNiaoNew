package com.example.ffmpeg07;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void writeFileList(File fileList, SparseArray<File> videomap) {
        FileWriter fw = null;
        Log.d("SIZE", String.valueOf(videomap.size()));
        try {
            fw = new FileWriter(fileList);
                for (int i=0;i<videomap.size();i++){
                    Log.d("SIZE1", String.valueOf(i));
                File source = new File(videomap.valueAt(i).getAbsolutePath());

                fw.write("file '" + source.getAbsolutePath() + "'" + "\n");

            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
