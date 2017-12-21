package com.ecjia.component.network.API;

import com.ecjia.component.network.base.BaseRes;
import com.ecjia.entity.Assets;
import com.ecjia.entity.Bank;
import com.ecjia.entity.BusinessDeal;
import com.gear.apifinder.annotation.APIService;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/17 09:46.
 */
@APIService
public interface APIAssets {

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/account/bank")
    Flowable<BaseRes<List<Bank>>> getBanks(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/account/info")
    Flowable<BaseRes<Assets>> getAssets(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/account/log")
    Flowable<BaseRes<List<BusinessDeal>>> getBusinessDeal(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/account/update")
    Flowable<BaseRes<List<String>>> putAssets(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/account/withdraw")
    Flowable<BaseRes<List<String>>> putwithdraw(@Field("json") String json);

}
