package com.ecjia.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/21 16:23.
 */

public class Species implements Serializable {

    @SerializedName("spec")
    private List<SpeciBean> speci;

    public List<SpeciBean> getSpeci() {
        return speci;
    }

    public void setSpeci(List<SpeciBean> speci) {
        this.speci = speci;
    }

    public static class SpeciBean implements Serializable {
        /**
         * cat_id : 1
         * cat_name : 女装
         * cat : [{"attr_id":81,"attr_name":"颜色","type":"1","values":["红","黄","蓝"]},{"attr_id":82,"attr_name":"尺寸","type":"1","values":["S","M","L","XL","XXL"]}]
         */

        @SerializedName("cat_id")
        private String catId;
        @SerializedName("cat_name")
        private String catName;
        @SerializedName("cat_arr")
        private List<CatBean> cat;

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public List<CatBean> getCat() {
            return cat;
        }

        public void setCat(List<CatBean> cat) {
            this.cat = cat;
        }

        public static class CatBean implements Serializable {
            /**
             * attr_id : 81
             * attr_name : 颜色
             * type : 1
             * values : ["红","黄","蓝"]
             */

            @SerializedName("attr_id")
            private String attrId;
            @SerializedName("attr_name")
            private String attrName;
            @SerializedName("type")
            private String type;
            @SerializedName("attr_input_type")
            private String input_type;
            @SerializedName("values")
            private List<Value> values;


            public String getAttrId() {
                return attrId;
            }

            public void setAttrId(String attrId) {
                this.attrId = attrId;
            }

            public String getAttrName() {
                return attrName;
            }

            public void setAttrName(String attrName) {
                this.attrName = attrName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Value> getValues() {
                return values;
            }

            public void setValues(List<Value> values) {
                this.values = values;
            }

            public String getInput_type() {
                return input_type;
            }

            public void setInput_type(String input_type) {
                this.input_type = input_type;
            }

            public static class Value implements Serializable {
                @SerializedName("value")
                String value;
                @SerializedName("value_show")
                String value_show;
                @SerializedName("vid")
                String valueId;

                public Value(){}

                public Value(String value) {
                    this.value = value;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getValue_show() {
                    return value_show;
                }

                public void setValue_show(String value_show) {
                    this.value_show = value_show;
                }

                public String getValueId() {
                    return valueId;
                }

                public void setValueId(String valueId) {
                    this.valueId = valueId;
                }
            }
        }
    }
}
