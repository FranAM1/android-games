package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

        loadMessage();
    }

    private void loadMessage(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        welcomeTextView.setText("Welcome " + savedUsername);
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