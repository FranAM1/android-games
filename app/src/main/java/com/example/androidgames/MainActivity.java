package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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