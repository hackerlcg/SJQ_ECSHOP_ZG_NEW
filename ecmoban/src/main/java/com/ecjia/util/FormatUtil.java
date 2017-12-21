package com.ecjia.util;

import java.text.DecimalFormat;

public class FormatUtil {

    /**
     * 小数点后
     */
    public static float getFloatTwoDecimal(float data) {
        data = (float) ((Math.round(data) * 100) / 100);
        return data;
    }

    public static String formatFloatTwoDecimal(float data) {

        String distance = "";
        distance = String.format("%.2f", data);

        return distance;
    }

    public static float formatStrToFloat(String data) {
        data=data.replace("元", "").replace("￥", "").replace("¥", "").replace(" ", "").trim();

        float result=0f;

        try {
            result=Float.valueOf(data);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return result;
    }

    public static String formatToPrice(String price) {
        String result = "";
        result = "￥" + price.replace("元", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace(" ", "") + "元";
        return result;
    }

    public static String formatToDigetPrice(String price) {
        String result = "";
        result = price.replace("元", "").replace("yuan", "").replace("¥", "").replace("￥", "").replace("Yuan", "").replace(" ", "");
        return result;
    }

    public static String stringFormatDecimal(String string) throws Exception {
        float f = Float.valueOf(string);
        return decimalFormatDecimal(f);
    }

    public static String decimalFormatDecimal(float string) throws Exception {
        DecimalFormat df = new DecimalFormat("#######0.00");
        String dff = df.format(string);
        return dff;
    }
}
