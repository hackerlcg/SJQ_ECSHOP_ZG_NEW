package com.ecjia.consts;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-15.
 */

public class StatusConst {

    /* 订单状态 *//*
    define('OS_UNCONFIRMED',            0); // 未确认
    define('OS_CONFIRMED',              1); // 已确认
    define('OS_CANCELED',               2); // 已取消
    define('OS_INVALID',                3); // 无效
    define('OS_RETURNED',               4); // 退货
    define('OS_SPLITED',                5); // 已分单
    define('OS_SPLITING_PART',          6); // 部分分单

    *//* 支付类型 *//*
    define('PAY_ORDER',                 0); // 订单支付
    define('PAY_SURPLUS',               1); // 会员预付款
    define('PAY_APPLYGRADE',            2); // 商家升级付款  by kong grade
    define('PAY_TOPUP',                 3); // 商家账户充值

    *//* 配送状态 *//*
    define('SS_UNSHIPPED',              0); // 未发货
    define('SS_SHIPPED',                1); // 已发货
    define('SS_RECEIVED',               2); // 已收货
    define('SS_PREPARING',              3); // 备货中
    define('SS_SHIPPED_PART',           4); // 已发货(部分商品)
    define('SS_SHIPPED_ING',            5); // 发货中(处理分单)
    define('OS_SHIPPED_PART',           6); // 已发货(部分商品)

    *//* 支付状态 *//*
    define('PS_UNPAYED',                0); // 未付款
    define('PS_PAYING',                 1); // 付款中
    define('PS_PAYED',                  2); // 已付款
    define('PS_PAYED_PART',             3); // 部分付款—预售定金*/

//    public final static int UnexistInformation = 13;//不存在的信息


}
