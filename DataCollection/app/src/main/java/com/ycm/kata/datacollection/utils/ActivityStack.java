package com.ycm.kata.datacollection.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by changmuyu on 2017/8/11.
 * Description:
 */
public class ActivityStack {

    @Nullable
    private static ActivityStack instance;

    @NonNull
    public static ActivityStack getInstanse() {
        if (instance == null) {
            synchronized (ActivityStack.class) {
                if (instance == null) {
                    instance = new ActivityStack();
                }
            }
        }
        return instance;
    }

    private ActivityStack() {
        activities = new LinkedList<>();
    }


    private LinkedList<Activity> activities;


    @NonNull
    public LinkedList<Activity> getAllActivities() {
        return activities;
    }


    @Nullable
    public Activity getTopActivity() {
        if (activities == null || activities.isEmpty()) {
            return null;
        }
        return activities.getFirst();
    }


    public void pushActivity(@NonNull Activity activity) {
//        activities.addFirst(activity);
    }


    public boolean popActivity(@NonNull Activity activity, boolean isFinish) {
        if (activities.isEmpty()) {
            return false;
        }
        if (activities.contains(activity)) {
            if (isFinish) {
                activity.finish();
            }
            return activities.remove(activity);
        }
        return false;
    }


    public void popAllActivities(boolean isFinish) {
        if (activities.isEmpty()) {
            return;
        }
        if (isFinish) {
            Iterator<Activity> iterator = activities.iterator();
            while (iterator.hasNext()) {
                iterator.next().finish();
            }
        }
        activities.clear();
    }

    public void goBackApp() {
        Activity activity = getTopActivity();
        if (activity == null || !isAppOnForeground(activity)) {
            return;
        }
        popActivity(activity, true);//弹出栈顶的页面并结束
        moveToBackAllActivities();//使应用置于后台
    }

    public void moveToBackAllActivities() {
        Activity activity = getTopActivity();
        if (activity == null || !isAppOnForeground(activity)) {
            return;
        }
        try {
            activity.moveTaskToBack(true);
        } catch (Exception e) {
            return;
        }
    }

    public void popActivitiesUntil(int position, boolean isFinish) {
        if (activities.isEmpty() || position < 0) {
            return;
        }

        for (int i = 0; i < position; i++) {
            if (isFinish) {
                activities.getFirst().finish();
            }
            activities.removeFirst();
        }
    }

    public void popActivitiesUntil(@NonNull Activity activity, boolean isFinish) {
        if (activities.isEmpty() || !activities.contains(activity)) {
            return;
        }
        int posistion = activities.indexOf(activity) - 1;
        popActivitiesUntil(posistion, isFinish);
    }


    public void pop2TopActivity(boolean isFinish) {
        final int position = activities.size() - 1;
        for (int i = 0; i < position; i++) {
            if (isFinish) {
                activities.getLast().finish();
            }
            activities.removeLast();
        }
    }

    /**
     * 从堆栈中弹出Activity直到某个指定类型的Activity为止（不弹出指定类型的Activity）
     *
     * @param cls      目标Activity类型
     * @param isFinish 出栈的同时是否结束Activity
     */
    public <T extends Activity> void popActivitiesUntil(@NonNull Class<T> cls, boolean isFinish) {
        if (activities.isEmpty()) {
            return;
        }
        int posistion = -1;
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getClass().equals(cls)) {
                posistion = i;
                break;
            }
        }
        popActivitiesUntil(posistion, isFinish);
    }

    /**
     * 通过类名从堆栈中弹出Activity
     *
     * @param cs       Activity的class对象
     * @param isFinish 出栈的同时是否结束Activity
     */
    public void popActivityByClass(@NonNull Class<? extends Activity> cs, boolean isFinish) {
        Activity ac = getActivityByClass(cs);
        if (ac != null) {
            popActivity(ac, isFinish);
        }
    }

    /**
     * 完全退出
     */
    public void exitApp() {
        popAllActivities(true);
    }

    /**
     * 根据class name获取activity
     * <p/>
     * Acitivity名称
     *
     * @return 获得的对象
     */
    @Nullable
    public Activity getActivityByClassName(@NonNull String activityName) {
        if (activities.isEmpty()) {
            return null;
        }
        for (Activity ac : activities) {
            if (ac.getClass().getName().contains(activityName)) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 根据Activity类名获取Activity对象
     *
     * @param cs Activity的class对象
     * @return 获得的对象
     */
    @Nullable
    public Activity getActivityByClass(Class<? extends Activity> cs) {
        if (activities.isEmpty()) {
            return null;
        }
        for (Activity ac : activities) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }
    /**
     * 程序是否在前台运行
     *
     * Created by licong ,2016/11/23
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appInfos =  am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info: appInfos) {
            if (info.processName.equals(context.getPackageName())) {
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

}
