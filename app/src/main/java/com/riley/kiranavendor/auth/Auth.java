package com.riley.kiranavendor.auth;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.riley.kiranavendor.HomeActivity;
import com.riley.kiranavendor.base.BaseActivity;

import com.riley.kiranavendor.R;
import com.riley.kiranavendor.modal.User;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Auth extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "Auth";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_EXISTING_USER = 1;
    private static final int STATE_INITIALIZED = 2;
    private static final int STATE_CODE_SENT = 3;
    private static final int STATE_VERIFY_FAILED = 4;
    private static final int STATE_VERIFY_SUCCESS = 5;
    private static final int STATE_SIGNIN_FAILED = 6;
    private static final int STATE_SIGNIN_SUCCESS = 7;
    private static DatabaseReference mDatabase;

    /**DATA FORM & AUTH  VIEWS [START]**/

    @BindView(R.id.dataForm)
    public ViewGroup mDataFm;


    @BindView(R.id.signedInButtons)
    public  ViewGroup mSignedInViews;
    @BindView(R.id.signUpView)
    public  ViewGroup mSignedUpViews;


    @BindView(R.id.helpView)
    public ViewGroup helpViews;


    @BindView(R.id.status)
    public TextView mStatusText;

    @BindView(R.id.detail)
    public TextView mDetailText;

    @BindView(R.id.ufname)
    public TextView mFirstname;
    @Nullable
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

    @BindView(R.id.skip)
    public
    MaterialButton mSkipDtForm;
    /**DATA FORM & AUTH  VIEWS [END]**/



    //Boolean firstRun = true;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        unbinder = ButterKnife.bind(this);
        checkNet();

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // On click listeners
        mSignUp.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        msaveData.setOnClickListener(this);
        mSkipDtForm.setOnClickListener(this);
        mReports.setOnClickListener(this);


       //[Auth]
        mAuth = FirebaseAuth.getInstance();
        //  auth callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "Credential is...:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
                // Show a message and update the UI
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                updateUI(STATE_CODE_SENT);
            }
        };

        //Account Type TextWatcher
        //TextWatcher
        mType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.subSequence(0, s.length()).toString())) {
                    AccountTypeAutoComplete(s.subSequence(0, s.length()).toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public void  AccountTypeAutoComplete(String text) {
        final ArrayList<String> account_type = new ArrayList<>();
        final ArrayAdapter<String> maadapter = new ArrayAdapter<String>(this,
                R.layout.acc_list, R.id.maccounts, account_type);
        mType.setAdapter(maadapter);
        account_type.add("Client");
        account_type.add("Vendor");

        for (int i = 0; i < account_type.size() ; i++) {

              text = account_type.get(i);
            Log.d(TAG, "AccountTypeAutoComplete: " + text);

        }


    }

    private  void onFirstRun(){
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);

            if (mVerificationInProgress && validatePhoneNumber()) {
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
            }

        }else{

            startActivity(new Intent(Auth.this, HomeActivity.class));
            finish();
            Toast.makeText(Auth.this, "Welcome Back", Toast.LENGTH_LONG)
                    .show();

        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();
    }
    // [START _check_user]
    @Override
    public void onStart() {
        super.onStart();
        checkNet();
       // onFirstRun();
        // Check if user is signed in (non-null) and update UI accordingly.

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
        showProgressDialog();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        showProgressDialog();
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }
    // Verify Code
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [Start] sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        updateUI(STATE_SIGNIN_SUCCESS, user);
                    } else {
                        // Sign in failed
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // invalid verification code entered was
                            mVerificationField.setError("Invalid code.");
                        }
                        // Update UI
                        updateUI(STATE_SIGNIN_FAILED);

                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

