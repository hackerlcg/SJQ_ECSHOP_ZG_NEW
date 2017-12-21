package com.ecjia.util.EventBus;

/**
 * Created by Administrator on 2015/2/5.
 */
public abstract class Event {

    public static final int TAG_MAIN = 1;
    public static final int TAG_BACK = 2;
    public static final int TAG_POST = 3;
    public static final int TAG_ASYNC = 4;

    protected boolean flag;
    protected int mTag;
    protected String msg;
    protected String content;

    public Event(boolean flag, int tag) {
        this.flag = flag;
        this.mTag = tag;
    }
    public Event(String msg, int tag) {
        this.msg = msg;
        this.mTag = tag;
    }

    public Event(String msg, int tag,String content) {
        this.msg = msg;
        this.mTag = tag;
        this.content=content;
    }



    public Event(boolean flag) {
        this.flag = flag;
    }
    public Event(String msg) {
        this.msg = msg;
    }
    public Event(int tag) {
        this.mTag = tag;
    }

    public boolean getFlag() {
        return flag;
    }

    public int getmTag() {
        return mTag;
    }

    public String getMsg() {
        return msg;
    }

    abstract void myEvent();

    public String getContent() {
        return content;
    }

}