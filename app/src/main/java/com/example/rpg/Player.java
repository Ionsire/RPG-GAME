package com.example.rpg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.rpg.GameView.screenRatioX;
import static com.example.rpg.GameView.screenRatioY;

import java.util.Timer;
import java.util.TimerTask;

public class Player {
    int frameIndex = 0;
    // variavel que conta a quantia de iteracoes para a execucao das animacoes
    int animFrameCount = 0;

    // Posicao 2D inicial na tela
    int velocity = 8;
    int posX = 0;
    int posY = 0;


    int contador = 0;

    // Dimensoes do player
    int width, height;

    // Imagens da animacao
    Bitmap sprite1, sprite2, sprite3, sprite4;

    //ArrayList<Bitmap> spriteArray = new ArrayList<Bitmap>();

    Timer timer1 = new Timer();
    Timer timer2 = new Timer();

    boolean running = false;

    Player(Resources res, int posX, int posY){

        this.posX = posX;
        this.posY = posY;

        // Removendo o Blur na imagem carregada
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        sprite1 = BitmapFactory.decodeResource(res, R.drawable.f1, options);
        sprite2 = BitmapFactory.decodeResource(res, R.drawable.f2, options);
        sprite3 = BitmapFactory.decodeResource(res, R.drawable.f1, options);
        sprite4 = BitmapFactory.decodeResource(res, R.drawable.f3, options);

        width = sprite1.getWidth();
        height = sprite1.getHeight();

        // diminuindo o tamanho do player
        //width /= 12;
        //height /= 12;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        width *= 5;
        height *= 5;

        // para ficar no centro da tela
        // a posicao atual do obj tem q diminuir com metade da dimensao da imagem
        this.posX -= (width/2);
        this.posY -= (height/2);

        sprite1 = Bitmap.createScaledBitmap(sprite1, width, height,false);
        sprite2 = Bitmap.createScaledBitmap(sprite2, width, height,false);
        sprite3 = Bitmap.createScaledBitmap(sprite3, width, height,false);
        sprite4 = Bitmap.createScaledBitmap(sprite4, width, height,false);
//        spriteArray.add(sprite1);
//        spriteArray.add(sprite2);
//        spriteArray.add(sprite3);


    }
    Bitmap walkAnimation(int contador){

        // contador é campo que conta quantas iteracoes passou para o proximo frame

        if (frameIndex == 0){
            if(contador == 5){
                frameIndex++;// deve incrementar somente se o contador ultrapassar o limite de
                // iteracoes e troca pro Proximo frame
            }
            return sprite2; //  pé direito
        }
        else if(frameIndex == 1){
            if(contador == 10){
                frameIndex++;
                //System.out.println("PROXIMO FRAME 2");
            }
            return sprite3; //  parado
        }
        else if(frameIndex == 2){
            if(contador == 15){
                frameIndex++;
            }
            return sprite4; // pé esquerdo
        }
        else if (frameIndex == 3)
        {
            // Reset sprite index
            // Finalizando o ciclo de animacao
            if(contador == 20){
                frameIndex=0;
            }
            return sprite1; // parado
        }
        else{
            //frameIndex=0;
            // recomeça o ciclo de animacao
            return sprite2; // pé direito
        }

    }


    public void Tarefa1(){
        int delay = 2000;   // delay de 2 seg.
        int interval = 5000;  // intervalo de 1 seg.

        timer1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("EXECUTEI A TAREFA");
            }
        }, delay, interval);
    }

    public void Tarefa2()
    {
        int delay = 0;   // delay de 0 seg. esse
        int interval = 300;  // intervalo de 2 seg.  aki era 2000

        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("Olá World no console");
            }
        }, delay, interval);
    }
    private void ticWait(int delay){
        for(int i=0;i<delay;i++){
            // System.out.println(".");
        }


        //long actual = System.currentTimeMillis() / 1000;
    }

}
