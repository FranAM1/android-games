package com.example.androidgames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

public class GameSenku extends AppCompatActivity {
    int[] voidCells = {1,2,6,7,8,9,13,14,36,37,41,42,43,44,48,49};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_senku);

        GridLayout gridLayout = findViewById(R.id.gridLayout2048);

        int rows = 7;
        int columns = 7;
        int id = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                    TextView textView = new TextView(this);
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
                    }else{
                         textView = new TextView(new ContextThemeWrapper(this, R.style.pieceStyle));
                    }
                    textView.setId(id);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        params.rowSpec = GridLayout.spec(row, 1f);

                        params.columnSpec = GridLayout.spec(col, 1f);
                        textView.setLayoutParams(params);
                    }
                    gridLayout.addView(textView);
            }
        }



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

}