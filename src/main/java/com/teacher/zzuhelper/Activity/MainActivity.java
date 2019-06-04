package com.teacher.zzuhelper.Activity;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.KeyEvent;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teacher.zzuhelper.HttpUtil.HttpPost;
import com.teacher.zzuhelper.HttpUtil.PostTask;
import com.teacher.zzuhelper.Mould.Cost;
import com.teacher.zzuhelper.Mould.Course;

import com.teacher.zzuhelper.Mould.NewsAndNotice;
import com.teacher.zzuhelper.Mould.Subject;
import com.teacher.zzuhelper.Mould.User;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.CostTask;
import com.teacher.zzuhelper.Util.MyApplication;
import com.teacher.zzuhelper.Util.NewsAndNoticeTask;
import com.teacher.zzuhelper.Util.ObjectSaveUtils;
import com.teacher.zzuhelper.Util.ScoreTask;
import com.teacher.zzuhelper.View.MyImageButton;
import com.teacher.zzuhelper.View.SlidingMenu;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView hello_textview, point_textview, sentence_textview,
            name_textview, college_textview, specialty_textview, clazz_textview, notice_textview;
    private SlidingMenu slidingMenu;
    private Toolbar toolbar;
    private ImageButton toggleButton;
    private MyImageButton score_button, table_button, balance_button, cost_button,
            book_button, seat_button, announcement_button, news_button;
    private boolean isLogin = false;
    private static boolean isExit = false;
    private CardView cardView;
    private ProgressDialog dialog;

    private IntentFilter intentFilter;
    private TimekChangeReceiver receiver;
    private Calendar c;
    private List<String> sentenceList;
    private Intent scoreIntent;

    private String number, password, grade, college, specialities, clazz, name,id;
    User user;
    boolean isGetScore = false;
    String html = null;

    List<NewsAndNotice> newsList;
    List<NewsAndNotice> noticeList;
    boolean isGetNews = false;
    boolean isGetNotice = false;
    private  NewsAndNotice newNotice;

    private  List<Cost> costList;


    private static final String SCOREURL = "http://jw.zzu.edu.cn/scripts/qscore.dll/search";
    private static final String TABLEURL = "http://jw.zzu.edu.cn/pks/pkisapi2.dll/kbofstu";
    private static final String NEWSURL = "http://jw.zzu.edu.cn/scripts/news.dll/morenews?type=2&pn=1";
    private static final String NOTICEURL = "http://jw.zzu.edu.cn/scripts/news.dll/morenews";
    private static final String COSTURL="http://ecard.zzu.edu.cn/c/portal/login";

    List<List<Course>> courseList = new ArrayList<>();
    List<Subject> subjectList = new ArrayList<>();
    String result;

    static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.activityList.add(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        receiver = new TimekChangeReceiver();
        registerReceiver(receiver, intentFilter);
        /*
        findViewById
         */
        initView();
        /*
        初始化
         */
//        initNoticeList();
        init();
        initSentenceList();
        updateWelcomeView();
        /*
        绑定方法
         */
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });
        score_button.setOnClickListener(this);
        table_button.setOnClickListener(this);
        seat_button.setOnClickListener(this);
        book_button.setOnClickListener(this);
        announcement_button.setOnClickListener(this);
        news_button.setOnClickListener(this);
        notice_textview.setOnClickListener(this);
        cost_button.setOnClickListener(this);
        balance_button.setOnClickListener(this);
        if (isLogin) {
            initScore();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public String returnList(List<Course> courseList) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < courseList.size(); i++) {
            result.append(courseList.get(i).getcName()).append("\n");
            result.append(courseList.get(i).getcType()).append("\n");
            result.append(courseList.get(i).getcCredit()).append("\n");
            result.append(courseList.get(i).getcScore()).append("\n");
            result.append(courseList.get(i).getcGpa()).append("\n");
        }
        Log.d("结果是", result.toString());
        return result.toString();
    }

    public void setTextviewContent() {
        if (!isLogin) {
            point_textview.setText("请登录");
            name_textview.setText("未登录");
            specialty_textview.setVisibility(View.INVISIBLE);
            college_textview.setVisibility(View.INVISIBLE);
            clazz_textview.setVisibility(View.INVISIBLE);
        }
    }

    public void toLoginActivity() {
        if (!isLogin) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            MyApplication.exitClient(MainActivity.this);
            System.exit(0);
        }
    }

    @Override
    public void onClick(View v) {
        if (!checkNetworkInfo()&&v.getId()!=R.id.table_button&&subjectList != null && !subjectList.isEmpty()) {
            Toast.makeText(MainActivity.this, "获取数据失败，请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ProgressDialog.show(MainActivity.this, "提示", "正在加载数据",
                true, true);
        switch (v.getId()) {
            case R.id.score_button:
                if (isLogin) {
                    try {
                        if (!isGetScore) {
                            initScore();
                        }
                        if (html.equals("")) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "获取数据失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        result = listToString(courseList);
                        if (result.equals("")) {
                            Toast.makeText(MainActivity.this, "正在读取数据，稍后请重试", Toast.LENGTH_SHORT).show();
                        } else {
                            scoreIntent = new Intent(MainActivity.this, ScoreActivity.class);
                            scoreIntent.putExtra("SCORERESULT", (Serializable) courseList);
                            startActivity(scoreIntent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "未登录，请登录后重试", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.table_button:
                if (isLogin) {
                    try {
                        if (subjectList == null || subjectList.isEmpty()) {
                            String html = null;
                            html = new PostTask().execute(new String[]{TABLEURL, TABLEURL, grade, number, password}).get();
                            if (html.equals("")) {
                                dialog.dismiss();
                                Toast.makeText(this, "获取数据失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            subjectList = HttpPost.parseSubjectHtml(html);
                            saveSubjectList();
                        }
                        if (returnSubject(subjectList).equals("")) {
                            Toast.makeText(MainActivity.this, "正在读取数据，稍后请重试", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, TableActivity.class);
                            intent.putExtra("RESULT", (Serializable) subjectList);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"未登录，请登录后重试",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                break;
            case R.id.seat_button:
                Intent toSeatIntent = new Intent(MainActivity.this, SeatActivity.class);
                startActivity(toSeatIntent);
                break;
            case R.id.book_button:
                Intent toQueryIntent = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(toQueryIntent);
                break;
            case R.id.news_button:
                if (!isGetNews) {
                    initNewsList();
                }
                Intent newsIntent = new Intent(MainActivity.this, NewsAndNoticeActivity.class);
                newsIntent.putExtra("NEWSANDNOTICE", (Serializable) newsList);
                startActivity(newsIntent);
                break;
            case R.id.announcement_button:
                if (!isGetNotice) {
                    initNoticeList();
                }
                Intent noticeIntent = new Intent(MainActivity.this, NewsAndNoticeActivity.class);
                noticeIntent.putExtra("NEWSANDNOTICE", (Serializable) noticeList);
                startActivity(noticeIntent);
                break;
            case R.id.cost_button:
                try {
                    costList=new CostTask().execute(new String[]{COSTURL}).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.balance_button:
                break;
            case R.id.notice_textview:
                if(newNotice==null){
                    getNewNotice();
                }
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                intent.putExtra("CONTENT", newNotice.getHtml());
                startActivity(intent);
        }
    }

    public String listToString(List<List<Course>> list) {
        String result = "";
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).size(); j++) {
                    Course course = list.get(i).get(j);
                    result += course.getcName() + "-----";
                }
                result += "\n";
            }
        }
        return result;
    }


    public String returnSubject(List<Subject> subjectList) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < subjectList.size(); i++) {
            result.append(subjectList.get(i).getsName());
        }
        return result.toString();
    }

    public void saveSubjectList() {

        try {
            ObjectSaveUtils.savaObject(this, subjectList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Subject> getSubjcteList() {
        try {
            return ObjectSaveUtils.getObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class TimekChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateWelcomeView();
        }
    }

    public void updateWelcomeView() {
        c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int week = c.get(Calendar.DAY_OF_WEEK);
        List<Subject> subjectList = getSubjcteList();
        Subject[] subjects = new Subject[5];
        String weekString = "";
        switch (week) {
            case 1:
                weekString = "星期日";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(6);
                    subjects[1] = subjectList.get(13);
                    subjects[2] = subjectList.get(20);
                    subjects[3] = subjectList.get(27);
                    subjects[4] = subjectList.get(34);
                }
                break;
            case 2:
                weekString = "星期一";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(0);
                    subjects[1] = subjectList.get(7);
                    subjects[2] = subjectList.get(14);
                    subjects[3] = subjectList.get(21);
                    subjects[4] = subjectList.get(28);
                }
                break;
            case 3:
                weekString = "星期二";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(1);
                    subjects[1] = subjectList.get(8);
                    subjects[2] = subjectList.get(15);
                    subjects[3] = subjectList.get(22);
                    subjects[4] = subjectList.get(29);
                }
                break;
            case 4:
                weekString = "星期三";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(2);
                    subjects[1] = subjectList.get(9);
                    subjects[2] = subjectList.get(16);
                    subjects[3] = subjectList.get(23);
                    subjects[4] = subjectList.get(30);
                }
                break;
            case 5:
                weekString = "星期四";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(3);
                    subjects[1] = subjectList.get(10);
                    subjects[2] = subjectList.get(17);
                    subjects[3] = subjectList.get(24);
                    subjects[4] = subjectList.get(31);
                }
                break;
            case 6:
                weekString = "星期五";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(4);
                    subjects[1] = subjectList.get(11);
                    subjects[2] = subjectList.get(18);
                    subjects[3] = subjectList.get(25);
                    subjects[4] = subjectList.get(32);
                }
                break;
            case 7:
                weekString = "星期六";
                if (subjectList != null) {
                    subjects[0] = subjectList.get(5);
                    subjects[1] = subjectList.get(12);
                    subjects[2] = subjectList.get(19);
                    subjects[3] = subjectList.get(26);
                    subjects[4] = subjectList.get(33);
                }
                break;
        }
        String time = "";
        if (hour < 6) {
            time = "夜深了，晚安  ";
        } else if (hour < 12) {
            time = "上午好，";
        } else if (hour < 14) {
            time = "中午好，";
        } else if (hour < 19) {
            time = "下午好，";
        } else if (hour < 24) {
            time = "晚上好，";
        }
        hello_textview.setText(time + "今天是" + weekString);
        if (subjectList == null) {
            if (isLogin) {
                point_textview.setText("欢迎来到郑大助手");
            }
        } else {
            int count = 5;
            for (int i = 0; i < subjects.length; i++) {
                if (subjects[i].getsName().equals("")) {
                    count -= 1;
                }
            }
            if (count == 0) {
                point_textview.setText("今天没课，好好休息或者继续前行");
            } else {
                if (hour <= 8) {
                    for (int i = 0; i < subjects.length; i++) {
                        String num = getSubjectNum(i);
                        if (!subjects[i].getsName().equals("")) {
                            point_textview.setText("下一节课是：" + "\n" + num + subjects[i].getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjects[i].getsClass() + ")");
                            break;
                        } else {
                            point_textview.setText("今天的课上完了，好好休息吧");
                        }
                    }
                } else if (hour <= 10) {
                    for (int i = 1; i < subjects.length; i++) {
                        String num = getSubjectNum(i);
                        if (!subjects[i].getsName().equals("")) {
                            point_textview.setText("下一节课是：" + "\n" + num + subjects[i].getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjects[i].getsClass() + ")");
                            break;
                        } else {
                            point_textview.setText("今天的课上完了，好好休息吧");
                        }
                    }
                } else if (hour <= 14) {
                    for (int i = 2; i < subjects.length; i++) {
                        String num = getSubjectNum(i);
                        if (!subjects[i].getsName().equals("")) {
                            point_textview.setText("下一节课是：" + "\n" + num + subjects[i].getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjects[i].getsClass() + ")");
                            break;
                        } else {
                            point_textview.setText("今天的课上完了，好好休息吧");
                        }
                    }
                } else if (hour <= 16) {
                    for (int i = 3; i < subjects.length; i++) {
                        String num = getSubjectNum(i);
                        if (!subjects[i].getsName().equals("")) {
                            point_textview.setText("下一节课是：" + "\n" + num + subjects[i].getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjects[i].getsClass() + ")");
                            break;
                        } else {
                            point_textview.setText("今天的课上完了，好好休息吧");
                        }
                    }
                } else if (hour <= 20) {
                    for (int i = 4; i < subjects.length; i++) {
                        String num = getSubjectNum(i);
                        if (!subjects[i].getsName().equals("")) {
                            point_textview.setText("下一节课是：" + "\n" + num + subjects[i].getsName().replaceAll("\\d+", "").replaceAll(":", "") + "(" + subjects[i].getsClass() + ")");
                            break;
                        } else {
                            point_textview.setText("今天的课上完了，好好休息吧");
                        }
                    }
                }
                if (hour > 20) {
                    point_textview.setText("今天的课上完了，好好休息吧");
                }
            }
        }
    }

    public boolean checkNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }

    public void initScore() {
        try {
            if (!isGetScore) {
                html = new PostTask().execute(new String[]{SCOREURL, SCOREURL, grade, number, password}).get();
                courseList = new ScoreTask().execute(html).get();
                isGetScore = true;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initNewsList() {
        if (!isGetNews) {
            try {
                String newsHtml = new PostTask().execute(new String[]{NEWSURL, null, null, null, null}).get();
                newsList = new NewsAndNoticeTask().execute(newsHtml).get();
                isGetNews = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void initNoticeList() {
        if (!isGetNotice) {
            try {
                String noticeHtml = new PostTask().execute(new String[]{NOTICEURL, null, null, null, null}).get();
                noticeList = new NewsAndNoticeTask().execute(noticeHtml).get();
                isGetNotice = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    public void init() {

        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        number = pref.getString("number", "");
        password = pref.getString("password", "");
        grade = pref.getString("grade", "");
        name = pref.getString("name", "");
        clazz = pref.getString("clazz", "");
        college = pref.getString("college", "");
        specialities = pref.getString("specialities", "");
        id=pref.getString("id","");
        if (getSubjcteList() != null) {
            subjectList = getSubjcteList();
        }

        if (!number.equals("")) {
            MyApplication.setUser(name,number,password,college,specialities,grade,clazz,id);
            user = MyApplication.getUser();
            specialty_textview.setVisibility(View.VISIBLE);
            college_textview.setVisibility(View.VISIBLE);
            clazz_textview.setVisibility(View.VISIBLE);
            name_textview.setText(name);
            specialty_textview.setText(specialities);
            college_textview.setText(college);
            clazz_textview.setText(clazz);
            isLogin = true;
        }

        if (!isLogin) {
            setTextviewContent();
        }
       getNewNotice();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        slidingMenu = (SlidingMenu) findViewById(R.id.sliding_menu);
        toggleButton = (ImageButton) findViewById(R.id.toggle_button);
        score_button = (MyImageButton) findViewById(R.id.score_button);
        table_button = (MyImageButton) findViewById(R.id.table_button);
        balance_button = (MyImageButton) findViewById(R.id.balance_button);
        cost_button = (MyImageButton) findViewById(R.id.cost_button);
        book_button = (MyImageButton) findViewById(R.id.book_button);
        seat_button = (MyImageButton) findViewById(R.id.seat_button);
        announcement_button = (MyImageButton) findViewById(R.id.announcement_button);
        news_button = (MyImageButton) findViewById(R.id.news_button);
        //文字
        hello_textview = (TextView) findViewById(R.id.hello_textview);
        point_textview = (TextView) findViewById(R.id.point_textview);
        sentence_textview = (TextView) findViewById(R.id.sentence_textview);
        name_textview = (TextView) findViewById(R.id.name_textview);
        college_textview = (TextView) findViewById(R.id.college_textview);
        specialty_textview = (TextView) findViewById(R.id.specialty_textview);
        clazz_textview = (TextView) findViewById(R.id.clazz_textview);
        //视图
        cardView = (CardView) findViewById(R.id.card_view);
        notice_textview = (TextView) findViewById(R.id.notice_textview);
    }

    public void getNewNotice(){
        if (checkNetworkInfo()) {
            try {
                String newNoticeHtml = new PostTask().execute(NOTICEURL, null, null, null, null).get();
                Document htmlDocument = Jsoup.parse(newNoticeHtml);
                Element element = htmlDocument.getElementsByTag("td").get(1);
                Elements a = element.getElementsByTag("a");
                String time = element.ownText();
                String[] timeArr = time.split("[]]");
                time = timeArr[1];
                String title = a.text();
                String oneHtml = a.outerHtml();
                String[] tempArr = oneHtml.split("\"");
                oneHtml = tempArr[3];
                newNotice = new NewsAndNotice(time, title, oneHtml);
                notice_textview.setText(newNotice.getTitle() + "(" + newNotice.getTime() + ")");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void initSentenceList() {
        sentenceList = new ArrayList<>();
        sentenceList.add("奈何长久坚强未使柔弱变珍贵，偶尔任性便有罪。");
        sentenceList.add("我们可以卑微如尘土，不可扭曲如蛆虫。");
        sentenceList.add("我所有的自负都来自我的自卑，所有的英雄气概都来自我内心的软弱，所有的振振有词都因为心中充满疑惑。");
        sentenceList.add("你如今的气质里，藏着你走过的路，读过的书，和爱过的人。");
        sentenceList.add("当你凝视深渊的时候，深渊也在凝视着你。");
        sentenceList.add("未佩妥剑，出门便已是江湖。");
        sentenceList.add("很怀念我们刚认识那会儿，大家都有些拘谨和真诚。");
        sentenceList.add("与这个世界交手多年，你是否光彩依旧 兴致盎然？");
        sentenceList.add("哪有人喜欢孤独，只不过是害怕失望罢了。");
        sentenceList.add("世人谓我恋长安 其实只恋长安某。");
        sentenceList.add("但行好事，莫问前程。");
        sentenceList.add("它侧身于生活的污泥中，虽不甘心，却又畏首畏尾");
        sentenceList.add("别人稍一注意你，你就敞开心扉，你觉得这是坦率，其实这是孤独");
        sentenceList.add("但是，假如你驯服了我，我们就彼此需要了。对我而言，你就是举世无双的；对你而言，我也是独一无二的……");
        sentenceList.add("其实任何人，在经历时，都不会知道自己正在经历一生中最幸福的时刻。");
        sentenceList.add("每个人只能陪你走一段路，迟早是要分开的。不是所有的东西都会被时间摧毁。");
        sentenceList.add("你要做一个不动声色的大人了。不准情绪化，不准偷偷想念，不准回头看。去过自己另外的生活。");
        sentenceList.add("一天不独处，我就会变得很虚弱。我不以孤独为荣，但我以此维生。 ");
        sentenceList.add("我怎么敢倒下，我身后空无一人。");
        sentenceList.add("在一群出色的人中间，常常误以为自己也是其中一员，然后忘了努力。");
        sentenceList.add("似乎大家都是这样。自命不凡，却无足轻重。");
        sentenceList.add("人的一切痛苦，本质上都是对自己无能的愤怒。");
        sentenceList.add("人生和电影不同，人生辛苦多了。如果你不出去走走，你就会以为这就是全世界。");
        sentenceList.add("从话语中，你很少能学到人性，从沉默中却能。假如还想学得更多，那就要继续一声不吭。");
        sentenceList.add("他依然向往着长岛的雪,依然向往着潘帕斯的风吟鸟唱。 很久我才知道，原来，长岛是没有雪的。");
        sentenceList.add("小时候刮奖刮出“谢”字还不扔，非要把“谢谢惠顾”都刮的干干净净才舍得放手，和后来太多的事一模一样。");
        sentenceList.add("最怕你一生碌碌无为，还安慰自己平凡可贵。");
        sentenceList.add("你的快乐一直不缺观众，也愿你的无邪有人真懂");
        sentenceList.add("愿你明朗坦荡纵情豁达，有得有失有坚持，能哭能笑能尽欢！");
        sentenceList.add("你的问题主要在于读书不多而想得太多。");
        Random r = new Random();
        int i = r.nextInt(sentenceList.size());
        sentence_textview.setText(sentenceList.get(i));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null) {
            dialog.dismiss();
        }
        subjectList = getSubjcteList();
        updateWelcomeView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public String getSubjectNum(int i) {
        String num = "";
        switch (i) {
            case 0:
                num = "(上午1-2节)";
                break;
            case 1:
                num = "(上午3-4节)";
                break;
            case 2:
                num = "(下午5-6节)";
                break;
            case 3:
                num = "(下午7-8节)";
                break;
            case 4:
                num = "(晚上9-10节)";
                break;
        }
        return num;
    }


}
