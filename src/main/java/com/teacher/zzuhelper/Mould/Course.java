package com.teacher.zzuhelper.Mould;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/10.
 */
public class Course implements Serializable{
    private String cName;
    private String cType;
    private String cCredit;
    private String cScore;
    private String cGpa;

    public Course(String cName, String cType, String cCredit, String cScore, String cGpa) {
        this.cName = cName;
        this.cType = cType;
        this.cCredit = cCredit;
        this.cScore = cScore;
        this.cGpa = cGpa;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getcCredit() {
        return cCredit;
    }

    public void setcCredit(String cCredit) {
        this.cCredit = cCredit;
    }

    public String getcScore() {
        return cScore;
    }

    public void setcScore(String cScore) {
        this.cScore = cScore;
    }

    public String getcGpa() {
        return cGpa;
    }

    public void setcGpa(String cGpa) {
        this.cGpa = cGpa;
    }
}
