package com.riley.kiranavendor.queries;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.riley.kiranavendor.fragments.ProductsFragment;

public class ProductsQ extends ProductsFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query purchaseQuery = databaseReference.child("products");
        return purchaseQuery;
    }
}
