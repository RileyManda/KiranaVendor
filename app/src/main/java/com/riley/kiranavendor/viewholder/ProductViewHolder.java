package com.riley.kiranavendor.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riley.kiranavendor.R;
import com.riley.kiranavendor.modal.Product;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView pVendor;
    public TextView prodId;
    public TextView pName;

    public TextView vAccount;
    public TextView pDescription;
    public TextView pTime;
    //Product views
    public ImageView mCart;//cart image
    public TextView mPurchases;
    public TextView uheader;
    public TextView pPrice;
    public TextView pQty;
    //userViews
    public TextView uName;
    public TextView uId;
    public TextView uPhone;
    public ImageView hCart;



    public ProductViewHolder(View itemView) {
        super(itemView);

        //recyclerviews
        pVendor = itemView.findViewById(R.id.vendor_name);
        hCart = itemView.findViewById(R.id.mcart);
        vAccount = itemView.findViewById(R.id.accType);
        //impression views
        mCart = itemView.findViewById(R.id.prodCart);
        mPurchases = itemView.findViewById(R.id.purchase_count);
        pQty = itemView.findViewById(R.id.product_qty);
        pPrice = itemView.findViewById(R.id.product_price);
        pDescription = itemView.findViewById(R.id.prod_description);
        pName = itemView.findViewById(R.id.productName);

        prodId = itemView.findViewById(R.id.product_id);
        //pTime = itemView.findViewById(R.id.prodTime);


        //Profile Views
        uName = itemView.findViewById(R.id.user_name);
        uId = itemView.findViewById(R.id.user_id);
        uPhone = itemView.findViewById(R.id.phone_number);



    }

    public void bindToProducts(Product product, View.OnClickListener cartClickListener) {
        //Recycler View Binding
        pName.setText(product.product_name);
        prodId.setText(product.product_id);
        pQty.setText(product.product_qty);
        pDescription.setText(product.description);
        // pTime.setText(product.date);
        pPrice.setText(product.product_price);

        //Purchases
        mPurchases.setText(String.valueOf(product.purchases));
        hCart.setOnClickListener(cartClickListener);


    }



}