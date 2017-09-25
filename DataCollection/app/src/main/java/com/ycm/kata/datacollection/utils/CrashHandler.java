package com.ycm.kata.datacollection.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by changmuyu on 2017/9/24.
 * Description:
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private static CrashHandler instance;
    private Context context;
    private Map<String, String> infos = new HashMap<>();
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.CHINA);

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException interruptException) {
                interruptException.printStackTrace();
            }
            Process.killProcess(Process.myPid());
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.loop();
            }
        }.start();
        Log.e("---------", ex.getMessage());
        collectDeviceInfo(context);
        saveCrashInfo2File(ex);
        return true;
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        StringWriter write = new StringWriter();
        PrintWriter printWriter = new PrintWriter(write);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = write.toString();
        sb.append(result);
        try {
            long timeStamp = System.currentTimeMillis();
            String time = format.format(new Date());
            String fileName = "crash" + "-" + time + "-" + timeStamp + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = CommonUtils.getCrashLogPath();
                File file = new File(path);
                if (!file.exists() && !file.mkdirs()) {
                    return null;
                }

                File logFile = new File(path, fileName);
                if (!logFile.exists() && !logFile.createNewFile()) {
                    return null;
                }

                FileOutputStream fos = new FileOutputStream(logFile);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("手机型号: ", android.os.Build.MODEL);
                infos.put("系统版本: ", "" + android.os.Build.VERSION.SDK_INT);
                infos.put("Android版本: ", android.os.Build.VERSION.RELEASE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occurred when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occurred when collect crash info", e);
            }
        }
    }
}
