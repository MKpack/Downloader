package com.example.downloaddemo.model;

import java.io.File;

public class DownloadRange {

    private DownloadObject downloadObject;
    private long start;
    private long end;

    public DownloadRange(long start, long end, DownloadObject downloadObject) {
        this.start = start;
        this.end = end;
        this.downloadObject = downloadObject;
    }

    //封装一下downloadObject的方法
    public void addDownload(long len) {
        downloadObject.addDownload(len);
    }
    public String getUrl() {
        return downloadObject.getUrl();
    }

    public DownloadObject getDownloadObject() {
        return downloadObject;
    }

    public File getTargetFile() {
        return downloadObject.getTargetFile();
    }

    public long getNowSize() {
        return downloadObject.getNowSize();
    }

    public long getTargetSize() {
        return downloadObject.getTargetSize();
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}
