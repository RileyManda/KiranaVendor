package com.riley.kiranavendor.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.activities.ProductDetail;
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.modal.Product;
import com.riley.kiranavendor.viewholder.ProductViewHolder;

import butterknife.ButterKnife;

/**
 * Displays List of All Products created by Vendor in Endo|
 * Fragment Activity:HomeActivity
 * Query:EndosFragQ
 * **/

public abstract class ProductsFragment extends Fragment {




    private static final String TAG = "ProductsFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    private  DatabaseReference mPostReference,searchRef;
    // [END define_database_reference]
    private ProgressDialog mProgressDialog;
    private FirebaseRecyclerAdapter<Product, ProductViewHolder> mAdapter;
    public RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public  String catsKey;
    // public  String searchtext;



    //public SwitchCompat mPrivacy;
    public ProductsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_products, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference.keepSynced(true);
       ButterKnife.bind(this,rootView);
        mRecycler = rootView.findViewById(R.id.productList);
        mRecycler.setHasFixedSize(true);
        return rootView;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        // Set up FirebaseRecyclerAdapter with the Query
        showProgressDialog();
        fetch();
    }
    private  void fetch(){

        Query postsQuery = getQuery(mDatabase);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(postsQuery, Product.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new ProductViewHolder(inflater.inflate(R.layout.item_product, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int position, @NonNull final Product model) {
                final DatabaseReference postRef = getRef(position);
                hideProgressDialog();
                // Set click listener for the whole post view
                final String prodKey = postRef.getKey();
                // final String hide = postRef.getKey();
                viewHolder.itemView.setOnClickListener(v -> {
                    // Launch PostDetailActivity
                    Intent intent = new Intent(getActivity(), ProductDetail.class);
                    intent.putExtra(ProductDetail.EXTRA_PRODUCT_KEY, prodKey);
                    startActivity(intent);
                    getUserView();
                });




                // Determine if the current user has liked this post and set UI accordingly
                if (model.purchased.containsKey(getUid())) {
                    viewHolder.hCart.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);

                } else {
                    viewHolder.hCart.setImageResource(R.drawable.ic_purchased);
                }


                // Determine if the current user has liked this post and set UI accordingly
                if (model.purchased.containsKey(getUid())) {
                   // viewHolder.privView.setChecked(true);


                } else {
                   // viewHolder.privView.setChecked(false);
                   // Toast.makeText(getActivity(), "Your Endo is Public", Toast.LENGTH_SHORT).show();

                }
/*****************************[Go To Category for each Chip = category position]*******************/




                // Bind Product to ViewHolder, setting OnClickListener for the cart
                viewHolder.bindToProducts(model, impView -> {
                    // Need to write to both places the post is stored
                    DatabaseReference globalPostRef = mDatabase.child("products").child(postRef.getKey());
                    DatabaseReference userPostRef = mDatabase.child("vendor-products").child(model.uid).child(postRef.getKey());

                    // Run two transactions
                    shoppingCart(globalPostRef);
                    shoppingCart(userPostRef);

                });


            }
        };
        mRecycler.setAdapter(mAdapter);
        //Collections.reverse();
    }



    private void getUserView() {


    }

    public void showprivAlert(){

        Toast.makeText(getActivity(), "Your Endo is Private", Toast.LENGTH_SHORT).show();

        Snackbar.make(getActivity().findViewById(R.id.main_content), "Your Endo is Private & only visible in your Control Center", Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {

                    }
                })
                .show();


        // Toast.makeText(getContext(),"Your Endo is now Private",Toast.LENGTH_LONG).show();
    }


    public  void showpubAlert(){
        Toast.makeText(getContext(), "Your Endo is now Public", Toast.LENGTH_SHORT).show();

        Snackbar.make(getActivity().findViewById(R.id.main_content), "Your Endo is now Public", Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {

                    }
                })
                .show();

        // Toast.makeText(getContext(), "Your Endo is Public", Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage("Loading...");
            // mProgressDialog = new ProgressDialog(getContext(), R.style.CustomDialog);
            // mProgressDialog.setIcon(R.drawable.ic_edit_black_24dp);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // [START post_stars_transaction]
    private void shoppingCart(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Product p = mutableData.getValue(Product.class);

                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.purchased.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.purchases = p.purchases - 1;
                    p.purchased.remove(getUid());


                } else {
                    // Star the post and add self to stars
                    p.purchases = p.purchases + 1;
                    p.purchased.put(getUid(), true);

                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        //showProgressDialog();
        if (mAdapter != null) {
            mAdapter.startListening();
        }else{
            noDataFound();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
            hideProgressDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.stopListening();
            hideProgressDialog();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //showProgressDialog();
       if (mAdapter != null) {
           mAdapter.startListening();
//
        }else{
           hideProgressDialog();
         noDataFound();
       }
    }

    private void noDataFound() {
        BaseActivity nodata = new BaseActivity();
        nodata.noDataFound();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //showProgressDialog();
        if (mAdapter != null) {
            mAdapter.stopListening();
            hideProgressDialog();
        }
    }

    @NonNull
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);



}
