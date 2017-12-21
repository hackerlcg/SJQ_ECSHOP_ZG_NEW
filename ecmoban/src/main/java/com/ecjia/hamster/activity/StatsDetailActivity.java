package com.ecjia.hamster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.StatsModel;
import com.ecjia.hamster.model.GROUP;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATS;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.TimeUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatsDetailActivity extends BaseActivity implements HttpResponse {
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    //顶部数据
    private TextView tv_type_text;
    private TextView tv_value1, tv_value2, tv_value3, tv_label1, tv_label2, tv_label3;//三个值
    private LinearLayout item_three;
    //四个table
    private RelativeLayout rl1, rl2, rl3, rl4;
    private String total_sales, today_max, average_sales, payed_order, wait_ship_order, shiped_order, total_visitors, visit_times;
    private int selected_color1, selected_color2, selected_color3, tabselected_color, unselected_color;
    private String[] selectedday, today, week, Month, nintydays;
    private int TYPE = 1;
    private StatsModel statsModel;
    private LineChart mChart;
    private LinearLayout back;
    private Intent intent;
    private String selected="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_stats_detail);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session = new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        initView();
    }

    private void initView() {
        tv_type_text = (TextView) findViewById(R.id.tv_type_text);
        today = TimeUtil.getDateBeforeToday(0, 0);//今天
        week = TimeUtil.getWeekdays();//本周
        Month = TimeUtil.getDateBeforeToday(-30, -1);//前30天
        nintydays = TimeUtil.getDateBeforeToday(-90, -1);//前90天
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
        TYPE=intent.getIntExtra("type",1);
        if(TYPE==1){
            tv_type_text.setText(res.getString(R.string.stats_sales));
        }else if(TYPE==2){
            tv_type_text.setText(res.getString(R.string.stats_orders));
        }else if(TYPE==3){
            tv_type_text.setText(res.getString(R.string.stats_visitor));
        }else{

        }

        back = (LinearLayout) findViewById(R.id.ll_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //初始化字符串
        total_sales = res.getString(R.string.total_sales);
        today_max = res.getString(R.string.today_max);
        average_sales = res.getString(R.string.average_sales);
        payed_order = res.getString(R.string.payed_order);
        wait_ship_order = res.getString(R.string.wait_ship_order);
        shiped_order = res.getString(R.string.shiped_order);
        total_visitors = res.getString(R.string.total_visitors);
        visit_times = res.getString(R.string.visit_times);


        selected_color1 = getResources().getColor(R.color.selected_color1);
        selected_color2 = getResources().getColor(R.color.selected_color2);
        selected_color3 = getResources().getColor(R.color.selected_color3);
        tabselected_color = res.getColor(R.color.selected_chart_color);
        unselected_color = res.getColor(R.color.unselected_chart_color);

        mChart = (LineChart) findViewById(R.id.chart_data);
        initChart();

        if (statsModel == null) {
            statsModel = new StatsModel(this);
            statsModel.addResponseListener(this);
        }
        //-----------------------顶部三组数据---------------------------
        tv_value1 = (TextView) findViewById(R.id.tv_value11);
        tv_value2 = (TextView) findViewById(R.id.tv_value22);
        tv_value3 = (TextView) findViewById(R.id.tv_value33);
        tv_label1 = (TextView) findViewById(R.id.tv_label11);
        tv_label2 = (TextView) findViewById(R.id.tv_label22);
        tv_label3 = (TextView) findViewById(R.id.tv_label33);
        item_three = (LinearLayout) findViewById(R.id.item_threes);
        //----------------四个table-------------------------
        rl1 = (RelativeLayout) findViewById(R.id.rl_11);
        rl2 = (RelativeLayout) findViewById(R.id.rl_22);
        rl3 = (RelativeLayout) findViewById(R.id.rl_33);
        rl4 = (RelativeLayout) findViewById(R.id.rl_44);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("one");
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("two");
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("three");
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab("four");
            }
        });

        initChart();
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
            rl1.setBackgroundColor(tabselected_color);
            rl2.setBackgroundColor(unselected_color);
            rl3.setBackgroundColor(unselected_color);
            rl4.setBackgroundColor(unselected_color);

            selectedday = today;
        } else if ("two".equals(selected)) {
            rl1.setBackgroundColor(unselected_color);
            rl2.setBackgroundColor(tabselected_color);
            rl3.setBackgroundColor(unselected_color);
            rl4.setBackgroundColor(unselected_color);
            selectedday = week;
        } else if ("three".equals(selected)) {
            rl1.setBackgroundColor(unselected_color);
            rl2.setBackgroundColor(unselected_color);
            rl3.setBackgroundColor(tabselected_color);
            rl4.setBackgroundColor(unselected_color);
            selectedday = Month;
        } else if ("four".equals(selected)) {
            rl1.setBackgroundColor(unselected_color);
            rl2.setBackgroundColor(unselected_color);
            rl3.setBackgroundColor(unselected_color);
            rl4.setBackgroundColor(tabselected_color);
            selectedday = nintydays;
        } else {

        }
        if (TYPE == 1) {
            statsModel.fetchSales(session, selectedday[0], selectedday[1], api);
        } else if (TYPE == 2) {
            statsModel.fetchOrders(session, selectedday[0], selectedday[1], api);
        } else if (TYPE == 3) {
            statsModel.fetchVisitor(session, selectedday[0], selectedday[1], api);
        } else {

        }
    }

    private void initChart() {


        // no description text
        mChart.setNoDataText("");
        mChart.setDescription("");
        mChart.setNoDataTextDescription("暂无数据");
        // enable touch gestures
        mChart.setTouchEnabled(false);
        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);//内外背景一直
        mChart.setPinchZoom(true);
        mChart.setViewPortOffsets(0, 20, 0, 60);//设置屏幕距离
