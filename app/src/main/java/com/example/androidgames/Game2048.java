package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                        } else {
                            Log.d("swipe", "left");
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            Log.d("swipe", "down");
                        } else {
                            Log.d("swipe", "up");
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

            TextView startCell = new TextView(new ContextThemeWrapper(this, R.style.pieceCells2048));

            if (random <= percentage2){
                startCell.setText("2");
            }else{
                startCell.setText("4");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(randomRow, 1f);

                params.columnSpec = GridLayout.spec(randomCol, 1f);
                board[randomRow][randomCol] = Integer.parseInt(startCell.getText().toString());
                startCell.setLayoutParams(params);
            }

            gridLayout.addView(startCell);
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

    private void
}