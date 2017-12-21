package com.ecjia.component.network.API;

import com.ecjia.component.network.base.BaseRes;
import com.ecjia.entity.Assets;
import com.ecjia.entity.Category;
import com.ecjia.entity.JsonRet;
import com.ecjia.entity.Update;
import com.gear.apifinder.annotation.APIService;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-01-24.
 */
@APIService
public interface APIPublic {

    /**
     * @param account     账号
     * @param passwordKey 密码
     * @return
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"appLogin")
    Flowable<BaseRes> login(@Header("drivenCode") String drivenCode, @Field("account") String account, @Field("passwordKey") String passwordKey);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"sms/validatecode")
    Flowable<BaseRes<Assets>> getSms(@Field("json") String json);

    @FormUrlEncoded
    @POST(SERVICE_TAG+"goods/category")
    Flowable<BaseRes<List<Category>>> getCategory(@Field("json") String json);

    @POST("http://www.sjq.cn/api/mj.php?")
    Flowable<JsonRet<Update>> updateFile(@Query("action") String action
            , @Query("version") String version, @Query("appType") String appType);

}
