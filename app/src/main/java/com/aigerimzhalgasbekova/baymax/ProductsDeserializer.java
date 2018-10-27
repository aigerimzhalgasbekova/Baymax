package com.aigerimzhalgasbekova.baymax;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by aigerimzhalgasbekova on 14/10/2018.
 */

public class ProductsDeserializer {

    protected Set<Map<String, Object>> deserialize(String json){
        Set<Map<String, Object>> productList = new LinkedHashSet<>();
        try
        {
            JSONObject obj = new JSONObject(json);
            JSONArray cdm_jArry = obj.getJSONArray("Partner's data");
            //productList = new ArrayList<HashMap<String, String>>();
            HashMap<String, Object> productInfo;

            for (int i = 0; i < cdm_jArry.length(); i++) {
                JSONObject j_inside = cdm_jArry.getJSONObject(i);
                String product = j_inside.getString("Product");
                Double price = j_inside.getDouble("Price");
                String bio = j_inside.getString("Bio");
                String discount = j_inside.getString("Discount");
                String store = j_inside.getString("Store");


                //Add your values in your `ArrayList` as below:
                productInfo = new HashMap<String, Object>();
                productInfo.put("Product", product);
                productInfo.put("Price", price);
                productInfo.put("Bio", bio);
                productInfo.put("Discount", discount);
                productInfo.put("Store", store);

                productList.add(productInfo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("check", productList.size()+"");
        return productList;
    }
}
