package com.ecjia.component.network.model;


import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategoryModel extends BaseModel {

    private String pkName;
    private String rootpath;
    public ArrayList<CATEGORY> categories = new ArrayList<CATEGORY>();

    public MyProgressDialog pd;

    private final static  String DATANAME="CategoryData";

    HanziToPinyin toPinyin = HanziToPinyin.getInstance();

	public CategoryModel(Context context)
	{
		super(context);
        pkName = mContext.getPackageName();
        rootpath = context.getCacheDir() + "/ECJiaSK/cache";//获取缓存信息
        pd= MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));

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

    public void readHomeDataCache() {

        String path = rootpath + "/" + pkName + "/"+DATANAME+".dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                categoryDataCache(bf.readLine());

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void categoryDataCache(String result) {

        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                    JSONArray categorygoodJsonArray = jsonObject.optJSONArray("category");
                    if (null != categorygoodJsonArray && categorygoodJsonArray.length() > 0) {
                        categories.clear();
                        for (int i = 0; i < categorygoodJsonArray.length(); i++) {
                            JSONObject categoryGoodsJsonObject = categorygoodJsonArray.getJSONObject(i);
                            CATEGORY item = CATEGORY.fromJson(categoryGoodsJsonObject);
                            categories.add(item);
                        }
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 缓存数据
    private PrintStream ps = null;

    public void fileSave(String result, String name) {

        String path = rootpath + "/" + pkName;

        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File file = new File(filePath + "/" + name + ".dat");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            ps.print(result);
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getGoodsCategory(SESSION session,String api){
       final String url = ProtocolConst.GOODSCATEGORY;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("device", device.toJson());

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                categories.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        CATEGORY item = CATEGORY.fromJson(listJsonObject);
                                        item.setPinyin(toPinyin.getPinYin(item.getCat_name()));
                                        categories.add(item);
                                    }
                                    Collections.sort(categories, comparator);
                                }


                                for(int i = 0; i < categories.size(); i++){
                                    StringBuffer temprange=new StringBuffer();
                                    CATEGORY category=categories.get(i);
                                        while (category.getParent_id()!=0){
                                            category=getParent(category);
                                            temprange.insert(0,("->"+category.getCat_name()));
                                        }
                                        if(!TextUtils.isEmpty(temprange.toString())){
                                            temprange.delete(0,2);
                                        }
                                        categories.get(i).setAll_parent(temprange.toString());
                                    }

                                if(categories.size()>0) {
                                    JSONObject requestJsonObject = new JSONObject();
                                    try {
                                        JSONArray itemJSONArray1 = new JSONArray();
                                        for (int i = 0; i < categories.size(); i++) {
                                            CATEGORY itemData = categories.get(i);
                                            JSONObject itemJSONObject = itemData.toJson();
                                            itemJSONArray1.put(itemJSONObject);
                                        }
                                        requestJsonObject.put("category", itemJSONArray1);
                                    } catch (JSONException e) {
                                        // TODO: handle exception
                                    }
                                    editor.putString("category",requestJsonObject.toString());
                                    editor.commit();
                                    fileSave(requestJsonObject.toString(),DATANAME);
                                }


                            }
                            pd.dismiss();
                            CategoryModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }


    public CATEGORY getParent(CATEGORY category){
        for (int i=0;i<categories.size();i++){
            if(categories.get(i).getCat_id()==category.getParent_id()){
                return  categories.get(i);
            }
        }
        return category;
    }


}
