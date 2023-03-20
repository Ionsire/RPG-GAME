package com.example.rpg;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);


        // ====================================
        // DisplayMetrics metrics = getResources().getDisplayMetrics();
        // int screenWidth = metrics.widthPixels;
        // int screenHeight = metrics.heightPixels;
        // 1384/720 samsung J6
        // System.out.println("TELA NOVA: "+ screenWidth + "/" + screenHeight);
        // ====================================

        /*if (Build.VERSION.SDK_INT >= 19) {
            // include navigation bar
            display.getRealSize(outPoint);
        } else {
            // exclude navigation bar
            display.getSize(outPoint);
        }*/

        gameView = new GameView(this, point.x, point.y);
        //gameView = new GameView(this, screenWidth, screenHeight);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs
        //Intent i=new Intent(Intent.ACTION_MAIN);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //finish();
        System.out.println("Nao fazer nada aki");
    }
}
