package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.teacher.zzuhelper.HttpUtil.HttpPost;
import com.teacher.zzuhelper.HttpUtil.PostTask;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2016/9/14.
 */
public class LoginActivity extends Activity {
    private EditText input_number,input_password;
    private Button btn_login;
    private Spinner spinner;
    private List<String> userInfoList = new ArrayList<>();
    String number,password,grade;
    private ProgressDialog dialog;
    private static final String URL ="http://jw.zzu.edu.cn/scripts/qscore.dll/search";
    private static final String SELEC  = "http://jw.zzu.edu.cn/scripts/qscore.dll/search";
    private static final String IDURL = "http://jw.zzu.edu.cn/scripts/stuinfo.dll/check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication.activityList.add(this);
        input_number = (EditText)findViewById(R.id.input_number);
        input_password = (EditText)findViewById(R.id.input_password);
        spinner=(Spinner)findViewById(R.id.spinner);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        String html;
        String idHtml;
        if(input_number.getText().equals("")||input_password.getText().equals("")||spinner.getSelectedItem().toString().equals("选择年级")){
            Toast.makeText(this,"用户名密码年级不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        number = input_number.getText().toString();
        password = input_password.getText().toString();
        grade = spinner.getSelectedItem().toString();
        try {
            html = new PostTask().execute(new String[]{URL, SELEC,grade,number,password}).get();
            idHtml = new PostTask().execute(new String[]{IDURL, IDURL,grade,number,password}).get();
            if(!HttpPost.isLoginSuccessful(html)||html==null){
                Toast.makeText(this,"登录失败，请检查学号密码和网络设置",Toast.LENGTH_SHORT).show();
                return;
            }
            String id = HttpPost.parseId(idHtml);
            userInfoList = HttpPost.parseUseInfo(html);
            SharedPreferences.Editor editor = getSharedPreferences("userInfo",
                    MODE_PRIVATE).edit();
            editor.putString("number", number);
            editor.putString("password", password);
            editor.putString("grade", grade);
            editor.putString("name", userInfoList.get(1));
            editor.putString("clazz", userInfoList.get(2));
            editor.putString("college", userInfoList.get(3));
            editor.putString("specialities", userInfoList.get(4));
            editor.putString("id",id);
            editor.commit();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        } catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e ){
            e.printStackTrace();
        }
    }
}
