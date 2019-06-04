package com.teacher.zzuhelper.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.teacher.zzuhelper.Activity.MainActivity;
import com.teacher.zzuhelper.Mould.Subject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/9/24.
 */
public class ObjectSaveUtils {
    /**
     * 序列化对象
     *
     * @param subjectList
     * @return
     * @throws IOException
     */
    public static void savaObject(Context context, List<Subject> subjectList) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(subjectList);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();

        SharedPreferences sp = context.getSharedPreferences("subjectList", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("subjectList", serStr);
        edit.commit();

    }

    /**
     * 反序列化对象
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static  List<Subject> getObject(Context context) throws IOException,
            ClassNotFoundException {
        SharedPreferences sp = context.getSharedPreferences("subjectList", 0);
        String str = sp.getString("subjectList", null);
        if(str!=null){
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<Subject> subjectList = (List<Subject>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return subjectList;}else {
            return null;
        }
    }


}
