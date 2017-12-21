package com.ecjia.hamster.activity;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.LG;

public class GalleryImageActivity extends BaseActivity {

    private List<View> list;
    private ViewPager imagePager;
    private PagerAdapter galleryImageAdapter;
    private ImageView startimage1;
    private ImageView startimage2;
    private ImageView startimage3;
    private ImageView startimage4;
    private ImageView startimage5;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private int pager_num;
    private Button go_ecjia, welcome_intent1, welcome_intent2, welcome_intent3, welcome_intent5;
    boolean isSettingGo;
    boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_image);

        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        isFirstRun = shared.getBoolean("isFirstRun", true);
        if(isFirstRun) {//启动或者从设置启动
            initView();//执行动画

        }else {
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
            finish();
        }
    }

    private void initView() {
        //先将设置页面的调为false。
        editor.putBoolean("isSettingGo", false);
        editor.commit();
        list = new ArrayList<View>();
        View lead_a = View.inflate(this, R.layout.start_a, null);
        View lead_b = View.inflate(this, R.layout.start_b, null);
        View lead_c = View.inflate(this, R.layout.start_c, null);
        View lead_d = View.inflate(this, R.layout.start_d, null);
        View lead_e = View.inflate(this, R.layout.start_e, null);
        startimage1 = (ImageView) lead_a.findViewById(R.id.starta);
        startimage2 = (ImageView) lead_b.findViewById(R.id.startb);
        startimage3=(ImageView) lead_c.findViewById(R.id.startc);
        startimage4 = (ImageView) lead_d.findViewById(R.id.startd);
        startimage5=(ImageView) lead_e.findViewById(R.id.starte);
//        startimage1.setBackgroundResource(R.drawable.start1);
//        startimage2.setBackgroundResource(R.drawable.start2);
//        startimage3.setBackgroundResource(R.drawable.start4);

        go_ecjia = (Button) lead_e.findViewById(R.id.go_ecjia);
//        welcome_intent1 = (Button) lead_a.findViewBart3);
//        startimage4.setBackgroundResource(R.drawable.styId(R.id.welcome_intent1);
//        welcome_intent1.setOnClickListener(this);

        Bitmap itemimage1=alterimage(R.drawable.start1);
        startimage1.setImageBitmap(itemimage1);
        Bitmap itemimage2=alterimage(R.drawable.start2);
        startimage2.setImageBitmap(itemimage2);
        Bitmap itemimage3=alterimage(R.drawable.start3);
        startimage3.setImageBitmap(itemimage3);
        Bitmap itemimage4=alterimage(R.drawable.start4);
        startimage4.setImageBitmap(itemimage4);
        Bitmap itemimage5=alterimage(R.drawable.start5);
        startimage5.setImageBitmap(itemimage5);




        list.add(lead_a);
        list.add(lead_b);
        list.add(lead_c);
//        list.add(lead_d);
        list.add(lead_e);
        imagePager = (ViewPager) findViewById(R.id.image_pager);
        //imagePager.setAlpha((float) 0.5);//设置透明度
        galleryImageAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));//删除页卡
            }

            public View instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡

                container.addView(list.get(position));//添加页卡
                return list.get(position);
            }
        };
        //-----------------------------------------2013-12-3修改导航界面------------------------------------------------
        imagePager.setAdapter(galleryImageAdapter);
        imagePager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                pager_num = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
            }
        });
//        imagePager.setOnTouchListener(this);

        go_ecjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFirstRun) {
                    editor.putBoolean("isFirstRun", false);
                    editor.commit();
//                    createShortcut();//创建图标
                }
                Intent intent = new Intent(GalleryImageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    //图片优化
    public Bitmap alterimage(int imageid) {
        InputStream is = this.getResources().openRawResource(imageid);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
//        options.inSampleSize = 2;
        options.inPreferredConfig=Bitmap.Config.ARGB_4444;
        Bitmap btp = BitmapFactory.decodeStream(is, null, options);
        return btp;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void createShortcut() {

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);// 快捷方式的名称
        shortcut.putExtra("duplicate", true);// 不允许重复创建
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), StartActivity.class));//指定快捷方式的启动对象StartActivity
        android.content.Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ecmoban_logo);// 快捷方式的图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut); // 发出广播
        LG.i("图标已创建");
    }


    protected void onDestroy(){
        super.onDestroy();
        if(shared.getBoolean("isSettingGo",true)){
            editor.putBoolean("isSettingGo", false);
            editor.commit();
        }
    }
}
