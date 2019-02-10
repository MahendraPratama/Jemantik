package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.vision.text.Line;

public class SplashActivity extends AppCompatActivity {
    private static int splashinterval = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        StartAnimation();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, BerandaActivity.class);
                startActivity(i);

                SplashActivity.this.finish();
            }


        }, splashinterval);
    }
    private void StartAnimation(){
        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade);
        animFadeIn.reset();
        LinearLayout iv = (LinearLayout) findViewById(R.id.logosplash);
        iv.clearAnimation();
        iv.startAnimation(animFadeIn);
    }
}
