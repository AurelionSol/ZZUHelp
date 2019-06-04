package com.teacher.zzuhelper.Util;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.teacher.zzuhelper.Mould.NewsAndNotice;

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
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewsAndNoticeTask extends AsyncTask<String, Integer, List<NewsAndNotice>> {
    List<NewsAndNotice> newsAndNoticeList;
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected List<NewsAndNotice> doInBackground(String... params) {
        newsAndNoticeList = new ArrayList<>();
        try {
            String html = params[0];
            Document htmlDocument = Jsoup.parse(html,"UTF-8");
            Elements tdElements = htmlDocument.getElementsByTag("td");
            tdElements.remove(0);
            tdElements.remove(tdElements.size() - 1);
            for (int i = 0; i < tdElements.size(); i++) {
                Elements a = tdElements.get(i).getElementsByTag("a");
                String time = tdElements.get(i).ownText();
                String[] timeArr = time.split("[]]");
                time = timeArr[1];
                String title = a.text();
                String oneHtml = a.outerHtml();
                String[] tempArr = oneHtml.split("\"");
                oneHtml = tempArr[3];
                NewsAndNotice newsAndNotice = new NewsAndNotice(time, title, oneHtml);
                newsAndNoticeList.add(newsAndNotice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsAndNoticeList;
    }

    public String getContent(String html) {
        String content = null;
        String htmlString = getHtml(html);
        Document htmlDocument = Jsoup.parse(htmlString);
        Elements pElements = htmlDocument.getElementsByClass("neirong");
        if (pElements.size() == 0) {
            pElements = htmlDocument.getElementsByClass("neirongnews");
            if (pElements.size() == 0) {
                pElements = htmlDocument.getElementsByTag("td");
                if(pElements.size() == 0){
                    pElements = htmlDocument.getElementsByTag("font").removeAttr("img");
                }
            }
        }
        if(pElements.size()==0){
            content = "这是一个文件";
        }else {
            content = pElements.get(0).text();
            if(content.trim().equals("　")){
                content="这是一张图片"+"\n"+"或者该条信息只提供给使用校园网的用户，您无权访问";
            }
        }
        return content.trim();
    }

    public String getHtml(String html) {
        try {
            URL loginUrl = new URL(html);
//            FormBody.Builder builder = new FormBody.Builder();
//            RequestBody requestBody = builder.build();
            Request request = new Request.Builder().url(loginUrl).build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
