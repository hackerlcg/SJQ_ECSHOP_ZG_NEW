
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ORDER_NUM
{


    private String shipped;


    private String await_ship;


    private String await_pay;


    private String finished;

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getAwait_ship() {
        return await_ship;
    }

    public void setAwait_ship(String await_ship) {
        this.await_ship = await_ship;
    }

    public String getAwait_pay() {
        return await_pay;
    }

    public void setAwait_pay(String await_pay) {
        this.await_pay = await_pay;
    }

    public static ORDER_NUM fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        ORDER_NUM localItem = new ORDER_NUM();

        JSONArray subItemArray;

        localItem.shipped = jsonObject.optString("shipped");

        localItem.await_ship = jsonObject.optString("await_ship");

        localItem.await_pay = jsonObject.optString("await_pay");

        localItem.finished = jsonObject.optString("finished");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("shipped", shipped);
        localItemObject.put("await_ship", await_ship);
        localItemObject.put("await_pay", await_pay);
        localItemObject.put("finished", finished);
        return localItemObject;
    }

}
