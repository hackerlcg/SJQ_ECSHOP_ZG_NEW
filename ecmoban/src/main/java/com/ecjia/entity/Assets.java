package com.ecjia.entity;

import com.ecjia.util.AnonymityStr;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/17 13:20.
 */

public class Assets implements Serializable {

    /**
     * ...."id":."41", //用户id
     * ...."mobile":."18256554543",//银行存的手机号码
     * ...."seller_name":张三
     * ...."bank_name":."中国银行",//银行名字
     * ...."card":."6214831203971234",//银行卡
     * ...."identity":."330682199504014301",//身份证
     * ...."verify_status":."1"//账户信息认证情况，1：审核通过 2：审核未通过
     * ...."seller_money": "654.00", //资产 可提现+提现中
     * ...."withdrawing_money": "580.00", //提现中金额
     * ...."can_withdraw_money": "74.00" //可提现金额
     */

    @SerializedName("id")
    private String id;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("seller_name")
    private String sellerName;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("card")
    private String card;
    @SerializedName("identity")
    private String identity;
    @SerializedName("verify_status")
    private String verifyStatus;
    @SerializedName("seller_money")
    private float sellerMoney;
    @SerializedName("withdrawing_money")
    private float withdrawingMoney;
    @SerializedName("can_withdraw_money")
    private float canWithdrawMoney;
    private String sms;

    @SerializedName("code")
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public float getSellerMoney() {
        return sellerMoney;
    }

    public void setSellerMoney(float sellerMoney) {
        this.sellerMoney = sellerMoney;
    }

    public float getWithdrawingMoney() {
        return withdrawingMoney;
    }

    public void setWithdrawingMoney(float withdrawingMoney) {
        this.withdrawingMoney = withdrawingMoney;
    }

    public float getCanWithdrawMoney() {
        return canWithdrawMoney;
    }

    public void setCanWithdrawMoney(float canWithdrawMoney) {
        this.canWithdrawMoney = canWithdrawMoney;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String showBindTypeStr() {
        if (verifyStatus == null)
            verifyStatus = "0";
        String str = "";
        switch (verifyStatus) {
            case "-1":
                str = "未绑定";
                break;
            case "0":
                str = "审核中";
                break;
            case "1":
                str = "审核通过";
                break;
            case "2":
                str = "审核未通过";
                break;
        }
        return str;
    }

    public String showCardRight() {
        StringBuffer sb = new StringBuffer();
        if (card != null) {
            sb.append("尾号")
                    .append(card.substring(card.length() - 4, card.length()))
                    .append("储蓄卡");
        } else {
            sb.append("没有银行卡信息");
        }
        return sb.toString();
    }

    public String showCanWithdrawMoney(){
        StringBuffer sb =new StringBuffer();
        if(canWithdrawMoney > 0){
            sb.append("可用提现金额")
            .append(canWithdrawMoney)
            .append("元");
        }else{
            sb.append("没有可用提现金额");
        }
        return sb.toString();
    }

    public String showName4An(){
        return AnonymityStr.onAnonymityName(sellerName);
    }

    public String showCard4An(){
        return AnonymityStr.onAnonymityCard(card);
    }

    public String showID4An(){
        return AnonymityStr.onAnonymityID(identity);
    }

    public String showPhone4An(){
        return AnonymityStr.onAnonymityPhone(mobile);
    }

    @Override
    public String toString() {
        return "Assets{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", card='" + card + '\'' +
                ", identity='" + identity + '\'' +
                ", verifyStatus='" + verifyStatus + '\'' +
                ", sellerMoney=" + sellerMoney +
                ", withdrawingMoney=" + withdrawingMoney +
                ", canWithdrawMoney=" + canWithdrawMoney +
                ", sms='" + sms + '\'' +
                '}';
    }
}
