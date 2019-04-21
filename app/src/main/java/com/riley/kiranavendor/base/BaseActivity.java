package com.riley.kiranavendor.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.HomeActivity;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.network.AppStatus;

import butterknife.Unbinder;

/**
 *BaseActivity|Snackbars|CheckNet|ProgressDialog
 * **/
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public static final String REQUIRED = "Required";
    public static final String TOO_LONG = "Acc Type Too long";
    public static final String NO_SUCH = "Incorrect Account Type";
    private ProgressDialog mProgressDialog;
    private DatabaseReference titleRef;
    private ValueEventListener mTitleListener;
    public String mTitle;
    private FirebaseAuth mAuth;
    public Unbinder unbinder;

    //Views



    //LayoutViews
    //@BindView(R.id.hView)
   // public ViewGroup homeViews; //signin or home views

   // @BindView(R.id.signInView)
   // public ViewGroup msignUpViews;

   // @BindView(R.id.phoneAuthFields)
   // public ViewGroup mPhoneNumberViews;







    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...........Please wait....");
            mProgressDialog = new ProgressDialog(this);
            // mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            // mProgressDialog.setIcon(R.mipmap.ic_launcher);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void userProfileSaved() {//TODO:Going to use this method for the on complete listener on User Profile

    }

    public void checkNet() {
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
        } else {
            Snackbar.make(findViewById(R.id.verifView), "You seem to be Offline", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .show();
            hideProgressDialog();

        }
    }

    public void noDataFound(){


        Snackbar.make(findViewById(R.id.main_content), "You seem to be Offline", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }

    //Redir to home after sucessfully saved profile

    public static void nextPass(Activity context) {
        Intent intent = new Intent(context,HomeActivity.class);
        context.startActivity(intent);
    }

    public void goHome(){

        Intent hm = new Intent(this,HomeActivity.class);
       startActivity(hm);
    }



    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
