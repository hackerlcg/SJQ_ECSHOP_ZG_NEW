package com.ecjia.component.network.model;


import android.content.Context;
import android.text.TextUtils;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.ACT;
import com.ecjia.hamster.model.BRAND;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.FAVOUR_INFO;
import com.ecjia.hamster.model.FAVOUR_LIST;
import com.ecjia.hamster.model.GIFT;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.USERRANK;
import com.ecjia.util.HanziToPinyin;
import com.ecjia.util.LG;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FavourModel extends BaseModel {

    public FAVOUR_INFO favour_info = new FAVOUR_INFO();
    public ArrayList<USERRANK> userranks = new ArrayList<USERRANK>();
    public ArrayList<BRAND> brands = new ArrayList<BRAND>();
    public ArrayList<CATEGORY> categories = new ArrayList<CATEGORY>();
    public ArrayList<GOODS> goodses = new ArrayList<GOODS>();
    public PAGINATED paginated;
    public ArrayList<FAVOUR_LIST> favour_lists=new ArrayList<FAVOUR_LIST>();

    public static final int PAGE_COUNT = 10;
    HanziToPinyin toPinyin = HanziToPinyin.getInstance();

	public FavourModel(Context context)
	{
		super(context);
	}
    public void getUserRank(String api){
        final String url = ProtocolConst.ADMIN_USER_RANK;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");

                                userranks.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    userranks.clear();
                                    userranks.add(new USERRANK(0,"非会员",false));
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        USERRANK userrank = USERRANK.fromJson(listJsonObject);
                                        userranks.add(userrank);
                                    }
                                }

                            }
                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void getFavourInfo(int act_id,String api){
        final String url = ProtocolConst.ADMIN_FAV_INFO;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("act_id",act_id);
            requestJsonObject.put("token",sid);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                favour_info = FAVOUR_INFO.fromJson(data);
                                switch (favour_info.getAct_range()){
                                  case 1:
                                      categories.clear();
                                      for(int i=0;i<favour_info.getAct_range_ext().size();i++){
                                          ACT temp=favour_info.getAct_range_ext().get(i);
                                          CATEGORY category=new CATEGORY();
                                          category.setCat_id(temp.getId());
                                          category.setCat_name(temp.getName());
                                          category.setPinyin(toPinyin.getPinYin(temp.getName()));
                                          categories.add(category);
                                      }

                                      addParent();

                                      Collections.sort(categories, comparator);
                                  break;
                                  case 2:
                                      brands.clear();
                                      for(int i=0;i<favour_info.getAct_range_ext().size();i++){
                                          ACT temp=favour_info.getAct_range_ext().get(i);
                                          BRAND brand=new BRAND();
                                          brand.setBrand_id(temp.getId());
                                          brand.setBrand_name(temp.getName());
                                          brand.setPinyin(toPinyin.getPinYin(temp.getName()));
                                          brands.add(brand);
                                      }
                                      Collections.sort(brands, comparator2);
                                  break;
                                  case 3:
                                      goodses.clear();
                                      for(int i=0;i<favour_info.getAct_range_ext().size();i++){
                                          ACT temp=favour_info.getAct_range_ext().get(i);
                                          GOODS goods=new GOODS();
                                          goods.setId(temp.getId()+"");
                                          goods.setName(temp.getName());
                                          goods.setImage(temp.getImage());
                                          goodses.add(goods);
                                      }
                                  break;
                                }
                            }

                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    private void addParent() {
        String category=shared.getString("category","");
        if(!TextUtils.isEmpty(category)){
            JSONObject jo=null;
            JSONArray listJsonArray = null;
            try {
                jo=new JSONObject(category);
                listJsonArray = jo.optJSONArray("category");
                if (null != listJsonArray && listJsonArray.length() > 0)
                {
                    for (int i = 0; i < listJsonArray.length(); i++)
                    {
                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                        CATEGORY item = CATEGORY.fromJson(listJsonObject);
                        for(int j=0;j<categories.size();j++){
                            if(categories.get(j).getCat_id()==item.getCat_id()){
                                categories.get(j).setAll_parent(item.getAll_parent());
                                break;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }


    public void getFavList( String status,String api, final boolean isfalse){
        //活动状态（coming：未开始，going：正在进行中，finished：已结束）
        final String url = ProtocolConst.ADMIN_FAV_LIST;
        if(isfalse){
            pd.show();
        }

        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("token",sid);
            requestJsonObject.put("status",status);
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(isfalse){
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                        }
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                favour_lists.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    favour_lists.clear();
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        FAVOUR_LIST favour_list = FAVOUR_LIST.fromJson(listJsonObject);
                                        favour_lists.add(favour_list);
                                    }
                                }

                            }
                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void getFavListMore(String status,String api){
        final String url = ProtocolConst.ADMIN_FAV_LIST;
        //  pd.show();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int)Math.ceil((double)favour_lists.size()*1.0/PAGE_COUNT)+1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("token",sid);
            requestJsonObject.put("status",status);
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        //   pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    favour_lists.clear();
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        FAVOUR_LIST favour_list = FAVOUR_LIST.fromJson(listJsonObject);
                                        favour_lists.add(favour_list);
                                    }
                                }
                            }
                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }


    public void addFavour(String act_name,String start_time,String end_time,String user_rank,int act_range,String act_range_ext,
                          String min_amount,String max_amount,int act_type,String act_type_ext,ArrayList<GIFT> gift,String api){
        final String url = ProtocolConst.ADMIN_FAV_ADD;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("act_name",act_name);
            requestJsonObject.put("start_time",start_time);
            requestJsonObject.put("end_time",end_time);
            requestJsonObject.put("user_rank",user_rank);   //优惠活动范围，会员等级id拼接，如1,2,3
            requestJsonObject.put("act_range",act_range);   //优惠活动范围 （0：全部商品、1：指定分类、2：指定品牌、3：指定商品）
            requestJsonObject.put("act_range_ext",act_range_ext);  //优惠活动范围，商品、品牌、商品的id拼接，如1,2,3
            requestJsonObject.put("min_amount",min_amount);
            requestJsonObject.put("max_amount",max_amount);
            if(act_type!=-1){
                requestJsonObject.put("act_type",act_type);
                requestJsonObject.put("act_type_ext",act_type_ext);
                if(gift.size()>0){
                    JSONArray itemJSONArray1 = new JSONArray();
                    for(int i =0; i< gift.size(); i++)
                    {
                        GIFT itemData =gift.get(i);
                        JSONObject itemJSONObject = itemData.toJson();
                        itemJSONArray1.put(itemJSONObject);
                    }
                    requestJsonObject.put("gift", itemJSONArray1);
                }
            }



        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void updateFavour(int act_id,String act_name,String start_time,String end_time,String user_rank,int act_range,String act_range_ext,
                          String min_amount,String max_amount,int act_type,String act_type_ext,ArrayList<GIFT> gift,String api){
        final String url = ProtocolConst.ADMIN_FAV_UPDATE;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("act_id",act_id);
            requestJsonObject.put("act_name",act_name);
            requestJsonObject.put("start_time",start_time);
            requestJsonObject.put("end_time",end_time);
            requestJsonObject.put("user_rank",user_rank);   //优惠活动范围，会员等级id拼接，如1,2,3
            requestJsonObject.put("act_range",act_range);   //优惠活动范围 （0：全部商品、1：指定分类、2：指定品牌、3：指定商品）
            requestJsonObject.put("act_range_ext",act_range_ext);  //优惠活动范围，商品、品牌、商品的id拼接，如1,2,3
            requestJsonObject.put("min_amount",min_amount);
            requestJsonObject.put("max_amount",max_amount);
            requestJsonObject.put("act_type",act_type);
            requestJsonObject.put("act_type_ext",act_type_ext);

            if(gift.size()>0){
                JSONArray itemJSONArray1 = new JSONArray();
                for(int i =0; i< gift.size(); i++)
                {
                    GIFT itemData =gift.get(i);
                    JSONObject itemJSONObject = itemData.toJson();
                    itemJSONArray1.put(itemJSONObject);
                }
                requestJsonObject.put("gift", itemJSONArray1);
            }

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }


    public void delFavour(int act_id,String api){
        final String url = ProtocolConst.ADMIN_FAV_DELETE;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("act_id",act_id);

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            FavourModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    Comparator comparator = new Comparator<CATEGORY>() {
        @Override
        public int compare(CATEGORY lhs, CATEGORY rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }

        }
    };

    Comparator comparator2 = new Comparator<BRAND>() {
        @Override
        public int compare(BRAND lhs, BRAND rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }

        }
    };


}
