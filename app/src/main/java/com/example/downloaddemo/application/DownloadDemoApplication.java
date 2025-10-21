package com.example.downloaddemo.application;

import android.app.Application;

import com.example.downloaddemo.thread.DownloadThreadPool;


public class DownloadDemoApplication extends Application {

    private DownloadThreadPool downloadThreadPool;

    @Override
    public void onCreate() {
        super.onCreate();
        //设置全局down池
        downloadThreadPool = DownloadThreadPool.getInstance();
    }

    public DownloadThreadPool getDownloadThreadPool() {
        return downloadThreadPool;
    }
}
