package com.ecjia.util.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecjia.hamster.model.USER;

/**
 * Created by yubaokang on 2016/9/28.
 */
public class SPUtils {
    private static final String FILE_NAME = "ECJIA";//文件名
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    //////////////////////////////////////////
    private final static String isLogined = "idLogined";//是否登录
    private final static String ACCOUNT = "account";//String 账号
    private final static String PWD = "pwd";//String md5加密处理保存 密码
    private final static String PWD_LENGTH = "pwd_LENGTH";//密码长度
    //UUID
    private final static String UUID = "uuid";//String

    private final static String SHOP_TOKEN = "shop_token"; //未登录状态下的token

    //用户信息-start
    private final static String JSESSIONID = "JSESSIONID";//String
    private final static String USER_ID = "userId";//String
    private final static String USER_NAME = "userName";//String
    private final static String HEAD_PORTRAIT = "headPortrait";//String
    private final static String REAL_NAME = "realName";//String
    private final static String ROLE_NAME = "role_name";//String
    private final static String ROLE_ID = "role_id";//int
    private final static String ROLE_CODE = "role_code";//String角色判断
    private final static String ORG_ID = "org_id";//int
    private final static String MOBILE = "mobile";//int
    private final static String[] userDatas = {JSESSIONID, USER_ID, USER_NAME, HEAD_PORTRAIT, REAL_NAME, ROLE_NAME, ROLE_ID, ROLE_CODE, ORG_ID, MOBILE};
    //用户信息-end

    //客服电话
    private final static String SERVICE_MOBILE = "service_mobile";//String

    /**
     * 初始化-Application中初始化
     *
     * @param context
     */
    public static void init(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 判断是否含有该key
     *
     * @param key 键
     * @return
     */
    public static boolean contains(String key) {
        return sp.contains(key);
    }

    public static boolean hasLogined() {
        return sp.getBoolean(isLogined, false);
    }

    /**
     * 保存用户账号
     *
     * @param value
     */
    public static void setAccount(String value) {
        editor.putString(ACCOUNT, value).apply();
    }

    /**
     * 保存密码-MD5加密
     *
     * @param value
     */
    public static void setPwd(String value) {
        editor.putString(PWD, value).apply();
    }


    /**
     * 保存用户信息
     *
     * @param userData
     */
    public static void setUserData(USER userData) {


        //        setJSESSIONID(userData.getJSESSIONID());
        //        setUserId(userData.getUserId());
        //        setUserName(userData.getUserName());
        //        setHeadPortrait(userData.getHeadPortrait());
        //        setRealName(userData.getRealName());
        //        setRoleName(userData.getRole_name());
        //        setRoleId(userData.getRole_id());
        //        setRoleCode(userData.getRole_code());
        //        setOrgId(userData.getOrg_id());
        //        setMobile(userData.getMobile());
    }

    /**
     * 移除 " 账号密码 " 和 " 用户信息 "
     */
    public static void loginOut() {
        //        editor.remove(ACCOUNT).remove(PWD);
        setUUID("");//清空UUID，下次手动登录可以重新获取到新的UUID
        editor.remove(isLogined);
        for (String key : userDatas) {
            editor.remove(key);
        }
        editor.apply();
    }

    public static void setJSESSIONID(String value) {
        editor.putString(JSESSIONID, value).apply();
    }

    public static void setUserId(String value) {
        editor.putString(USER_ID, value).apply();
    }

    public static void setUserName(String value) {
        editor.putString(USER_NAME, value).apply();
    }

    public static void setHeadPortrait(String value) {
        editor.putString(HEAD_PORTRAIT, value).apply();
    }

    public static void setRealName(String value) {
        editor.putString(REAL_NAME, value).apply();
    }

    public static void setRoleName(String value) {
        editor.putString(ROLE_NAME, value).apply();
    }

    public static void setRoleId(int value) {
        editor.putInt(ROLE_ID, value).apply();
    }

    public static void setRoleCode(String value) {
        editor.putString(ROLE_CODE, value).apply();
    }

    public static void setOrgId(int value) {
        editor.putInt(ORG_ID, value).apply();
    }

    public static void setMobile(String value) {
        editor.putString(MOBILE, value).apply();
    }

    public static void setServiceMobile(String value) {
        editor.putString(SERVICE_MOBILE, value).apply();
    }

    public static void setHasLogined(boolean value) {
        editor.putBoolean(isLogined, value);
    }

    public static String getAccount() {
        return sp.getString(ACCOUNT, "");
    }

    public static String getPwd() {
        return sp.getString(PWD, "");
    }

    public static String getJSESSIONID() {
        return sp.getString(JSESSIONID, "");
    }

    public static String getUserId() {
        return sp.getString(USER_ID, "");
    }

    public static String getUserName() {
        return sp.getString(USER_NAME, "");
    }

    public static String getHeadPortrait() {
        return sp.getString(HEAD_PORTRAIT, "");
    }

    public static String getRealName() {
        return sp.getString(REAL_NAME, "");
    }

    public static String getRoleName() {
        return sp.getString(ROLE_NAME, "");
    }

    public static int getRoleId() {
        return sp.getInt(ROLE_ID, -1);
    }

    public static String getRoleCode() {
        return sp.getString(ROLE_CODE, "");
    }

    public static int getOrgId() {
        return sp.getInt(ORG_ID, -1);
    }

    public static String getMobile() {
        return sp.getString(MOBILE, "");
    }

    public static String getServiceModile() {
        return sp.getString(SERVICE_MOBILE, "");
    }

    public static void setUUID(String value) {
        editor.putString(UUID, value).apply();
    }

    public static String getUUID() {
        return sp.getString(UUID, "");
    }

    public static void setPwdLength(int value) {
        editor.putInt(PWD_LENGTH, value).apply();
    }

    public static int getPwdLength() {
        return sp.getInt(PWD_LENGTH, 0);
    }


}
