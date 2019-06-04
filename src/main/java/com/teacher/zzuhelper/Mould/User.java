package com.teacher.zzuhelper.Mould;

/**
 * Created by Administrator on 2016/9/13.
 */
public class User {
    private String name;
    private String studentNumber;
    private String password;
    private String college;
    private String specialities;
    private String grade;
    private String clazz;
    private String id;

    private static User instance = null;
    public static synchronized User getInstance(String name, String studentNumber, String password, String college, String specialities, String grade, String clazz,String id){
        if(instance==null)
        {
            instance=new User(name,studentNumber,password,college,specialities,grade,clazz,id);
        }
        return instance;
    }



    public User(String name, String studentNumber, String password, String college, String specialities, String grade, String clazz,String id) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.password = password;
        this.college = college;
        this.specialities = specialities;
        this.grade = grade;
        this.clazz = clazz;
        this.id=id;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
