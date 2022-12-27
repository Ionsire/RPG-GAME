package com.example.rpg;

import static com.example.rpg.GameView.screenRatioX;
import static com.example.rpg.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ui {
    private Bitmap button_R, button_L, button_D, button_U;
    int width, height;

    Ui(Resources res){
        // Ui for move
        button_R = BitmapFactory.decodeResource(res, R.drawable.ui_move_r);
        button_L = BitmapFactory.decodeResource(res, R.drawable.ui_move_l);
        button_D = BitmapFactory.decodeResource(res, R.drawable.ui_move_d);
        button_U = BitmapFactory.decodeResource(res, R.drawable.ui_move_u);

        width = button_R.getWidth();
        height = button_R.getHeight();

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        button_R = Bitmap.createScaledBitmap(button_R, 120, 120,false);
        button_L = Bitmap.createScaledBitmap(button_L, 120, 120,false);
        button_D = Bitmap.createScaledBitmap(button_D, 120, 120,false);
        button_U = Bitmap.createScaledBitmap(button_U, 120, 120,false);
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
}
