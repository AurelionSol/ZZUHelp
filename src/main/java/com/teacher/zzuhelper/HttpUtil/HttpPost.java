package com.teacher.zzuhelper.HttpUtil;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.teacher.zzuhelper.Activity.LoginActivity;
import com.teacher.zzuhelper.Activity.MainActivity;
import com.teacher.zzuhelper.Mould.Course;
import com.teacher.zzuhelper.Mould.Subject;
import com.teacher.zzuhelper.Util.MyApplication;
import com.teacher.zzuhelper.Util.ObjectSaveUtils;
import com.teacher.zzuhelper.Util.ScoreTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HttpPost {
    public static List<String> parseUseInfo(String html) {
        List<String> infoList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByTag("font").get(1);
        String info = element.text();
        String[] strings = info.split("：");
        for (int i = 1; i < strings.length; i++) {
            String value = strings[i].split(" ")[0];
            infoList.add(value);
        }
        return infoList;
    }

    public static boolean isLoginSuccessful(String html) {
        List<String> infoList = new ArrayList<>();
        try {
            Document document = Jsoup.parse(html);
            Element element = document.getElementsByTag("font").get(0);
            String tips = element.text();
            if (tips.equals("系统没有找到你的信息，原因可能如下：")) {
                return false;
            } else {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

    }

    public static List<Subject> parseSubjectHtml(String html) {
        List<Subject> subjectList = new ArrayList<>();
        Document document = Jsoup.parse(html,"UTF-8");
        Element element = document.getElementsByTag("tbody").get(3);
        Elements trs = element.getElementsByTag("tr");
        for (int i = 3; i < trs.size(); i++) {
            Elements tds = trs.get(i).getElementsByTag("td");
            for (int j = 1; j < tds.size(); j++) {
                String tdsText = tds.get(j).text();
                String[] brs = tdsText.trim().split(" ");
                Subject subject = null;
                if (brs.length > 3) {
                    String sName = brs[0];
                    String sCredit = brs[2];
                    String sClass = brs[3];
                    String sTeacher = brs[4];
                    subject = new Subject(sName, sCredit, sClass, sTeacher);
//                    tdsText = brs[0].replaceAll("\\d+","").replaceAll(":","") +"("+ brs[3]+")";
                } else {
                    subject = new Subject("", "", "", "");
                }
//                Log.d("课表字符串", tdsText);
//                subjectList.add(tdsText);
                subjectList.add(subject);
            }
        }
        return subjectList;
    }
    public static String parseId(String html){
        String id = null;
        Document document = Jsoup.parse(html,"UTF-8");
        Element element = document.getElementsByTag("tr").get(6);
        Element fontElement = element.getElementsByTag("font").get(0);
        id = fontElement.text();
        id = id.substring(id.length()-6,id.length());
        if(id.indexOf("x")!=-1){
            id.replace("x","0");
        }
        return id;
    }



}
