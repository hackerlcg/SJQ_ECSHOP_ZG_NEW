package com.ecjia.hamster.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.component.imagecircle.CircleImage;
import com.ecjia.component.network.model.ShopModel;
import com.ecjia.component.view.CYTextView;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.activity.AddressActivity;
import com.ecjia.hamster.activity.CustomerCenterActivity;
import com.ecjia.hamster.activity.QRCodeActivity;
import com.ecjia.hamster.activity.SettingActivity;
import com.ecjia.hamster.activity.ShopChangeActivity;
import com.ecjia.hamster.activity.ShopEditActivity;
import com.ecjia.hamster.activity.assets.myassets.MyAssetsActivity;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
/*
个人中心页面
 */
public class ShopFragment extends Fragment implements HttpResponse{

	private View view;
	private SharedPreferences shared;
    private TextView top_view_text,top_right_tv,tv_name,tv_api;
    private ImageView top_view_back;
    private LinearLayout shop_zxing,setting;
    private RelativeLayout myCash;

    private String uid;
    private String sid;
    private String api;
    private String area;
    private SESSION session;

    private LinearLayout ll_shop_category,ll_shop_area,ll_address,ll_phone;
    private TextView tv_shop_category,tv_my_price,tv_my_no_price,tv_phone,tv_shop_area,tv_shop_address;
    private EditText et_phone,et_shop_adress,et_shop_introduction;
    private CircleImage iv_logo;
    private LinearLayout edit_phone,edit_address,edit_introduction,ll_customer_center;
    private FrameLayout fl_shopinfo_null;
    private CYTextView tv_shop_introduction;
    private String phone,address,province,city,description,category,money, unmoney;

    private ShopModel shopModel;

    private Resources resource;
    private MyDialog myDialog;

