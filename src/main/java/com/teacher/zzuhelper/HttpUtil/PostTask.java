package com.teacher.zzuhelper.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teacher.zzuhelper.Activity.MainActivity;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/15.
 */
public class PostTask extends AsyncTask<String, Integer, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String URL = params[0];
        String SELEC = params[1];
        String NIANJI = params[2];
        String XUEHAO = params[3];
        String MIMA = params[4];
        try {
            OkHttpClient okHttpClient = null;
            okHttpClient = new OkHttpClient();
            java.net.URL loginUrl = new URL(URL);
            FormBody.Builder builder = new FormBody.Builder();
            if (NIANJI != null) {
                builder.add("nianji", NIANJI);
                builder.add("xuehao", XUEHAO);
                builder.add("mima", MIMA);
                builder.add("selec", SELEC);
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder().url(loginUrl).post(requestBody)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}