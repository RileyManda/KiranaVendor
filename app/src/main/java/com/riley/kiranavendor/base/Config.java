package com.riley.kiranavendor.base;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
    /**
     * Data Persistance for entire app|Offline Storage|
     * **/
    public class Config extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            /* Enable disk persistence  */
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }



    }

