package com.ecjia.hamster.activity.goods;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.component.network.base.BaseRes;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.query.GoodQuery;
import com.ecjia.component.network.responsmodel.goodinfo.GoodDescImage;
import com.ecjia.component.network.responsmodel.goodinfo.GoodInfoBase;
import com.ecjia.component.network.responsmodel.goodinfo.GoodInfoDetail;
import com.ecjia.component.network.responsmodel.goodinfo.GoodInfoImage;
import com.ecjia.entity.Species;
import com.ecjia.entity.ValuePrice;
import com.ecjia.hamster.activity.goods.push.goodsinfo.GoodsInfoImgPushActivity;
import com.ecjia.hamster.activity.goods.push.goodsinfo.GoodsInfoPushActivity;
import com.ecjia.hamster.activity.goods.push.goodsinfo.GoodsInfoPushActivity_Builder;
import com.ecjia.hamster.activity.goods.push.goodstype.GoodsTypePushActivity;
import com.ecjia.hamster.activity.goods.push.setprice.SetPriceActivity;
import com.ecjia.hamster.activity.goods.push.type.GoodsPushTypeActivity;
import com.ecjia.hamster.adapter.ReleaseGoodImgGridAdapter;
import com.ecjia.util.StringSpeciBeanUtil;
import com.ecjia.util.common.Base64;
import com.ecjia.util.common.BitmapUtils;
import com.ecjia.util.common.BottomChoosePhotoUtils;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.common.GsonUtils;
import com.ecjia.util.gallery.GalleryImageUtils;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.google.gson.Gson;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：s商品发布页面
 * Created by sun
 * Created time 2017-03-21.
 */

public class ReleaseGoodActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;

    @BindView(R.id.tv_good_title)
    EditText tv_good_title;//商品描述
    @BindView(R.id.edit_good_num)
    EditText edit_good_num;//商品货号
    @BindView(R.id.edit_good_weight)
    EditText edit_good_weight;//商品重量

    @BindView(R.id.tv_good_type)
    TextView tv_good_type;//商品分类 显示
    @BindView(R.id.tv_good_sizecolor)
    TextView tv_good_sizecolor;//商品规格 显示
    @BindView(R.id.tv_good_price)
    TextView tv_good_price;//商品价格 显示
    @BindView(R.id.cb_good_haveshippingfee)
    CheckBox cb_good_haveshippingfee;//商品是否包邮 显示

    @BindView(R.id.edit_good_totalcount)
    EditText edit_good_totalcount;//商品规格库存
    @BindView(R.id.tv_good_info)
    TextView tv_good_info;//商品详情描述 显示

    @BindView(R.id.noScrollgridview)
    GridView noScrollgridview;

    private ReleaseGoodImgGridAdapter imgGridAdapter;
    private List<GoodInfoImage> imgPathList = new ArrayList<>();
    private List<GoodInfoImage> uploudImgList = new ArrayList<>();
    private GoodInfoDetail goodInfoData;

    @Extra
    String goodId;

    private int current = 0;
    private String imgHtmlSt = "";
    private String infoHtmlSt = "";


    @Override
    public int getLayoutId() {
        return R.layout.act_release_good;
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);
        RxBus.getInstance().register(this);
        GalleryImageUtils.configGallery(mActivity);
        if (TextUtils.isEmpty(goodId)) {
            finish();
            return;
        }
        if ("0".equals(goodId)) {
            top_view_text.setText("新增商品");
            goodInfoData = new GoodInfoDetail();
        } else {
            top_view_text.setText("修改商品");
            getGoodDetails();
        }
        imgGridAdapter = new ReleaseGoodImgGridAdapter(mActivity, imgPathList);
        imgGridAdapter.update();
        noScrollgridview.setAdapter(imgGridAdapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgPathList.size()) {
                    //如果点击了"+"号；弹框添加图片
                    setChooesImg(200000);
                } else {
                    setChooesImg(position + 1000);
//                    ToastUtil.getInstance().makeLongToast(mActivity, "position+" + position);
                }
            }
        });
    }

    private void initEditGoodsInfoView() {
        tv_good_title.setText(goodInfoData.getGoods_name() + "");
        edit_good_num.setText(goodInfoData.getGoods_sn() + "");
        edit_good_weight.setText(goodInfoData.getGoods_weight() + "");
        tv_good_type.setText(goodInfoData.getCat_str() + "");

        tv_good_sizecolor.setText(StringSpeciBeanUtil.getSpecString(goodInfoData.getSpec().getCat()));
        tv_good_price.setText(goodInfoData.getShop_price() + "");
        cb_good_haveshippingfee.setChecked("1".equals(goodInfoData.getIs_shipping()) ? true : false);
        edit_good_totalcount.setText(goodInfoData.getGoods_number() + "");
        tv_good_info.setText(goodInfoData.getGoods_desc() + "");
    }

    @OnClick({R.id.top_view_back, R.id.btn_left, R.id.btn_right, R.id.ll_good_type, R.id.ll_good_sizecolor,
            R.id.ll_good_info, R.id.ll_good_price, R.id.ll_good_info_img})
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
            case R.id.btn_left://直接上架
                chekInputValues("1");
                break;
            case R.id.btn_right://
                chekInputValues("0");
                break;
            case R.id.ll_good_type://分类
                startActivity(new Intent(this, GoodsPushTypeActivity.class));
                break;
            case R.id.ll_good_sizecolor://规格
                startActivity(new Intent(this, GoodsTypePushActivity.class));
                break;
            case R.id.ll_good_info://详情
                startActivity(new Intent(this, GoodsInfoPushActivity.class));
                GoodsInfoPushActivity_Builder.intent(mActivity).infoStr(goodInfoData.getGoods_desc());
                break;
            case R.id.ll_good_price://价格
                Intent intent = new Intent(mActivity, SetPriceActivity.class);
                if (!"0".equals(goodId)) {
                    intent.putParcelableArrayListExtra("GoodPriceList", goodInfoData.getVolume_priceArray());
                    intent.putExtra("price", goodInfoData.getShop_price());
                }
                startActivity(intent);
                //SetPriceActivity_Builder.intent(mActivity).data(goodInfoData.getVolume_priceArray()).price(goodInfoData.getShop_price()).start();
                break;
            case R.id.ll_good_info_img://
                Intent intentImg = new Intent(mActivity, GoodsInfoImgPushActivity.class);
