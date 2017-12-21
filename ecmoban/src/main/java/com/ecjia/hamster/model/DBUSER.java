
package com.ecjia.hamster.model;

public class DBUSER
{


    private String name;//用户名
    private String pwd;//密码
    private String api;
    private int isDefault;//是否当前登录  默认0未登录，登录后当前置为1其他置0
    private int isCheck;//是否验证 默认0未验证，1验证

    public DBUSER() {
    }
    public DBUSER(String name, String pwd, String api, int isDefault, int isCheck) {
        this.name = name;
        this.pwd = pwd;
        this.api = api;
        this.isDefault = isDefault;
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
}
