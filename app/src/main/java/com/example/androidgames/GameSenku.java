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
    TextView positionSelected = null;

    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);
        gridLayout = findViewById(R.id.gridLayoutSenku);

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
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 2 || row > 4) && (column < 2 || column > 4)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                }

                Position position = new Position(row, column);
                textView.setTag(position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    params.rowSpec = GridLayout.spec(row, 1f);
                    params.columnSpec = GridLayout.spec(column, 1f);
                    textView.setLayoutParams(params);
                }

                gridLayout.addView(textView);
            }
        }
    }

    private void createPieces() {
        for (int row = 0; row < 7; row++){
            for (int column = 0; column < 7; column++){
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if (board[row][column] == 1 && (row != 3 || column != 3)){
                    textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.piece_senku);
                    addClickListenerToPiece(textView);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);

                    Position position = new Position(row, column);
                    textView.setTag(position);
                }
            }
        }
    }

    private void createTableGame() {
        createBaseBoard();
        createPieces();

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
                    positionSelected = (TextView) view;

                    if (checkIfPieceCanMove()){
                        pieceSelected.setBackgroundResource(R.drawable.piece_senku);
                        //animacion
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Path path = new Path();
                            path.moveTo(pieceSelected.getX(), pieceSelected.getY());
                            path.lineTo(view.getX(), view.getY());

                            // Crear y ejecutar la animación
                            ObjectAnimator animator = ObjectAnimator.ofFloat(pieceSelected, View.X, View.Y, path);
                            animator.setDuration(200);
                            animator.start();
                        }
                        pieceSelected.setTag(positionSelected.getTag());
                        pieceSelected = null;
                    }
                }
            }
        });
    }

    private boolean checkIfPieceCanMove(){
        boolean canMove = false;

        Position piecePosition = (Position) pieceSelected.getTag();
        Position movePosition = (Position) positionSelected.getTag();

        if (piecePosition.getRow() == movePosition.getRow()){
            if (piecePosition.getColumn() - movePosition.getColumn() == 2){
                if (board[piecePosition.getRow()][piecePosition.getColumn() - 1] == 1){
                    canMove = true;
                }
            } else if (movePosition.getColumn() - piecePosition.getColumn() == 2){
                if (board[piecePosition.getRow()][piecePosition.getColumn() + 1] == 1){
                    canMove = true;
                }
            }
        } else if (piecePosition.getColumn() == movePosition.getColumn()){
            if (piecePosition.getRow() - movePosition.getRow() == 2){
                if (board[piecePosition.getRow() - 1][piecePosition.getColumn()] == 1){
                    canMove = true;
                }
            } else if (movePosition.getRow() - piecePosition.getRow() == 2){
                if (board[piecePosition.getRow() + 1][piecePosition.getColumn()] == 1){
                    canMove = true;
                }
            }
        }

        return canMove;
    }

}