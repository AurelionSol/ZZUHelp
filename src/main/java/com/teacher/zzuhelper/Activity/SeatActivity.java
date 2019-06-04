package com.teacher.zzuhelper.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

/**
 * Created by Administrator on 2016/10/13.
 */
public class SeatActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        MyApplication.activityList.add(this);
        webView = (WebView) findViewById(R.id.seat_web_view);
        webView.loadUrl("http://202.197.191.152/roompre/Default.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        //设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //设置是否缓存
        webView.getSettings().setAppCacheEnabled(true);
        //设置是否数据库存储
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);

        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        // LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
        // 总结：根据以上两种模式，建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT，无网络时，使用LOAD_CACHE_ELSE_NETWORK。
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        Toast.makeText(this,"提示：用户名为学号，默认密码为学号",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&& webView.canGoBack()) {
            // WEBVIEW返回前一个页面
            webView.goBack();
            return true;
            // 返回时应该进行的操作

        }
        return super.onKeyDown(keyCode, event);
    }
}
