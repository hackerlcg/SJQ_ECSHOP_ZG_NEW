package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.FavourModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyListView;
import com.ecjia.component.view.ToastView;
import com.ecjia.hamster.adapter.RankListAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.USERRANK;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


public class SelectRankActivity extends BaseActivity implements HttpResponse {

    private FavourModel dataModel;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private MyListView lv_rank;
    private SESSION session;
    private TextView top_view_text;
    private ImageView top_view_back;
    private Button btn_save;
    private LinearLayout ll_rank_all;
    private RankListAdapter adapter;
    private String rank;
    private String[] rankchecks;

    public SelectRankActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_rank);

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        Intent intent=getIntent();
        rank=intent.getStringExtra("rank");



        if(dataModel==null){
            dataModel=new FavourModel(this);
            dataModel.addResponseListener(this);
            dataModel.getUserRank(api);
        }

        initView();

    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        btn_save = (Button) findViewById(R.id.btn_rank_save);
        top_view_text.setText(res.getText(R.string.add_discount_rank_level));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_rank_all=(LinearLayout) findViewById(R.id.ll_rank_all);

        lv_rank = (MyListView) findViewById(R.id.lv_rank);

        adapter=new RankListAdapter(this,dataModel.userranks);
        lv_rank.setAdapter(adapter);


        lv_rank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final USERRANK userrank= adapter.lists.get(position);
                if(userrank.isChecked()){
                    adapter.lists.get(position).setChecked(false);
                }else{
                    adapter.lists.get(position).setChecked(true);
                }
                adapter.notifyDataSetChanged();
            }
        });

        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer ranks=new StringBuffer();
                StringBuffer rank_ids=new StringBuffer();
                for (int i=0;i<adapter.lists.size();i++){
                    if (adapter.lists.get(i).isChecked()){
                        ranks.append(","+adapter.lists.get(i).getRank_name());
                        rank_ids.append(","+adapter.lists.get(i).getRank_id());
                    }
                }
                if(!TextUtils.isEmpty(ranks.toString())&&!TextUtils.isEmpty(rank_ids.toString())){
                    Intent intent=new Intent(SelectRankActivity.this,AddDiscountActivity.class);
                    ranks.deleteCharAt(0);
                    intent.putExtra("rank",ranks.toString());
                    rank_ids.deleteCharAt(0);
                    intent.putExtra("rank_id",rank_ids.toString());
                    setResult(100,intent);
                    finish();
                }else{
                    ToastView toast = new ToastView(SelectRankActivity.this, res.getString(R.string.add_rank_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });


    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.ADMIN_USER_RANK)) {
            if (status.getSucceed() == 1) {
                ll_rank_all.setVisibility(View.VISIBLE);

                if(!TextUtils.isEmpty(rank)){
                    if(rank.contains(",")){
                        rankchecks=rank.split(",");
                    }else{
                        rankchecks=new String[]{rank};
                    }

                    for (int i=0;i<dataModel.userranks.size();i++){
                        for(int j=0;j<rankchecks.length;j++){
                            if(dataModel.userranks.get(i).getRank_name().equals(rankchecks[j])){
                                adapter.lists.get(i).setChecked(true);
                            }
                        }

                    }
                }

                adapter.notifyDataSetChanged();

            }else{
                ll_rank_all.setVisibility(View.GONE);
            }
        }
    }
}
