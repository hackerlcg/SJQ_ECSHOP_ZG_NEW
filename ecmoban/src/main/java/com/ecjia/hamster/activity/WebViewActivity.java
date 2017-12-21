package com.ecjia.hamster.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyProgressDialog;
import com.umeng.message.PushAgent;

public class WebViewActivity extends BaseActivity {

    public static final String WEBURL ="webUrl";
    public static final String WEBTITLE ="webtitle";

    private TextView title;
	private ImageView back;
    
    private ImageView web_back;
    private ImageView goForward;
    private ImageView reload;
    private String qrcode;

    WebView webView;
    private MyProgressDialog pd;
    private String loadurl;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        loadurl = intent.getStringExtra(WEBURL);
        String webTitle = intent.getStringExtra(WEBTITLE);

        pd= MyProgressDialog.createDialog(this);
        pd.setMessage(getResources().getString(R.string.loading));
        title = (TextView) findViewById(R.id.top_view_text);
        if(TextUtils.isEmpty(webTitle)){
            title.setText(getResources().getString(R.string.browser));
        }else{
            title.setText(webTitle);
        }
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        final Activity activity = this;

        webView = (WebView)findViewById(R.id.webview_webView);
        webView.setWebViewClient(new WebViewClient() { //通过webView打开链接，不调用系统浏览器

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                if(url.contains("com.ecjia.b2b2c.shopkeeper"+"://")&&loadurl.contains("qrcode=EC")){
                    qrcode=loadurl.substring(loadurl.lastIndexOf("qrcode=EC")+7,loadurl.length());
                    Intent intent=new Intent(WebViewActivity.this,QRBindActivity.class);
                    intent.putExtra("code",qrcode);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }else{
                    view.loadUrl(url);
                }

                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    WebViewActivity.this.title.setText(title);
                }
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        String ua=webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+" ECJiaBrowse/1.2.0");

        if (null != loadurl)
        {
            webView.loadUrl(loadurl);
        }

        web_back = (ImageView) findViewById(R.id.web_back);
        web_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(webView.canGoBack()) {
                    webView.goBack();
                } else {

                }
            }
        });


        goForward = (ImageView) findViewById(R.id.goForward);
        goForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.goForward();

            }
        });

        reload = (ImageView) findViewById(R.id.reload);
        reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });


    }

    @Override
    protected void onPause() {
        if(null!=pd){
            pd.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }
}
