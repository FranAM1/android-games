package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Game2048 extends AppCompatActivity {
    double percentage2 = 0.6;

    int[][] board = new int[4][4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        GridLayout gridLayout = findViewById(R.id.gridLayout2048);

        int rows = gridLayout.getRowCount();
        int columns = gridLayout.getColumnCount();

        createTableGame(gridLayout, rows, columns);

        createInitCells(gridLayout, rows, columns);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToTitle();
            }
        });
    }

    private void createInitCells(GridLayout gridLayout, int rows, int columns) {
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

    private void createTableGame(GridLayout gridLayout, int rows, int columns) {
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
}