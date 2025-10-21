package com.example.downloaddemo.model;

import java.io.File;

public class IsDownloadObject {
    private String name;
    private File file;
    private String downloadDate;
    private String size;

    public IsDownloadObject() {

    }
    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "IsDownloadObject{" +
                "downloadDate='" + downloadDate + '\'' +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", size='" + size + '\'' +
                '}';
    }
}
