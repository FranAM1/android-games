package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;

public class GameSenku extends AppCompatActivity {

    int[][] board = new int[7][7];
    TextView pieceSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);


        createTableGame();

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToTitle();
            }
        });
    }

    private void backToTitle() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createBaseBoard() {
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                board[i][j] = 0;
                if ((i < 2 || i > 4) && (j < 2 || j > 4)) {
                    board[i][j] = 0;

                } else {
                    board[i][j] = 1;
                }
            }
        }
    }

    private void createTableGame() {
        createBaseBoard();

        System.out.println("Valor del array: "+Arrays.deepToString(board));
    }

    private void addClickListenerToPiece(View piece) {
        piece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (pieceSelected == view){
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                    pieceSelected = null;
                    return;
                 }

                 if (pieceSelected != null){
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                }

                pieceSelected = (TextView) view;
                pieceSelected.setBackgroundResource(R.drawable.piece_senku_selected);
            }
        });
    }

    private void addClickListenerToVoid(View voidPiece){
        voidPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceSelected != null){
                    pieceSelected.setBackgroundResource(R.drawable.piece_senku);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Path path = new Path();
                        path.moveTo(pieceSelected.getX(), pieceSelected.getY());
                        path.lineTo(view.getX(), view.getY());

                        // Crear y ejecutar la animación
                        ObjectAnimator animator = ObjectAnimator.ofFloat(pieceSelected, View.X, View.Y, path);
                        animator.setDuration(200);
                        animator.start();
                    }

                    pieceSelected = null;
                }
            }
        });
    }

}