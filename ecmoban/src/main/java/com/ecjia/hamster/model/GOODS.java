
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GOODS
{


    private String id;
    private String name;
    private String goods_name;
    private String number;
    private String number2;
    private String market_price;
    private String unformatted_market_price;
    private String shop_price;
    private String formatted_shop_price;
    private String clicks;
    private String time;
    private String category;
    private String price;
    private String gift_price;
    private String subtotal;   //总价格
    private String goods_attr;   //规格说明

    private String formatted_promote_price;

    private String promote_price;   //促销价格 （false表示没有促销价格，否则为格式化后的价格）
    private String promote_start_date; //促销开始时间
    private String formatted_promote_start_date; //促销开始时间

    private String give_integral;   //赠送消费积分数
    private String rank_integral;   //赠送等级积分数
    private String integral;   //积分购买金额

    private String promote_end_date;   //促销结束时间
    private String formatted_promote_end_date;   //促销结束时间

    private String is_promote; //是否促销 1是
    private String unformatted_promote_price;
    private String goods_desc;

    private PHOTO  img;

    private String image;

    private ArrayList<USERRANK> user_rank=new ArrayList<USERRANK>();
    private ArrayList<VOLUME> volume_number=new ArrayList<VOLUME>();

    private boolean checked;

    private String stock;

    private String goods_sn;//"ECS003380",货号

    //    @SerializedName("return")
    private String return_id;

    private String return_status;

    private String ret_id;
    private String review_content;
    private String review_status;

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getReturn_id() {
        return return_id;
    }

    public void setReturn_id(String return_id) {
        this.return_id = return_id;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getRet_id() {
        return ret_id;
    }

    public void setRet_id(String ret_id) {
        this.ret_id = ret_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGift_price() {
        return gift_price;
    }

    public void setGift_price(String gift_price) {
        this.gift_price = gift_price;
    }

    public String getFormatted_shop_price() {
        return formatted_shop_price;
    }

    public String getGive_integral() {
        return give_integral;
    }

    public void setGive_integral(String give_integral) {
        this.give_integral = give_integral;
    }

    public String getRank_integral() {
        return rank_integral;
    }

    public void setRank_integral(String rank_integral) {
        this.rank_integral = rank_integral;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public void setFormatted_shop_price(String formatted_shop_price) {
        this.formatted_shop_price = formatted_shop_price;
    }

    public String getFormatted_promote_start_date() {
        return formatted_promote_start_date;
    }

    public void setFormatted_promote_start_date(String formatted_promote_start_date) {
        this.formatted_promote_start_date = formatted_promote_start_date;
    }

    public String getFormatted_promote_end_date() {
        return formatted_promote_end_date;
    }

    public void setFormatted_promote_end_date(String formatted_promote_end_date) {
        this.formatted_promote_end_date = formatted_promote_end_date;
    }

    public String getIs_promote() {
        return is_promote;
    }

    public void setIs_promote(String is_promote) {
        this.is_promote = is_promote;
    }

    public String getUnformatted_promote_price() {
        return unformatted_promote_price;
    }

    public void setUnformatted_promote_price(String unformatted_promote_price) {
        this.unformatted_promote_price = unformatted_promote_price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getFormatted_promote_price() {
        return formatted_promote_price;
    }

    public void setFormatted_promote_price(String formatted_promote_price) {
        this.formatted_promote_price = formatted_promote_price;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public String getPromote_start_date() {
        return promote_start_date;
    }

    public void setPromote_start_date(String promote_start_date) {
        this.promote_start_date = promote_start_date;
    }

    public String getUnformatted_market_price() {
        return unformatted_market_price;
    }

    public void setUnformatted_market_price(String unformatted_market_price) {
        this.unformatted_market_price = unformatted_market_price;
    }

    public String getPromote_end_date() {
        return promote_end_date;
    }

    public void setPromote_end_date(String promote_end_date) {
        this.promote_end_date = promote_end_date;
    }

    public ArrayList<USERRANK> getUser_rank() {
        return user_rank;
    }

    public void setUser_rank(ArrayList<USERRANK> user_rank) {
        this.user_rank = user_rank;
    }

    public ArrayList<VOLUME> getVolume_number() {
        return volume_number;
    }

    public void setVolume_number(ArrayList<VOLUME> volume_number) {
        this.volume_number = volume_number;
    }

    private boolean isChoose;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(String goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PHOTO getImg() {
        return img;
    }

    public void setImg(PHOTO img) {
        this.img = img;
    }

    public String getStatusStr(){
        String str = "";
        switch (getStatusColor()){
            case 5:
                str = "无需审核";
                break;
            case 1:
                str = "未审核";
                break;
            case 2:
                str = "审核未通过";
                break;
            case 3:
            case 4:
                str = "通过";
                break;
        }
        return str;
    }

    public int getStatusColor(){
        if("".equals(review_status) || review_status == null){
            return 1;
        }
        if("1".equals(review_status)){
            return 1;
        } else if("2".equals(review_status)) {
            return 2;
        } else if("3".equals(review_status)){
            return 3;
        } else if("4".equals(review_status)){
            return 4;
        } else if("5".equals(review_status)){
            return 5;
        } else {
            return 1;
        }
    }


    public static GOODS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        GOODS   localItem = new GOODS();

        JSONArray subItemArray;

        localItem.id = jsonObject.optString("goods_id");

        localItem.name = jsonObject.optString("name");
        localItem.goods_name = jsonObject.optString("goods_name");
        localItem.number = jsonObject.optString("goods_number");
        localItem.number2 = jsonObject.optString("number");
        localItem.price = jsonObject.optString("unformatted_shop_price");
        localItem.market_price = jsonObject.optString("market_price");
        localItem.shop_price = jsonObject.optString("shop_price");
        localItem.clicks = jsonObject.optString("clicks");
        localItem.time = jsonObject.optString("last_updatetime");
        localItem.category = jsonObject.optString("category_name");
        localItem.subtotal = jsonObject.optString("subtotal");
        localItem.goods_attr = jsonObject.optString("goods_attr");
        localItem.promote_price = jsonObject.optString("promote_price");
        localItem.formatted_promote_price = jsonObject.optString("formatted_promote_price");
        localItem.promote_start_date = jsonObject.optString("promote_start_date");
        localItem.promote_end_date = jsonObject.optString("promote_end_date");
        localItem.formatted_promote_start_date = jsonObject.optString("formatted_promote_start_date");
        localItem.formatted_promote_end_date = jsonObject.optString("formatted_promote_end_date");
        localItem.unformatted_market_price = jsonObject.optString("unformatted_market_price");
        localItem.formatted_shop_price = jsonObject.optString("formated_shop_price");
        localItem.give_integral = jsonObject.optString("give_integral");
        localItem.rank_integral = jsonObject.optString("rank_integral");
        localItem.integral = jsonObject.optString("integral");
        localItem.unformatted_promote_price = jsonObject.optString("unformatted_promote_price");
        localItem.is_promote = jsonObject.optString("is_promote");
        localItem.goods_desc = jsonObject.optString("goods_desc");
        localItem.image = jsonObject.optString("image");
        localItem.stock = jsonObject.optString("stock");

        localItem.img = PHOTO.fromJson(jsonObject.optJSONObject("img"));
        localItem.goods_sn = jsonObject.optString("goods_sn");
        localItem.return_status = jsonObject.optString("return_status");
        localItem.ret_id = jsonObject.optString("ret_id");
        localItem.return_id = jsonObject.optString("return");
        localItem.review_status = jsonObject.optString("review_status");
        localItem.review_content = jsonObject.optString("review_content");
        subItemArray = jsonObject.optJSONArray("user_rank");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                USERRANK subItem = USERRANK.fromJson(subItemObject);
                localItem.user_rank.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("volume_number");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                VOLUME subItem = VOLUME.fromJson(subItemObject);
                localItem.volume_number.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray1 = new JSONArray();
        JSONArray itemJSONArray2 = new JSONArray();
        localItemObject.put("goods_id", id);
        localItemObject.put("name", name);
        localItemObject.put("goods_name", goods_name);
        localItemObject.put("goods_number", number);
        localItemObject.put("number", number2);
        localItemObject.put("market_price", market_price);
        localItemObject.put("shop_price", shop_price);
        localItemObject.put("clicks", clicks);
        localItemObject.put("last_updatetime", time);
        localItemObject.put("category_name", category);
        localItemObject.put("unformatted_shop_price", price);
        localItemObject.put("formated_shop_price", formatted_shop_price);
        localItemObject.put("subtotal", subtotal);
        localItemObject.put("goods_attr", goods_attr);
        localItemObject.put("promote_price", promote_price);
        localItemObject.put("formatted_promote_price", formatted_promote_price);
        localItemObject.put("promote_start_date", promote_start_date);
        localItemObject.put("promote_end_date", promote_end_date);
        localItemObject.put("formatted_promote_start_date", formatted_promote_start_date);
        localItemObject.put("formatted_promote_end_date", formatted_promote_end_date);
        localItemObject.put("unformatted_market_price", unformatted_market_price);
        localItemObject.put("give_integral", give_integral);
        localItemObject.put("rank_integral", rank_integral);
        localItemObject.put("integral", integral);
        localItemObject.put("unformatted_promote_price", unformatted_promote_price);
        localItemObject.put("is_promote", is_promote);
        localItemObject.put("goods_desc", goods_desc);
        localItemObject.put("image", image);
        localItemObject.put("goods_sn", goods_sn);
        localItemObject.put("return_status", return_status);
        localItemObject.put("ret_id", ret_id);
        localItemObject.put("return", return_id);
        localItemObject.put("review_content", review_content);
        localItemObject.put("review_status", review_status);
        if(null!=img)
        {
            localItemObject.put("img", img.toJson());
        }


        for(int i =0; i< user_rank.size(); i++)
        {
            USERRANK itemData =user_rank.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray1.put(itemJSONObject);
        }
        localItemObject.put("user_rank", itemJSONArray1);

        for(int i =0; i< volume_number.size(); i++)
        {
            VOLUME itemData =volume_number.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray2.put(itemJSONObject);
        }
        localItemObject.put("volume_number", itemJSONArray2);
        localItemObject.put("stock", stock);
        return localItemObject;
    }

}
