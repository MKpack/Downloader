package com.example.downloaddemo.utils;

import com.example.downloaddemo.model.DownloadObject;
import com.example.downloaddemo.model.DownloadRange;

import java.util.ArrayList;
import java.util.List;

public class FileDownloader {
    public static int calculateOptimalThreadCount(long fileSize) {
        int minThreads = 1;
        int maxThreads = 8;
        long perThreadMinSize = 1024*1024*2; //每个线程至少处理2MB

        int needThread = (int) (fileSize/perThreadMinSize);
        //选择适合的线程数
        return Math.max(minThreads, Math.min(needThread, maxThreads));
    }

    public static List<DownloadRange> calculateRanges(DownloadObject downloadObject) {
        List<DownloadRange> ranges = new ArrayList<>();
        int threadCount = downloadObject.getThreadCount();
        long blockSize = downloadObject.getTargetSize() / threadCount;

        for (int i = 0; i < threadCount; i++) {
            long start = i * blockSize;
            long end = (i == threadCount - 1) ? downloadObject.getTargetSize() - 1 : start + blockSize -1;
            ranges.add(new DownloadRange(start, end, downloadObject));
        }
        return ranges;
    }

}
