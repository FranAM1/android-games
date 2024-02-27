package com.example.androidgames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterPage extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });
    }

    public void openLoginPage() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    public void handleRegister() {
        String username = usernameEditText.getText().toString();
        if (!username.equals("") && !checkIfUserExists(username)){
            if (confirmPassword()){
                String password = passwordEditText.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();
                showOkMessage();
            }else{
                showPasswordErrorMessage();
            }
        }else{
            showErrorMessage();
        }
    }

    private boolean confirmPassword(){
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (password.equals(confirmPassword)){
            return true;
        }
        return false;
    }

    private boolean checkIfUserExists(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        if (savedUsername.equals(username)){
            return true;
        }
        return false;
    }

    private void showOkMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario registrado con éxito.")
                .setTitle("Éxito")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openLoginPage();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El usuario ya existe o el nombre de usuario está vacío.")
                .setTitle("Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra el diálogo (no necesitas hacer nada aquí porque solo es un mensaje de error)
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPasswordErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Las contraseñas no coinciden.")
                .setTitle("Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra el diálogo (no necesitas hacer nada aquí porque solo es un mensaje de error)
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}