package com.example.androidgames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SettingsMenu extends AppCompatActivity {
    private TextView editTextUsername;

    private TextView editTextPassword;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);

        TextView highScore2048 = findViewById(R.id.highScore2048);
        TextView highScoreSenku = findViewById(R.id.highScoreSenku);

        highScore2048.setText(""+sharedPreferences.getInt("bestScore2048", 0));
        highScoreSenku.setText(""+sharedPreferences.getInt("bestScoreSenku", 0));

        Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.grow);
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(growAnimation);
                backToMainMenu();
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings(view);
            }
        });
    }

    private void backToMainMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveSettings(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", editTextUsername.getText().toString());
        editor.putString("password", editTextPassword.getText().toString());
        editor.apply();
        showAppliedSettingsDialog(view);
    }

    public void showAppliedSettingsDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario y contraseña guardados")
                .setTitle("Exito")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}