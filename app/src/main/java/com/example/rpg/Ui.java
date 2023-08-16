package com.example.rpg;

import static com.example.rpg.GameView.screenRatioX;
import static com.example.rpg.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class Ui {
    private Bitmap button_R, button_L, button_D, button_U;
    private int desiredImageWidth, desiredImageHeight;
    int width, height;


    Ui(Resources res, int screenWidth, int screenHeight){

        // Ui for move
        button_R = BitmapFactory.decodeResource(res, R.drawable.ui_move_r);
        button_L = BitmapFactory.decodeResource(res, R.drawable.ui_move_l);
        button_D = BitmapFactory.decodeResource(res, R.drawable.ui_move_d);
        button_U = BitmapFactory.decodeResource(res, R.drawable.ui_move_u);

        width = button_R.getWidth();
        height = button_R.getHeight();

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        desiredImageWidth = (int) (screenWidth * 0.1f); // 50% da largura da tela
        desiredImageHeight = (int) (screenHeight * 0.1f); // 30% da altura da tela

//        Rect srcRect = new Rect(0, 0, button_R.getWidth(), button_R.getHeight()); // Região da imagem a ser desenhada (toda a imagem)
//        Rect dstRect = new Rect(0, 0, desiredImageWidth, desiredImageHeight); // Região de destino (coordenadas e dimensões desejadas)



        button_R = Bitmap.createScaledBitmap(button_R, 120, 120,false);
        button_L = Bitmap.createScaledBitmap(button_L, 120, 120,false);
        button_D = Bitmap.createScaledBitmap(button_D, 120, 120,false);
        button_U = Bitmap.createScaledBitmap(button_U, 120, 120,false);

//        button_R = Bitmap.createScaledBitmap(button_R, dstRect.width(), dstRect.height(), false);
//        button_L = Bitmap.createScaledBitmap(button_L, dstRect.width(), dstRect.height(), false);
//        button_D = Bitmap.createScaledBitmap(button_D, dstRect.width(), dstRect.height(), false);
//        button_U = Bitmap.createScaledBitmap(button_U, dstRect.width(), dstRect.height(), false);
    }

    public Bitmap getButton_R() {
        return button_R;
    }

    public Bitmap getButton_L() {
        return button_L;
    }

    public Bitmap getButton_D() {
        return button_D;
    }

    public Bitmap getButton_U() {
        return button_U;
    }

    public int getWidth(){ return desiredImageWidth; }
    public int getHeight(){ return desiredImageHeight; }
}
