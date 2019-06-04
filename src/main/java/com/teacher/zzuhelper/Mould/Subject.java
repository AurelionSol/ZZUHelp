package com.teacher.zzuhelper.Mould;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Subject implements Serializable{
    private String sName;
    private String sClass;
    private String sCredit;
    private String sTeacher;

    public Subject(String sName, String sCredit, String sClass, String sTeacher) {
        this.sName = sName;
        this.sCredit = sCredit;
        this.sClass = sClass;
        this.sTeacher = sTeacher;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getsCredit() {
        return sCredit;
    }

    public void setsCredit(String sCredit) {
        this.sCredit = sCredit;
    }

    public String getsTeacher() {
        return sTeacher;
    }

    public void setsTeacher(String sTeacher) {
        this.sTeacher = sTeacher;
    }
}
