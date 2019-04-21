package com.riley.kiranavendor.queries;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.riley.kiranavendor.fragments.ProductsFragment;

public class PurchasesQ  extends ProductsFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        return databaseReference.child("vendor-products")
                .child(getUid());
    }
}