//        mChart.setExtraOffsets(0, 0, 0, 0);//设置内部距离
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的显示位置
        xAxis.setDrawGridLines(true);//水平网格线条显示
//        xAxis.setLabelRotationAngle(20);//斜体字
        xAxis.enableGridDashedLine(10f, 10f, 0f);//虚线网格
        xAxis.setGridColor(Color.parseColor("#f0f0f0"));//网格线颜色
        xAxis.setGridLineWidth(0.8f);
        xAxis.setAvoidFirstLastClipping(true);//设置x轴起点和终点label不超出屏幕
        xAxis.setSpaceBetweenLabels(0);//X轴显示间隔
        xAxis.setLabelsToSkip(4);//网格间距
        xAxis.setDrawAxisLine(true);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        leftAxis.setYOffset(0f);
        leftAxis.setDrawGridLines(true);//垂直网格线条显示
        leftAxis.enableGridDashedLine(10f, 10f, 0f);//虚线网格
        leftAxis.setGridColor(Color.parseColor("#f0f0f0"));//网格线颜色
        leftAxis.setGridLineWidth(0.8f);
//        leftAxis.setAxisLineColor(Color.parseColor("#000000"));
        leftAxis.setDrawAxisLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);//是否显示右侧Y轴
//        mChart.getAxisRight().setDrawLabels(false);//不显示Y轴的值
//        mChart.getAxisRight().setDrawGridLines(false);

        // add data

