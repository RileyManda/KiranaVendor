package com.riley.kiranavendor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.Splash;
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.modal.Product;
import com.riley.kiranavendor.vendor.EditProduct;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetail extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ProductDetail";
    public static final String EXTRA_PRODUCT_KEY = "prodKey";
    private static final String TEXT_EMPTY = "Please add content";
    private static final String CONT_REQUIRED = "Type something before saving";
    private static final String EMPTY_EDITCOM = "Cannot be Empty";
    //Db Reference
    private DatabaseReference mDatabase;
    private DatabaseReference mProductReference;
    private ValueEventListener mProductListener;
    private static FirebaseAuth mAuth;


    public ProductDetail() {

    }

    /**Product Detail View[START]s**/
//[TextViews]


    @BindView(R.id.productName)
    public TextView mProdName;

    @BindView(R.id.product_id)
    public TextView mProdId;

    @BindView(R.id.product_price)
    public TextView mProdPrice;

    @BindView(R.id.prod_description)
    public TextView mProdDesc;

    @BindView(R.id.date)
    public TextView dateView;

    @BindView(R.id.vendor_name)
    public TextView mVendor;

    @Nullable
    @BindView(R.id.purchase_count)
    public TextView mPurchases;

    //ImageButtons
    @BindView(R.id.delProdbtn)
    public ImageButton mdelProd;

    @BindView(R.id.editProdbtn)
    public ImageButton meditProd;
    /**Product Detail View[END]s**/
    private String prodKey;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    // PostTime newTime = new PostTime();
    String Date;

    public String catsItemKey,mapsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);
        unbinder = ButterKnife.bind(this);


        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mProductReference = FirebaseDatabase.getInstance().getReference();
        mProductReference.keepSynced(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Get post key from intent
        prodKey = getIntent().getStringExtra(EXTRA_PRODUCT_KEY);
        if (prodKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_PRODUCT_KEY");
        }

        // Initialize Database
        mProductReference = FirebaseDatabase.getInstance().getReference()
                .child("products").child(prodKey);

        mdelProd.setOnClickListener(this);
        meditProd.setOnClickListener(this);

        //Date stuff
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date = simpledateformat.format(calendar.getTime());
        // simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    }



    /********************************[PRODUCT Interface|CallBack]*****************************************************************/
    //Post interface
    public interface PostCallback {
        void onCallback(String prod_value);
    }

    public interface IdCallBack {
        void onCallback(String prodpost_id);
    }

    //getPost method

    public void postsBody(PostCallback postCallback) {
        ValueEventListener prodlistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Product product = dataSnapshot.getValue(Product.class);
                if (product != null) {

                    //final String post_uid = (String) dataSnapshot.child("uid").getValue();
                    final String post_value = (String) dataSnapshot.child("body").getValue();
                    Log.d(TAG, "Post Body is" + post_value);
                    postCallback.onCallback(post_value);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]

            }
        };
        mProductReference.addValueEventListener(prodlistener);
        // [END Merge]
    }




    //********************************[Post Interface|CallBack END]*****************************************************************/


    /********************************[Crud Buttons]**********************************************/

    @OnClick(R.id.delProdbtn)
    public void mdeletePost() {
        delProduct();
    }

    @OnClick(R.id.editProdbtn)
    public void meditPost() {
        editProduct();
    }


    //********************************[Buttons END]**********************************************/



//********************************[Methods]**************************************************************/


    /********************************[visibility methods]**************************************************************/


    private void enablePostButtons() {
        //enable buttons post owner is verified
        mdelProd.setClickable(true);
        mdelProd.setAlpha(1.0f);
        meditProd.setClickable(true);
        meditProd.setAlpha(1.0f);

    }

    private void disablePostButtons() {

        mdelProd.setClickable(false);
        mdelProd.setAlpha(0.5f);
        //enable  post owner is Not verified
        meditProd.setClickable(false);
        meditProd.setAlpha(0.5f);


    }





    /********************************[Delete Product]**************************************************************/
    //Delete Post Method
    private void delProduct() {
        //Delete Post Method
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetail.this);
        builder.setTitle("Confirm Deletion");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("You are about to delete an Endo that might be useful to other users viewing this Endo?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String uid = getUid();
                String catsid = catsItemKey;
                //final String catid = getCatId;

                FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                //delete endo
                FirebaseDatabase.getInstance().getReference("products").child(prodKey).removeValue();

                FirebaseDatabase.getInstance().getReference("vendor-products").child(uid).child(prodKey).removeValue();

                //Show deleted toast
                Toast toast = Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                //redirect user to Splash
                Intent redir = new Intent(ProductDetail.this, Splash.class);
                startActivity(redir);


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


            }
        });

        builder.show();
    }
    /********************************[Delete Product END]**************************************************************/




    /********************************[Edit Product]**************************************************************/

    private void editProduct() {

        Intent edit = new Intent(ProductDetail.this, EditProduct.class);
        edit.putExtra(ProductDetail.EXTRA_PRODUCT_KEY, prodKey);
        startActivity(edit);

    }

    /********************************[Edit Post END]**************************************************************/
    @Override
    public void onStart() {
        super.onStart();
        disablePostButtons();
        // [value_event_listener]
        showProgressDialog();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                // Get Post object and use the values to update the UI
                Product product = dataSnapshot.getValue(Product.class);
                // Cat cat = dataSnapshot.getValue(Cat.class);
                if (product != null) {




                    mProdName.setText(product.product_name);
                    mProdId.setText(product.product_id);
                    mProdPrice.setText(product.product_price);
                    mProdDesc.setText(product.description);
                    mVendor.setText(product.description);
                    dateView.setText(product.date);



                    mPurchases.setText(String.valueOf(product.purchases));



                    //ownership test
                    final String post_uid = (String) dataSnapshot.child("uid").getValue();
                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                        //enable delete post owner is verified
                        enablePostButtons();
                    }

                    //hideProgressDialog();
                } else {


                    hideProgressDialog();


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ProductDetail.this, "Failed to load endos.",
                        Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        };

        mProductReference.addValueEventListener(postListener);
        // [END post_value_event_listener]
        // Keep copy of post listener so we can remove it when app stops
        mProductListener = postListener;


    }


    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mProductListener != null) {
            mProductReference.removeEventListener(mProductListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.delProdbtn) {
            delProduct();
            hideKeyboard();
        }
        if (i == R.id.editProdbtn) {
            editProduct();
            hideKeyboard();
        }


    }



    @NonNull
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
