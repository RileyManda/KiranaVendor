package com.riley.kiranavendor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.riley.kiranavendor.auth.Auth;
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.clients.Profile;
import com.riley.kiranavendor.queries.ProductsQ;
import com.riley.kiranavendor.queries.PurchasesQ;
import com.riley.kiranavendor.vendor.CreateProduct;

import butterknife.ButterKnife;


public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private FragmentStatePagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout home_tabLayout;
    private static FirebaseAuth mAuth;
    public FloatingActionButton fab;
    private AppBarLayout mAppbar;


    final int[] tabIcons = new int[]{
            R.drawable.ic_business_center_black_24dp,
            R.drawable.ic_shopping_cart_black_24dp,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView homeHeader = findViewById(R.id.home_header);
        homeHeader.setAlpha(0.2f);
        mAuth = FirebaseAuth.getInstance();
        //Pager Adapter
        mPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] mFragments = new Fragment[]{
                    new ProductsQ(),
                    new PurchasesQ()
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.products),
                    getString(R.string.purchases)

            };

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                // Show 3 total pages.
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        home_tabLayout = findViewById(R.id.tabs);
        home_tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        fab = findViewById(R.id.createFab);
        fab.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, CreateProduct.class)));


        home_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    homeHeader.setImageResource(R.drawable.ic_business_center_black_24dp);
                } else {
                    homeHeader.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //scrollbar listerner
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
        mAppbar = findViewById(R.id.appbar);
        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                fab.hide();
            } else {
                //Expanded
                fab.show();
            }
        });

    }

    private void setupTabIcons() {
        home_tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        home_tabLayout.getTabAt(1).setIcon(tabIcons[1]);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.signout) {
            signOut();
            return true;
        }

        if (id == R.id.notifications) {

            return true;
        }


        if (id == R.id.profile) {
           // openProfile();
            return true;
        }
        return true;
    }

    private void openProfile() {

        Intent pr = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(pr);
    }

    public void signOut() {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Singing Out");
            builder.setIcon(R.drawable.ic_add_alert_black_24dp);
            builder.setMessage("You are about to sign out,Please confirm...");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmedSignout();
                    Toast.makeText(HomeActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            builder.show();

    }

    private void confirmedSignout() {


            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Auth.class));
            finish();



    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
