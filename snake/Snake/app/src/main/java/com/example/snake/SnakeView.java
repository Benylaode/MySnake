package com.example.snake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Random;

public class SnakeView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private Thread thread = null;
    private volatile boolean playing = true;
    private static final int DEFAULT_SIZE = 400; // Ukuran default jika tidak ada ukuran yang diberikan

    // Ukuran layar

    private final Point screenSize;


    // Tubuh ular

    private ArrayList<Point> snake;

    // Makanan
    private Point food;
    // Ukuran elemen permainan
    private int blockSize;
    private final int NUM_BLOCKS_WIDE = 30;
    private int numBlocksHigh;
    boolean berhasil ;
    private Direction currentDirection = Direction.LEFT; // Arah awal ular
//    private Direction pastDirection = Direction.UP; // Arah awal ular

    private ArrayList<Direction> directions;

    public int getSkor() {
        return skor;
    }

    public void setSkor(int skor) {
        this.skor = skor;
    }

    public   int skor = 0;

    // Konstruktor
    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Mendapatkan dimensi yang diberikan dalam atribut size dari XML
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnakeView);
        int size = a.getDimensionPixelSize(R.styleable.SnakeView_size,
                getResources().getDimensionPixelSize(R.dimen.snake_view_size));
        a.recycle();
        // Mendapatkan width dan height dari dimensi
        int width = size;
        int height = size;

        Log.d("TAG", "SnakeView: " + width + " " +height);


        // Menginisialisasi screenSize

        screenSize = new Point(width, height);
        // Hitung ukuran blok   

        blockSize = screenSize.x / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenSize.y / blockSize;

        getHolder().addCallback(this);


        // Inisialisasi komponen permainan
    }



    private void init() {

        // Inisialisasi komponen permainan
        this.snake = new ArrayList<>();
        this.directions = new ArrayList<>();
        Random random = new Random();

        int X = random.nextInt(NUM_BLOCKS_WIDE) ;
        int Y = random.nextInt(numBlocksHigh) ;
        this.snake.add(new Point(X , Y ));
        this.directions.add(currentDirection);
        this.snake.add(new Point(X + 1 , Y ));
        this.directions.add(currentDirection);
        this.snake.add(new Point(X + 2 , Y ));
        this.directions.add(currentDirection);
        Log.d("DUDE", "init: init" + snake.get(0).x + " " + snake.get(0).y);

        // Letakkan makanan secara acak
        spawnFood();

    }


    private void spawnFood() {
        Random random = new Random();
        int foodX = random.nextInt(NUM_BLOCKS_WIDE);
        int foodY = random.nextInt(numBlocksHigh);
        this.food = new Point(foodX, foodY);

    }
 

    @Override

    public void run() {
        init();
        while (playing) {
            // Update logika permainan
            playing = update();

            // Gambar tampilan
            this.draw();
        }
    }


    private Boolean update() {
        this.berhasil = moveSnake();
        // Cek apakah ular memakan makanan

        if (berhasil && snake.get(0).equals(food)) {
            this.skor = this.skor + 1;
            int last = snake.size()-1;
            int x_tammbah = snake.get(last).x;
            int y_tammbah = snake.get(last).y;
            Direction lastDirection = directions.get(last);

            // Tambah panjang ular
            switch (lastDirection) {
                case UP:
                    snake.add(new Point(x_tammbah , y_tammbah - 1));
                    directions.add(Direction.UP);
                    break;
                case DOWN:
                    snake.add(new Point(x_tammbah , y_tammbah + 1));
                    directions.add(Direction.DOWN);
                    break;
                case LEFT:
                    snake.add(new Point(x_tammbah - 1, y_tammbah ));
                    directions.add(Direction.LEFT);
                    break;
                case RIGHT:
                    snake.add(new Point(x_tammbah + 1, y_tammbah));
                    directions.add(Direction.RIGHT);
                    break;
            }
            spawnFood();
        }
        return berhasil;
    }




    private void draw() {
        Canvas canvas = new Canvas();
        try {
            SurfaceHolder surfaceHolder = getHolder();
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                System.out.println(("test"));

                // Gambar latar belakang dan elemen permainan
                canvas.drawColor(Color.WHITE);
                drawBitmap(R.drawable.pala, this.snake.get(0).x, this.snake.get(0).y, canvas);
                // Gambar tubuh ular
                for (int i = 1; i < this.snake.size(); i++) {
                    drawBitmap(R.drawable.peaces, this.snake.get(i).x, this.snake.get(i).y, canvas);
                    Log.d("drawBitmap", "Canvas is not null");
                }
                // Gambar makanan
                drawBitmap(R.drawable.makanan, this.food.x, this.food.y, canvas);
            } else {
                // Jika canvas null, tampilkan pesan kesalahan
                Log.e("SnakeView", "Canvas is null, drawing failed.");
                // Atau jika Anda ingin menampilkan pesan kesalahan pada UI, Anda dapat menggunakan Toast
                // Toast.makeText(getContext(), "Canvas is null, drawing failed.", Toast.LENGTH_SHORT).show();

            }

        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas); // Pastikan unlockCanvasAndPost(canvas) dipanggil dalam finally block
            }

        }

    }





    private void drawBitmap(int resourceId, int x, int y, Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    
        if (bitmap != null) {
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect destRect = new Rect(x * blockSize, y * blockSize,
                    (x + 1) * blockSize, (y + 1) * blockSize);

            Log.e("Bitmap", "Canvas is not null");
    
            canvas.drawBitmap(bitmap, srcRect, destRect, null);
            bitmap.recycle(); // Pastikan untuk mendaur ulang bitmap setelah menggambar
        } else {
            Log.e("SnakeView", "Failed to load bitmap for resource: " + resourceId);
        }
    }
    

