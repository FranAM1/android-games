package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView welcomeTextView;
    private RecyclerView recyclerView;
    private ArrayList<String> games = new ArrayList<String>(){{
        add("2048");
        add("Senku");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        recyclerView = findViewById(R.id.recyclerView);

        Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.grow);
        findViewById(R.id.iconUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(growAnimation);
                goToSettings();
            }
        });

        loadMessage();

        MenuAdapter menuAdapter = new MenuAdapter(games, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String game = (String) view.getTag();
                if (game.equalsIgnoreCase("2048")) {
                    start2048Game();
                } else if (game.equalsIgnoreCase("Senku")) {
                    startSenkuGame();
                }
            }
        });
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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