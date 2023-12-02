package com.example.androidgames;

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

public class Game2048 extends AppCompatActivity {

    private GestureDetector gestureDetector;
    double percentage2 = 0.6;

    int[][] board = new int[4][4];

    private GridLayout gridLayout;
    private int rows, columns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        gridLayout = findViewById(R.id.gridLayout2048);

        rows = gridLayout.getRowCount();
        columns = gridLayout.getColumnCount();

        createTableGame();

        createInitCells();

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

            updateBoard();
        }
    }

    private void updateBoard() {
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                TextView cell = (TextView) gridLayout.getChildAt(i * columns + j);
                if(board[i][j] != 0){
                    cell.setText(String.valueOf(board[i][j]));
                    //set style of peiceCells2048
                    cell.setTextAppearance(this, R.style.pieceCells2048);
                    cell.setBackgroundResource(R.drawable.cell2);
                } else {
                    cell.setText("");
                    cell.setTextAppearance(this, R.style.voidCells2048);
                    cell.setBackgroundResource(R.drawable.rounded_border_cell2048);
                }
            }
        }
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
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(board[i][j] != 0){
                    int k = i;
                    while(k > 0 && board[k-1][j] == 0){
                        board[k-1][j] = board[k][j];
                        board[k][j] = 0;
                        k--;
                    }
                }
            }
        }

        updateBoard();
    }

    private void moveDown(){
        for(int i = rows - 1; i >= 0; i--){
            for(int j = 0; j < columns; j++){
                if(board[i][j] != 0){
                    int k = i;
                    while(k < rows - 1 && board[k+1][j] == 0){
                        board[k+1][j] = board[k][j];
                        board[k][j] = 0;
                        k++;
                    }
                }
            }
        }

        updateBoard();
    }

    private void moveLeft(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(board[i][j] != 0){
                    int k = j;
                    while(k > 0 && board[i][k-1] == 0){
                        board[i][k-1] = board[i][k];
                        board[i][k] = 0;
                        k--;
                    }
                }
            }
        }

        updateBoard();
    }

    private void moveRight(){
        for(int i = 0; i < rows; i++){
            for(int j = columns - 1; j >= 0; j--){
                if(board[i][j] != 0){
                    int k = j;
                    while(k < columns - 1 && board[i][k+1] == 0){
                        board[i][k+1] = board[i][k];
                        board[i][k] = 0;
                        k++;
                    }
                }
            }
        }

        updateBoard();
    }
}