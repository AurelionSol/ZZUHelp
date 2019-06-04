package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teacher.zzuhelper.Apapter.ScoreAdapter;
import com.teacher.zzuhelper.Mould.Course;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ScoreActivity extends Activity {
    List<List<Course>> listCourseList;
    RecyclerView recyclerView;
    ViewPager viewPager;
    List<View> viewList;
    List<Course> courseList;
    ScoreAdapter adapter;
    TextView termtextview, endtextview,judgetextview;
    ImageView judgeimageview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        MyApplication.activityList.add(this);
        Intent intent = getIntent();
        listCourseList = (List<List<Course>>) intent.getExtras().getSerializable("SCORERESULT");
        Collections.reverse(listCourseList);
        viewList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < listCourseList.size(); i++) {
            View view = inflater.inflate(R.layout.view_score, null);
            viewList.add(view);
        }
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        initView();


    }

    public void initView() {
        for (int i = 0; i < listCourseList.size(); i++) {
            termtextview = (TextView) viewList.get(i).findViewById(R.id.termtextview);
            endtextview = (TextView) viewList.get(i).findViewById(R.id.endtextview);
            judgetextview=(TextView)viewList.get(i).findViewById(R.id.judgetextview);
            judgeimageview=(ImageView) viewList.get(i).findViewById(R.id.judgeImageView);
            recyclerView = (RecyclerView) viewList.get(i).findViewById(R.id.recyclerView);
            String text = "第" + (listCourseList.size() - i) + "学期";
            termtextview.setText(text);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            courseList = listCourseList.get(i);
            adapter = new ScoreAdapter(courseList, ScoreActivity.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            float totalCredit = 0;
            float totalGpa = 0;
            for (Course c : courseList) {
                if (c.getcType().equals("必修课")) {
                    totalCredit += Float.parseFloat(c.getcCredit());
                    totalGpa += (Float.parseFloat(c.getcGpa())*Float.parseFloat(c.getcCredit()));
                }
            }
            float aveCredit = totalGpa / totalCredit;
            DecimalFormat format = new DecimalFormat(".0");
            String aveCreditString = format.format(aveCredit);
            String totalCreditString = format.format(totalCredit);
            String totalGpaString = format.format(totalGpa);
            endtextview.setTextSize(20);
            endtextview.setText("学期总绩点：" + totalGpaString +"  "+ "学期总学分：" + totalCreditString +"\n"+ "学期平均绩点：" + aveCreditString+"\n");
            String rank="S";
            if(aveCredit<=4.0){
                rank="S";
            }if(aveCredit<3.5){
                rank="A";
            }if(aveCredit<3.0){
                rank="B";
            }if(aveCredit<2.5){
                rank="C";
            }if(aveCredit<2.0){
                rank="D";
            }if(aveCredit<1.0){
                rank="E";
            }
            switch (rank){
                case "S":
                    judgeimageview.setImageResource(R.drawable.niubi);
                    judgetextview.setText("你考99分是因为你的极限就是99分，而我考100分是因为试卷只有100分");
                    break;
                case "A":
                    judgeimageview.setImageResource(R.drawable.zixin);
                    judgetextview.setText("闲来倚碧黛，起而令千军。");
                    break;
                case "B":
                    judgeimageview.setImageResource(R.drawable.huaji);
                    judgetextview.setText("苟利国家生死以，岂因祸福避趋之。");
                    break;
                case "C":
                    judgeimageview.setImageResource(R.drawable.hehe);
                    judgetextview.setText("那时我含泪发誓，各位必须看到我。");
                    break;
                case "D":
                    judgeimageview.setImageResource(R.drawable.piezui);
                    judgetextview.setText("我见诸君多傻逼，料诸君见我应如是。");
                    break;
                case "E":
                    judgeimageview.setImageResource(R.drawable.liuhan);
                    judgetextview.setText("需要退学的学生由本人提出申请或由各系部提出报告，教务处审核，由院长会议研究决定。教务处电话：67781096。");
                    break;
            }

        }
    }
}
