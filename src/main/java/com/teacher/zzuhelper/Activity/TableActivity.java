package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teacher.zzuhelper.Mould.Course;
import com.teacher.zzuhelper.Mould.Subject;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;
import com.teacher.zzuhelper.Util.ObjectSaveUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/19.
 */
public class TableActivity extends Activity implements View.OnClickListener {
    EditText edit_sName, edit_sCredit, edit_sClass, edit_sTeacher;
    List<TextView> textViewList = new ArrayList<>();
    //    TextView  textView1, textView2, textView3, textView4, textView5, textView6, textView7,
//            textView8, textView9, textView10, textView11, textView12, textView13, textView14,
//            textView15, textView16, textView17, textView18, textView19, textView20, textView21,
//            textView22, textView23, textView24, textView25, textView26, textView27, textView28, textView29, textView30;
//    TextView[] textViewArr = {textView1, textView2, textView3, textView4, textView5, textView6, textView7,
//            textView8, textView9, textView10, textView11, textView12, textView13, textView14,
//            textView15, textView16, textView17, textView18, textView19, textView20, textView21,
//            textView22, textView23, textView24, textView25, textView26, textView27, textView28, textView29, textView30};
    int[] textViewId = {R.id.table1, R.id.table2, R.id.table3, R.id.table4, R.id.table5, R.id.table6,
            R.id.table7, R.id.table8, R.id.table9, R.id.table10, R.id.table11, R.id.table12,
            R.id.table13, R.id.table14, R.id.table15, R.id.table16, R.id.table17, R.id.table18,
            R.id.table19, R.id.table20, R.id.table21, R.id.table22, R.id.table23, R.id.table24,
            R.id.table25, R.id.table26, R.id.table27, R.id.table28, R.id.table29, R.id.table30,
            R.id.table31, R.id.table32, R.id.table33, R.id.table34, R.id.table35};
    String[] colorArr = {"#66cccc", "#9999ff", "#ff99cc", "#ff9966", "#666699", "#66cc99"};
    private List<Subject> subjectList = new ArrayList<Subject>();
    View layout, title;
    AlertDialog.Builder builder;
    AlertDialog dlg;
    Subject subjcetClick;
    int clickViewsId = 0;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        MyApplication.activityList.add(this);
        Intent intent = getIntent();
        subjectList = (List<Subject>) intent.getExtras().getSerializable("RESULT");
        initValue();
    }

    public void initValue() {
        for (int i = 0; i < textViewId.length; i++) {
            TextView textview = (TextView) findViewById(textViewId[i]);
            textViewList.add(textview);
        }
        for (int i = 0; i < textViewList.size(); i++) {
            String tableValue = subjectList.get(i).getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjectList.get(i).getsClass() + ")";
            textViewList.get(i).setOnClickListener(this);
            initTextView(textViewList.get(i),tableValue);
        }
    }

    public void saveSubjectList() {
        try {
            ObjectSaveUtils.savaObject(this, subjectList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < textViewId.length; i++) {
            if (v.getId() == textViewId[i]) {
                clickViewsId = i;
                break;
            }
        }
        subjcetClick = subjectList.get(clickViewsId);
        showDialog();

    }

    public void showDialog() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        layout = inflater.inflate(R.layout.dialog_layout, null);//获取自定义布局
        title = inflater.inflate(R.layout.dalog_title, null);
        builder.setView(layout);
        builder.setCustomTitle(title);
        edit_sName = (EditText) layout.findViewById(R.id.edit_sName);
        edit_sCredit = (EditText) layout.findViewById(R.id.edit_sCredit);
        edit_sClass = (EditText) layout.findViewById(R.id.edit_sClass);
        edit_sTeacher = (EditText) layout.findViewById(R.id.edit_sTeacher);

        edit_sName.setText(subjcetClick.getsName());
        edit_sCredit.setText(subjcetClick.getsCredit());
        edit_sClass.setText(subjcetClick.getsClass());
        edit_sTeacher.setText(subjcetClick.getsTeacher());

        edit_sName.setEnabled(false);
        edit_sCredit.setEnabled(false);
        edit_sClass.setEnabled(false);
        edit_sTeacher.setEnabled(false);
        //builder.setMessage("");//显示自定义布局内容
        //确认按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Subject subject = new Subject(edit_sName.getText().toString(), edit_sCredit.getText().toString(),
                        edit_sClass.getText().toString(), edit_sTeacher.getText().toString());
                subjectList.remove(clickViewsId);
                subjectList.add(clickViewsId, subject);
                String tableValue = subjectList.get(clickViewsId).getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjectList.get(clickViewsId).getsClass() + ")";
                initTextView(textViewList.get(clickViewsId),tableValue);
                saveSubjectList();

            }
        });
        //取消
        builder.setNegativeButton("编辑", null);
        dlg = builder.create();
        dlg.show();
        dlg.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sName.setEnabled(true);
                edit_sCredit.setEnabled(true);
                edit_sClass.setEnabled(true);
                edit_sTeacher.setEnabled(true);
            }
        });

    }

    public  void  initTextView(TextView textView,String tableValue){
        if (!tableValue.equals("()")) {
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(5);
            textView.setTextColor(Color.WHITE);
            textView.setText(tableValue);
            textView.setBackgroundColor(Color.parseColor(colorArr[(int) (Math.random() * 6)]));
            textView.setAlpha((float) 0.57);
        }else {
            textView.setText("");
            textView.setAlpha(0);
        }
    }
}
