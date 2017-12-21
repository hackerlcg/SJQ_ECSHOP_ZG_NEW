package com.ecjia.util;

import android.content.res.Resources;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.google.gson.annotations.Expose;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Math;

/**
 * User: howie
 * Date: 13-5-11
 * Time: 下午4:09
 */
public class TimeUtil {

    public static String timeAgo(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - timeStamp) / 1000;

        long minutes = Math.abs(seconds / 60);
        long hours = Math.abs(minutes / 60);
        long days = Math.abs(hours / 24);


        if (seconds <= 15) {
            return "刚刚";
        } else if (seconds < 60) {
            return seconds + "秒前";
        } else if (seconds < 120) {
            return "1分钟前";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (minutes < 120) {
            return "一小时前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (hours < 24 * 2) {
            return "一天前";
        } else if (days < 30) {
            return days + "天前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(date);
            return dateString;
        }

    }

    public static String timeLeft(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;
    }


    /**
     * 比较两个日期
     * 1：d1在d2之后
     * -1：d1在d2之前
     * 0：同一天
     */
    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断促销
     * 1：未开始促销
     * 2：进行中促销
     * 3：已结束促销
     * 0：不是促销
     */
    public static int compare_promotion(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);

            Date currentTime = new Date();
            long currentTimeStamp = currentTime.getTime();

            if(currentTimeStamp<dt1.getTime()){
               return 1;
            }else if(dt1.getTime()<=currentTimeStamp&&currentTimeStamp<=dt2.getTime()){
                return 2;
            }else if(currentTimeStamp>dt2.getTime()){
                return 3;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String fomartPromoteDate(String time) {
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
        return format3.format(date);
    }

    //判断是否本周
    public static boolean getFomartWeek(Calendar gettime) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.WEEK_OF_YEAR) == gettime.get(Calendar.WEEK_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }

