package com.riley.kiranavendor;

import android.os.Bundle;

import com.riley.kiranavendor.base.BaseActivity;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

    }

}
