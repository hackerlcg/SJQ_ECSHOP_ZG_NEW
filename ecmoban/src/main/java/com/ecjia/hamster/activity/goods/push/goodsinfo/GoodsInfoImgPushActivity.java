package com.ecjia.hamster.activity.goods.push.goodsinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.component.network.base.BaseRes;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.query.GoodQuery;
import com.ecjia.component.network.responsmodel.goodinfo.GoodDescImage;
import com.ecjia.hamster.activity.goods.ReleaseGoodActivity;
import com.ecjia.util.common.Base64;
import com.ecjia.util.common.BitmapUtils;
import com.ecjia.util.common.BottomChoosePhotoUtils;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.gallery.GalleryImageUtils;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-02.
 */

public class GoodsInfoImgPushActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;
    @BindView(R.id.noScrollgridview)
    GridView noScrollgridview;

    private GoodPushImgGridAdapter imgGridAdapter;
    private ArrayList<GoodDescImage> imgPathList;
    private ArrayList<GoodDescImage> uploudImgList = new ArrayList<>();

    private int current = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_goodsinfo_imgpush;
    }

    @Override
    public void init(Bundle savedInstanceState) {
//        RxBus.getInstance().register(this);
        GalleryImageUtils.configGallerySelect(mActivity);
        top_view_text.setText("详情");
        top_right_tv.setText("保存");
        imgPathList = getIntent().getParcelableArrayListExtra("GoodInfoImgList");
        if (imgPathList == null) {
            imgPathList = new ArrayList<>();
        }
        imgGridAdapter = new GoodPushImgGridAdapter(mActivity, imgPathList);
        imgGridAdapter.update();
        noScrollgridview.setAdapter(imgGridAdapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgPathList.size()) {
                    //如果点击了"+"号；弹框添加图片
                    setChooesImg(200000);
                }
            }
        });
    }

    private void setChooesImg(int requestPostion) {
        BottomChoosePhotoUtils.newInstance().show(getSupportFragmentManager(),9, new BottomChoosePhotoUtils.OnChoosePhoneResult() {
            @Override
            public void onHandlerSuccess(List<PhotoInfo> resultList, int requestPostion) {
                if (requestPostion == 200000) {
                    for (PhotoInfo img : resultList) {
                        GoodDescImage image = new GoodDescImage();
                        image.setFile_id("0");
                        image.setFile_path(img.getPhotoPath());
                        imgPathList.add(image);
                    }
                }
                imgGridAdapter.update();
            }

            @Override
            public void onHandlerFailure(String errorMsg, int requestPostion) {

            }
        },requestPostion);
    }

    @OnClick({R.id.top_view_back, R.id.top_right_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view_back:
                DialogUtils.showDialog(mActivity, "提示", "填写资料未保存,是否退出", new DialogUtils.ButtonClickListener() {
                    @Override
                    public void negativeButton() {//取消
                    }

                    @Override
                    public void positiveButton() {//确定
                        finish();
                    }
                });
                break;
            case R.id.top_right_tv:
                DialogUtils.showDialog(mActivity, "提示", "确认无误,是否保存当前详情页图片", new DialogUtils.ButtonClickListener() {
                    @Override
                    public void negativeButton() {//取消
                    }

                    @Override
                    public void positiveButton() {//确定
                        upload();
                    }
                });
                break;
        }
    }

    public void upload() {
        uploudImgList.clear();
        for (int i = 0; i < imgPathList.size(); i++) {
            if ("0".equals(imgPathList.get(i).getFile_id())) {
                uploudImgList.add(imgPathList.get(i));
            }
        }
        current = 1;
        ToastUtil.getInstance().makeLongToast(mActivity, "正在上传图片 第" + current + "张");
        ProgressDialogUtil.getInstance().build(mActivity).show();
        Flowable.fromIterable(uploudImgList)
                .filter(s -> !TextUtils.isEmpty(s.getFile_path()))
                .observeOn(Schedulers.io())
                .flatMap(new Function<GoodDescImage, Publisher<BaseRes<GoodDescImage>>>() {
                    @Override
                    public Publisher<BaseRes<GoodDescImage>> apply(GoodDescImage goodInfoImage) {
                        return RetrofitAPIManager.getAPIPush().uploudGoodDescImg(GoodQuery.getInstance().uploudGoodDescImg(Base64.encode(BitmapUtils.getImage(mActivity, goodInfoImage.getFile_path(), 300))))
                                .compose(liToDestroy());
                    }
                })
                .onErrorReturn(throwable -> null)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(liToDestroy())
                .subscribeWith(new ResourceSubscriber<BaseRes<GoodDescImage>>() {
                    @Override
                    public void onNext(BaseRes<GoodDescImage> baseRes) {
                        if (baseRes.getStatus().getSucceed() != 1) {
                            ToastUtil.getInstance().makeLongToast(mActivity, "上传图片失败");
                            return;
                        } else {
                            GoodDescImage img = baseRes.getData();
                            for (int i = 0; i < imgPathList.size(); i++) {
                                if ("0".equals(imgPathList.get(i).getFile_id())) {
                                    imgPathList.get(i).setFile_id(img.getFile_id());
                                    imgPathList.get(i).setFile_path(img.getFile_path());
                                    break;
                                }
                            }
                            ToastUtil.getInstance().makeLongToast(mActivity, "正在上传图片 第" + ++current + "张");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        //上传商品所有信息
                        saveImg();
                    }
                });
    }

    private void saveImg() {
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < imgPathList.size(); i++) {
//            stringBuffer.append(imgPathList.get(i).getFile_id() + ",");
//        }
//        RxBus.getInstance().post(ReleaseGoodActivity.IN_INFO, stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
        RxBus.getInstance().post(ReleaseGoodActivity.IN_INFO_IMG, imgPathList);
        ProgressDialogUtil.getInstance().build(mActivity).dismiss();
        finish();
    }

    @Override
    public void dispose() {

    }
}
