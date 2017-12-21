package com.ecjia.util.EventBus;

import com.ecjia.hamster.model.MYMESSAGE;

/**
 * Created by Administrator on 2015/2/5.
 */
public class MyEvent extends Event {


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MYMESSAGE getMyMessage() {
        return myMessage;
    }

    public void setMyMessage(MYMESSAGE myMessage) {
        this.myMessage = myMessage;
    }

    private MYMESSAGE myMessage;

    private float price ;

    private int action;

    private String id;

    public MyEvent(boolean flag, int tag) {
        super(flag,tag);
        this.flag = flag;
        this.mTag = tag;
    }
    public MyEvent(String msg, int tag) {
        super(msg,tag);
        this.msg = msg;
        this.mTag = tag;
    }
    public MyEvent(String msg) {
        super(msg);
        this.msg = msg;
    }
    public MyEvent(int tag) {
        super(tag);
        this.mTag = tag;
    }
    public MyEvent(boolean flag) {
        super(flag);
        this.flag = flag;
    }

    public MyEvent(String msg, int tag,String content) {
        super(msg,tag,content);
        this.msg = msg;
        this.mTag = tag;
        this.content=content;
    }

    @Override
    void myEvent() {

    }
}
