package com.teacher.zzuhelper.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.teacher.zzuhelper.Mould.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/15.
 */
public class MyApplication extends Application {
    public static List<Activity> activityList = new ArrayList<Activity>();
    static User user;
    public static void setUser(String name, String studentNumber, String password, String college, String specialities, String grade, String clazz,String id){
        user = new User(name, studentNumber, password, college, specialities, grade, clazz,id);
    }
    public static User getUser(){
        return user;
    }
    public static void exitClient(Context ctx) {
        try {
            for (Activity activity:activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
