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
    int[] voidCells = {1,2,6,7,8,9,13,14,36,37,41,42,43,44,48,49};

    TextView pieceSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);


        createTableGame();

        int id = 127;

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

    private void createTableGame() {
        GridLayout gridLayout = findViewById(R.id.gridLayout2048);

        int rows = 7;
        int columns = 7;
        int id = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                id++;

                boolean isIdInVoidCells = false;

                for (int voidCell : voidCells) {
                    if (id == voidCell) {
                        isIdInVoidCells = true;
                        break;
                    }
                }
                if (isIdInVoidCells){
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);

                        params.columnSpec = GridLayout.spec(col, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);

                }
                textView.setId(id);
            }
        }

        id = 100;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                TextView piece = null;
                id++;

                boolean isIdInVoidCells = false;

                for (int voidCell : voidCells) {
                    if ((id-100) == voidCell) {
                        isIdInVoidCells = true;
                        break;
                    }
                }
                if (!isIdInVoidCells && id != 125){
                    piece = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    addClickListenerToPiece(piece);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.rowSpec = GridLayout.spec(row, 1f);

                        params.columnSpec = GridLayout.spec(col, 1f);
                        piece.setLayoutParams(params);
                    }
                    gridLayout.addView(piece);
                    piece.setId(id);
                }
            }
        }
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