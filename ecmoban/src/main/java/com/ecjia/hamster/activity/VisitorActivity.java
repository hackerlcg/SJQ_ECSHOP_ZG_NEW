package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.StatsModel;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.TimeUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VisitorActivity extends BaseActivity implements HttpResponse {

    private int selected_color1, selected_color2, selected_color3, unselected_color;
    private String[] selectedday, today, week, Month, nintydays;
    //四个table
    private RelativeLayout rl1, rl2, rl3, rl4;
    private TextView rl1_tv, rl2_tv, rl3_tv, rl4_tv;
    private View line1, line2, line3, line4;
    private TextView title;
    private ImageView back;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private PieChart mChart;
    private StatsModel statsModel;
    private int distance;
    private int TYPE = 3;
    private Intent intent;
    private String selected="";
    private LinearLayout tv_no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_visitor);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session = new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        title = (TextView) findViewById(R.id.top_view_text);
        String det = res.getString(R.string.visitor_analysis);
        title.setText(det);
        tv_no_data= (LinearLayout) findViewById(R.id.no_data);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        statsModel = new StatsModel(this);
        statsModel.addResponseListener(this);
        initView();


    }

    private void initView() {
        today= TimeUtil.getDateBeforeToday(0, 0);//今天
        week=TimeUtil.getWeekdays();//本周
        Month=TimeUtil.getDateBeforeToday(-30,-1);//前30天
        nintydays=TimeUtil.getDateBeforeToday(-90,-1);//前90天
        intent=getIntent();
        selected=intent.getStringExtra("selectedday");
        if("today".equals(selected)){
            selectedday=today;
        }else if("week".equals(selected)){
            selectedday=week;
        }else if("month".equals(selected)){
            selectedday=Month;
        }else if("nintydays".equals(selected)){
            selectedday=nintydays;
        }else{
            selectedday=today;
        }


        selected_color1=getResources().getColor(R.color.selected_color1);
        selected_color2=getResources().getColor(R.color.selected_color2);
        selected_color3=getResources().getColor(R.color.selected_color3);
        unselected_color=getResources().getColor(R.color.text_login_color);
        //----------------四个table-------------------------
        rl1 = (RelativeLayout) findViewById(R.id.rl_1p);
        rl2 = (RelativeLayout) findViewById(R.id.rl_2p);
        rl3 = (RelativeLayout) findViewById(R.id.rl_3p);
        rl4 = (RelativeLayout) findViewById(R.id.rl_4p);

        rl1_tv = (TextView) findViewById(R.id.tv_1p);
        rl2_tv = (TextView) findViewById(R.id.tv_2p);
        rl3_tv = (TextView) findViewById(R.id.tv_3p);
        rl4_tv = (TextView) findViewById(R.id.tv_4p);

        line1 = findViewById(R.id.line_1p);
        line2 = findViewById(R.id.line_2p);
        line3 = findViewById(R.id.line_3p);
        line4 = findViewById(R.id.line_4p);
        rl1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("one");
            }
        });
        rl2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("two");
            }
        });
        rl3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("three");
            }
        });
        rl4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("four");
            }
        });

        initPieChart();
        if(selectedday==today){
            selectedTab("one");
        }else if(selectedday==week){
            selectedTab("two");
        }else if(selectedday==Month){
            selectedTab("three");
        }else if(selectedday==nintydays){
            selectedTab("four");
        }else{
            selectedTab("one");
        }

    }

    //table选择
    private void selectedTab(String selected) {
        if ("one".equals(selected)) {
//            rl1_tv.setTextColor(selected_color);
            rl2_tv.setTextColor(unselected_color);
            rl3_tv.setTextColor(unselected_color);
            rl4_tv.setTextColor(unselected_color);

            rl1_tv.setTextColor(selected_color3);
            line1.setBackgroundColor(selected_color3);


            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
            line4.setVisibility(View.INVISIBLE);
            selectedday = today;
        } else if ("two".equals(selected)) {
            rl1_tv.setTextColor(unselected_color);
//            rl2_tv.setTextColor(selected_color);
            rl3_tv.setTextColor(unselected_color);
            rl4_tv.setTextColor(unselected_color);

            rl2_tv.setTextColor(selected_color3);
            line2.setBackgroundColor(selected_color3);


            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.INVISIBLE);
            line4.setVisibility(View.INVISIBLE);
            selectedday = week;
        } else if ("three".equals(selected)) {
            rl1_tv.setTextColor(unselected_color);
            rl2_tv.setTextColor(unselected_color);
//            rl3_tv.setTextColor(selected_color);
            rl4_tv.setTextColor(unselected_color);

            rl3_tv.setTextColor(selected_color3);
            line3.setBackgroundColor(selected_color3);


            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.INVISIBLE);
            selectedday = Month;
        } else if ("four".equals(selected)) {
            rl1_tv.setTextColor(unselected_color);
            rl2_tv.setTextColor(unselected_color);
            rl3_tv.setTextColor(unselected_color);
//            rl4_tv.setTextColor(selected_color);

            rl4_tv.setTextColor(selected_color3);
            line4.setBackgroundColor(selected_color3);


            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
            line4.setVisibility(View.VISIBLE);
            selectedday = nintydays;
        } else {

        }
        statsModel.fetchVisitor(session, selectedday[0], selectedday[1], api);

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.STATS_VISITOR)) {
            if (status.getSucceed() == 1) {
                if(Integer.valueOf(statsModel.visitor.getTotal_visitors())>0) {
                    mChart.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                    setDataPieChart();
                }else{
                    mChart.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }
            } else {

            }
        }
    }

    void initPieChart() {
        distance = (int) getResources().getDimension(R.dimen.txt60);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                getDisplayMetricsWidth() * 1 / 2,
                (getDisplayMetricsWidth() * 1 / 2));
        lp.setMargins(0, distance, 0, distance);
        mChart = (PieChart) findViewById(R.id.piechart);
        tv_no_data.setLayoutParams(lp);
        mChart.setLayoutParams(lp);
        mChart.setNoDataText("暂无数据");
        mChart.setDrawMarkerViews(true);
        mChart.setDescription("");

