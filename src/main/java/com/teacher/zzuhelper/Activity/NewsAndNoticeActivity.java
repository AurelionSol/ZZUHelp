package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.teacher.zzuhelper.Apapter.NewsAndNoticeAdapter;
import com.teacher.zzuhelper.Mould.NewsAndNotice;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewsAndNoticeActivity extends Activity {
    List<NewsAndNotice> newsAndNoticeList;
   NewsAndNoticeAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsandnotice);
        MyApplication.activityList.add(this);
        recyclerView = ( RecyclerView)findViewById(R.id.newsandnotice_recyclerView);
        newsAndNoticeList = (List<NewsAndNotice>)getIntent().getExtras().getSerializable("NEWSANDNOTICE");
        adapter = new NewsAndNoticeAdapter(newsAndNoticeList,NewsAndNoticeActivity.this);
        adapter.setOnItemClickListener(new NewsAndNoticeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NewsAndNotice newsAndNotice) {
                Intent intent = new Intent(NewsAndNoticeActivity.this,ContentActivity.class);
                intent.putExtra("CONTENT",newsAndNotice.getHtml());
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
