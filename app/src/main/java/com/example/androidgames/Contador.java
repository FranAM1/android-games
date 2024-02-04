package com.example.androidgames;

import static java.lang.Thread.sleep;

public class Contador implements Runnable{
    private int segundos;
    private boolean running;

    public Contador() {
        this.segundos = 600; // 10 minutos en segundos
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                sleep(1000);
                segundos--;

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
}
