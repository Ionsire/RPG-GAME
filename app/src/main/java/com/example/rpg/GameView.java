package com.example.rpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.animation.Animation;

import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private final GameActivity activity;
    private final int screenX;
    private final int screenY;
    public static float screenRatioX, screenRatioY;
    private final Paint paint;

    private final Player player;

    long last_time = System.nanoTime();

    private int directionX = 0;
    private int directionY = 0;
    private int velocity = 10; // quantia de pixels de movimento
    private int positionX = 50;
    private int positionY = 50;

    private Bitmap button;
    private Bitmap button_R, button_L, button_D, button_U;
    private Ui ui;
    private final Background background;

    int tics;
    boolean playWalk = false;

    Timer timer;
    Bitmap frame;
    Animation animation;

    int contador = 0;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        System.out.println("TELA: "+ screenX + "/" + screenY);
        // 2220/948

        tics = 0;
        //button = BitmapFactory.decodeResource(getResources(), R.drawable.player);

        // Ui for move
        ui = new Ui(getResources());

        button_R = ui.getButton_R();
        button_L = ui.getButton_L();
        button_D = ui.getButton_D();
        button_U = ui.getButton_U();

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        player = new Player(getResources());

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        background = new Background(screenX, screenY, getResources());

    }

    @Override
    public void run() {
        while (true) { // Laço principal

            update ();
            draw ();

        }
    }

    // Usado para iniciar a Thread que roda o jogo
    public void resume () {

        //isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }
    public void pause () {

        //isPlaying = true;
        thread = new Thread(this);
        thread.interrupt();

    }


    private void update () {
        /*System.out.println(screenX + "x" +screenY);
        if (player.isMoveX){
            player.x += 3 * screenRatioX;
        }
        if (player.isMoveY){
            player.y += 3 * screenRatioY;
        }

        if (player.x < 0)
            player.x = 0;

        if (player.x >= screenX - player.width)
            player.x = screenX - player.width;*/

    }
    private void draw () {

        // Responsavel por desenhar na tela
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            // Testando se canva é null para evitar um erro quando a tela é minimizada
            if(canvas != null){
                // pintando o fundo de branco
                //canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(background.background, background.x, background.y, paint);

                // Drawn the Ui buttons with fixed positions
                canvas.drawBitmap(button_R, 310,730, paint);
                canvas.drawBitmap(button_L, 50,730, paint);
                canvas.drawBitmap(button_D, 180,860, paint);
                canvas.drawBitmap(button_U, 180,600, paint);

                //positionX = positionX + (directionX * velocity);
                //positionY = positionY + (directionY * velocity);

                // Calc the player sprite position
                player.posX = player.posX + (directionX * player.velocity);
                player.posY = player.posY + (directionY * player.velocity);

                if (playWalk){
                    // Drawning the player on screen frame 1

                    // 20 iteracoes é o ciclo completo para passar todos os frames
                    if(contador > 20){
                        contador = 0;
                    }

                    //if(player.animFrameCount > 20){
                    //    player.animFrameCount = 0;
                    //}

                    //canvas.drawBitmap(player.walkAnimation(player.animFrameCount), player.posX, player.posY, null);
                    canvas.drawBitmap(player.walkAnimation(contador), player.posX, player.posY, paint);
                    contador ++; // acho q posso botar o contador em player e ir so adicionando aki fora
                    //player.animFrameCount ++;

                }
                else{
                    canvas.drawBitmap(player.sprite1, player.posX, player.posY, paint);
                    //tics = 0;
                    //player.animFrameCount = 0;
                    contador = 0;
                }

                getHolder().unlockCanvasAndPost(canvas);
            }

        }
        // Re-drawing screen
        invalidate();
    }


    // Detectar toque na tela
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // Se clicar na tela
            case MotionEvent.ACTION_DOWN:
                int xTouch = (int) event.getX();
                int yTouch = (int) event.getY();

                // Test for button Right
                if ( xTouch >= 310 && xTouch < (310 + button_R.getWidth())
                        && yTouch >= 730 && yTouch < (730 + button_R.getHeight())){
                    //System.out.println("TOCOU EM R");
                    directionX = 1;
                    playWalk = true;

                }
                // Test for button Left
                if ( xTouch >= 50 && xTouch < (50 + button_R.getWidth())
                        && yTouch >= 730 && yTouch < (730 + button_R.getHeight())){
                    //System.out.println("TOCOU EM L");
                    directionX = -1;
                    playWalk = true;
                }
                // Test for button Down
                if ( xTouch >= 180 && xTouch < (180 + button_R.getWidth())
                        && yTouch >= 860 && yTouch < (860 + button_R.getHeight())){
                    //System.out.println("TOCOU EM D");
                    directionY = 1;
                    playWalk = true;
                }
                // Test for button Up
                if ( xTouch >= 180 && xTouch < (180 + button_R.getWidth())
                        && yTouch >= 600 && yTouch < (600 + button_R.getHeight())){
                    //System.out.println("TOCOU EM U");
                    directionY = -1;
                    playWalk = true;
                }


                //if (event.getX() < screenX / 2) {
                //player.isMoveX = true;
                //}
                //else if (event.getX() > screenX / 2){
                //player.isMoveY = true;
                //}
                break;

            // Se tirar o dedo da tela
            case MotionEvent.ACTION_UP:
                directionX = 0;
                directionY = 0;
                playWalk = false;
                break;
        }
        return true;
    }
}
