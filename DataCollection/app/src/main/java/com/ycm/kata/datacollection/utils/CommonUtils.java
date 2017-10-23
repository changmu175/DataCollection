package com.ycm.kata.datacollection.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.ycm.kata.datacollection.model.entity.ImageInfo;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by changmuyu on 2017/9/20.
 * Description:
 */

public class CommonUtils {

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
        String imageRootFilePath = rootPath + File.separator + "数据采集照片" + formatDate(currTime);
        File imageDirectFile = new File(imageRootFilePath);
        if (!imageDirectFile.exists() && !imageDirectFile.mkdir()) {
            return null;
        }

        String imageFilePath = imageRootFilePath + File.separator + formatDate2(currTime) + ".png";
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
        return getDataFilePath(currTime, 0);
    }

    public static String getDataFilePath(long currTime, int index) {
        String dataFileRootPath = getLevelTwoDataRootPath(currTime);
        String indexStr = index == 0 ? "" : "-" + index;
        String dataFilePath = dataFileRootPath + File.separator + "数据采集" + formatDate(currTime) + indexStr + ".xls";
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

    public static String getLevelOneDataRootPath() {
        String rootPath = getRootPath();
        String dataFileRootPath = rootPath + File.separator + "数据采集文件";
        File dataDirectFile = new File(dataFileRootPath);
        if (!dataDirectFile.exists() && !dataDirectFile.mkdirs()) {
            return null;
        }
        return dataFileRootPath;
    }

    public static String getLevelTwoDataRootPath(long currTime) {
        String data2FileRootPath = getLevelOneDataRootPath() + File.separator + formatDate(currTime);
        File dataDirectFile = new File(data2FileRootPath);
        if (!dataDirectFile.exists() && !dataDirectFile.mkdirs()) {
            return null;
        }
        return data2FileRootPath;
    }

    public static String getRootPath() {
        String rootPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "A数据采集";
        File rootFile = new File(rootPath);
        if (!rootFile.exists() && !rootFile.mkdir()) {
            return null;
        }
        return rootPath;
    }

    public static void destroyBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static String getCrashLogPath() {
        return getRootPath() + File.separator + "log";
    }


    public static String combinationStr(String pileContent) {

        if (TextUtils.isEmpty(pileContent)) {
            return "";
        }

        int length = pileContent.length();
        if (length == 3) {
            return "K0" + "+" + pileContent;
        }

        if (length >= 3) {
            String strPre = pileContent.substring(0, length - 3);
            String strFor = pileContent.substring(length - 3, length);
            return "K" + strPre + "+" + strFor;
        }
        return pileContent;
    }

    /**
     * 压缩文件,文件夹
     *
     * @param srcFilePath 要压缩的文件/文件夹名字
     * @param zipFilePath 指定压缩的目的和名字
     */

    public static void zipFolder(String srcFilePath, String zipFilePath) {
        //创建Zip包
        ZipOutputStream outZip = null;
        try {
            outZip = new ZipOutputStream(new FileOutputStream(zipFilePath));
            //打开要输出的文件
            File file = new File(srcFilePath);

            //压缩
            zipFiles(file.getParent() + File.separator, file.getName(), outZip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //完成,关闭
            try {
                if (outZip != null) {
                    outZip.finish();
                    outZip.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }//end of func

    /**
     * 压缩文件
     *
     * @param folderPath
     * @param filePath
     * @param zipOut
     * @throws Exception
     */
    private static void zipFiles(String folderPath, String filePath, ZipOutputStream zipOut) {
        if (zipOut == null) {
            return;
        }

        File file = new File(folderPath + filePath);
        try {

            //判断是不是文件
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(filePath);
                FileInputStream inputStream;

                inputStream = new FileInputStream(file);

                zipOut.putNextEntry(zipEntry);

                int len;
                byte[] buffer = new byte[4096];

                while ((len = inputStream.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, len);
                }

                zipOut.closeEntry();
            } else {
                //文件夹的方式,获取文件夹下的子文件
                String fileList[] = file.list();

                //如果没有子文件, 则添加进去即可
                if (fileList.length <= 0) {
                    ZipEntry zipEntry =
                            new ZipEntry(filePath + File.separator);
                    zipOut.putNextEntry(zipEntry);
                    zipOut.closeEntry();
                }

                //如果有子文件, 遍历子文件
                for (int i = 0; i < fileList.length; i++) {
                    zipFiles(folderPath, filePath + File.separator + fileList[i], zipOut);
                }//end of for

            }//end of if
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end of func
}
