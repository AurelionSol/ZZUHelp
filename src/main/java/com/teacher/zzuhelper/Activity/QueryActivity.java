package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

/**
 * Created by Administrator on 2016/10/15.
 */
public class QueryActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        MyApplication.activityList.add(this);
        webView = (WebView) findViewById(R.id.query_web_view);
        webView.loadUrl("http://202.197.191.171:8991/F/9KNG5UL1E8K3XRYVR9SB2JA8U1HLM2L1BSKAFIYTC3T8I5E2HJ-32581");
        //设置编码
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient(){
            // 这个方法在用户试图点开页面上的某个链接时被调用
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url!=null) {
                    // 如果想继续加载目标页面则调用下面的语句
                     view.loadUrl(url);
                    // 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
                }
                // 返回true表示停留在本WebView（不跳转到系统的浏览器）
                return true;
            }
        });
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&& webView.canGoBack()) {
            webView.goBack();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