//                if (!"0".equals(goodId)) {
                intentImg.putParcelableArrayListExtra("GoodInfoImgList", goodInfoData.getFile_arr());
//                }
                startActivity(intentImg);
                //SetPriceActivity_Builder.intent(mActivity).data(goodInfoData.getVolume_priceArray()).price(goodInfoData.getShop_price()).start();
                break;
        }
    }

    private void chekInputValues(String isSale) {//1 上架   0  放到仓库
        if (imgPathList.size() <= 0) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请选择商品图片");
            return;
        }
        String titleStr = tv_good_title.getText().toString();
        if (TextUtils.isEmpty(titleStr)) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请填写商品名称");
            return;
        }
//                String  good_numStr=edit_good_num.getText().toString();
//                if(TextUtils.isEmpty(good_numStr)){
//                    ToastUtil.getInstance().makeLongToast(mActivity,"请填写商品货号");
//                    return;
//                }
        String good_weightStr = edit_good_weight.getText().toString();
        if (TextUtils.isEmpty(good_weightStr)) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请填写商品重量");
            return;
        }
        String good_typeStr = tv_good_type.getText().toString();
        if (TextUtils.isEmpty(good_typeStr)) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请选择商品分类");
            return;
        }
        String good_sizecolorStr = tv_good_sizecolor.getText().toString();
        if (TextUtils.isEmpty(good_sizecolorStr)) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请选择商品规格");
            return;
        }
        String good_priceStr = tv_good_price.getText().toString();
        if (TextUtils.isEmpty(good_priceStr)) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请设置商品价格");
            return;
        }
        String good_totalcountStr = edit_good_totalcount.getText().toString();
        if (TextUtils.isEmpty(good_totalcountStr)||Integer.parseInt(good_totalcountStr)<=0) {
            ToastUtil.getInstance().makeLongToast(mActivity, "请设置商品每个规格的平均库存");
            return;
        }
