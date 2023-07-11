package com.example.pawsome.view.activity.adam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pawsome.R;
import com.example.pawsome.view.activity.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
//        findViews();
//        lottieAnimationView.resumeAnimation();
//        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });

    }

//    private void findViews() {
//        lottieAnimationView = findViewById(R.id.animation_view);
//    }
}