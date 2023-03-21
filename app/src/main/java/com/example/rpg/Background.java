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

        this.x = screenX;
        this.y = screenY;

        // Removendo o Blur na imagem carregada
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        background = BitmapFactory.decodeResource(res, R.drawable.in, options);

        width = background.getWidth();
        height = background.getHeight();

        width *= 3;
        height *= 3;

        // para ficar no centro da tela
        // a posicao atual do obj tem q diminuir com metade da dimensao da imagem
        this.x -= (width/2);
        this.y -= (height/2);

        // posicao temporaria para o farm
        // + o player vai pra esquerda e - o player vai pra direita
        this.x += 350;

        background = Bitmap.createScaledBitmap(background, width, height, false);

    }
}
