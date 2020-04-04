package com.saad.example.nearbyservices.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.saad.example.nearbyservices.R;


public class PreferenceActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);


        imageView = (ImageView) findViewById(R.id.img_heart);
        //check if the preferences are not set.
        for (int i = 0; i < 2; i++) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            imageView.startAnimation(shake);

        }
    }
    public void CategoryPreferences(View view) {
        startActivity(new Intent(this,CategoryActivity.class));
    }
}
