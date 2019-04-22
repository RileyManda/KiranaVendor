package com.riley.kiranavendor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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
import com.riley.kiranavendor.base.BaseActivity;
import com.riley.kiranavendor.clients.Profile;

import butterknife.ButterKnife;


public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";
    private FragmentStatePagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout profile_layout;
    private static FirebaseAuth mAuth;
    public FloatingActionButton fab;
    private AppBarLayout mAppbar;


    final int[] tabIcons = new int[]{
            R.drawable.ic_person_black_24dp,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layouts);
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
                    new Profile()

            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.profile)



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
        profile_layout = findViewById(R.id.tabs);
        profile_layout.setupWithViewPager(mViewPager);
        setupTabIcons();


        profile_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    homeHeader.setImageResource(R.drawable.ic_person_black_24dp);
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
        profile_layout.getTabAt(0).setIcon(tabIcons[0]);



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

        return true;
    }



    public void signOut() {
        mAuth.signOut();
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
