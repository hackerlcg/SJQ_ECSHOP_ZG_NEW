package com.ecjia.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class CommonMethod {
    private static CommonMethod instance;
    private static Context mContext;
    private static List<String> list;
    private static String secret = "#FENGCHEN#";

    private CommonMethod() {

    }

    public static CommonMethod getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new CommonMethod();
        }
        return instance;
    }

    public void setSeachStringToShared(String str) {
        if (str == null || "".equals(str)) return;
        SharedPreferences sahrePreferences = mContext.getSharedPreferences(
                "my_shared", 0);
        Editor editor = sahrePreferences.edit();
        StringBuffer listString = new StringBuffer();
        String oldString = sahrePreferences.getString("search_list", "");
        List<String> replaceString = getSeachStringFromShared();
        for (int i = 0; i < replaceString.size(); i++) {
            if (str.equals(replaceString.get(i))) {
                oldString.replace("" + secret + str + secret, secret);
                oldString.replace("" + str + secret, "");
                oldString.replace("" + secret, "");
            }

        }
        if ("".equals(oldString)) {

            listString.append(str);
            listString.append(secret);

        } else {
            listString.append(str);
            listString.append(secret);
            listString.append(oldString);

        }
        editor.putString("search_list", listString.toString());
        editor.commit();

    }

    public List<String> getSeachStringFromShared() {
        list = new ArrayList<String>();
        SharedPreferences sahrePreferences = mContext.getSharedPreferences(
                "my_shared", 0);
        String oldString = sahrePreferences.getString("search_list", "");
        if(!TextUtils.isEmpty(oldString)){
            String[] str = oldString.split(secret);
            for (int i = 0; i < str.length; i++) {
                list.add(str[i]);
            }
        }
        //取消重复元素
        for (int i = 0; i < list.size() - 1; i++) {  //循环遍历集体中的元素
            for (int j = list.size() - 1; j > i; j--) {//倒序
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }

        return list;
    }

}