    private int privilege=1;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.shop, null);
		shared = getActivity().getSharedPreferences("userInfo", 0);
        resource =getActivity().getResources();
        EventBus.getDefault().register(this);

        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);


        if(null==shopModel){
            shopModel=new ShopModel(getActivity());
            shopModel.addResponseListener(this);
        }
        
        initView();

        shopModel.getShopInfo(session,api);

		return view;
	}

    private void initView() {
        top_view_text=(TextView)view.findViewById(R.id.top_view_text);
        top_right_tv=(TextView)view.findViewById(R.id.top_right_tv);
        myCash =(RelativeLayout)view.findViewById(R.id.rl_cash);
        top_view_back=(ImageView)view.findViewById(R.id.top_view_back);

        iv_logo=(CircleImage)view.findViewById(R.id.iv_logo);

        edit_phone=(LinearLayout)view.findViewById(R.id.edit_phone);
        edit_address=(LinearLayout)view.findViewById(R.id.edit_address);
        edit_introduction=(LinearLayout)view.findViewById(R.id.edit_introduction);

        tv_shop_introduction=(CYTextView)view.findViewById(R.id.tv_shop_introduction);

        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_api=(TextView)view.findViewById(R.id.tv_api);

        tv_shop_category=(TextView)view.findViewById(R.id.tv_shop_category);
        tv_my_price=(TextView)view.findViewById(R.id.tv_my_price);
        tv_my_no_price=(TextView)view.findViewById(R.id.tv_my_no_price);
        tv_phone=(TextView)view.findViewById(R.id.tv_phone);
        tv_shop_area=(TextView)view.findViewById(R.id.tv_shop_area);
        tv_shop_address=(TextView)view.findViewById(R.id.tv_shop_address);

        et_phone=(EditText)view.findViewById(R.id.et_phone);
        et_shop_adress=(EditText)view.findViewById(R.id.et_shop_adress);
        et_shop_introduction=(EditText)view.findViewById(R.id.et_shop_introduction);

        shop_zxing=(LinearLayout)view.findViewById(R.id.shop_zxing);
        setting=(LinearLayout)view.findViewById(R.id.setting);
        ll_shop_category=(LinearLayout)view.findViewById(R.id.ll_shop_category);
        ll_shop_area=(LinearLayout)view.findViewById(R.id.ll_shop_area);
        ll_address=(LinearLayout)view.findViewById(R.id.ll_address);
        ll_phone=(LinearLayout)view.findViewById(R.id.ll_phone);
        ll_customer_center=(LinearLayout)view.findViewById(R.id.ll_customer_center);

        fl_shopinfo_null=(FrameLayout)view.findViewById(R.id.fl_shopinfo_null);

        top_view_text.setText(resource.getText(R.string.shop));
        top_right_tv.setVisibility(View.GONE);
        top_right_tv.setText(resource.getText(R.string.shop_top_right));
        top_view_back.setVisibility(View.GONE);
        tv_my_price.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), MyAssetsActivity.class));
        });

        ll_customer_center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CustomerCenterActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        shop_zxing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), QRCodeActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        ll_shop_category.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ll_shop_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddressActivity.class);
                intent.putExtra("province",province);
                intent.putExtra("privilege",privilege);
                intent.putExtra("city",city);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        top_right_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ShopChangeActivity.class);
                intent.putExtra("fromInner",true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        edit_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShopEditActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("content",phone);
                intent.putExtra("privilege",privilege);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        edit_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShopEditActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("content",address);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        edit_introduction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShopEditActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("content",description);
                intent.putExtra("privilege",privilege);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            }
        });
    }

    private void openDialog(final int type) {
        String tips =getActivity().getResources().getString(R.string.tip);
        String tips_content =getActivity().getResources().getString(R.string.shop_dialog);
        myDialog = new MyDialog(getActivity(), tips, tips_content);
        myDialog.show();
        myDialog.negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();

            }
        });
        myDialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type){
                    case 1:
                        phone=et_phone.getText().toString();
                    break;
                    case 2:
                        address=et_shop_adress.getText().toString();
                    break;
                    case 3:
                        description=et_shop_introduction.getText().toString();
                    break;
                }
                shopModel.updateShop(session,0,phone,0,0,address,description,type,api);
                myDialog.dismiss();
            }


        });
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url .equals(ProtocolConst.SHOPINFO)) {
            if (status.getSucceed() == 1) {
                privilege=shopModel.privilege;

                if(privilege==0){
                    fl_shopinfo_null.setVisibility(View.VISIBLE);
                }else{
                    fl_shopinfo_null.setVisibility(View.GONE);
                    if(shopModel.shopinfo.getId()==0){
                        ll_shop_category.setVisibility(View.GONE);
                    }else{
                        ll_shop_category.setVisibility(View.VISIBLE);
                    }


                    tv_name.setText(shopModel.shopinfo.getSeller_name());
                    tv_api.setText(shared.getString("uname", ""));

                    ImageLoaderForLV.getInstance(getActivity()).setImageRes(iv_logo,shopModel.shopinfo.getSeller_logo());

                    phone=shopModel.shopinfo.getSeller_telephone();
                    province=shopModel.shopinfo.getSeller_province();
                    city=shopModel.shopinfo.getSeller_city();
                    address=shopModel.shopinfo.getSeller_address();
                    category=shopModel.shopinfo.getSeller_category();
                    money=shopModel.shopinfo.getSeller_money();
                    unmoney=shopModel.shopinfo.getSeller_un_money();
                    description=shopModel.shopinfo.getSeller_description();

                    tv_phone.setText(phone);
                    tv_shop_address.setText(address);
                    tv_shop_category.setText(category);
                    tv_my_price.setText(money);
                    tv_my_no_price.setText(unmoney);
                    tv_shop_area.setText(city+" "+province);

                    if(!TextUtils.isEmpty(description)){
                        tv_shop_introduction.setVisibility(View.VISIBLE);
                        tv_shop_introduction.SetText(description);
                    }else{
                        tv_shop_introduction.setVisibility(View.GONE);
                        tv_shop_introduction.SetText("");
                    }
                }



            }
        }
        if (url .equals(ProtocolConst.UPDATESHOP)) {
            if (status.getSucceed() == 1) {
                shopModel.getShopInfo(session,api);
            }else{
                ToastView toast = new ToastView(getActivity(),resource.getString(R.string.edit_shop_failed));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(200);
                toast.show();
            }
        }

    }

    public void onEvent(MyEvent event) {
        if("EDITSHOP".equals(event.getMsg())) {
            shopModel.getShopInfo(session,api);
        }
        if(AppConst.SCATEGORYT==event.getmTag()) {
            if (!TextUtils.isEmpty(event.getMsg())) {
                int cid = Integer.parseInt(event.getMsg());
                shopModel.updateShop(session,cid,phone,0,0,address,description,0,api);
            }
        }
        if(AppConst.AREA==event.getmTag()) {
            if (!TextUtils.isEmpty(event.getMsg())) {
                String s=event.getMsg();
                LG.e("===areas==="+s);
                String[]sp=s.split("===");
                int r1 = Integer.parseInt(sp[0]);
                int r2 = Integer.parseInt(sp[1]);
                shopModel.updateShop(session,0,phone,r1,r2,address,description,0,api);
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
