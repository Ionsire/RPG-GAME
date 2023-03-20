package com.example.rpg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0, y = 0;

    // Dimensoes do background
    int width, height;
    Bitmap background;

    Background (int screenX, int screenY, Resources res) {

        // Removendo o Blur na imagem carregada
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        background = BitmapFactory.decodeResource(res, R.drawable.main_bg_1, options);

        width = background.getWidth();
        height = background.getHeight();

        width *= 3;
        height *= 3;

        background = Bitmap.createScaledBitmap(background, width, height, false);

    }
}
