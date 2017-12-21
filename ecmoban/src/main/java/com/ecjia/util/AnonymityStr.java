package com.ecjia.util;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/20 20:18.
 */

public class AnonymityStr {
    static final char INIT_ANONYMITY = '*';
    static char[] chars;
    public static String onAnonymityName(String str){
        chars=str.toCharArray();
        if(chars.length == 2){
            return onAnonymityStr(chars,0,1);
        } else if(chars.length >= 3){
            return onAnonymityStr(chars,1,chars.length-1);
        } else {
            return str;
        }
    }

    public static String onAnonymityCard(String str){
        chars=str.toCharArray();
        if(chars.length >= 9){
            return onAnonymityStr(chars,4,chars.length-4);
        } else {
            return str;
        }
    }

    public static String onAnonymityID(String str){
        chars=str.toCharArray();
        if(chars.length >= 10){
            return onAnonymityStr(chars,6,chars.length-3);
        } else {
            return str;
        }
    }
    public static String onAnonymityPhone(String str){
        chars=str.toCharArray();
        if(chars.length >= 8){
            return onAnonymityStr(chars,7,chars.length);
        } else {
            return str;
        }
    }

    private static String onAnonymityStr(char[] chars, int start, int end){
        for(int i=0;i<chars.length;i++){
            if(i >= start && i<end){
                chars[i] = INIT_ANONYMITY;
            }
        }
        return String.valueOf(chars);
    }
}
