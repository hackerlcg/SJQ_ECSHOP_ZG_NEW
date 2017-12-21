package com.ecjia.component.wheel;

import java.util.Arrays;
import java.util.List;


import android.view.View;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.LG;


public class WheelMain {

	private View view;
	private WheelTimeView wv_year;
	private WheelTimeView wv_month;
	private WheelTimeView wv_day;
	private WheelTimeView wv_hours;
	private WheelTimeView wv_mins;
	private boolean hasSelectTime;
    public int screenheight;
    private static int START_YEAR = 1990, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		setView(view);
	}
	public WheelMain(View view,boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}
	public void initDateTimePicker(int year ,int month,int day){
		this.initDateTimePicker(year, month, day, 0, 0);
	}
	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year ,int month ,int day,int h,int m) {
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = calendar.get(Calendar.DATE);
        setSTART_YEAR(year-10);
        setEND_YEAR(year+10);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelTimeView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelTimeView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelTimeView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
        wv_day.setLabel("日");
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
        wv_day.setCurrentItem(day - 1);

		wv_hours = (WheelTimeView)view.findViewById(R.id.hour);
		wv_mins = (WheelTimeView)view.findViewById(R.id.min);
		if(hasSelectTime){
			wv_hours.setVisibility(View.VISIBLE);
			wv_mins.setVisibility(View.VISIBLE);

			wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
			wv_hours.setCyclic(true);// 可循环滚动
			wv_hours.setLabel("时");// 添加文字
			wv_hours.setCurrentItem(h);


			wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
			wv_mins.setCyclic(true);// 可循环滚动
			wv_mins.setLabel("分");// 添加文字
			wv_mins.setCurrentItem(m);
		}else{
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
		}
		
		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelTimeView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    if(wv_day.getCurrentItem()>=30){
                        wv_day.setCurrentItem(30);
                    }
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        if(wv_day.getCurrentItem()>=29){
                            wv_day.setCurrentItem(29);
                        }
                    }else{
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        if(wv_day.getCurrentItem()>=28){
                            wv_day.setCurrentItem(28);
                        }
                    }
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelTimeView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    if(wv_day.getCurrentItem()>=30){
                        wv_day.setCurrentItem(30);
                    }
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0){
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        if(wv_day.getCurrentItem()>=29){
                            wv_day.setCurrentItem(29);
                        }
                    }else{
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        if(wv_day.getCurrentItem()>=28){
                            wv_day.setCurrentItem(28);
                        }
                    }
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if(hasSelectTime)
			textSize = (int)((screenheight / 100) * 2.5);
		else
			textSize = (screenheight / 100) * 3;
        LG.e("===textSize====" + textSize);
        wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();

        String fsmonth = "";
        int fmonth = wv_month.getCurrentItem() + 1;
        if (fmonth > 9) {
            fsmonth = fmonth + "";
        } else {
            fsmonth = "0" + fmonth;
        }

        String fsday = "";
        int fday = wv_day.getCurrentItem() + 1;
        if (fday > 9) {
            fsday = fday + "";
        } else {
            fsday = "0" + fday;
        }

		if(!hasSelectTime) {

            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                    .append(fsmonth).append("-")
                    .append(fsday);
        }else{
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                    .append(fsmonth).append("-")
                    .append(fsday).append(" ")
			.append(wv_hours.getCurrentItem()<10?"0"+wv_hours.getCurrentItem():wv_hours.getCurrentItem()).append(":")
			.append(wv_mins.getCurrentItem()<10?"0"+wv_mins.getCurrentItem():wv_mins.getCurrentItem()).append(":00");
            }
		return sb.toString();
	}
}