package com.riley.kiranavendor.vendor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.modal.Product;
import com.riley.kiranavendor.modal.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create New Endo Activity
 **/
public class CreateProduct extends BaseActivity {

    //Tags
    private static final String TAG = "CreateProduct";
    private static final String REQUIRED = "Required";
    private static final String tTOO_LONG = "Title is too long";
    private static final String cTOO_LONG = "Category is too long";
    //Firebase
    private DatabaseReference mDatabase;

    /**
     * CREATE Views[START]s
     **/

    @BindView(R.id.cr_product_id)
    public TextInputEditText crProdId;

    @BindView(R.id.cr_product_name)
    public TextInputEditText crProdName;

    @BindView(R.id.cr_prod_description)
    public TextInputEditText crProdDesc;

    @BindView(R.id.cr_product_price)
    public TextInputEditText crProdPrice;

    @BindView(R.id.cr_prod_quantity)
    public TextInputEditText crProdQty;

    @BindView(R.id.fabSubmitProd)
    public FloatingActionButton crSubmit;


    /**
     * CREATE ViewS[END]s
     **/


    //keys
    public String catid;
    public String tid;
    public String categoryKey;
    public String existingKey;//existing key
    public String catKey;//category node from db
    public String chiCount;
    public long mchildren;
    // public  String children;

    //Date
    //Date
    @BindView(R.id.getDate)
    TextView DisplayDateTime;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);
        //mDatabase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Initialize Database
        //[Views]
        crProdDesc.setMovementMethod(new ScrollingMovementMethod());
        //toogle

        getKey();

        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        DisplayDateTime = findViewById(R.id.getDate);


    }


    @OnClick(R.id.fabSubmitProd)
    public void crSubmit() {
        submitProduct();
    }


    public void getKey() {
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("categories").orderByKey();
        // Query query = mFirebaseDatabaseReference.child("categories").orderByKey();//gets the key
        //query.addValueEventListener(ValueEventListener);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // pushedKey = postSnapshot.getKey();//numeric pushed key
                    existingKey = postSnapshot.getKey();//numeric pushed key
                    Log.d(TAG, "existing key is " + existingKey);
                    catKey = postSnapshot.child("category").getValue().toString();
                    Log.d(TAG, "Category node is " + catKey);
                    //TODO get the data here
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private void submitProduct() {

        final String product_name = crProdName.getText().toString().toUpperCase().trim();
        String gen_id = Math.random() + product_name.substring(2);
        final String product_id = gen_id + crProdId.getText().toString().toUpperCase().trim();
        //checkCategory();
        final String description = crProdDesc.getText().toString().trim();//save title to Db as Uppercase

        final String product_price = crProdPrice.getText().toString().trim();//save title to Db as Uppercase

        final String product_qty = crProdQty.getText().toString().trim();//save title to Db as Uppercase


        DisplayDateTime.setText(Date);
        final String date = DisplayDateTime.getText().toString().trim();


        // Title is required
        if (TextUtils.isEmpty(product_name)) {
            crProdName.setError(REQUIRED);
            return;
        } else if (crProdName.length() > 12) {

            crProdName.setError(tTOO_LONG);
            return;
        }

        if (TextUtils.isEmpty(description)) {
            crProdDesc.setError(REQUIRED);
            return;
        } else if (crProdDesc.length() > 30) {

            crProdDesc.setError(cTOO_LONG);
            return;
        }

        if (TextUtils.isEmpty(product_price)) {
            crProdPrice.setError(REQUIRED);
            return;
        }


        if (TextUtils.isEmpty(product_qty)) {
            crProdQty.setError(REQUIRED);
            return;
        }


        // String catid = mDatabase.child("cat").push().getKey();
        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Snackbar.make(findViewById(R.id.newpost_activity), "Saving your Product........", Snackbar.LENGTH_LONG)
                .setDuration(1000)
                .show();


        // [START single_value_read]
        final String userId = getUid();

        Log.d(TAG, "Pushed key is" + existingKey);//pushed key existing
        final String thisKey = catKey;
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        Product product = dataSnapshot.getValue(Product.class);
                        assert user != null;
                        char vCode = user.account_type.toString().charAt(0);
                        String vendor = vCode + (user.firstname + user.lastname);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            //does user iput existbin db

                        } else {
                            writeNewPost(userId, vendor, user.account_type, product_id, product_name, description, product_price, product_qty, date);
                            // Finish this Activity, back to the stream
                            setEditingEnabled(true);
                            finish();
                            // [END_EXCLUDE]
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        crProdName.setEnabled(enabled);
        crProdDesc.setEnabled(enabled);
        crProdPrice.setEnabled(enabled);
        crProdQty.setEnabled(enabled);

        if (enabled) {
            crSubmit.show();
        } else {
            crSubmit.hide();
        }
    }


    // [START write_fan_out]
    private void writeNewPost(String userId, String vendor, String account_type, String product_id, String product_name, String description, String product_price, String product_qty, String date) {
        String key = mDatabase.child("posts").push().getKey();
        final String pushKey = mDatabase.child("categories").push().getKey();
        categoryKey = pushKey;
        catid = pushKey;

        // tid = emapKey;


        Product product = new Product(userId, vendor, account_type, product_id, product_name, description, product_price, product_qty, date);
        Map<String, Object> productValues = product.toMap();

        //child updates
        Map<String, Object> childUpdates = new HashMap<>();
        // childUpdates.put("timestamp", ServerValue.TIMESTAMP);
        //create product category
        //category push

        childUpdates.put("/products/" + key, productValues);
        childUpdates.put("/vendor-products/" + userId + "/" + key, productValues);

        mDatabase.updateChildren(childUpdates);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}