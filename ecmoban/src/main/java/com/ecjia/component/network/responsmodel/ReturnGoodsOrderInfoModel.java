package com.ecjia.component.network.responsmodel;

import android.widget.Switch;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-20.
 */

public class ReturnGoodsOrderInfoModel implements Serializable {

    /*“return_status":0,//0为已申请,1为卖家收到，2为卖家寄出（分单），3为卖家寄出，4为完成，5为同意申请，6为买家寄出，9为卖家拒绝
            "refound_status":0,//0为未退款（退货，换货），1为已退款(退货，换货)
            "return_type":1,//0为仅退款,1为退款退货,2为换货
            "ff_return_status": "已申请",//退换货状态
            "ff_refound_status": "未退款",//退款状态
            "ff_return_type": "退货",//退换货类型
            "order_sn": "2017031714169928436",//订单号
            "shipping_time": "",//发货时间
            "should_return": "120.00",//退款金额
            "return_shipping_fee": "0.00",//退运费金额
            "return_sn": "2017031714233889013",//退换货流水号
            "apply_time": "2017-03-17 14:18:16",//申请时间
            "return_brief": "已付款 退款 不处理",//问题描述
            "cause_name": "质量瑕疵",//退换货原因

            "log": [//日志
    {
        "return_status": "您发起退货申请",
            "action_note": "原因：质量瑕疵 金额：120",
            "log_time": "2017-03-17 14:18:16"
    }
    ],
            "images": ["http://test.sjq.cn/data/return_images/1489702695431774850.png","http://test.sjq.cn/data/return_images/1489702695431774850.png","http://test.sjq.cn/data/return_images/1489702695431774850.png"]*/

    private String return_status;
    private String refound_status;
    private String return_type;
    private String ff_return_status;
    private String ff_refound_status;
    private String ff_return_type;
    private String order_sn;
    private String shipping_time;
    private String should_return;
    private String return_shipping_fee;
    private String return_sn;
    private String apply_time;
    private String return_brief;
    private String cause_name;

    private List<ReturnLog> log;
    private List<String> images;

    public String getAllReturn() {
        return  (Float.parseFloat(should_return)+Float.parseFloat(return_shipping_fee))+"";
    }

    public String getReturn_status() {
        return return_status;
    }

    public String getReturn_statusStr() {
        String str="";
        //0为已申请,1为卖家收到，2为卖家寄出（分单），3为卖家寄出，4未完成，5为同意申请，6为买家寄出，9为卖家拒绝
        switch (return_status){
            case "0":
                str="已申请";
                break;
            case "1":
                str="卖家收到";
                break;
            case "2":
                str="卖家寄出";
                break;
            case "3":
                str="卖家寄出";
                break;
            case "4":
                str="未完成";
                break;
            case "5":
                str="同意申请";
                break;
            case "6":
                str="买家寄出";
                break;
            case "9":
                str="卖家拒绝";
                break;
        }
        return str;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getRefound_status() {
        return refound_status;
    }

    public void setRefound_status(String refound_status) {
        this.refound_status = refound_status;
    }

    public String getReturn_type() {
        return return_type;
    }

    public String getReturn_typeStr() {
        String str="";
        //0为仅退款,1为退款退货,2为换货
        switch (return_type) {
            case "0":
                str = "仅退款";
                break;
            case "1":
                str = "退款退货";
                break;
            case "2":
                str = "换货";
                break;
        }
        return str;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public String getFf_return_status() {
        return ff_return_status;
    }

    public void setFf_return_status(String ff_return_status) {
        this.ff_return_status = ff_return_status;
    }

    public String getFf_refound_status() {
        return ff_refound_status;
    }

    public void setFf_refound_status(String ff_refound_status) {
        this.ff_refound_status = ff_refound_status;
    }

    public String getFf_return_type() {
        return ff_return_type;
    }

    public void setFf_return_type(String ff_return_type) {
        this.ff_return_type = ff_return_type;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getShipping_time() {
        return shipping_time;
    }

    public void setShipping_time(String shipping_time) {
        this.shipping_time = shipping_time;
    }

    public String getShould_return() {
        return should_return;
    }

    public void setShould_return(String should_return) {
        this.should_return = should_return;
    }

    public String getReturn_shipping_fee() {
        return return_shipping_fee;
    }

    public void setReturn_shipping_fee(String return_shipping_fee) {
        this.return_shipping_fee = return_shipping_fee;
    }

    public String getReturn_sn() {
        return return_sn;
    }

    public void setReturn_sn(String return_sn) {
        this.return_sn = return_sn;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getReturn_brief() {
        return return_brief;
    }

    public void setReturn_brief(String return_brief) {
        this.return_brief = return_brief;
    }

    public String getCause_name() {
        return cause_name;
    }

    public void setCause_name(String cause_name) {
        this.cause_name = cause_name;
    }

    public List<ReturnLog> getLog() {
        return log;
    }

    public void setLog(List<ReturnLog> log) {
        this.log = log;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public class ReturnLog implements Serializable {


        private String  return_status;
        private String  action_note;
        private String  shipping_code;
        private String  invoice_no;
        private String  log_time;

        public String getShipping_code() {
            return shipping_code;
        }

        public void setShipping_code(String shipping_code) {
            this.shipping_code = shipping_code;
        }

        public String getInvoice_no() {
            return invoice_no;
        }

        public void setInvoice_no(String invoice_no) {
            this.invoice_no = invoice_no;
        }

        public String getReturn_status() {
            return return_status;
        }

        public void setReturn_status(String return_status) {
            this.return_status = return_status;
        }

        public String getAction_note() {
            return action_note;
        }

        public void setAction_note(String action_note) {
            this.action_note = action_note;
        }

        public String getLog_time() {
            return log_time;
        }

        public void setLog_time(String log_time) {
            this.log_time = log_time;
        }
    }


}