//TODO:Useless method
    private String getPhone(String phone) {

            return phone;
        }



    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {

        switch (uiState) {


            case STATE_INITIALIZED:
                // Initialized state
                enableViews(mSignUp, mPhoneNumberField);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                //sent state
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mSignUp);
                mDetailText.setText(R.string.status_code_sent);
                break;
            case STATE_VERIFY_FAILED:
                hideProgressDialog();
                // Verification has failed, show all options
                enableViews(mSignUp, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                hideProgressDialog();
                // Verification sign in
                disableViews(mSignUp, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_succeeded);
               mDataFm.setVisibility(View.VISIBLE);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                //sign-in check
                mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                //show start profile activity
                mSignedUpViews.setVisibility(View.GONE);
                helpViews.setVisibility(View.GONE);
                mSignedInViews.setVisibility(View.VISIBLE);
                mDataFm.setVisibility(View.VISIBLE);
                enableViews(mPhoneNumberField, mVerificationField);

                String phonenumber = mPhoneNumberField.getText().toString();
                mPhoneNumberField.setText(phonenumber);

                mPhoneNumberField.setText(null);
                mVerificationField.setText(null);

                mStatusText.setText(R.string.signed_in);


                mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));

                //Used for customer/User help and support
                mDetailText.setText(getString(R.string.firebase_status_fmt,user.getUid().substring(18)));
                Toast.makeText(Auth.this, "Welcome", Toast.LENGTH_LONG)
                        .show();

              //  onFirstRun();
                break;
        }

        if (user == null) {
            // Signed out
            mSignedUpViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);
            mDataFm.setVisibility(View.GONE);
            mStatusText.setText(R.string.signed_out);
        }


        }

    private void getUData(FirebaseUser user) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        if (user != null) {
            String userId = user.getUid();

            String phonenumber = user.getPhoneNumber();
            mPhoneNumberField.setText(phonenumber);

            String firstname = mFirstname.getText().toString().trim();
            mFirstname.setText(firstname);

            String lastname = mLastName.getText().toString().trim();
            mLastName.setText(firstname);

            String age = mAge.getText().toString();
            mAge.setText(age);

            final String account_type = mType.getText().toString().trim();
            mType.setText(account_type);

            String status = mStatusText.getText().toString();

            writeUserData(user.getUid(),user.getPhoneNumber(),firstname,lastname,age,account_type,status);

        }
    }


    private  void writeUserData(String userId,String phonenumber, String firstname,String lastname, String age,String account_type,String status) {
        User user = new User(userId,phonenumber,firstname,lastname,age,account_type,status);

        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent h = new Intent(Auth.this,HomeActivity.class);
                startActivity(h);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }




    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }
    private boolean validateForm() {
        boolean valid = true;



        String firstname = mFirstname.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            mFirstname.setError(REQUIRED);
            valid = false;
        } else {
            mFirstname.setError(null);
        }

        String lastname = mLastName.getText().toString();
        if (TextUtils.isEmpty(lastname)) {
            mLastName.setError(REQUIRED);
            valid = false;
        } else {
            mLastName.setError(null);
        }

        String age = mAge.getText().toString();
        if (TextUtils.isEmpty(age)) {
            mAge.setError(REQUIRED);
            valid = false;
        } else {
            mAge.setError(null);

        }

        String account_type = mType.getText().toString();
        if (TextUtils.isEmpty(account_type)) {
            mType.setError(REQUIRED);
            valid = false;

        }else if(mType.length()>6){
            mType.setError(TOO_LONG);//TODO:Check if mTYpe contains array strings matching "Vendor and Client"
            valid = false;

        }else {
            mType.setError(null);
        }



        return valid;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verificationBtn:
                hideKeyboard();
                if (!validatePhoneNumber()) {
                    return;
                }

                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.buttonVerifyPhone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.btnResend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;

            case R.id.save_data:
                FirebaseUser user = mAuth.getCurrentUser();
              getUData(user);
              hideKeyboard();

                break;

            case R.id.skip:
                goHome();
                hideKeyboard();

                break;

            case R.id.repissue:
                goToReport();
                hideKeyboard();

                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    private void goToReport() {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kiranavendors@kvapp.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "App Issue");
        i.putExtra(Intent.EXTRA_TEXT   , "I would like to report an Issue with the App Concerning...");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Auth.this, "There are no email clients installed on your device...", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}