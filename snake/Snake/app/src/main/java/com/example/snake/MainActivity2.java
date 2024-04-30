package com.example.snake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

public class MainActivity2 extends Activity {

    private SnakeView snakeView;

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Mendapatkan ukuran layar
        @SuppressLint("ResourceType") AttributeSet attrs = Xml.asAttributeSet(getResources().getXml(R.layout.activity_main));

        // Membuat instance SnakeView dan menetapkannya sebagai konten utama activity
        this.snakeView = new SnakeView(this, attrs);

        this.snakeView = findViewById(R.id.snakeView);


        // Mengontrol gerakan ular menggunakan tombol
        Button buttonUp = findViewById(R.id.buttonUp);
        Button buttonDown = findViewById(R.id.buttonDown);
        Button buttonLeft = findViewById(R.id.buttonLeft);
        Button buttonRight = findViewById(R.id.buttonRight);
        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonPause = findViewById(R.id.buttonPause);
        TextView skor = findViewById(R.id.tv_skor);

        final Handler handler = new Handler();
        final Runnable updateScore = new Runnable() {
            @Override
            public void run() {
                TextView skor = findViewById(R.id.tv_skor);
                String text = String.valueOf(snakeView.getSkor());
                Boolean berhasil = snakeView.berhasil;
                if (berhasil != true){
                    buttonStart.setVisibility(View.VISIBLE);
                    buttonStart.setText("ReStart");
                }
                skor.setText(text);
                handler.postDelayed(this, 1000); // Update skor setiap detik
            }
        };
        handler.post(updateScore); // Mulai pembaruan skor





        buttonUp.setOnClickListener(v -> this.snakeView.setDirection(SnakeView.Direction.UP));

        buttonDown.setOnClickListener(v -> this.snakeView.setDirection(SnakeView.Direction.DOWN));

        buttonLeft.setOnClickListener(v -> this.snakeView.setDirection(SnakeView.Direction.LEFT));

        buttonRight.setOnClickListener(v -> this.snakeView.setDirection(SnakeView.Direction.RIGHT));

        buttonStart.setOnClickListener(v -> {
            buttonStart.setVisibility(View.GONE); // Menghilangkan tombol "Start"
            this.snakeView.resume();// Memulai permainan
        });
        buttonPause.setOnClickListener(v -> {
            buttonStart.setVisibility(View.VISIBLE);
            this.snakeView.pause(); // Memulai permainan
            onPause();
        });
        String text = String.valueOf(this.snakeView.skor);
        skor.setText(text);
    }
}
