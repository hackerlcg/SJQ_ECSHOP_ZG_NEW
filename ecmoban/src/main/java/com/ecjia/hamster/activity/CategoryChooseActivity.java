package com.ecjia.hamster.activity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.CategoryModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.MyLetterListView;
import com.ecjia.hamster.adapter.ChooseCategoryAdapter;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CategoryChooseActivity extends BaseActivity implements HttpResponse,OnClickListener {

    private TextView top_view_text;
    private ImageView top_view_back;
    private ListView lv_category_choose;
    private FrameLayout fl_null,fl_notnull;
    private ArrayList<CATEGORY> categories=new ArrayList<CATEGORY>();
    private ArrayList<CATEGORY> temp=new ArrayList<CATEGORY>();
    private ChooseCategoryAdapter adapter;
    private CategoryModel dataModel;
    private LinearLayout ll_search,topview,searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView tv_category_search;
    private View search_bottom;
    private MyDialog myDialog;
    private String KEYWORDS;
    private long selectNo;
    private ImageView iv_category_check;
    private TextView tv_choose_num;
    private Button btn_add_choice;
    private MyLetterListView letterListView;
    private TextView overlay;
    private WindowManager windowManager;
    private Handler handler;
    private OverlayThread overlayThread;
    private boolean dialogflag;

    public CategoryChooseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_category_choose);

        KEYWORDS="";

        if (null == dataModel) {
            dataModel = new CategoryModel(this);
            dataModel.addResponseListener(this);
        }

        initView();

        dataModel.getGoodsCategory(session, api);
    }

    private void initView() {

        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();

        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getText(R.string.select_category_list));
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

        lv_category_choose = (ListView) findViewById(R.id.lv_category_choose);

        letterListView = (MyLetterListView) findViewById(R.id.mmlv_category);
        letterListView .setOnTouchingLetterChangedListener(new LetterListViewListener());

        iv_category_check=(ImageView) findViewById(R.id.iv_category_check);
        tv_choose_num=(TextView) findViewById(R.id.tv_choose_num);
        btn_add_choice=(Button) findViewById(R.id.btn_add_choice);
        btn_add_choice.setOnClickListener(this);

        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        searchlayout_bg = (View)findViewById(R.id.fragment_category_searchlayout_bg);
        tv_category_search = (TextView)findViewById(R.id.tv_category_search);
        searchlayout_in = (LinearLayout) findViewById(R.id.fragment_category_searchlayout_in);
        topview = (LinearLayout) findViewById(R.id.category_search_topview);
        search_bottom = (View) findViewById(R.id.search_bottom);
        searchLayout = (FrameLayout) findViewById(R.id.fragment_category_searchlayout);
        searchLayout.setOnClickListener(this);

        adapter=new ChooseCategoryAdapter(this,categories);

        lv_category_choose.setAdapter(adapter);

        lv_category_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CATEGORY category = adapter.list.get(position);
                if (category.isChoose()) {
                    adapter.list.get(position).setChoose(false);
                    selectNo -= 1;
                } else {
                    adapter.list.get(position).setChoose(true);
                    selectNo += 1;
                }
                refreshNo();
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void refreshNo() {
        tv_choose_num.setText("已选中"+selectNo+"分类");

        if(selectNo>0){
            iv_category_check.setImageResource(R.drawable.goods_cb_checked);
            btn_add_choice.setEnabled(true);
        }else{
            iv_category_check.setImageResource(R.drawable.goods_cb_unchecked);
            btn_add_choice.setEnabled(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.GOODSCATEGORY)) {
            if (status.getSucceed() == 1) {
                checkNull();

            }
        }

    }

    private void setData() {
        temp.clear();
        temp.addAll(categories);

        categories.clear();
        categories.addAll(dataModel.categories);

        adapter.resetLetter();

        selectNo=0;
        for (int i=0;i<temp.size();i++){
            if(temp.get(i).isChoose()){

                for (int j=0;j<categories.size();j++){
                    if(categories.get(j).getCat_id()==temp.get(i).getCat_id()){
                        categories.get(j).setChoose(temp.get(i).isChoose());
                        if(categories.get(j).isChoose()){
                            selectNo+=1;
                        }
                    }
                }
            }
        }

        adapter.notifyDataSetChanged();
        refreshNo();
    }

    private void checkNull() {
        if(dataModel.categories.size()==0){
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
                if(categories.size()>0){
                    JSONObject requestJsonObject = new JSONObject();
                    try
                    {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for(int i =0; i< categories.size(); i++)
                    {
                        CATEGORY tempData =categories.get(i);
                        if(tempData.isChoose()){
                            JSONObject itemJSONObject = tempData.toJson();
                            itemJSONArray1.put(itemJSONObject);
                        }
                    }
                    requestJsonObject.put("category", itemJSONArray1);
                } catch (JSONException e) {
                        // TODO: handle exception
                }

                Intent intent=new Intent(CategoryChooseActivity.this,SelectCategoryActivity.class);
                intent.putExtra("category",requestJsonObject.toString());
                startActivityForResult(intent, 101);
            }
            break;
            case R.id.fragment_category_searchlayout:
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
                        Intent intent = new Intent(CategoryChooseActivity.this, SearchActivity.class);
                        intent.putExtra("type","category");
                        KEYWORDS=tv_category_search.getText().toString();
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
                if(!TextUtils.isEmpty(KEYWORDS)){
                    tv_category_search.setText(KEYWORDS);

                    categories.clear();
                    if(null!=dataModel){
                    categories.addAll(dataModel.categories);
                    }

                    ArrayList<CATEGORY> categories1=new ArrayList<CATEGORY>();
                    for(int i=0;i<categories.size();i++){
                        if(categories.get(i).getCat_name().contains(KEYWORDS)){
                            categories1.add(categories.get(i));
                        }
                    }
                    categories.clear();
                    categories.addAll(categories1);
                    adapter.resetLetter();
                    adapter.notifyDataSetChanged();
                }else{
                    if(null!=dataModel){
                        tv_category_search.setText("");
                        categories.clear();
                        categories.addAll(dataModel.categories);
                        adapter.resetLetter();
                        adapter.notifyDataSetChanged();
                    }
                }
            }

        }else
            if(requestCode==101&&resultCode==100){
               String category=data.getStringExtra("category");
                int categorynum=data.getIntExtra("categorynum",0);
              Intent intent =new Intent();
              intent.putExtra("category",category);
              intent.putExtra("categorynum",categorynum);
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
                lv_category_choose.setSelection(position);
                overlay.setText(adapter.sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                handler.postDelayed(overlayThread, 1500);
            }
        }
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
