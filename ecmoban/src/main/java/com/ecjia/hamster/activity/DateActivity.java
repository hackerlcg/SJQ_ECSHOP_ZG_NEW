package com.ecjia.hamster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.ecjia.component.wheel.JudgeDate;
import com.ecjia.component.wheel.ScreenInfo;
import com.ecjia.component.wheel.WheelMain;
import com.ecjia.consts.AppConst;
import com.ecjia.util.EventBus.MyEvent;

import de.greenrobot.event.EventBus;


public class DateActivity extends BaseActivity {
	
    private Calendar calendar;
    private WheelMain wheelMain;
    private int code;
    private boolean haveSelectTime;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(DateActivity.this);
        final View timepickerview=inflater.inflate(R.layout.time_scroll, null);
        setContentView(timepickerview);
        Window lp = getWindow();
        lp.setGravity(Gravity.BOTTOM);
        lp.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        EventBus.getDefault().register(this);

        Intent intent=getIntent();
        String date = intent.getStringExtra("date");
        code=intent.getIntExtra("code", AppConst.PROMOTES);

        if(code==AppConst.PROMOTES||code==AppConst.PROMOTEE){
            haveSelectTime=false;
        }else if(code==AppConst.DISCOUNTS||code==AppConst.DISCOUNTE){
            haveSelectTime=true;
        }

        ScreenInfo screenInfo = new ScreenInfo(DateActivity.this);
        wheelMain = new WheelMain(timepickerview,haveSelectTime);
        wheelMain.screenheight = screenInfo.getHeight();

        calendar = Calendar.getInstance(Locale.CHINA);

        if(TextUtils.isEmpty(date)){
            Date mydate = new Date(); // 获取当前日期Date对象
            calendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
        }else{

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            if(JudgeDate.isDate(date, "yyyy-MM-dd HH:mm")){
            try {
                calendar.setTime(dateFormat.parse(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                Date mydate = new Date();
                calendar.setTime(mydate);
                e.printStackTrace();
            }
        }
        }


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        wheelMain.initDateTimePicker(year, month, day,h,m);

	}

    @Override
    public void finish() {
        super.finish();

        EventBus.getDefault().post(new MyEvent(wheelMain.getTime(),code));

        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

    }

}
