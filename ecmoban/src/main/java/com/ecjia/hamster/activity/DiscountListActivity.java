package com.ecjia.hamster.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.FavourableModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.XListView;
import com.ecjia.hamster.adapter.DiscountListAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONException;
import org.json.JSONObject;


public class DiscountListActivity extends BaseActivity implements HttpResponse,XListView.IXListViewListener{
    private RelativeLayout rl_1,rl_2,rl_3;
    private TextView tab_text_1,tab_text_2,tab_text_3;
    private View tab_line_1,tab_line_2,tab_line_3;
    private XListView xListView;
    private DiscountListAdapter discountListAdapter;
    private TextView title,title_right_text;
    private ImageView back;
    private int selected_color,un_select_color;
    private FavourableModel favourableModel;
    private String[] status=new String[]{"coming","going","finished"};//分别表示 未开始、进行中和已完成
    private int tab_selected_id=0;
    private RelativeLayout rl_discount_null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_discount);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.top_view_text);
        title.setText(res.getString(R.string.magic_discount));
        title_right_text= (TextView) findViewById(R.id.top_right_tv);
        title_right_text.setText(res.getString(R.string.discount_add));
        title_right_text.setVisibility(View.VISIBLE);

        title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DiscountListActivity.this,AddDiscountActivity.class);
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

        rl_discount_null=(RelativeLayout)findViewById(R.id.rl_discount_null);

        xListView= (XListView) findViewById(R.id.xlv_discount);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(this,0);
        xListView.setRefreshTime();
        favourableModel=new FavourableModel(this);
        favourableModel.addResponseListener(this);
        if(discountListAdapter==null){
            discountListAdapter=new DiscountListAdapter(this,favourableModel.favourableArrayList,0);
        }
        xListView.setAdapter(discountListAdapter);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(DiscountListActivity.this,AddDiscountActivity.class);
                if(tab_selected_id==2){
                    intent.putExtra("TYPE",2);
                }else{
                    intent.putExtra("TYPE",1);
                }
                if(position>0){
                    position-=1;
                }
                int aid=favourableModel.favourableArrayList.get(position).getAct_id();
                intent.putExtra("act_id",aid);
                startActivityForResult(intent, 101);
            }
        });

        selected_color=res.getColor(R.color.bg_theme_color);
        un_select_color=res.getColor(R.color.text_login_color);
        initTab();
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
        discountListAdapter.setFlag(i);
        favourableModel.getFavourableList(status[tab_selected_id],api,true);
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
            favourableModel.getFavourableList(status[tab_selected_id],api,true);
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
        if(url== ProtocolConst.ADMIN_FAVOURABLE_LIST){
            if(status.getSucceed()==1){
                if(favourableModel.favourableArrayList.size()>0){
                    xListView.setVisibility(View.VISIBLE);
                    rl_discount_null.setVisibility(View.GONE);
                }else{
                    xListView.setVisibility(View.GONE);
                    rl_discount_null.setVisibility(View.VISIBLE);
                }

                discountListAdapter.notifyDataSetChanged();
                if(favourableModel.paginated.getMore()==1){
                    xListView.setPullLoadEnable(true);
                }else{
                    xListView.setPullLoadEnable(false);
                }
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        favourableModel.getFavourableList(status[tab_selected_id],api,false);
    }

    @Override
    public void onLoadMore(int id) {
        favourableModel.getFavourableListMore(status[tab_selected_id],api);
    }
}
