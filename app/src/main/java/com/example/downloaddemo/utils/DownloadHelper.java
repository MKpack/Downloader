package com.example.downloaddemo.utils;

import android.util.Log;

import com.example.downloaddemo.model.DownloadObject;
import com.example.downloaddemo.model.DownloadTemp;

import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadHelper {
    private final static String TAG = "DownloadHelper";
    public static Response<DownloadTemp> canConnect(String url) {
        HttpURLConnection connection = null;
        try {
            //1.先使用 head 请求看能否成功
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "url不可用");
                return new Response<>(-100, null);
            }
            String raw = connection.getHeaderField("Content-Disposition");
            String name = null;
            if (raw != null && raw.contains("filename=")) {
                name = raw.split("filename=")[1].replace("\"", "");
            }

            //2.使用range尝试请求,判断是否有可下载资源
            String type = connection.getContentType();
            if (type != null && !type.contains("text/html")) {
                Response<Long> res = tryRangeDownload(url);
                if (res.code != -100) {
                    Log.d(TAG, "资源可下载(range测试成功 可多)");
                    return new Response<>(206, new DownloadTemp(url, name, res.data));
                }
                else {
                    Log.w(TAG, "资源不可下载");
                }
            }else {
                Log.w(TAG, "可能是网页不是文件");
            }

            return new Response<>(-100, null);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new Response<>(-100, null);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    protected static Response<Long> tryRangeDownload(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Range", "bytes=0-10");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            //返回206可以片段下载，返回200直接给文件给你了
            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                Log.d(TAG, "拿到部分数据");
                String range = connection.getHeaderField("Content-Range");
                long size = Long.parseLong(range.split("/")[1]);
                return new Response<Long>(206, size);
            }
            else if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "拿到全部数据");
                return new Response<Long>(200, connection.getContentLengthLong());
            }
            return new Response<Long>(-100, null);
        }catch (Exception e) {
            return new Response<Long>(-100, null);
        }
    }
}
