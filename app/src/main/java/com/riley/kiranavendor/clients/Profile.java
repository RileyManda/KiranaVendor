package com.riley.kiranavendor.clients;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riley.kiranavendor.R;
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.modal.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Profile extends Fragment {

    public Profile() {
    }


    /**DATA FORM & AUTH  VIEWS [START]**/

    @BindView(R.id.dataForm)
    public ViewGroup mDataFm;




    @BindView(R.id.tvstatus)
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








    @BindView(R.id.save_data)
    public
    Button msaveData;

    @BindView(R.id.skip)
    public
    Button mSkipDtForm;
    /**DATA FORM & AUTH  VIEWS [END]**/

    private static final String TAG = "Profile";

    private TextView mEmailStatus;


    @BindView(R.id.chopdtxt)
    TextView TvChop;

    @BindView(R.id.user_id)
    TextView TvUid;

    @BindView(R.id.phone_number)
    TextView TvPhone;



    //@BindView(R.id.detail)
    //TextView mDetailTextView;

    @BindView(R.id.status)
    TextView TvUStatus;

    @BindView(R.id.estatus)
    TextView TvUVerif;

    // [Auth]
    private static FirebaseAuth mAuth;
    private static DatabaseReference mUserRef;
    private ValueEventListener mUserListener;
    BaseActivity progressD = new BaseActivity();

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        // [START create_database_reference]
        ButterKnife.bind(this,rootView);


        // [START initialize_auth]
        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference();
        // [END initialize_auth]
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    public void updateUserInfo(FirebaseUser user) {
        progressD.hideProgressBar();
        if (user != null) {

            //set First letter of Users name chopped
           // String account_type = user.getAccount_type.toUpperCase();TODO:Get users account type from db
           // TvChop.setText(getSafeSubstring(account_type, 1));

            //User statsus/online/offline
            String userId = user.getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    //get verification info for user from DB
                    TvUVerif.setText(user.status);
                    //users status/Online/Offline
                    TvUStatus.setText(user.status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //set Username
            String phonenumber = usernameFromEmail(user.getPhoneNumber());
            TvPhone.setText(phonenumber);
            //set user ID
            TvUid.setText(user.getUid().substring(18));

            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));


        }

        /*else {
           mEmailStatus.setText(R.string.signed_out);


        }*/
    }

    public String getSafeSubstring(String s, int maxLength) {
        if (!TextUtils.isEmpty(s)) {
            if (s.length() >= maxLength) {
                return s.substring(0, maxLength);
            }
        }
        return s;
    }

    @Override
    public void onStart() {
        super.onStart();
        //if(mAuth.getCurrentUser() != null){
        //     updateUI(mAuth.getCurrentUser());
        //}
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUserInfo(currentUser);


    }

    @Override
    public void onPause() {
        super.onPause();
        progressD.hideProgressBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        progressD.hideProgressBar();//progress dialog from Base Activity
    }

    @Override
    public void onStop() {
        super.onStop();

        progressD.hideProgressBar();
    }


}