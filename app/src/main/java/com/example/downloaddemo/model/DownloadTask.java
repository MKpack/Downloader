package com.example.downloaddemo.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.downloaddemo.callback.DownloadCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable{
    private static final String TAG = "DownloadTask";

    private long lastUpdateTime = 0; //上次刷新时间
    private final long updateInterval = 200; //200ms刷新一次
    private DownloadRange downloadRange;

    private DownloadCallback callback;

    public DownloadTask(DownloadRange downloadRange, DownloadCallback downloadCallback) {
        this.downloadRange = downloadRange;
        this.callback = downloadCallback;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        InputStream input = null;
        RandomAccessFile raf = null;
        String threadName = Thread.currentThread().getName();
        Log.d(TAG, threadName + ": start: " + downloadRange.getStart() + ", end: " + downloadRange.getEnd());
        try {
            //打开连接
            conn = (HttpURLConnection) new URL(downloadRange.getUrl()).openConnection();
            conn.setRequestProperty("Range", "bytes=" + downloadRange.getStart() + "-" + downloadRange.getEnd());
            input = conn.getInputStream();

            //打开文件（读写模式）
            byte[] buff = new byte[1024*10];
            raf = new RandomAccessFile(downloadRange.getTargetFile(), "rw");
            raf.seek(downloadRange.getStart());

            //开始写入
            int bytesReads;
            while ((bytesReads = input.read(buff)) != -1) {
                raf.write(buff, 0, bytesReads);
                long now = System.currentTimeMillis();
                if (now - lastUpdateTime >= updateInterval) {
                    lastUpdateTime = now;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (callback != null) {
                            callback.onProgress(downloadRange.getDownloadObject());
                            Log.d(TAG, threadName + "进行更新");
                        }
                    });
                }
                downloadRange.addDownload(bytesReads);
            }
            if (downloadRange.getNowSize() == downloadRange.getTargetSize()){
                new Handler(Looper.getMainLooper()).post(() -> {
                   if (callback != null) {
                       callback.onComplete(downloadRange.getDownloadObject());
                       Log.d(TAG, "完成下载");
                   }
                });
            }
            Log.d(TAG, threadName + "下载完成");
        }catch (Exception e) {

        }finally {
            //资源释放
            if (conn != null) conn.disconnect();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

//    private void notifyProgress() {
//        if (viewHolder != null) {
//            new Handler(Looper.getMainLooper()).post(()-> {
//                viewHolder.textView_smallTitle.setText(downloadRange.showSize());
//                viewHolder.textView_state.setText(downloadRange.getSpeed());
//                viewHolder.progressIndicator.setProgress((int) (downloadRange.getNowSize().longValue() * 100 / downloadRange.getTargetSize()));
//            });
//        }
//    }
}
