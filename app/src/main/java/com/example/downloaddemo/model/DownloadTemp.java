package com.example.downloaddemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DownloadTemp implements Parcelable {
    private String url;
    private long targetSize;
    private String name;

    public DownloadTemp(String url, String name, long targetSize) {
        this.url = url;
        this.name = name;
        this.targetSize = targetSize;
    }


    protected DownloadTemp(Parcel in) {
        url = in.readString();
        name = in.readString();
        targetSize = in.readLong();
    }

    public static final Creator<DownloadTemp> CREATOR = new Creator<DownloadTemp>() {
        @Override
        public DownloadTemp createFromParcel(Parcel in) {
            return new DownloadTemp(in);
        }

        @Override
        public DownloadTemp[] newArray(int size) {
            return new DownloadTemp[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownloadTemp{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", targetSize=" + targetSize +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(name);
        dest.writeLong(targetSize);
    }
}
