package com.ecjia.component.wheel;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.REGIONS;

import java.util.ArrayList;


public class WheelAddress {

	private View view;
	private WheelTimeView r1;
	private WheelTimeView r2;
	private WheelTimeView r3;
    private TextView tv_address;
    private ImageView iv_del_address;
    private ArrayList<REGIONS> list;
    private ArrayList<REGIONS> list1;
    private ArrayList<REGIONS> list2;
    private ArrayList<REGIONS> temp;
    private boolean flag=false;
    public int screenheight,now1,now2;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelAddress(View view) {
		super();
		this.view = view;
		setView(view);
	}

	/**
     * 省  城市
	 * @Description: TODO 弹出地区选择器
	 */
	public void initAreaPicker(ArrayList<REGIONS> olist ,String sr1,String sr2) {

        list=new ArrayList<REGIONS>();//省
        list1=new ArrayList<REGIONS>();//省
        list2=new ArrayList<REGIONS>();
        temp=new ArrayList<REGIONS>();//市的集合
        list.clear();
        list.addAll(olist);

        for(int i=0;i<list.size();i++){
            if(list.get(i).getLevel()==1){//1省
                list1.add(list.get(i));
            }
            if(list.get(i).getLevel()==2){//2 市
                temp.add(list.get(i));
            }
        }

        for (int i=0;i<list1.size();i++){
            if(list1.get(i).getName().equals(sr1)){
               now1=i;
               break;
            }
        }

        tv_address=(TextView)view.findViewById(R.id.tv_address);
        iv_del_address=(ImageView)view.findViewById(R.id.iv_del_address);

		r1 = (WheelTimeView) view.findViewById(R.id.r1);
        r1.setAdapter(new RegionsWheelAdapter<REGIONS>(list1,list1.size()));
        r1.setCyclic(true);// 可循环滚动
        r1.setCurrentItem(now1);// 初始化时显示的数据


		r2 = (WheelTimeView) view.findViewById(R.id.r2);
        r2.setCyclic(false);

        for (int i=0;i<temp.size();i++){
            if(temp.get(i).getParent_id()==list1.get(r1.getCurrentItem()).getId()){
                list2.add(temp.get(i));
            }
        }

        for (int i=0;i<list2.size();i++){
            if(list2.get(i).getName().equals(sr2)){
                now2=i;
                break;
            }
        }

        int length=list2.size();
        length=length<5?5:length;
        r2.setCurrentItem(now2);
        r2.setAdapter(new RegionsWheelAdapter<REGIONS>(list2,length));



		OnWheelChangedListener wheelListener_r1 = new OnWheelChangedListener() {
			public void onChanged(WheelTimeView wheel, int oldValue, int newValue) {
                list2.clear();
                for (int i=0;i<temp.size();i++){
                    if(temp.get(i).getParent_id()==list1.get(newValue).getId()){
                        list2.add(temp.get(i));
                    }
                }
                r2.setAdapter(new RegionsWheelAdapter<REGIONS>(list2,list2.size()));
                r2.setCurrentItem(0);
//                if(flag){
                    tv_address.setText(getAddress());
//                }
			}
		};

		OnWheelChangedListener wheelListener_r2 = new OnWheelChangedListener() {
			public void onChanged(WheelTimeView wheel, int oldValue, int newValue) {
//                if(flag){
                    tv_address.setText(getAddress());
//                }
			}
		};

		r1.addChangingListener(wheelListener_r1);
		r2.addChangingListener(wheelListener_r2);

        if(TextUtils.isEmpty(tv_address.getText().toString())){
            tv_address.setText(getAddress());
            flag=true;
        }

        iv_del_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_address.setText("");
                flag=true;
            }
        });

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
			textSize = (screenheight / 100) * 3;
//        LG.e("===textSize===="+textSize);
        r1.TEXT_SIZE = textSize;
		r2.TEXT_SIZE = textSize;

	}

	public String getAddress() {
		StringBuffer sb = new StringBuffer();
			sb.append(list1.get(r1.getCurrentItem()).getName()).append("-")
				.append(list2.get(r2.getCurrentItem()).getName());
		return sb.toString();
	}

	public String getAddressId() {
		StringBuffer sb = new StringBuffer();
			sb.append(list1.get(r1.getCurrentItem()).getId()).append("===")
				.append(list2.get(r2.getCurrentItem()).getId());
		return sb.toString();
	}
}