    //判断今天还是昨天
    public static boolean getFomartDay(Calendar gettime) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_YEAR) == (gettime.get(Calendar.DAY_OF_YEAR))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getFomartYesterday(Calendar gettime) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_YEAR) == (gettime.get(Calendar.DAY_OF_YEAR) + 1)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 日期统一格式
     */
    public final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final static SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat format4 = new SimpleDateFormat("MM-dd HH:mm");


    /**
     * User: leming
     * Date: 2015-09-18
     * 今日热点时间转化
     */
    public static String getFomartNewsTopTime(String time) {
        Date date = new Date();
        try {
            date = format2.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, 0);
        date = cal.getTime();

        SimpleDateFormat formatNowDay = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat formatNowTime = new SimpleDateFormat("HH:mm");

        if (getFomartWeek(cal)) {
            if (getFomartDay(cal)) {
                return formatNowTime.format(date);
            } else if (getFomartYesterday(cal)) {
                return "昨天 " + formatNowTime.format(date);
            } else {
                return getWeekDay(cal) + " " + formatNowTime.format(date);
            }

        } else {
            return formatNowDay.format(date) + " " + getAMPM(cal) + formatNowTime.format(date);
        }

    }

    public static String getFormatNowDay(String time) {
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatNowDay = new SimpleDateFormat("MM月dd日");
        return formatNowDay.format(date);
    }

    public static String getFormatDay(String time) {
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatNowDay = new SimpleDateFormat("MM-dd");
        return formatNowDay.format(date);
    }

    public static String getAMPM(Calendar gettime) {
        int ampm = gettime.get(Calendar.AM_PM);
        int hour = gettime.get(Calendar.HOUR_OF_DAY);
        if (ampm == Calendar.AM) {
            return "早上";
        } else {
            if (hour >= 18) {
                return "晚上";
            } else {
                return "下午";
            }
        }
    }

    public static String getWeekDay(Calendar gettime) {
        int dayOfWeek = gettime.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }

    public static String getDayFormatDay(String time) {
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatNowDay = new SimpleDateFormat("HH:mm");
        return formatNowDay.format(date);
    }

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    //获取当前月的第一天
    public static String[] getFirstDayandLastDay() {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = formatter.format(c.getTime());

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = formatter.format(ca.getTime());
        return new String[]{first, last};
    }

    //获取某个月的信息
    public static String[] geteveryFirstDayandLastDay(String date) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = formatter.format(c.getTime());

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        try {
            ca.setTime(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = formatter.format(ca.getTime());
        return new String[]{first, last};
    }

    //获取本日之前的几天
    public static String[] getDateBeforeToday(int startday, int endday) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, startday);//正数表示向后几天负数变色往前推几天
        String start = formatter.format(c.getTime());
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, endday);
        String end = formatter.format(ca.getTime());
        return new String[]{start, end};
    }

    //获取本日之前的七天
    public static String[] getLastWeekDays() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        String startday = formatter.format(c.getTime());
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -1);
        String endday = formatter.format(ca.getTime());
        return new String[]{startday, endday};
    }

    public static String[] getWeekdays() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar1.add(Calendar.WEEK_OF_YEAR, 1);
        return new String[]{formatter.format(calendar.getTime()), formatter.format(calendar1.getTime())};
    }

    public static String[] getFomartTwoTime(String time) {
        String date1, date2;
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, 0);
        date = cal.getTime();

        SimpleDateFormat formatNowDay = new SimpleDateFormat("MM-dd");
        SimpleDateFormat formatNowTime = new SimpleDateFormat("HH:mm");

        if (getFomartDay(cal)) {//是否是今天
            date1 = "今天";
            date2 =formatNowTime.format(cal.getTime());
        } else if (getFomartYesterday(cal)) {//是否是昨天
            date1 = "昨天";
            date2 =formatNowTime.format(cal.getTime());
        } else {
            date1 = getWeekDay(cal);
            date2 = formatNowDay.format(cal.getTime());
        }

        return new String[]{date1,date2};
    }

    public static String getFomartMonth(String time){
        Calendar gettime=Calendar.getInstance();
        try {
            gettime.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar now=Calendar.getInstance();
        if(now.get(Calendar.YEAR)==now.get(Calendar.YEAR)&&now.get(Calendar.MONTH)==gettime.get(Calendar.MONTH)){
            return "本月";
        }else{
            return (gettime.get(Calendar.MONTH)+1)+"月";
        }
    }


    //获取格式化的显示时间
    public static String getFomartDate(String time,Resources res) {
        Calendar gettime=Calendar.getInstance();
        try {
            gettime.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (gettime.get(Calendar.MONTH) + 1 < 10) {

        }
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) != gettime.get(Calendar.YEAR)) {
            return gettime.get(Calendar.YEAR) + "-" + fomarShowTime(gettime.get(Calendar.MONTH) + 1) + "-" + fomarShowTime(gettime.get(Calendar.DATE));
        } else {
            if (now.get(Calendar.DAY_OF_YEAR) == gettime.get(Calendar.DAY_OF_YEAR)) {
                return res.getString(R.string.today) + fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE));
            } else if (now.get(Calendar.DAY_OF_YEAR) - gettime.get(Calendar.DAY_OF_YEAR) == 1) {
                return res.getString(R.string.yesterday) + fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE));
            } else {
                return fomarShowTime(gettime.get(Calendar.MONTH) + 1) + "-" + fomarShowTime(gettime.get(Calendar.DATE));
            }
        }
    }

    //获取咨询格式化的显示时间
    public static String getFeedBackFormattedDate(String time,Resources res) {
        Calendar gettime=Calendar.getInstance();
        try {
            gettime.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (gettime.get(Calendar.MONTH) + 1 < 10) {

        }
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) != gettime.get(Calendar.YEAR)) {
            return gettime.get(Calendar.YEAR) + "-" + fomarShowTime(gettime.get(Calendar.MONTH) + 1) + "-" + fomarShowTime(gettime.get(Calendar.DATE))+" "+ fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE))+":"+fomarShowTime(gettime.get(Calendar.SECOND));
        } else {
            if (now.get(Calendar.DAY_OF_YEAR) == gettime.get(Calendar.DAY_OF_YEAR)) {
                return res.getString(R.string.today) +" "+ fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE))+":"+fomarShowTime(gettime.get(Calendar.SECOND));
            } else if (now.get(Calendar.DAY_OF_YEAR) - gettime.get(Calendar.DAY_OF_YEAR) == 1) {
                return res.getString(R.string.yesterday) +" "+ fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE))+":"+fomarShowTime(gettime.get(Calendar.SECOND));
            } else {
                return fomarShowTime(gettime.get(Calendar.MONTH) + 1) + "-" + fomarShowTime(gettime.get(Calendar.DATE))+" "+ fomarShowTime(gettime.get(Calendar.HOUR_OF_DAY)) + ":" + fomarShowTime(gettime.get(Calendar.MINUTE))+":"+fomarShowTime(gettime.get(Calendar.SECOND));
            }
        }
    }


    private static String fomarShowTime(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return "" + time;
    }

    public static String getKeepingTime(String time){
        Date date=null;
        Date nowDate=null;
        String keepingTime="";
        try {
            date=format.parse(time);
            nowDate=new Date();
            long ss=(nowDate.getTime()-date.getTime())/1000;
            String MM=(int)ss/60>=10?(int)ss/60+"":"0"+(int)ss/60;
            int hh=(int)ss/3600;
            String s;
            if(ss%60>=10){
                s=ss%60+"";
            }else{
                s="0"+ss%60;
            }
            keepingTime=hh+":"+MM+":"+s;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return keepingTime;
    }

    public static String getBusinessTime(String time){
        try {
            long lt = Long.valueOf(time)*1000L;
            Date date = new Date(lt);
            return format4.format(date);
        } catch (Exception e) {
            return time;
        }
    }

}
