package com.example.mashwara.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mashwara.R;

public class Splash extends AppCompatActivity {

    private static SharedPreferences sp;
    private ImageView imageView;
    private final int splashlen = 1000;
    private TextView times_text,two_text;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       imageView = findViewById(R.id.applogo);
        //two_text=(TextView) findViewById(R.id.text_view2);
        times_text = (TextView) findViewById(R.id.times_text);
        //font style
        Typeface mTextView = Typeface.createFromAsset(getAssets(), "fonts/Canterbury.ttf");
        times_text.setTypeface(mTextView);


        Animation an2= AnimationUtils.loadAnimation(this,R.anim.bounce);
        imageView.startAnimation(an2);
//
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(times_text, "translationY", 100);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
//
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imageView, "translationX", 20f);
        objectAnimator1.setDuration(1000);
        objectAnimator1.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                if (!sp.getBoolean("logged", true)) {
                Intent i = new Intent(Splash.this, login.class);
                startActivity(i);
                finish();
//                } else {
//                    Intent i = new Intent(Splash.this, Login.class);
//                    startActivity(i);
//                    finish();

            }
        }, 3000);

    }
    @Override
    public void onBackPressed()
    {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }
}
