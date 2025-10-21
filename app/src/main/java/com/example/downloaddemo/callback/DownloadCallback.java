package com.example.downloaddemo.callback;

import com.example.downloaddemo.model.DownloadObject;

public interface DownloadCallback {
    void onProgress(DownloadObject downloadObject);
    void onComplete(DownloadObject downloadObject);
    void onError(DownloadObject downloadObject);
}
