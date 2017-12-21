package com.ecjia.hamster.activity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.GoodsBrandModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.MyLetterListView;
import com.ecjia.component.view.XListView;
import com.ecjia.hamster.adapter.ChooseBrandsAdapter;
import com.ecjia.hamster.model.BRAND;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BrandChooseActivity extends BaseActivity implements HttpResponse,OnClickListener,XListView.IXListViewListener {

    private TextView top_view_text;
    private ImageView top_view_back;
    private XListView lv_brands_choose;
    private FrameLayout fl_null,fl_notnull;
    private ArrayList<BRAND> brands=new ArrayList<BRAND>();
    private ArrayList<BRAND> temp=new ArrayList<BRAND>();
    private ChooseBrandsAdapter adapter;
    private GoodsBrandModel dataModel;
    private LinearLayout ll_search,topview,searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView tv_brands_search;
    private View search_bottom;
    private MyDialog myDialog;
    private String KEYWORDS;
    private PAGINATED paginated;
    private long selectNo;
    private ImageView iv_brands_check;
    private TextView tv_choose_num;
    private Button btn_add_choice;
    private MyLetterListView letterListView;
    private TextView overlay;
    private WindowManager windowManager;
    private Handler handler;
    private OverlayThread overlayThread;
    private boolean dialogflag;

    public BrandChooseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_brand_choose);

        KEYWORDS="";

        if (null == dataModel) {
            dataModel = new GoodsBrandModel(this);
            dataModel.addResponseListener(this);
        }

        initView();

        dataModel.getBrandList(KEYWORDS,api,true);
    }

    private void initView() {

        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();

        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getText(R.string.select_brand_list));
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

        lv_brands_choose = (XListView) findViewById(R.id.xlv_brand_choose);

        letterListView = (MyLetterListView) findViewById(R.id.mlv_brands);
        letterListView .setOnTouchingLetterChangedListener(new LetterListViewListener());

        iv_brands_check=(ImageView) findViewById(R.id.iv_brands_check);
        tv_choose_num=(TextView) findViewById(R.id.tv_choose_num);
        btn_add_choice=(Button) findViewById(R.id.btn_add_choice);
        btn_add_choice.setOnClickListener(this);

        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        searchlayout_bg = (View)findViewById(R.id.fragment_brands_searchlayout_bg);
        tv_brands_search = (TextView)findViewById(R.id.tv_brands_search);
        searchlayout_in = (LinearLayout) findViewById(R.id.fragment_brands_searchlayout_in);
        topview = (LinearLayout) findViewById(R.id.brands_search_topview);
        search_bottom = (View) findViewById(R.id.search_bottom);
        searchLayout = (FrameLayout) findViewById(R.id.fragment_brands_searchlayout);
        searchLayout.setOnClickListener(this);

        adapter=new ChooseBrandsAdapter(this,brands);

        lv_brands_choose.setAdapter(adapter);

        lv_brands_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    position=position-1;
                }
                final BRAND brand = adapter.list.get(position);
                if (brand.isClicked()) {
                    adapter.list.get(position).setClicked(false);
                    selectNo -= 1;
                } else {
                    adapter.list.get(position).setClicked(true);
                    selectNo += 1;
                }
                refreshNo();
                adapter.notifyDataSetChanged();
            }
        });



        lv_brands_choose.setPullLoadEnable(false);
        lv_brands_choose.setPullRefreshEnable(true);
        lv_brands_choose.setXListViewListener(this, 0);
        lv_brands_choose.setRefreshTime();
    }

    private void refreshNo() {
        tv_choose_num.setText("已选中"+selectNo+"品牌");

        if(selectNo>0){
            iv_brands_check.setImageResource(R.drawable.goods_cb_checked);
            btn_add_choice.setEnabled(true);
        }else{
            iv_brands_check.setImageResource(R.drawable.goods_cb_unchecked);
            btn_add_choice.setEnabled(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ADMIN_GOODS_BRAND)) {
            if (status.getSucceed() == 1) {

                checkNull();

                lv_brands_choose.stopRefresh();
                lv_brands_choose.stopLoadMore();
                lv_brands_choose.setRefreshTime();

                paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    lv_brands_choose.setPullLoadEnable(false);
                } else {
                    lv_brands_choose.setPullLoadEnable(true);
                }
                adapter.resetLetter();
                adapter.notifyDataSetChanged();

            }
        }

    }

    private void setData() {
        temp.clear();
        temp.addAll(brands);

        brands.clear();
        brands.addAll(dataModel.brands);

        selectNo=0;
        for (int i=0;i<temp.size();i++){
            if(temp.get(i).isClicked()){

                for (int j=0;j<brands.size();j++){
                    if(brands.get(j).getBrand_id()==temp.get(i).getBrand_id()){
                        brands.get(j).setClicked(temp.get(i).isClicked());
                        if(brands.get(j).isClicked()){
                            selectNo+=1;
                        }
                    }
                }
            }
        }
        refreshNo();
    }

    private void checkNull() {
        if(dataModel.brands.size()==0){
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
                if(brands.size()>0){
                    JSONObject requestJsonObject = new JSONObject();
                    try
                    {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for(int i =0; i< brands.size(); i++)
                    {
                        BRAND tempData =brands.get(i);
                        if(tempData.isClicked()){
                            JSONObject itemJSONObject = tempData.toJson();
                            itemJSONArray1.put(itemJSONObject);
                        }
                    }
                    requestJsonObject.put("brands", itemJSONArray1);
                } catch (JSONException e) {
                        // TODO: handle exception
                }

                Intent intent=new Intent(BrandChooseActivity.this,SelectBrandsActivity.class);
                intent.putExtra("brands",requestJsonObject.toString());
                startActivityForResult(intent,101);
            }
            break;
            case R.id.fragment_brands_searchlayout:
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
                        Intent intent = new Intent(BrandChooseActivity.this, SearchActivity.class);
                        intent.putExtra("type","brands");
                        KEYWORDS=tv_brands_search.getText().toString();
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
                tv_brands_search.setText(KEYWORDS);
                dataModel.getBrandList(KEYWORDS,api,true);
            }

        }else
            if(requestCode==101&&resultCode==100){
               String brands=data.getStringExtra("brands");
                int brandsnum=data.getIntExtra("brandsnum",0);
              Intent intent =new Intent();
              intent.putExtra("brands",brands);
              intent.putExtra("brandsnum",brandsnum);
              setResult(100,intent);
                finish();
            }else
            if(requestCode==101&&resultCode==99){
                dialogflag=true;
            }

    }

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.choose_overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }

    private class LetterListViewListener implements
            MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (adapter.alphaIndexer.get(s) != null) {
                int position = adapter.alphaIndexer.get(s);
                lv_brands_choose.setSelection(position);
                overlay.setText(adapter.sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                handler.postDelayed(overlayThread, 1500);
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        dataModel.getBrandList(KEYWORDS, api, true);
    }

    @Override
    public void onLoadMore(int id) {
        dataModel.getBrandListMore(KEYWORDS, api);
    }

    @Override
    public void finish() {
        if (null != windowManager) {
            windowManager.removeView(overlay);
        }
        super.finish();
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
