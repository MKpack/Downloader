package com.example.downloaddemo.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SomeUtils {

    //单位转换
    public static String compareSize(long flag) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        if (flag >= 1073741824) {
            double size = flag/1073741824.0;
            return df.format(size) + " GB";
        }
        else if (flag >= 1048576) {
            double size = flag/1048576.0;
            return df.format(size) + " MB";
        }
        else if (flag >= 1024) {
            double size = flag/1024.0;
            return df.format(size) + " KB";
        }
        else {
            return flag + " B";
        }
    }
}
