package com.ecjia.hamster.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.GoodsListModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.XListView;
import com.ecjia.hamster.adapter.GiftChooseAdapter;
import com.ecjia.hamster.model.GIFT;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GiftChooseActivity extends BaseActivity implements HttpResponse,OnClickListener,XListView.IXListViewListener {

    private TextView top_view_text;
    private ImageView top_view_back;
    private XListView lv_gift_choose;
    private FrameLayout fl_null,fl_notnull;
    private ArrayList<GOODS> goods=new ArrayList<GOODS>();
    private ArrayList<GOODS> temp=new ArrayList<GOODS>();
    private ArrayList<GIFT> gifts=new ArrayList<GIFT>();
    private GiftChooseAdapter adapter;
    private GoodsListModel dataModel;
    private LinearLayout ll_search,topview,searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView tv_gifts_search;
    private View search_bottom;
    private MyDialog myDialog;
    private String KEYWORDS;
    private PAGINATED paginated;
    private long selectNo;
    private ImageView iv_gifts_check;
    private TextView tv_choose_num;
    private Button btn_add_choice;
    private boolean dialogflag;

    public GiftChooseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gift_choose);

        KEYWORDS="";

        if (null == dataModel) {
            dataModel = new GoodsListModel(this);
            dataModel.addResponseListener(this);
        }

        initView();

        dataModel.fetchPreSearch(session,"","","",KEYWORDS,0,api,true,true);
    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getText(R.string.gift_list));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogflag){
                    showBackDialog();
                }else{
                    finish();
                }
            }
        });
        fl_null = (FrameLayout) findViewById(R.id.fl_null);
        fl_notnull = (FrameLayout) findViewById(R.id.fl_notnull);

        lv_gift_choose = (XListView) findViewById(R.id.lv_gift_choose);

        iv_gifts_check=(ImageView) findViewById(R.id.iv_gifts_check);
        tv_choose_num=(TextView) findViewById(R.id.tv_choose_num);
        btn_add_choice=(Button) findViewById(R.id.btn_add_choice);
        btn_add_choice.setOnClickListener(this);

        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        searchlayout_bg = (View)findViewById(R.id.fragment_gifts_searchlayout_bg);
        tv_gifts_search = (TextView)findViewById(R.id.tv_gifts_search);
        searchlayout_in = (LinearLayout) findViewById(R.id.fragment_gifts_searchlayout_in);
        topview = (LinearLayout) findViewById(R.id.gifts_search_topview);
        search_bottom = (View) findViewById(R.id.search_bottom);
        searchLayout = (FrameLayout) findViewById(R.id.fragment_gifts_searchlayout);
        searchLayout.setOnClickListener(this);

        adapter=new GiftChooseAdapter(goods,this);

        lv_gift_choose.setAdapter(adapter);

        adapter.setOnRightItemClickListener(new GiftChooseAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                final GOODS good= adapter.list.get(position);
                if(good.isChecked()){
                    adapter.list.get(position).setChecked(false);
                    selectNo-=1;
                }else{
                    adapter.list.get(position).setChecked(true);
                    selectNo+=1;
                }
                refreshNo();
                adapter.notifyDataSetChanged();
            }
        });

        lv_gift_choose.setPullLoadEnable(false);
        lv_gift_choose.setPullRefreshEnable(true);
        lv_gift_choose.setXListViewListener(this, 0);
        lv_gift_choose.setRefreshTime();
    }

    private void refreshNo() {
        tv_choose_num.setText("已选中"+selectNo+"件商品");

        if(selectNo>0){
            iv_gifts_check.setImageResource(R.drawable.goods_cb_checked);
            btn_add_choice.setEnabled(true);
        }else{
            iv_gifts_check.setImageResource(R.drawable.goods_cb_unchecked);
            btn_add_choice.setEnabled(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.GOODS)) {
            if (status.getSucceed() == 1) {

                checkNull();

                lv_gift_choose.stopRefresh();
                lv_gift_choose.stopLoadMore();
                lv_gift_choose.setRefreshTime();

                paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    lv_gift_choose.setPullLoadEnable(false);
                } else {
                    lv_gift_choose.setPullLoadEnable(true);
                }
                adapter.notifyDataSetChanged();

            }
        }

    }

    private void setData() {
        temp.clear();
        temp.addAll(goods);

        goods.clear();
        goods.addAll(dataModel.goodsList);

        selectNo=0;
        for (int i=0;i<temp.size();i++){
            if(temp.get(i).isChecked()||!TextUtils.isEmpty(temp.get(i).getPrice())){

                for (int j=0;j<goods.size();j++){
                    if(goods.get(j).getId().equals(temp.get(i).getId())){
                        goods.get(j).setChecked(temp.get(i).isChecked());
                        goods.get(j).setPrice(temp.get(i).getPrice());
                        if(goods.get(j).isChecked()){
                            selectNo+=1;
                        }
                    }
                }
            }
        }
        refreshNo();
    }

    private void checkNull() {
        if(dataModel.goodsList.size()==0){
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
        }else {
            fl_null.setVisibility(View.GONE);
            fl_notnull.setVisibility(View.VISIBLE);
            setData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_choice:
                if(goods.size()>0){
                    JSONObject requestJsonObject = new JSONObject();
                    try
                    {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for(int i =0; i< goods.size(); i++)
                    {
                        GOODS tempData =goods.get(i);
                        if(tempData.isChecked()){


                        String price=tempData.getGift_price();
                        String oprice=tempData.getShop_price();
//                            if("0".equals(tempData.getPromote_price())||TextUtils.isEmpty(tempData.getPromote_price())){
//                                oprice = tempData.getShop_price();
//                            }else {
//                                oprice = tempData.getPromote_price();
//                            }

                        if(TextUtils.isEmpty(price)){
//                            if("0".equals(tempData.getPromote_price())||TextUtils.isEmpty(tempData.getPromote_price())){
                                price = tempData.getShop_price();
//                            }else {
//                                price = tempData.getPromote_price();
//                            }
//                            price="¥ 0.00";
                        }else{
                            price="¥ "+tempData.getGift_price();
                        }
                        GIFT itemData=new GIFT(tempData.getId(),tempData.getName(),price,oprice,tempData.getImg().getThumb());
                        JSONObject itemJSONObject = itemData.toJson();
                        itemJSONArray1.put(itemJSONObject);
                        }
                    }
                    requestJsonObject.put("gift", itemJSONArray1);
                } catch (JSONException e) {
                        // TODO: handle exception
                }

                Intent intent=new Intent(GiftChooseActivity.this,SelectGiftsActivity.class);
                intent.putExtra("gift",requestJsonObject.toString());
                startActivityForResult(intent,101);
            }
            break;
            case R.id.fragment_gifts_searchlayout:
                search_bottom.setVisibility(View.GONE);
                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -topview.getHeight());
                ScaleAnimation animation1 = new ScaleAnimation(1f, 0.85f, 1f, 1f);
                int screenWidth =getWindowManager().getDefaultDisplay().getWidth();
                int leftMagin = (int)getResources().getDimension(R.dimen.dim20);
                TranslateAnimation animation2 = new TranslateAnimation(0, -screenWidth / 2 + 2 * leftMagin + searchlayout_in.getWidth() / 2, 0, 0);
                animation.setDuration(300);
                animation1.setDuration(300);
                animation2.setDuration(300);
                animation.setFillAfter(true);
                animation1.setFillAfter(true);
                animation2.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(GiftChooseActivity.this, SearchActivity.class);
                        intent.putExtra("type","gifts");
                        KEYWORDS=tv_gifts_search.getText().toString();
                        intent.putExtra("KEYWORDS",KEYWORDS);
                        startActivityForResult(intent, 100);
                        overridePendingTransition(R.anim.animation_2, R.anim.animation_1);
                    }
                });
                ll_search.startAnimation(animation);
                searchlayout_bg.startAnimation(animation1);
                searchlayout_in.startAnimation(animation2);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int leftMagin = (int)getResources().getDimension(R.dimen.dim10);
            //动画
            TranslateAnimation animation = new TranslateAnimation(0, 0, -topview.getHeight(), 0);
            ScaleAnimation animation1 = new ScaleAnimation(0.85f, 1f, 1f, 1f);
            TranslateAnimation animation2 = new TranslateAnimation(-screenWidth / 2 + 2 * leftMagin + searchlayout_in.getWidth() / 2, 0, 0, 0);
            animation.setDuration(300);
            animation1.setDuration(300);
            animation2.setDuration(300);
            animation.setFillAfter(true);
            animation1.setFillAfter(true);
            animation2.setFillAfter(true);
            ll_search.startAnimation(animation);
            searchlayout_bg.startAnimation(animation1);
            searchlayout_in.startAnimation(animation2);
            search_bottom.setVisibility(View.VISIBLE);
            if(resultCode==100){
                KEYWORDS=data.getStringExtra("KEYWORDS");
                tv_gifts_search.setText(KEYWORDS);
                dataModel.fetchPreSearch(session,"","","",KEYWORDS,0,api,true,true);
            }

        }else
            if(requestCode==101&&resultCode==100){
               String gift=data.getStringExtra("gift");
                int giftnum=data.getIntExtra("giftnum",0);
              Intent intent =new Intent();
              intent.putExtra("gift",gift);
              intent.putExtra("giftnum",giftnum);
              setResult(100,intent);
                finish();
        }else
            if(requestCode==101&&resultCode==99){
                dialogflag=true;
            }

    }

    @Override
    public void onRefresh(int id) {
        dataModel.fetchPreSearch(session,"","","",KEYWORDS,0,api,true,true);
    }

    @Override
    public void onLoadMore(int id) {
        dataModel.fetchPreSearchMore(session,"","","",KEYWORDS,0,api,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(dialogflag){
                showBackDialog();
            }else{
                finish();
            }
        }
        return true;
    }

    private void showBackDialog() {
        myDialog = new MyDialog(this, res.getString(R.string.tip), res.getString(R.string.tips_content_back));
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
                myDialog.dismiss();
                finish();
            }
        });
    }
}
