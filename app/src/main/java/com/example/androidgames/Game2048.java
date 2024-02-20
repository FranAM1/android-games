package com.example.androidgames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Game2048 extends AppCompatActivity {

    Map<String, Integer> colorsOfCells = new HashMap<String, Integer>() {{
        put("2", R.color.cell2);
        put("4", R.color.cell4);
        put("8", R.color.cell8);
        put("16", R.color.cell16);
        put("32", R.color.cell32);
        put("64", R.color.cell64);
        put("128", R.color.cell128);
        put("256", R.color.cell256);
        put("512", R.color.cell512);
        put("1024", R.color.cell1024);
        put("2048", R.color.cell2048);
    }};

    private GestureDetector gestureDetector;
    double percentage2 = 0.6;

    int[][] board = new int[4][4];

    private GridLayout gridLayout;

    private TextView score;
    private int rows, columns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        gridLayout = findViewById(R.id.gridLayout2048);
        score = findViewById(R.id.score);

        rows = gridLayout.getRowCount();
        columns = gridLayout.getColumnCount();

        createTableGame();

        createInitCells();

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

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            Log.d("swipe", "right");
                            moveRight();
                        } else {
                            Log.d("swipe", "left");
                            moveLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            Log.d("swipe", "down");
                            moveDown();
                        } else {
                            Log.d("swipe", "up");
                            moveUp();
                        }
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void createInitCells() {
        for(int i = 0; i < 2;i++){
            double random = Math.random();
            int randomRow = (int) (Math.random() * rows);
            int randomCol = (int) (Math.random() * columns);

            while(board[randomRow][randomCol] != 0){
                randomRow = (int) (Math.random() * rows);
                randomCol = (int) (Math.random() * columns);
            }

            if(random < percentage2){
                board[randomRow][randomCol] = 2;
            } else {
                board[randomRow][randomCol] = 4;
            }
        }
        updateBoard();
    }

    public void startNewGame() {
        resetBoard();
        resetScore();
    }

    public void resetScore() {
        score.setText("0");
    }

    private void resetBoard() {
        gridLayout.removeAllViews();
        for (int row = 0; row < board.length; row++) {
            Arrays.fill(board[row], 0);
        }
        createTableGame();
        createInitCells();
    }

    private void updateBoard() {
        checkGameOver();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                TextView cell = (TextView) gridLayout.getChildAt(i * columns + j);
                if(board[i][j] != 0){
                    cell.setText(String.valueOf(board[i][j]));
                    //set style of peiceCells2048
                    cell.setTextAppearance(this, R.style.pieceCells2048);
                    assingColorToPiece(cell, String.valueOf(board[i][j]));
                } else {
                    cell.setText("");
                    cell.setTextAppearance(this, R.style.voidCells2048);
                    cell.setBackgroundResource(R.drawable.rounded_border_cell2048);
                }
            }
        }

        generateNewPiece();
    }

    private void backToTitle() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createTableGame() {
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++){
                TextView voidCell = new TextView(new ContextThemeWrapper(this, R.style.voidCells2048));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.rowSpec = GridLayout.spec(i, 1f);

                    params.columnSpec = GridLayout.spec(j, 1f);
                    voidCell.setLayoutParams(params);
                }
                gridLayout.addView(voidCell);
            }
        }
    }

    private void moveUp(){
        for(int i = 1; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(board[i][j] != 0){
                    int k = i;
                    while(k > 0 && (board[k-1][j] == 0 || board[k-1][j] == board[i][j])){
                        if (board[k-1][j] == 0) {
                            // Move to an empty cell
                            board[k-1][j] = board[k][j];
                            board[k][j] = 0;
                        } else if (board[k-1][j] == board[i][j]) {
                            // Merge with the same value
                            board[k-1][j] *= 2;
                            board[k][j] = 0;
                            // Update score if applicable
                            // Add your scoring logic here
                            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + board[k-1][j]));
                        }
                        k--;
                    }
                }
            }
        }

        updateBoard();
    }

    private void moveDown(){
        for(int i = rows - 2; i >= 0; i--){
            for(int j = 0; j < columns; j++){
                if(board[i][j] != 0){
                    int k = i;
                    while(k < rows - 1 && (board[k+1][j] == 0 || board[k+1][j] == board[i][j])){
                        if (board[k+1][j] == 0) {
                            // Move to an empty cell
                            board[k+1][j] = board[k][j];
                            board[k][j] = 0;
                        } else if (board[k+1][j] == board[i][j]) {
                            // Merge with the same value
                            board[k+1][j] *= 2;
                            board[k][j] = 0;
                            // Update score if applicable
                            // Add your scoring logic here
                            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + board[k+1][j]));
                        }
                        k++;
                    }
                }
            }
        }

        updateBoard();
    }

    private void moveLeft(){
        for(int i = 0; i < rows; i++){
            for(int j = 1; j < columns; j++){
                if(board[i][j] != 0){
                    int k = j;
                    while(k > 0 && (board[i][k-1] == 0 || board[i][k-1] == board[i][j])){
                        if (board[i][k-1] == 0) {
                            // Move to an empty cell
                            board[i][k-1] = board[i][k];
                            board[i][k] = 0;
                        } else if (board[i][k-1] == board[i][j]) {
                            // Merge with the same value
                            board[i][k-1] *= 2;
                            board[i][k] = 0;
                            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + board[i][k-1]));
                        }
                        k--;
                    }
                }
            }
        }

        updateBoard();
    }

    private void moveRight(){
        for(int i = 0; i < rows; i++){
            for(int j = columns - 2; j >= 0; j--){
                if(board[i][j] != 0){
                    int k = j;
                    while(k < columns - 1 && (board[i][k+1] == 0 || board[i][k+1] == board[i][j])){
                        if (board[i][k+1] == 0) {
                            board[i][k+1] = board[i][k];
                            board[i][k] = 0;
                        } else if (board[i][k+1] == board[i][j]) {
                            board[i][k+1] *= 2;
                            board[i][k] = 0;
                            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + board[i][k+1]));
                        }
                        k++;
                    }
                }
            }
        }

        updateBoard();
    }

    private void generateNewPiece() {
        //check if there is a void cell
        boolean isVoidCell = false;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(board[i][j] == 0){
                    isVoidCell = true;
                    break;
                }
            }
        }

        if(isVoidCell){
            double random = Math.random();
            int randomRow = (int) (Math.random() * rows);
            int randomCol = (int) (Math.random() * columns);

            while(board[randomRow][randomCol] != 0){
                randomRow = (int) (Math.random() * rows);
                randomCol = (int) (Math.random() * columns);
            }

            if(random < percentage2){
                board[randomRow][randomCol] = 2;
            } else {
                board[randomRow][randomCol] = 4;
            }
        }
    }

    private void checkGameOver() {
        boolean isGameOver = true;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(board[i][j] == 0){
                    isGameOver = false;
                    break;
                }
            }
        }

        if(isGameOver){
            //check if there is a possible move
            boolean isPossibleMove = false;
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < columns; j++){
                    if(i > 0 && board[i][j] == board[i-1][j]){
                        isPossibleMove = true;
                        break;
                    }
                    if(i < rows - 1 && board[i][j] == board[i+1][j]){
                        isPossibleMove = true;
                        break;
                    }
                    if(j > 0 && board[i][j] == board[i][j-1]){
                        isPossibleMove = true;
                        break;
                    }
                    if(j < columns - 1 && board[i][j] == board[i][j+1]){
                        isPossibleMove = true;
                        break;
                    }
                }
            }

            if(!isPossibleMove){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Game Over");
                builder.setMessage("You lost the game");
                builder.show();
            }
        }
    }

    private void assingColorToPiece(TextView piece, String text) {
        piece.setBackgroundResource(colorsOfCells.get(text));
    }


}