//        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDrawHoleEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);


        mChart.setDrawHoleEnabled(true);// 设置是否显示饼图中心的空白区 默认显示
        mChart.setHoleColorTransparent(true);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(0);

        mChart.setHoleRadius(60f);// 设置圆盘中间区域大小
        mChart.setTransparentCircleRadius(85f);//设置中间透明圈的大小
        mChart.setUsePercentValues(true);  //显示成百分比
        mChart.setDrawCenterText(true);// 是否显示圆盘中间文字 默认显示
//        mChart.setCenterText("共400人");

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);// 可以手动旋转
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDrawSliceText(false);// 显示X轴的值
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        // mChart.setOnChartValueSelectedListener(this);
    }


    private void setDataPieChart() {


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        yVals1.add(new Entry(Float.valueOf(statsModel.visitor.getMobile_visitors()), 0));
        yVals1.add(new Entry(Float.valueOf(statsModel.visitor.getWeb_visitors()), 1));
//        yVals1.add(new Entry(300, 0));
//        yVals1.add(new Entry(100, 1));


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("手机:"+statsModel.visitor.getMobile_visitors());
        xVals.add("网页:"+statsModel.visitor.getWeb_visitors());
//        xVals.add("手机:"+300);
//        xVals.add("网页:"+100);

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(0f);// 设置饼状图之间的间距
        dataSet.setSelectionShift(0f);//点击效果

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(Color.parseColor("#36a4ff"));
        colors.add(Color.parseColor("#cde8ff"));
//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        // dataSet.setSelectionShift(0f);


        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(5f);
        l.setYEntrySpace(5f);
//        l.setYOffset(1f);
//        l.setTextSize(13f);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = getWindowManager().getDefaultDisplay().getWidth();
        int j = getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }
}
