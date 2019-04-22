package com.riley.kiranavendor.vendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.modal.Product;
import com.riley.kiranavendor.modal.User;
import com.riley.kiranavendor.viewholder.ProductViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edit Product Activity
 * Fragment Activity:HomeActivity
 * POJO:Product
 * **/
public class EditProduct extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EditProduct";
    public static final String EXTRA_PRODUCT_KEY = "prodKey";
    private static final String REQUIRED = "Title is Required";
    private static final String TOO_LONG = "Too long";


    private DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Product, ProductViewHolder> mAdapter;
    public EditProduct() {
    }
    //old
    private DatabaseReference mProductReference;
    private ValueEventListener mProductListener;
    private String prodKey;



  //TextViews
    @BindView(R.id.prod_id)
    TextView prodId;

    @BindView(R.id.vendor_name)
    TextView prodVendor;

    //[EditText]
    @BindView(R.id.edt_product_name)
    TextInputEditText prodName;


    @BindView(R.id.edt_prod_description)
    TextInputEditText prodDesc;

    @BindView(R.id.edt_product_price)
    TextInputEditText prodPrice;

    @BindView(R.id.edt_prod_quantity)
    TextInputEditText prodQty;


    //[Buttons]

    @BindView(R.id.saveEdits)
    FloatingActionButton mUpdateEdts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        FirebaseApp.initializeApp(this);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mProductReference = FirebaseDatabase.getInstance().getReference();
        //mDatabase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get post key from intent
        prodKey = getIntent().getStringExtra(EXTRA_PRODUCT_KEY);
        if (prodKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_PRODUCT_KEY");
        }
        // getKey();//TODO:TEST KEY..IGNORE
        // Initialize Database
        mProductReference = FirebaseDatabase.getInstance().getReference()
                .child("products").child(prodKey);

       // mProductReference = FirebaseDatabase.getInstance().getReference()
              //  .child("vendor-products").child(prodKey);

        mUpdateEdts.setOnClickListener(this);


    }


    private void updateProduct() {

        final  String product_name = prodName.getText().toString().toUpperCase();//save title to Db as Uppercase
        final  String description = prodDesc.getText().toString().toUpperCase();//save title to Db as Uppercase
        final  String product_price = prodPrice.getText().toString().toUpperCase();//save title to Db as Uppercase
        final  String product_quantity = prodQty.getText().toString().toUpperCase();//save title to Db as Uppercase


        // Title is required
        if (TextUtils.isEmpty(product_name)) {
            prodName.setError(REQUIRED);
            return;
        }else if(prodName.length()>18){

            prodName.setError(TOO_LONG);
            return;
        }

        // Cats is required
        if (TextUtils.isEmpty(description)) {
            prodDesc.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(product_price)) {
            prodPrice.setError(REQUIRED);
            return;
        }


        // Body is required
        if (TextUtils.isEmpty(product_quantity)) {
            prodQty.setError(REQUIRED);
            return;
        }

        // Disable button to avoid multiple posts
        setEditingEnabled(false);
        Snackbar.make(findViewById(R.id.editProductView), "Saving your Product........", Snackbar.LENGTH_LONG)
                .setAction("Close", null)
                .setDuration(1000)
                .show();


        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);


                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(EditProduct.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //update product
                            updatemProduct();
                        }
                        setEditingEnabled(true);
                        finish();
                        // Finish
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }



    private void setEditingEnabled(boolean enabled) {
        prodName.setEnabled(enabled);
        prodDesc.setEnabled(enabled);
        prodPrice.setEnabled(enabled);
        prodQty.setEnabled(enabled);


        if (enabled) {
            mUpdateEdts.show();
        } else {
            mUpdateEdts.hide();
        }
    }


    private  void updatemProduct(){
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

                final  String product_name = prodName.getText().toString().trim();//save title to Db as Uppercase
                final String description = prodDesc.getText().toString().trim();//save category to Db as Uppercase
                final String product_price = prodPrice.getText().toString().trim();
                final String product_qty = prodQty.getText().toString().trim();
                String userId = getUid();

                /*******************************UPDATE ****************************************************/
                Map<String, Object> updateProducts = new HashMap<String, Object>();
                updateProducts.put("product_name",product_name);
                updateProducts.put("description",description);
                updateProducts.put("product_price",product_price);
                updateProducts.put("product_qty",product_qty);
                mDatabase.child("vendor-products").child(userId).child(prodKey).updateChildren(updateProducts);
                mDatabase.child("products").child(prodKey).updateChildren(updateProducts);
                /*******************************[END]-Update****************************************************/
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(EditProduct.this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        };
        mProductReference.addValueEventListener(productListener);
        // [ENDevent_listener]


    }



    //[Delete Post Method]******************************************************************************************************

    //[Display Post Content from Db]**********************************************************************************************
    @Override
    public void onStart() {
        super.onStart();
        // Add value event listener
        //showProgressBar();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Product product = dataSnapshot.getValue(Product.class);


                if (product != null) {
                    prodId.setText(product.product_id);//id txtview[Cannot be changed]
                    prodName.setText(product.product_name);//name editable
                    prodDesc.setText(product.description);//description editable
                    prodPrice.setText(product.product_price);//price editable
                    prodQty.setText(product.product_qty);//qty editable
                    prodVendor.setText(product.vendor);//id txtview[Cannot be changed]


                    final String post_uid = (String) dataSnapshot.child("uid").getValue();
                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                        //enable delete post owner is verified

                    }

                }
                hideProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(EditProduct.this, "Failed to load endos.",
                        Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        };
        mProductReference.addValueEventListener(postListener);
        mProductListener = postListener;

        showProgressBar();
    }


    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mProductListener != null) {
            mProductReference.removeEventListener(mProductListener);
        }

        // Clean up listener
        if (mAdapter != null) {
            mAdapter.stopListening();
            hideProgressBar();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mProductListener != null) {
            mProductReference.removeEventListener(mProductListener);
        }
        if (mAdapter != null) {
            mAdapter.stopListening();
            hideProgressBar();
        }
    }



    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.saveEdits) {
            updateProduct();
            hideKeyboard();
        }


    }



    @NonNull
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }







}
