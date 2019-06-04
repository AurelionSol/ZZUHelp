package com.teacher.zzuhelper.Util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.teacher.zzuhelper.Mould.Cost;
import com.teacher.zzuhelper.Mould.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/21.
 */
public class CostTask extends AsyncTask<String, Integer, List<Cost>> {
    List<Cost> costList;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String htmlString = msg.obj.toString();
                    Document htmlDocument = Jsoup.parse(htmlString);
                    Elements elements = htmlDocument.getElementsByClass("portlet-content-container");
                    Log.d("我的信息", elements.toString());
                    break;
            }
        }
    };

    @Override
    protected List<Cost> doInBackground(String... params) {
        costList = new ArrayList<>();
        String html = params[0];
        reGetHtml(html);
        Document htmlDocument = Jsoup.parse(html, "UTF-8");
        return costList;
    }

    public void reGetHtml(final String html) {
        try {
            URL loginUrl = new URL(html);
            final Headers.Builder headersBuilder = new Headers.Builder();
            headersBuilder.add("Host", "ecard.zzu.edu.cn");
            headersBuilder.add("Connection", "keep-alive");
            headersBuilder.add("Upgrade-Insecure-Requests", "1");
            headersBuilder.add("Cookie", "COOKIE_SUPPORT=true");
            headersBuilder.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36");
            Headers requestHeaders = headersBuilder.build();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("cmd", "already-registered");
            builder.add("tabs1", "already-registered");
            builder.add("redirect", "");
            builder.add("rememberMe", "false");
            builder.add("login", MyApplication.getUser().getStudentNumber());
            builder.add("password", MyApplication.getUser().getId());
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(loginUrl).headers(requestHeaders).post(requestBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("失败", "失败了");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    Document htmlDocument = Jsoup.parse(sb.toString());
                    Elements elements = htmlDocument.getElementsByTag("script");
                    String getSessionId = elements.get(0).toString().split("getSessionId")[1].split("\"")[1];
                    Log.d("函数", getSessionId);
                    queryData(getSessionId);
                }
            });


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCost(final String SessionId) {
        String html = "http://ecard.zzu.edu.cn/web/guest/stu?p_p_id=DataObject_INSTANCE_PDsL&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=5&_DataObject_INSTANCE_PDsL_forward_=query&_DataObject_INSTANCE_PDsL_themeId_=370&_DataObject_INSTANCE_PDsL_themeName_=%E4%B8%AA%E4%BA%BA%E8%B4%A6%E6%88%B7%E4%BF%A1%E6%81%AF";
        final String ahtml = "portlet";
        try {
            URL loginUrl = new URL(html);
            Headers.Builder headersBuilder = new Headers.Builder();
            headersBuilder.add("Connection", "keep-alive");
            headersBuilder.add("Upgrade-Insecure-Requests", "1");
            headersBuilder.add("Cookie", "LFR_SESSION_STATE_1894872=1477128910128; COOKIE_SUPPORT=true; JSESSIONID=" + SessionId);
            headersBuilder.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36");
            Headers requestHeaders = headersBuilder.build();
            FormBody.Builder builder = new FormBody.Builder();
//            builder.add("p_p_id", "DataObject_INSTANCE_PDsL");
//            builder.add("p_p_lifecycle", "0");
//            builder.add("p_p_state", "maximized");
//            builder.add("p_p_mode", "view");
//            builder.add("p_p_col_id", "column-1");
//            builder.add("p_p_col_count", "5");
//            builder.add("_DataObject_INSTANCE_PDsL_forward_", "query");
//            builder.add("_DataObject_INSTANCE_PDsL_themeId_", "370");
//            builder.add("_DataObject_INSTANCE_PDsL_themeName_", "个人账户信息");
            builder.add("themeId", "370");
            builder.add("userName", MyApplication.getUser().getStudentNumber());
            builder.add("themeName_", "个人账户信息");
            builder.add("userId", MyApplication.getUser().getStudentNumber());
            builder.add("maxNum", "8000");
            builder.add("pageSize", "30");
            builder.add("userDept", "sbsbsbsb郑州大学sbsbsbsb");
            builder.add("timestamp", "1477141302789");
            builder.add("auid", "CAF158BADD7E7D14AF77D47369B6E91E");

            RequestBody requestBody = builder.build();
             Request request = new Request.Builder().url(loginUrl).headers(requestHeaders).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    System.out.print(sb.toString());
                    Document htmlDocument = Jsoup.parse(sb.toString());
                    Elements elements = htmlDocument.getElementsByTag("td");
                    Log.d("个人信息", elements.toString() + "没获取到");
                    queryData(SessionId);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryData(String SessionId){
        try {
            String html = "http://ecard.zzu.edu.cn/query/queryData.do";
            URL loginUrl = new URL(html);
            Headers.Builder headersBuilder = new Headers.Builder();
            headersBuilder.add("Upgrade-Insecure-Requests", "1");
            headersBuilder.add("Cookie", "LFR_SESSION_STATE_1894872=1477128910128; COOKIE_SUPPORT=true; JSESSIONID=" + SessionId);
            headersBuilder.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36");
            Headers requestHeaders = headersBuilder.build();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("start", "0");
            builder.add("themeId", "370");
            builder.add("whereSql", "[]");
            builder.add("orderGroup", "");
            builder.add("limit", "30");
            builder.add("pageSize", "30");
            builder.add("varValue", "");
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder().url(loginUrl).headers(requestHeaders).post(requestBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    System.out.print(sb.toString());
                    String cost = sb.toString().split("\"")[27];
                    Log.d("个人信息", cost + "没获取到");
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
