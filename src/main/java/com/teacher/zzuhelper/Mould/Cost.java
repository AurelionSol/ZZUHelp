package com.teacher.zzuhelper.Mould;

/**
 * Created by Administrator on 2016/10/21.
 */
public class Cost {
    String time;
    String type;
    String place;
    String value;

    public Cost(String time, String type, String place, String value) {
        this.time = time;
        this.type = type;
        this.place = place;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
