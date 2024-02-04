package com.example.androidgames;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class Contador implements Runnable{
    private int segundos;
    private boolean running;

    private TextView timeView;

    public Contador(TextView timeView) {
        this.segundos = 10; // 10 minutos en segundos
        this.running = true;
        this.timeView = timeView;
    }

    @Override
    public void run() {
        while (running) {
            try {
                sleep(1000);
                segundos--;
                timeView.post(new Runnable() {
                    @Override
                    public void run() {
                        timeView.setText(String.format("%02d:%02d", segundos / 60, segundos % 60));
                    }
                });
                if (segundos <= 0) {
                    running = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void detenerContador() {
        running = false;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
