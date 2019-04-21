package com.riley.kiranavendor;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.riley.kiranavendor.auth.Auth;
import com.riley.kiranavendor.base.BaseActivity;

public class Splash extends BaseActivity {
    public void onAttachedToWindow() {

        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** activity is first created. */
    Thread splashTread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartSplash();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressBar();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressBar();
    }


    private void StartSplash() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l= findViewById(R.id.appname_layout);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);




        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;

                    }
                    Intent intent = new Intent(Splash.this,
                            Auth.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splash.this.finish();

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splash.this.finish();


                }

            }
        };
        splashTread.start();

    }


}
