package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.GoodsDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsDescActivity extends BaseActivity implements HttpResponse {

	
	private TextView title;
	private ImageView back;
	private GoodsDetailModel goodsModel;
	private WebView webView;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private String id;
    private String webUrl;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpweb);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");
        webUrl = shared.getString("webUrl", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);
        
		title = (TextView) findViewById(R.id.top_view_text);
        String det=res.getString(R.string.good_detail);
		title.setText(det);
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		webView = (WebView) findViewById(R.id.help_web);

		webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});
		
		webView.setInitialScale(25);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);

        if(!TextUtils.isEmpty(webUrl)){
            webView.loadDataWithBaseURL(null,webUrl,"text/html","utf-8",null);
        }else{
            goodsModel = new GoodsDetailModel(this);
            goodsModel.addResponseListener(this);
            goodsModel.fetchGoodDesc(session, id, api);

        }
		
	}


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url .equals(ProtocolConst.GOODSDESC)) {
            if (status.getSucceed() == 1) {
                webView.loadDataWithBaseURL(null,goodsModel.goodsWeb,"text/html","utf-8",null);
            }
        }
    }
}
