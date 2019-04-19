package com.riley.kiranavendor.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.HomeActivity;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.network.AppStatus;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 *BaseActivity|Snackbars|CheckNet|ProgressDialog
 * **/
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
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



    @BindView(R.id.signedInButtons)
    public  ViewGroup mSignedInViews;

    @BindView(R.id.dataForm)
    public  ViewGroup mDataFm;

    @BindView(R.id.signUpView)
    public  ViewGroup mSignedUpViews;

    @BindView(R.id.helpView)
    public ViewGroup helpViews;


    //Data Views
    //TextViews
    @BindView(R.id.status)
    public TextView mStatusText;
    @BindView(R.id.detail)
    public TextView mDetailText;

    //user data[Form Views]
    @BindView(R.id.ufname)
    public TextView mFirstname;

    @BindView(R.id.ulname)
    public TextView mLastName;

    @BindView(R.id.uage)
    public TextView mAge;
    //user data[Form Views]END

    //EditTexts
    @BindView(R.id.fieldPhoneNumber)
    public TextInputEditText mPhoneNumberField;

    //EditTexts
    @BindView(R.id.fieldVerificationCode)
    public
    TextInputEditText mVerificationField;


   /** @BindView(R.id.s_fieldPhoneNumber)
    public
    TextInputEditText mPhoneLogField;**/

    //Buttons
    @BindView(R.id.verificationBtn)
    public Button mSignUp;


    @BindView(R.id.buttonVerifyPhone)
    public
    Button mVerifyButton;

    @BindView(R.id.btnResend)
    public
    Button mResendButton;

    @BindView(R.id.signOutButton)
    public
    Button mSignOutButton;

    @BindView(R.id.noAcc)
    public
    Button mNoAcc;

    @BindView(R.id.save_data)
    public
    Button msaveData;

   /** @BindView(R.id.hasAcc)
    public
    Button mHasAcc;**/

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

    //Redir to home after sucessfully saved profile
    public void goHome() {
        Intent i = new Intent(BaseActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }



}
