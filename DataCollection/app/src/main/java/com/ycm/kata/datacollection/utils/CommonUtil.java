package com.ycm.kata.datacollection.utils;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by changmuyu on 2017/9/20.
 * Description:
 */

public class CommonUtil {

    public static String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public static String formatDate2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public static String getImageFilePath(long currTime) {
        String rootPath = getRootPath();
        String imageRootFilePath = rootPath + File.separator + "image" + formatDate(currTime);
        File imageDirectFile = new File(imageRootFilePath);
        if (!imageDirectFile.exists() && !imageDirectFile.mkdir()) {
            return null;
        }

        String imageFilePath  = imageRootFilePath + File.separator + formatDate2(currTime) + ".png";
        File imageFile = new File(imageFilePath);
        try {
            if (!imageFile.exists() && !imageFile.createNewFile()) {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFilePath;
    }

    public static String getDataFilePath(long currTime) {
        String rootPath = getRootPath();
        String dataFileRootPath = rootPath + File.separator + "data_file" + File.separator + formatDate(currTime);
        File dataDirectFile = new File(dataFileRootPath);
        if (!dataDirectFile.exists() && !dataDirectFile.mkdirs()) {
            return null;
        }

        String dataFilePath = dataFileRootPath + File.separator + formatDate(currTime) + ".xls";
        File dataFile = new File(dataFilePath);
        try {
            if (!dataFile.exists() && !dataFile.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataFilePath;
    }

    public static String getRootPath() {
        String rootPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection";
        File rootFile = new File(rootPath);
        if (!rootFile.exists() && !rootFile.mkdir()) {
            return null;
        }
        return rootPath;
    }
}