//    public interface BitmapLoader {

//        Bitmap loadBitmap(int resourceId);

//    }


    @Override

    public void surfaceCreated(@NonNull SurfaceHolder holder) {


    }


    @Override

    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {


    }


    @Override

    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {


    }



    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }



    public void setDirection(Direction direction) {
        // Menghindari perubahan arah yang bertentangan
        Direction currentDirection = directions.get(0);
        if (direction == Direction.UP && currentDirection != Direction.DOWN ||
                direction == Direction.DOWN && currentDirection != Direction.UP ||
                direction == Direction.LEFT && currentDirection != Direction.RIGHT ||
                direction == Direction.RIGHT && currentDirection != Direction.LEFT) {
            directions.add(0, direction);  // Tambahkan arah baru ke depan daftar
        }
    }


    private boolean moveSnake() {
        // Pergerakan ular berdasarkan arah saat ini
        Point point = snake.get(0);
        Direction direction = directions.get(0);
            switch (direction) {
                case UP:
                    point.y--;
                    break;
                case DOWN:
                    point.y++;
                    break;
                case LEFT:
                    point.x--;
                    break;
                case RIGHT:
                    point.x++;
                    break;
            }

        snake.set(0, new Point(point));

        for (int i = snake.size() -1; i > 0; i--) {
            snake.set(i, new Point(snake.get(i-1)));
        }


        for (int i = 2; i < snake.size(); i++) {
            if (snake.get(i).equals(snake.get(1)) ||
                    snake.get(i).x == 30 || snake.get(i).y == 31 ||
                    snake.get(i).x == -1 || snake.get(i).y == -1) {
                return false;// Game over, snake hit itself
            }
        }


        // Hapus arah terakhir jika lebih besar dari ukuran ular
        if (directions.size() > snake.size()) {
            directions.remove(directions.size() - 1);
        }


        // Hapus ekor ular yang lama dan tambahkan kepala ular yang baru
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)

    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();

    }


    public void pause() {

        playing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}