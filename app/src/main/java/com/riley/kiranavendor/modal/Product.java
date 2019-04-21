package com.riley.kiranavendor.modal;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Product {

    public String uid;
    public String vendor;
    public String account_type;
    public String product_id;
    public String product_name;
    public String description;
    public String product_price;
    public String product_qty;
    public String date;
    public int purchases = 0;

    public Map<String, Boolean> purchased = new HashMap<>();

    public Product() {

    }

    public Product(String uid, String vendor, String account_type, String product_id, String product_name, String description, String product_price, String product_qty, String date) {
        this.uid = uid;
        this.vendor = vendor;
        this.account_type = account_type;
        this.product_id = product_id;
        this.product_name = product_name;
        this.description = description;
        this.product_price = product_price;
        this.product_qty = product_qty;
        this.date = date;


    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("vendor", vendor);
        result.put("account_type", account_type);
        result.put("product_id", product_id);
        result.put("product_name", product_name);
        result.put("description", description);
        result.put("product_price", product_price);
        result.put("product_qty", product_qty);
        result.put("date", date);

        //Purchases
        result.put("purchases", purchases);//number of purchases
        result.put("purchased", purchased);//bolean value true or false


        return result;
    }


}
