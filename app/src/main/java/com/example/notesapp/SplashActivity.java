package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.KeyFrames;
import androidx.core.view.ViewPropertyAnimatorCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout layout_app_title;
    private LinearLayout layout_developer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        layout_app_title = findViewById(R.id.layout_app_title);
        layout_developer = findViewById(R.id.layout_developer);

        playStartAnimations();

        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        new android.os.Handler().postDelayed(
            new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                    finish();
                }
            }, 3000
        );

    }

    private void initAnimations() {
        layout_app_title.setAlpha(0f);
        layout_app_title.setTranslationY(-300);
        layout_app_title.setScaleX(2f);
        layout_app_title.setScaleY(2f);

        layout_developer.setAlpha(0f);
        layout_developer.setTranslationY(100);
    }

    private void playStartAnimations() {

        initAnimations();

        layout_app_title.animate().alpha(1f).setStartDelay(100).setDuration(600).start();
        layout_app_title.animate().translationY(0).setStartDelay(400).setDuration(300).start();
        layout_app_title.animate().scaleX(1f).setStartDelay(100).setDuration(500).start();
        layout_app_title.animate().scaleY(1f).setStartDelay(100).setDuration(500).start();

        layout_developer.animate().alpha(1f).setStartDelay(600).setDuration(300).start();
        layout_developer.animate().translationY(0).setStartDelay(600).setDuration(300).start();

    }
}
