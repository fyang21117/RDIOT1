package com.fyang21117.rdiot1.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fyang21117.rdiot1.R;

public class StartActivity extends AppCompatActivity{

    private ImageView welcomeImg=null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //设置铺满屏幕
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("仿生嗅觉实验室");

        welcomeImg = findViewById(R.id.logo_start);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f,1.0f);
        alphaAnimation.setDuration(4000);
        welcomeImg.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AnimationImpl());

        progressBar = findViewById(R.id.progress_bar);


       final Handler mHandler = new Handler();
         Runnable r = new Runnable() {
        @Override
        public void run() {
            int progress = progressBar.getProgress();
            progress = progress +25;
            progressBar.setProgress(progress);
            mHandler.postDelayed(this, 1000);
          }
       };//每隔1s循环执行run方法
        mHandler.postDelayed(r, 1000);
    }

    public class AnimationImpl implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.drawable.logo);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //MainActivity.actionStart(StartActivity.this);
            Intent intent = new Intent(StartActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
