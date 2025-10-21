package com.example.downloaddemo.model;


import com.example.downloaddemo.utils.SomeUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadObject {
    private String url;
    private long targetSize;
    private final AtomicLong nowSize;
    private String name;
    private File targetFile;
    private int threadCount;
    private String lastSpeed = "0 B/s";
    private Boolean isDownloading;
    public long taskId;
    private long lastUpdateTime = 0;
    private long lastSize = 0;

    public DownloadObject(String url, long targetSize, String name, long id) {
        this.url = url;
        this.targetSize = targetSize;
        this.name = name;
        this.isDownloading = true;
        nowSize = new AtomicLong(0);
        this.taskId = id;
    }

    //该实例只有一个nowSize，并且加的时候写入该值,防止多个值写入导致大小变化
    public void addDownload(long len) {
        nowSize.addAndGet(len);
    }

    //拼出13.06 MB/1.93 GB
    public String showSize() {
        String res;
        res = SomeUtils.compareSize(nowSize.get()) + " /" + SomeUtils.compareSize(targetSize);
        return res;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public Boolean getDownloading() {
        return isDownloading;
    }

    public void setDownloading(Boolean downloading) {
        isDownloading = downloading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeed() {
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime >= 1000) {
            lastUpdateTime = now;
            lastSpeed = SomeUtils.compareSize(getNowSize() - lastSize) + "/s";
            lastSize = getNowSize();
            return lastSpeed;
        }
        return lastSpeed;
    }

    public long getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(long targetSize) {
        this.targetSize = targetSize;
    }

    public String getUrl() {
        return url;
    }

    public long getNowSize() {
        return nowSize.get();
    }


    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownloadTask{" +
                "isDownloading=" + isDownloading +
                ", url='" + url + '\'' +
                ", targetSize=" + targetSize +
                ", nowSize=" + nowSize +
                ", name='" + name + '\'' +
                '}';
    }
}