//                String  good_infoStr=tv_good_info.getText().toString();
//                if(TextUtils.isEmpty(good_infoStr)){
//                    ToastUtil.getInstance().makeLongToast(mActivity,"请设置商品详细描述");
//                    return;
//                }

        DialogUtils.showDialog(mActivity, "提示", "是否上传商品，并把商品" + ("1".equals(isSale) ? "上架" : "放入仓库"), new DialogUtils.ButtonClickListener() {
            @Override
            public void negativeButton() {//取消
            }

            @Override
            public void positiveButton() {//确定
                goodInfoData.setIs_on_sale(isSale);
                upload();
            }
        });
    }

    private void getGoodDetails() {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIPush()
                .getGoodDetails(GoodQuery.getInstance().getGoodDetails(goodId))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<GoodInfoBase>() {

                    @Override
                    public void onNext(GoodInfoBase goodInfoBase) {
                        ProgressDialogUtil.getInstance().dismiss();
                        imgPathList.clear();
                        goodInfoData = goodInfoBase.getGoods_info();
                        goodInfoData.setGoods_desc("");
                        imgPathList.addAll(goodInfoData.getImg_arr());
//                        Collections.reverse(imgPathList);
                        imgGridAdapter.notifyDataSetChanged();
                        initEditGoodsInfoView();
                    }

                    @Override
                    public void onError(Throwable t) {
                        ProgressDialogUtil.getInstance().dismiss();
                        ToastUtil.getInstance().makeLongToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setChooesImg(int requestPostion) {
        BottomChoosePhotoUtils.newInstance().show(getSupportFragmentManager(), new BottomChoosePhotoUtils.OnChoosePhoneResult() {
            @Override
            public void onHandlerSuccess(List<PhotoInfo> resultList, int requestPostion) {
                if (requestPostion == 200000) {
                    GoodInfoImage image = new GoodInfoImage();
                    image.setImg_id("0");
                    image.setUrl(resultList.get(0).getPhotoPath());
//                    if (imgPathList.size() <= 0) {
                    imgPathList.add(image);
//                    } else {
//                        imgPathList.add(image);
//                    }
                } else {
                    GoodInfoImage image = imgPathList.get(requestPostion - 1000);
                    image.setImg_id("0");
                    image.setUrl(resultList.get(0).getPhotoPath());
                    imgPathList.set(requestPostion - 1000, image);
                }
                imgGridAdapter.update();
            }

            @Override
            public void onHandlerFailure(String errorMsg, int requestPostion) {

            }
        }, requestPostion);
    }

    public void upload() {
        uploudImgList.clear();
        for (int i = 0; i < imgPathList.size(); i++) {
            imgPathList.get(i).setOrder(i);
            if ("0".equals(imgPathList.get(i).getImg_id())) {
                uploudImgList.add(imgPathList.get(i));
            }
        }
        current = 1;
        ToastUtil.getInstance().makeLongToast(mActivity, "正在上传图片 第" + current + "张");
        ProgressDialogUtil.getInstance().build(mActivity).show();
        Flowable.fromIterable(uploudImgList)
                .filter(s -> !TextUtils.isEmpty(s.getUrl()))
                .observeOn(Schedulers.io())
                .flatMap(new Function<GoodInfoImage, Publisher<BaseRes<GoodInfoImage>>>() {
                    @Override
                    public Publisher<BaseRes<GoodInfoImage>> apply(GoodInfoImage goodInfoImage) {
                        return RetrofitAPIManager.getAPIPush()
                                .uploadGoodDetailsImg(GoodQuery.getInstance()
                                        .uploudGoodDetailsImg(Base64.encode(BitmapUtils.getImage(mActivity,
                                                goodInfoImage.getUrl(), 300)), goodInfoImage.getOrder() + ""))
                                .compose(liToDestroy());
                    }
                })
                .onErrorReturn(throwable -> null)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(liToDestroy())
                .subscribeWith(new ResourceSubscriber<BaseRes<GoodInfoImage>>() {
                    @Override
                    public void onNext(BaseRes<GoodInfoImage> baseRes) {
                        if (baseRes.getStatus().getSucceed() != 1) {
                            ToastUtil.getInstance().makeLongToast(mActivity, "上传图片失败");
                            return;
                        } else {
                            GoodInfoImage img = baseRes.getData();
                            imgPathList.get(img.getOrder()).setImg_id(baseRes.getData().getImg_id());
                            //imgPathList.get(img.getOrder()).setUrl("");
                            ToastUtil.getInstance().makeLongToast(mActivity, "正在上传图片 第" + ++current + "张");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        //上传商品所有信息
                        submitGoodInfo();
                    }
                });
    }

    /**
     * 上传数据信息
     */
    private void submitGoodInfo() {
        //设置数据
        goodInfoData.setInfo();//setSession
        goodInfoData.setImg_arr(imgPathList);//设置图片
        goodInfoData.setGoods_name(tv_good_title.getText().toString().trim());//名称
        goodInfoData.setGoods_sn(edit_good_num.getText().toString().trim());//货号
        goodInfoData.setGoods_weight(edit_good_weight.getText().toString().trim());//重量
        goodInfoData.setIs_shipping(String.valueOf(cb_good_haveshippingfee.isChecked()));//是否包邮
        goodInfoData.setGoods_number(edit_good_totalcount.getText().toString().trim());//是否包邮
        if (goodInfoData.getFile_arr() == null) {

        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < goodInfoData.getFile_arr().size(); i++) {
                stringBuffer.append(goodInfoData.getFile_arr().get(i).getFile_id() + ",");
            }
            goodInfoData.setFiles_list(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
        }
//        goodInfoData.setIs_on_sale("1");//TODO：这里为了测试写死了，后面要根据点击的按钮更改
        Gson gson = new Gson();
        RetrofitAPIManager.getAPIPush()
//                .putGoodsInfo(JsonUtil.toHttpGsonString(goodInfoData))//这里要使用Gson,不然反序列化后json参数不对
                .putGoodsInfo(GsonUtils.getInstance().toHttpGsonString(goodInfoData))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> success(), e -> showError(e));
    }

    public void success() {
        ToastUtil.getInstance().makeShortToast(this, "上传成功");
        finish();
    }

    public void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    public static final int IN_CAT = 10;

    /**
     * 设置分类
     *
     * @param id 分类id
     */
    @Subscribe(tag = IN_CAT)
    public void setCat(int id) {
        goodInfoData.setCat_id(String.valueOf(id));
    }

    public static final int IN_CAT_STR = 11;

    /**
     * 显示分类
     *
     * @param str 分类字符串
     */
    @Subscribe(tag = IN_CAT_STR)
    public void showCatStr(String str) {
        tv_good_type.setText(str);
    }

    public static final int IN_SPEC = 20;

    /**
     * 设置规格
     *
     * @param spec 规格model
     */
    @Subscribe(tag = IN_SPEC)
    public void setSpec(Species.SpeciBean spec) {
        goodInfoData.setSpec(spec);
        tv_good_sizecolor.setText(StringSpeciBeanUtil.getSpecString(spec.getCat()));
    }

    public static final int IN_PRICE = 30;

    /**
     * 设置价格并显示
     *
     * @param price 价格
     */
    @Subscribe(tag = IN_PRICE)
    public void setPrice(float price) {
        goodInfoData.setShop_price(String.valueOf(price));
        tv_good_price.setText(String.valueOf(price) + "");
    }

    public static final int IN_PRICE_VOLUME = 31;

    /**
     * 设置阶梯价格
     *
     * @param price
     */
    @Subscribe(tag = IN_PRICE_VOLUME)
    public void setPrice(List<ValuePrice> price) {
        goodInfoData.setVolume_price(price);
    }

    public static final int IN_INFO = 40;

    /**
     * 设置商品详情并显示
     *
     * @param info
     */
    @Subscribe(tag = IN_INFO)
    public void setInfo(String info) {
        goodInfoData.setGoods_desc(info);
        tv_good_info.setText(info);
    }

    public static final int IN_INFO_IMG = 41;

    /**
     * 设置商品详情图片并显示
     *
     * @param imgs
     */
    @Subscribe(tag = IN_INFO_IMG)
    public void setInfoImg(ArrayList<GoodDescImage> imgs) {
//        StringBuffer stringBuffer = new StringBuffer();
//        StringBuffer stringInfoDescBuffer = new StringBuffer();
//        for (int i = 0; i < imgs.size(); i++) {
//            stringBuffer.append(imgs.get(i).getFile_id() + ",");
//            stringInfoDescBuffer.append("<p><img src=\'’" + imgs.get(i).getFile_path() + "\'/></p>");
//        }
//        ToastUtil.getInstance().makeShortToast(mActivity, stringInfoDescBuffer.toString());
//        imgHtmlSt = stringInfoDescBuffer.toString();
        goodInfoData.setFile_arr(imgs);
//        goodInfoData.setFiles_list(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
    }

    private void isFinishDialog() {
        DialogUtils.showDialog(mActivity, "提示", "填写资料未保存,是否退出", new DialogUtils.ButtonClickListener() {
            @Override
            public void negativeButton() {//取消
            }

            @Override
            public void positiveButton() {//确定
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isFinishDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dispose() {

    }
}
