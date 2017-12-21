package com.ecjia.component.network.responsmodel.goodinfo;

import com.ecjia.component.network.requestmodel.BaseRequest;
import com.ecjia.entity.Species;
import com.ecjia.entity.ValuePrice;
import com.ecjia.util.common.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-24.
 */

public class GoodInfoDetail extends BaseRequest implements Serializable  {



    /*"goods_id":123, //商品id
            "is_on_sale":1,
            "goods_name":"质量很好的衬衫", //商品名
            "goods_sn":"ECS600078", //货号
            "goods_weight":"23.44", //重量
            "cat_str":"男装-衬衫-短袖衬衫",//类别
            "cat_id":"15",//短袖衬衫id
            "shop_price":23,//店铺
            "spec_set":1, //是否设置规格，1：已设置 0：未设置
            "price_set":1,//是否设置阶梯价 1：已设置 0：未设置
            "is_shipping":1, //是否包邮 1：包邮 0：未包邮
            "goods_number":9999,//库存
            "goods_desc":"这是商品详情" //商品详情
*/
    private String goods_id;
    private String is_on_sale;
    private String goods_name;
    private String goods_sn;//货号
    private String goods_weight;//重量
    private String cat_str;//类别
    private String cat_id;//类别 最里面id
    private String shop_price;//店铺销售价格
    private String spec_set;//是否设置规格
    private String price_set;//是否设置阶梯价
    private String is_shipping;
    private String goods_number;
    private String goods_desc;

    private List<GoodInfoImage> img_arr;//图片集合
    private List<ValuePrice> volume_price;//阶梯价
//    private List<GoodIfoSpec> spec;////商品类型
    private Species.SpeciBean spec;////商品类型

    private String files_list;

    private ArrayList<GoodDescImage> file_arr;

    public String getFiles_list() {
        return files_list;
    }

    public void setFiles_list(String files_list) {
        this.files_list = files_list;
    }

    public ArrayList<GoodDescImage> getFile_arr() {
        return file_arr;
    }

    public void setFile_arr(ArrayList<GoodDescImage> file_arr) {
        this.file_arr = file_arr;
    }

    public Species.SpeciBean getSpec() {
        return spec;
    }

    public void setSpec(Species.SpeciBean spec) {
        this.spec = spec;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getIs_on_sale() {
        return is_on_sale;
    }

    public void setIs_on_sale(String is_on_sale) {
        this.is_on_sale = is_on_sale;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getGoods_weight() {
        return goods_weight;
    }

    public void setGoods_weight(String goods_weight) {
        this.goods_weight = goods_weight;
    }

    public String getCat_str() {
        return cat_str;
    }

    public void setCat_str(String cat_str) {
        this.cat_str = cat_str;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getSpec_set() {
        return spec_set;
    }

    public void setSpec_set(String spec_set) {
        this.spec_set = spec_set;
    }

    public String getPrice_set() {
        return price_set;
    }

    public void setPrice_set(String price_set) {
        this.price_set = price_set;
    }

    public String getIs_shipping() {
        return is_shipping;
    }

    public void setIs_shipping(String is_shipping) {
        this.is_shipping = is_shipping;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public List<GoodInfoImage> getImg_arr() {
        return img_arr;
    }

    public void setImg_arr(List<GoodInfoImage> img_arr) {
        this.img_arr = img_arr;
    }

    public List<ValuePrice> getVolume_price() {
        return volume_price;
    }

    public ArrayList<ValuePrice> getVolume_priceArray() {
        ArrayList<ValuePrice> arrayList=new ArrayList<>();
        for (ValuePrice v:volume_price){
            arrayList.add(v);
        }
        return arrayList;
    }

    public void setVolume_price(List<ValuePrice> volume_price) {
        this.volume_price = volume_price;
    }

}
