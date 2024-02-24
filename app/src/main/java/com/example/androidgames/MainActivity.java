package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        findViewById(R.id.btnStartGame2048).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start2048Game();
            }
        });

        findViewById(R.id.btnStartSenku).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSenkuGame();
            }
        });

        Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.grow);
        findViewById(R.id.iconUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(growAnimation);
                goToSettings();
            }
        });

        loadMessage();
    }

    private void loadMessage(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        welcomeTextView.setText("Welcome " + savedUsername);
    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsMenu.class);
        startActivity(intent);
    }

    private void start2048Game() {
        Intent intent = new Intent(this, Game2048.class);
        startActivity(intent);
    }

    private void startSenkuGame() {
        Intent intent = new Intent(this, GameSenku.class);
        startActivity(intent);
    }
}