//        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();//底部描述

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);//底部文字描述样式
        l.setXEntrySpace(20);
        l.setTextColor(Color.GREEN);
        l.setFormToTextSpace(20);//文字描述和格子的距离
        l.setFormSize(5);//格子大小
        l.setYEntrySpace(20);
        l.setXEntrySpace(20);
        l.setEnabled(false);//是否显示描述


    }

    private void setData(int type) {
        ArrayList<GROUP> groups = null;
        ArrayList<STATS> statsdata = null;
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        STATS stats = new STATS();
        stats.setValue("0");
        if (type == 1) {
            groups = statsModel.sales.getGrouplist();
            statsdata = statsModel.sales.getList();
            statsdata.add(0, stats);
            for (int i = 0; i < statsdata.size(); i++) {
                yVals.add(new Entry(Float.valueOf(statsdata.get(i).getValue()), i));
            }
        } else if (type == 2) {
            groups = statsModel.sorders.getGrouplist();
            statsdata = statsModel.sorders.getList();
            statsdata.add(0, stats);
            for (int i = 0; i < statsdata.size(); i++) {
                yVals.add(new Entry(Float.valueOf(statsdata.get(i).getValue()), i));
            }
        } else if (type == 3) {
            groups = statsModel.visitor.getGrouplist();
            statsdata = statsModel.visitor.getList();
            statsdata.add(0, stats);
            for (int i = 0; i < statsdata.size(); i++) {
                yVals.add(new Entry(Float.valueOf(statsdata.get(i).getValue()), i));
            }
        } else {

        }
        mChart.getAxisLeft().resetAxisMaxValue();
        for(int i=0;i<yVals.size();i++){
            if(yVals.get(i).getVal()>=5){
                break;
            }
            if(i==yVals.size()-1){
                mChart.getAxisLeft().setAxisMaxValue(10);
            }
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < 31; i++) {
            if (i % 5 == 0) {
                if (selectedday == today) {
                    xVals.add(i, groups.get(i / 5).getHour_formatted_time());
                } else {
                    xVals.add(i, groups.get(i / 5).getDay_formatted_time());
                }
            } else {
                xVals.add(i, "");
            }
        }


        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "本月收入");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);//虚线设置
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        if (type == 1) {
            set1.setColor(selected_color1);
            set1.setFillColor(selected_color1);
        } else if (type == 2) {
            set1.setColor(selected_color2);
            set1.setFillColor(selected_color2);
        } else if (type == 3) {
            set1.setColor(selected_color3);
            set1.setFillColor(selected_color3);
        } else {

        }
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(0.9f);//线条宽度
        set1.setCircleSize(3f);//圆点半径
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawCircles(false);//显示圆点
        set1.setDrawCubic(true);//曲线，false为折线
        set1.setFillAlpha(20);//填充色透明度
        set1.setDrawFilled(true);//填充色
        set1.setDrawValues(false);//显示value
        set1.setCubicIntensity(0.15f);
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getXIndex() % 5 == 0) {
                    return v + "";
                } else {
                    return "";
                }
            }
        });

        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.STATS_SALES)) {
            if (status.getSucceed() == 1) {
                setData(1);//绘制统计图
                setitemdata(1);//中部布局数据设置
            }
        } else if (url.equals(ProtocolConst.STATS_ORDERS)) {
            if (status.getSucceed() == 1) {
                setData(2);
                setitemdata(2);
            }
        } else if (url.equals(ProtocolConst.STATS_VISITOR)) {
            if (status.getSucceed() == 1) {
                setData(3);
                setitemdata(3);
            }
        }
    }

    //设置中部布局数据
    void setitemdata(int type) {
        if (type == 1) {
            item_three.setVisibility(View.VISIBLE);
            tv_value3.setVisibility(View.VISIBLE);
            tv_value1.setText(statsModel.sales.getTotal_sales_volume());
            tv_value2.setText(statsModel.sales.getMaximum_sales_volume());
            tv_value3.setText(statsModel.sales.getAverage_sales_volume());
            tv_label1.setText(total_sales);
            tv_label2.setText(today_max);
            tv_label3.setText(average_sales);
        } else if (type == 2) {
            item_three.setVisibility(View.VISIBLE);
            tv_value3.setVisibility(View.VISIBLE);
            tv_value1.setText(statsModel.sorders.getPayed_orders());
            tv_value2.setText(statsModel.sorders.getWait_ship_orders());
            tv_value3.setText(statsModel.sorders.getShipped_orders());
            tv_label1.setText(payed_order);
            tv_label2.setText(wait_ship_order);
            tv_label3.setText(shiped_order);
        } else if (type == 3) {
            item_three.setVisibility(View.GONE);
            tv_value3.setVisibility(View.GONE);
            tv_value1.setText(statsModel.visitor.getTotal_visitors());
            tv_value2.setText(statsModel.visitor.getVisit_times());
            tv_label1.setText(total_visitors);
            tv_label2.setText(visit_times);
            tv_label3.setText("");
        } else {

        }
    }
}
