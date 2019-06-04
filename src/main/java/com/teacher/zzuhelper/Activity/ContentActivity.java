package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ContentActivity extends Activity{
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        MyApplication.activityList.add(this);
        webView = (WebView) findViewById(R.id.content_web_view);
        Intent intent= getIntent();
        String url = intent.getExtras().getString("CONTENT");
        if(url.indexOf(".doc")!=-1||url.indexOf(".pdf")!=-1){
            Toast.makeText(ContentActivity.this,"这是一个文件,无法显示,请返回",Toast.LENGTH_SHORT).show();
        }
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        //设置是否缓存
//        webView.getSettings().setAppCacheEnabled(true);
//        //设置是否数据库存储
//        webView.getSettings().setDatabaseEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);

        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        // LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
        // 总结：根据以上两种模式，建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT，无网络时，使用LOAD_CACHE_ELSE_NETWORK。
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }
}
