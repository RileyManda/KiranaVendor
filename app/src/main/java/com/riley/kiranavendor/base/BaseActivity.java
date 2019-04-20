package com.riley.kiranavendor.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.BuildConfig;
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






    @BindView(R.id.dataForm)
    public  ViewGroup mDataFm;


    @BindView(R.id.signedInButtons)
    public  ViewGroup mSignedInViews;

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

    @BindView(R.id.accType)
    public AutoCompleteTextView mType;
    //user data[Form Views]END

    //EditTexts
    @BindView(R.id.fieldPhoneNumber)
    public TextInputEditText mPhoneNumberField;

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
    MaterialButton mVerifyButton;

    @BindView(R.id.btnResend)
    public
    MaterialButton mResendButton;

    @BindView(R.id.signOutButton)
    public
    MaterialButton mSignOutButton;

    @BindView(R.id.repissue)
    public
    MaterialButton mReports;



    @BindView(R.id.save_data)
    public
    MaterialButton msaveData;

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

    public static void nextPass(Activity context) {
        Intent intent = new Intent(context,HomeActivity.class);
        context.startActivity(intent);
    }



    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
