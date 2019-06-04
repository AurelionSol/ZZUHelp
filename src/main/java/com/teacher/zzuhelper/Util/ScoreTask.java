package com.teacher.zzuhelper.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.teacher.zzuhelper.Activity.MainActivity;
import com.teacher.zzuhelper.Mould.Course;
import com.teacher.zzuhelper.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/11.
 */
public  class   ScoreTask extends AsyncTask<String, Integer, List<List<Course>>> {
    List<String> resultList;
    List<List<Course>> listCourseList;
    Elements hrefElements;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<List<Course>> doInBackground(String... params) {
        listCourseList=new ArrayList<>();
        resultList = new ArrayList<>();
        try {
            String html = params[0];
            Document htmlDocument = Jsoup.parse(html,"UTF-8");
            hrefElements = htmlDocument.getElementsByTag("a");
            hrefElements.remove(0);
            hrefElements.remove(hrefElements.size() - 1);
            hrefElements.remove(hrefElements.size() - 1);
            for (int i = 0; i < hrefElements.size(); i++) {
                OkHttpClient okHttpClient = null;
                okHttpClient = new OkHttpClient();
                String oneHtml = hrefElements.get(i).outerHtml();
                String[] tempArr = oneHtml.split("\"");
                oneHtml = tempArr[1];
                java.net.URL loginUrl = new URL(oneHtml);
                FormBody.Builder builder = new FormBody.Builder();
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder().url(loginUrl).post(requestBody)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36")
                        .build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        //此时登陆成功后我们就可以开始查成绩了
                        BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
                        String line;
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        resultList.add(sb.toString());
                        Log.d("POST!!!!!!!!!", "成功~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (resultList.size() == hrefElements.size()) {
                resultList.add(html);
                for (int i = 0; i < resultList.size(); i++) {
                    List<Course> courseList = getCourseList(resultList.get(i));
                    listCourseList.add(courseList);
                }
                if(listCourseList.size()==hrefElements.size()+1){
                    return listCourseList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<List<Course>> lists) {
        super.onPostExecute(lists);
    }
    public  List<Course> getCourseList(String html) {
        List<Course> courseList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByTag("tbody").get(0);
        //得到所有的行
        Elements trs = element.getElementsByTag("tr");
        for (int i = 1; i < trs.size(); i++) {
            Element e = trs.get(i);
            //得到一行中的所有列
            Elements tds = e.getElementsByTag("td");
            String cName = tds.get(0).text();
            String cType = tds.get(1).text();
            String cCredit = tds.get(2).text();
            String cScore = tds.get(3).text();
            String cGpa = tds.get(4).text();
            Course course = new Course(cName, cType, cCredit, cScore, cGpa);
            courseList.add(course);
        }
        return courseList;
    }
}
