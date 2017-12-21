package com.ecjia.hamster.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.PromotionModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.XListView;
import com.ecjia.hamster.adapter.PromotionListAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONException;
import org.json.JSONObject;


public class PromotionListActivity extends BaseActivity implements HttpResponse,XListView.IXListViewListener{
    private RelativeLayout rl_1,rl_2,rl_3;
    private TextView tab_text_1,tab_text_2,tab_text_3;
    private View tab_line_1,tab_line_2,tab_line_3;
    private XListView xListView;
    private PromotionListAdapter adapter;
    private TextView title,title_right_text;
    private ImageView back;
    private int selected_color,un_select_color;
    private PromotionModel model;
    private String[] status=new String[]{"coming","going","finished"};//分别表示 未开始、进行中和已完成
    private int tab_selected_id=0;
    private RelativeLayout rl_promotion_null;
    private LinearLayout ll_search,topview,searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView tv_promotion_search;
    private View ll_search_bottom;
    private String KEYWORDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_promotion);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.top_view_text);
        title.setText(res.getString(R.string.magic_promotion));
        title_right_text= (TextView) findViewById(R.id.top_right_tv);
        title_right_text.setText(res.getString(R.string.discount_add));
        title_right_text.setVisibility(View.VISIBLE);

        title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PromotionListActivity.this,AddPromotionActivity.class);
                intent.putExtra("TYPE",0);
                startActivityForResult(intent, 101);
            }
        });

        back= (ImageView) findViewById(R.id.top_view_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_promotion_null=(RelativeLayout)findViewById(R.id.rl_promotion_null);

        xListView= (XListView) findViewById(R.id.xlv_promotion);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(this, 0);
        xListView.setRefreshTime();
        model=new PromotionModel(this);
        model.addResponseListener(this);
        if(adapter==null){
            adapter=new PromotionListAdapter(this,model.goodsList,0);
        }
        xListView.setAdapter(adapter);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(PromotionListActivity.this,AddPromotionActivity.class);
                if(tab_selected_id==2){
                    intent.putExtra("TYPE",2);
                }else{
                    intent.putExtra("TYPE",1);
                }
                if(position>0){
                    position-=1;
                }
                String goods_id=model.goodsList.get(position).getId();
                intent.putExtra("goods_id",goods_id);
                startActivityForResult(intent, 101);
            }
        });

        selected_color=res.getColor(R.color.bg_theme_color);
        un_select_color=res.getColor(R.color.text_login_color);
        initTab();

        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        searchlayout_bg = (View)findViewById(R.id.fragment_promotion_searchlayout_bg);
        tv_promotion_search = (TextView)findViewById(R.id.tv_promotion_search);
        searchlayout_in = (LinearLayout) findViewById(R.id.fragment_promotion_searchlayout_in);
        topview = (LinearLayout) findViewById(R.id.promotion_search_topview);
        searchLayout = (FrameLayout) findViewById(R.id.fragment_promotion_searchlayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -topview.getHeight());
                ScaleAnimation animation1 = new ScaleAnimation(1f, 0.85f, 1f, 1f);
                int screenWidth = PromotionListActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                int leftMagin = (int) PromotionListActivity.this.getResources().getDimension(R.dimen.dim20);
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
                        Intent intent = new Intent(PromotionListActivity.this, SearchActivity.class);
                        intent.putExtra("type","promotion");
                        intent.putExtra("KEYWORDS",KEYWORDS);
                        startActivityForResult(intent, 102);
                        overridePendingTransition(R.anim.animation_2, R.anim.animation_1);
                    }
                });
                ll_search.startAnimation(animation);
                searchlayout_bg.startAnimation(animation1);
                searchlayout_in.startAnimation(animation2);
            }
        });

    }
    //初始化Tab加载数据
    private void initTab() {
        rl_1= (RelativeLayout) findViewById(R.id.rl_1);
        rl_2= (RelativeLayout) findViewById(R.id.rl_2);
        rl_3= (RelativeLayout) findViewById(R.id.rl_3);
        tab_text_1= (TextView) findViewById(R.id.tv_1);
        tab_text_2= (TextView) findViewById(R.id.tv_2);
        tab_text_3= (TextView) findViewById(R.id.tv_3);
        tab_line_1=findViewById(R.id.line_1);
        tab_line_2=findViewById(R.id.line_2);
        tab_line_3=findViewById(R.id.line_3);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(0);
            }
        });
        rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(1);
            }
        });
        rl_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(2);
            }
        });
        selectTab(1);
    }

    private void selectTab(int i) {
        switch (i){
            case 0:
                tab_text_1.setTextColor(selected_color);
                tab_text_2.setTextColor(un_select_color);
                tab_text_3.setTextColor(un_select_color);
                tab_line_1.setVisibility(View.VISIBLE);
                tab_line_2.setVisibility(View.INVISIBLE);
                tab_line_3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tab_text_2.setTextColor(selected_color);
                tab_text_1.setTextColor(un_select_color);
                tab_text_3.setTextColor(un_select_color);
                tab_line_2.setVisibility(View.VISIBLE);
                tab_line_1.setVisibility(View.INVISIBLE);
                tab_line_3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tab_text_3.setTextColor(selected_color);
                tab_text_2.setTextColor(un_select_color);
                tab_text_1.setTextColor(un_select_color);
                tab_line_3.setVisibility(View.VISIBLE);
                tab_line_2.setVisibility(View.INVISIBLE);
                tab_line_1.setVisibility(View.INVISIBLE);
                break;
            default:
                tab_text_1.setTextColor(selected_color);
                tab_text_2.setTextColor(un_select_color);
                tab_text_3.setTextColor(un_select_color);
                tab_line_1.setVisibility(View.VISIBLE);
                tab_line_2.setVisibility(View.INVISIBLE);
                tab_line_3.setVisibility(View.INVISIBLE);
                break;
        }
        tab_selected_id=i;
        adapter.setFlag(i);
        model.getPromotionList(status[tab_selected_id], KEYWORDS,api,true);
    }

    /**
     * 设置优惠数据
     */
    private void setDiscountData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==100){
            model.getPromotionList(status[tab_selected_id], KEYWORDS, api, true);
        }else
        if(resultCode==100&&requestCode==102){
            int screenWidth = PromotionListActivity.this.getWindowManager().getDefaultDisplay().getWidth();
            int leftMagin = (int) PromotionListActivity.this.getResources().getDimension(R.dimen.dim10);
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url== ProtocolConst.ADMIN_PROMOTION_LIST){
            if(status.getSucceed()==1){
                if(model.goodsList.size()>0){
                    xListView.setVisibility(View.VISIBLE);
                    rl_promotion_null.setVisibility(View.GONE);
                }else{
                    xListView.setVisibility(View.GONE);
                    rl_promotion_null.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
                if(model.paginated.getMore()==1){
                    xListView.setPullLoadEnable(true);
                }else{
                    xListView.setPullLoadEnable(false);
                }
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        model.getPromotionList(status[tab_selected_id], KEYWORDS, api, false);
    }

    @Override
    public void onLoadMore(int id) {
        model.getPromotionListMore(status[tab_selected_id], KEYWORDS,api);
    }
}
