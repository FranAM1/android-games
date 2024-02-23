package com.example.androidgames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;

public class GameSenku extends AppCompatActivity {

    int[][] board = new int[7][7];
    int[][] lastMove = new int[7][7];
    TextView pieceSelected = null;
    TextView positionSelected = null;
    private TextView undoButton;
    private TextView timeView;
    private CountDownTimer timer;
    private TextView actualScore;
    private int lastScore = 0;
    private TextView bestScore;

    private int minutos = 2;
    private int segundos = 0;

    GridLayout gridLayout;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);
        gridLayout = findViewById(R.id.gridLayoutSenku);
        timeView = findViewById(R.id.timeView);
        undoButton = findViewById(R.id.undoButton);
        actualScore = findViewById(R.id.actualScore);
        makeUndoButtonInvisible();
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        bestScore = findViewById(R.id.bestScore);
        bestScore.setText(String.valueOf(sharedPreferences.getInt("bestScoreSenku", 0)));

        createTableGame();
        startCountdownTimer();

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoLastMove();
            }
        });

        findViewById(R.id.buttonNewGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToTitle();
            }
        });
    }

    private void undoLastMove() {
        actualScore.setText(String.valueOf(lastScore));
        redrawBoard();
        makeUndoButtonInvisible();
    }

    private void saveLastScore(){
        lastScore = Integer.parseInt(actualScore.getText().toString());
    }

    private void redrawBoard() {
        gridLayout.removeAllViews();
        createBaseBoard();

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                board[row][column] = lastMove[row][column];
            }
        }

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                if (board[row][column] == 2) {
                    TextView textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    textView.setBackgroundResource(R.drawable.piece_senku);
                    addClickListenerToPiece(textView);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);
                        params.columnSpec = GridLayout.spec(column, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);
                    Position position = new Position(row, column, "piece");
                    textView.setTag(position);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void backToTitle() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startCountdownTimer() {
        timer = new CountDownTimer((minutos*60+segundos)*1000, 1000) { // 60 segundos, actualizando cada segundo
            public void onTick(long millisUntilFinished) {

                if(segundos == 0) {
                    minutos--;
                    segundos = 59;
                }else{
                    segundos--;
                }
                timeView.setText(String.format("%02d:%02d", minutos, segundos));
            }

            public void onFinish() {
                showGameOverDialog();
            }
        }.start();
    }

    private void saveLastMove() {
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                lastMove[row][column] = board[row][column];
            }
        }
    }

    public void startNewGame() {
        makeUndoButtonInvisible();
        resetBoard();
        restartTimer();
        resetScore();
    }

    private void resetScore(){
        actualScore.setText("0");
    }

    private void resetBoard() {
        gridLayout.removeAllViews();
        for (int row = 0; row < board.length; row++) {
            Arrays.fill(board[row], 0);
        }
        createTableGame();
    }

    private void restartTimer() {
        timer.cancel();
        minutos = 2;
        segundos = 00;
        startCountdownTimer();
    }

    private boolean checkWin() {
        int pieces = 0;
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                if (board[row][column] == 2) {
                    pieces++;
                }
            }
        }
        return pieces == 1;
    }

    private void showWinDialog() {
        timer.cancel();
        checkBestScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Victoria, has conseguido "+actualScore.getText()+" puntos")
                .setTitle("Victoria")
                .setCancelable(false)
                .setPositiveButton("Nueva Partida", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startNewGame();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createBaseBoard() {
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                TextView textView;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                if ((row < 2 || row > 4) && (column < 2 || column > 4)) {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.invisiblePiece));
                    board[row][column] = 0;
                    Position position = new Position(row, column, "invisible");
                    textView.setTag(position);
                } else {
                    textView = new TextView(new ContextThemeWrapper(this, R.style.voidPieceStyle));
                    addClickListenerToVoid(textView);
                    board[row][column] = 1;
                    Position position = new Position(row, column, "void");
                    textView.setTag(position);
                }



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
                    board[row][column] = 2;
                    Position position = new Position(row, column, "piece");
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
                        Position piecePosition = (Position) pieceSelected.getTag();
                        piecePosition.setRow(((Position) positionSelected.getTag()).getRow());
                        piecePosition.setColumn(((Position) positionSelected.getTag()).getColumn());
                        pieceSelected = null;
                        calculScore();
                        if (checkWin()){
                            showWinDialog();
                        } else if (checkGameOver()){
                            showGameOverDialog();
                        }

                        makeUndoButtonVisible();
                    }else{
                        System.out.println("No se puede mover");
                    }
                }
            }
        });
    }

    public void showGameOverDialog() {
        timer.cancel();
        checkBestScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Derrota, has conseguido "+actualScore.getText()+" puntos")
                .setTitle("Derrota")
                .setCancelable(false)
                .setPositiveButton("Nueva partida", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startNewGame();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void calculScore(){
        int score = Integer.parseInt(actualScore.getText().toString());

        // Calcula la diferencia en segundos desde 600 segundos
        int differenceTo600 = Math.abs((minutos * 60 + segundos) - 120);

        // Calcula los puntos por tiempo (máximo 60 puntos)
        int timeScore = Math.max(0, 60 - differenceTo600);

        // Calcula los puntos por fichas restantes
        int piecesScore = 32 - countRemainingPieces();


        score += Math.min(60, timeScore) + piecesScore;
        actualScore.setText(String.valueOf(score));
        
    }

    private int countRemainingPieces() {
        int count = 0;
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                if (board[row][column] == 2) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean checkIfPieceCanMove(){
        boolean canMove = false;
        saveLastScore();

        Position piecePosition = (Position) pieceSelected.getTag();
        Position movePosition = (Position) positionSelected.getTag();

        if (piecePosition.getRow() == movePosition.getRow()){
            if (piecePosition.getColumn() - movePosition.getColumn() == 2){
                if (board[piecePosition.getRow()][piecePosition.getColumn() - 1] == 2){
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow(), piecePosition.getColumn() - 1, "piece"));
                    canMove = true;
                }
            } else if (movePosition.getColumn() - piecePosition.getColumn() == 2){
                if (board[piecePosition.getRow()][piecePosition.getColumn() + 1] == 2){
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow(), piecePosition.getColumn() + 1, "piece"));
                    canMove = true;
                }
            }
        } else if (piecePosition.getColumn() == movePosition.getColumn()){
            if (piecePosition.getRow() - movePosition.getRow() == 2){
                if (board[piecePosition.getRow() - 1][piecePosition.getColumn()] == 2){
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow() - 1, piecePosition.getColumn(),"piece"));
                    canMove = true;
                }
            } else if (movePosition.getRow() - piecePosition.getRow() == 2){
                if (board[piecePosition.getRow() + 1][piecePosition.getColumn()] == 2){
                    saveLastMove();
                    deletePiece(new Position(piecePosition.getRow() + 1, piecePosition.getColumn(),"piece"));
                    canMove = true;
                }
            }
        }

        if (canMove){
            board[piecePosition.getRow()][piecePosition.getColumn()] = 1;
            board[movePosition.getRow()][movePosition.getColumn()] = 2;
        }

        return canMove;
    }

    private void deletePiece(Position position){
        board[position.getRow()][position.getColumn()] = 1;

        for (int i = 0; i < gridLayout.getChildCount(); i++){
            TextView piece = (TextView) gridLayout.getChildAt(i);
            Position piecePosition = (Position) piece.getTag();
            if (piecePosition.getRow() == position.getRow() && piecePosition.getColumn() == position.getColumn() && piecePosition.getType().equals("piece")){
                gridLayout.removeView(piece);
                break;
            }
        }
    }

    private boolean checkGameOver() {
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                if (board[row][column] == 2) { // Si hay una pieza en la posición actual
                    // Verificar si hay algún movimiento posible para esta pieza
                    if (checkPossibleMoves(new Position(row, column, "piece"))) {
                        return false; // Hay al menos un movimiento posible, el juego no ha terminado
                    }
                }
            }
        }
        return true; // No hay movimientos posibles para ninguna pieza, el juego ha terminado
    }

    private boolean checkPossibleMoves(Position position) {
        // Verificar si hay un movimiento posible hacia arriba
        if (position.getRow() >= 2 && board[position.getRow() - 1][position.getColumn()] == 2 && board[position.getRow() - 2][position.getColumn()] == 1) {
            return true;
        }
        // Verificar si hay un movimiento posible hacia abajo
        if (position.getRow() <= 4 && board[position.getRow() + 1][position.getColumn()] == 2 && board[position.getRow() + 2][position.getColumn()] == 1) {
            return true;
        }
        // Verificar si hay un movimiento posible hacia la izquierda
        if (position.getColumn() >= 2 && board[position.getRow()][position.getColumn() - 1] == 2 && board[position.getRow()][position.getColumn() - 2] == 1) {
            return true;
        }
        // Verificar si hay un movimiento posible hacia la derecha
        if (position.getColumn() <= 4 && board[position.getRow()][position.getColumn() + 1] == 2 && board[position.getRow()][position.getColumn() + 2] == 1) {
            return true;
        }
        return false;
    }

    private void makeUndoButtonVisible() {
        undoButton.setVisibility(View.VISIBLE);
    }

    private void makeUndoButtonInvisible() {
        undoButton.setVisibility(View.INVISIBLE);
    }

    private void checkBestScore(){
        int score = Integer.parseInt(actualScore.getText().toString());
        int bestScoreSenku = sharedPreferences.getInt("bestScoreSenku", 0);
        if (score > bestScoreSenku){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("bestScoreSenku", score);
            editor.apply();
            bestScore.setText(String.valueOf(score));
        }
    }

}