package com.teacher.zzuhelper.Mould;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewsAndNotice implements Serializable {
    private String time;
    private String title;
    private String html;

    public NewsAndNotice(String time, String title, String html) {
        this.time = time;
        this.title = title;
        this.html = html;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
