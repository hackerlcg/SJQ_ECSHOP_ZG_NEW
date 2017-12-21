package com.ecjia.component.network.requestmodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-20.
 */

public class ReturnStatus extends BaseRequest implements Serializable {

    private String ret_id;
    private String return_status;

    public  ReturnStatus(){
        super.setInfo();
    }

    public String getRet_id() {
        return ret_id;
    }

    public void setRet_id(String ret_id) {
        this.ret_id = ret_id;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }
}
