package com.ecjia.component.network.API;

import com.ecjia.component.network.base.BaseRes;
import com.ecjia.component.network.responsmodel.goodinfo.GoodDescImage;
import com.ecjia.component.network.responsmodel.goodinfo.GoodInfoBase;
import com.ecjia.component.network.responsmodel.goodinfo.GoodInfoImage;
import com.ecjia.entity.Species;
import com.gear.apifinder.annotation.APIService;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * ecjia-shopkeeper-android  Good相关
 * Created by YichenZ on 2017/3/17 09:46.
 */
@APIService
public interface APIPush {

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/goods/attribute")
    Flowable<BaseRes<Species>> getAttribute(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/goods/info")//获取商品详情
    Flowable<BaseRes<GoodInfoBase>> getGoodDetails(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/goods/upload")//上传商品详情页面图片
    Flowable<BaseRes<GoodInfoImage>> uploadGoodDetailsImg(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/goods/upload_file")//上传商品详情页面 商品描述图片
    Flowable<BaseRes<GoodDescImage>> uploudGoodDescImg(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/goods/update")//商品的添加和编辑
    Flowable<BaseRes<GoodInfoImage>> putGoodsInfo(@Field("json") String json);

}
