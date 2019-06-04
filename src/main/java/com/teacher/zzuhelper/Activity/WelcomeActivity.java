package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/14.
 */
public class WelcomeActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        MyApplication.activityList.add(this);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(task, 1000);
    }
}
