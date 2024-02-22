package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView copyright = findViewById(R.id.copyright);
        ImageView logo = findViewById(R.id.logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        copyright.startAnimation(fadeIn);
        logo.startAnimation(fadeIn);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                    backToTitle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void backToTitle() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}