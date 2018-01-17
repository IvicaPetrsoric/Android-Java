package com.example.ivica.clumsybox.GameView;


import android.content.Context;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.GameView.Animations.Explosion;
import com.example.ivica.clumsybox.GameView.BottomLayer.FpsLbl;
import com.example.ivica.clumsybox.GameView.MidLayer.Background;
import com.example.ivica.clumsybox.GameView.MidLayer.BackgroundEnd;
import com.example.ivica.clumsybox.GameView.MidLayer.Clouds;
import com.example.ivica.clumsybox.GameView.MidLayer.Counter;
import com.example.ivica.clumsybox.GameView.MidLayer.Obstacles;
import com.example.ivica.clumsybox.GameView.MidLayer.ScoreLine;
import com.example.ivica.clumsybox.GameView.MidLayer.Wall;
import com.example.ivica.clumsybox.GameView.TopLayer.Coin;
import com.example.ivica.clumsybox.GameView.TopLayer.Level;
import com.example.ivica.clumsybox.GameView.TopLayer.Lifes;
import com.example.ivica.clumsybox.GameView.TopLayer.ScoreBar;
import com.example.ivica.clumsybox.GameView.TopLayer.ScoreLbl;

import java.util.ArrayList;

/**
 * Created by Ivica on 28.3.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public MainThread thread;

    // za dobivanje rnd pozicije
    private GameLogic gameLogic;

    // objects
    private Background background;
    private BackgroundEnd backgroundTransition;
    private Counter counter;
    private Player player;
    private ArrayList<Wall> boxWall;
    private Obstacles obstacles;
    private ScoreLine scoreLine;
    private ScoreBar scoreBar;
    private ArrayList<Lifes> lifes;
    private Coin coins;
    private ScoreLbl scoreLbl;
    private ArrayList<Clouds> clouds;
    private Explosion bombExplosion;
    private Level level;
    private GameEnd gameEnd;

    // ispis FPS-a
    private FpsLbl fpsLbl;

    //private int rndPlayerPozX;
    private float rndWallPozY;

    // timeri
    private long timeDelay;

    public boolean enableTouchRespond = false;

    private String statusPoz;

    private boolean enableBomb = false;
    private boolean bombUp = false;

    //Context mainActivity;

    public GamePanel(Context context){
        super(context);

        //this.mainActivity = context;

        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);

        Constants.CURRENT_CONTEXT = context;

        // radi presretanja evenata, touch
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        //getHolder().addCallback(this);
        //thread = new MainThread(getHolder(),this);
        System.out.println("surfaceChanged");
        //thread.setRunning(true);
    }

    // pokretanje glavnog threada
    @Override
    public void surfaceCreated(SurfaceHolder holder){

        System.out.println("surfaceCreated");

        thread = new MainThread(getHolder(), this);

        // podešavanje svih objekata od igre
        createScene();

        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        System.out.println("surfaceDestroyed");

        boolean retry = true;
        int counter = 0;


        //ponekad treba vise pokusaja da se zaustavi thread
        while (retry && counter < 1000){

            counter++;
            // dok ovo ne uspije ici ce u krug
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    public void createScene(){
        drawCanvas = true;

        // load all objects from spriteSheet

        // ispis FPS-a
        fpsLbl = new FpsLbl();

        backgroundTransition = new BackgroundEnd();
        backgroundTransition.activateBackground(true);

        // podešavanje pozadine igre
        background = new Background();

        //  TOP LAYER OBJ
        // scoreBar
        scoreBar = new ScoreBar();
        // zivoti
        lifes = new ArrayList<Lifes>();
        for (int i = 0; i < 3; i++){
            lifes.add(new Lifes(i));
        }

        // score rez lbl
        scoreLbl = new ScoreLbl();

        // zlatnici
        coins = new Coin();

        // game logic, provjera pozicije igrača
        gameLogic = new GameLogic(getContext());

        // oderdivanja pozicija zida i igrača
        rndWallPozY = gameLogic.rndWallPozY();

        // stvaranje igrača
        player = new Player();

        //  bomba
        obstacles = new Obstacles();

        // stvaranje zida
        boxWall = new ArrayList<Wall>();
        for (int i = 0; i < 10; i++){
            if (i == player.getRndPozX()){
                boxWall.add(new Wall(i, rndWallPozY, true));
            }else{
                boxWall.add(new Wall(i, rndWallPozY, false));
            }
        }

        // scoreLine
        scoreLine = new ScoreLine(rndWallPozY);

        //  oblaci
        clouds = new ArrayList<Clouds>();
        for (int i = 0; i < 2; i++){
            clouds.add(new Clouds());
        }

        // game end Obj/ or pause
        gameEnd = new GameEnd();

        bombExplosion = new Explosion();

        // timer
        counter = new Counter();
        counter.startCounter();

        level = new Level();
    }

    // resetanje igre
    private void restartGame(){

        // ako ima zivote makni ih
        for (int i = lifes.size(); i< 3; i++){
            lifes.add(new Lifes(i));
        }

        // pokretanje brojača
        enableCounter = true;
        counter.startCounter();

        playerNewPosition();
        player.upgradeVelocity(false);

        // postavljanje score bara na 0
        scoreBar.updateScoreBarW(false);

        // resetanje scora na 0
        scoreLbl.setScore();
    }

    // ZA HENDLANJE TOUCHA-A
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (enableTouchRespond){

            switch(event.getAction()){

                // registriranje klika dole
                case MotionEvent.ACTION_DOWN:


                    String status = gameEnd.checkBtnPressed(event.getX(),event.getY());


                    // ako su dugmici prikazani
                    if (gameEnd.btnStatus()){

                        if (status.equals("RESTART")){
                            enableTouchRespond = false;
                            restartGame();
                        }

                        if (status.equals("HOME")){
                            backgroundTransition.activateBackground(false);
                            enableTouchRespond = false;
                           /* Intent intent = new Intent(mainActivity,Loading.class);
                            Animation animation = AnimationUtils.loadAnimation(mainActivity.getApplicationContext(), R.anim.fade_out);
                            mainActivity.startActivity(intent);
                            *///this.startAnimation(animation);

                        }
                        //enableTouchRespond = true;
                    }

                    // ako dugmici nisu prikazani
                    else{
                        //System.out.println("PLAY");
                        enableTouchRespond = false;

                        statusPoz =  gameLogic.checkPlayerPoz(player.getPozY(), boxWall.get(0).getWallPozY(),
                                boxWall.get(0).getGrayBoxPozY(), boxWall.get(0).getGrayBoxHeight());

                        switch (statusPoz){

                            case "IZVAN":

                                player.prepareResize();
                                scoreBar.updateScoreBarW(false);

                                if (lifes.size() > 0){
                                    lifes.get(lifes.size()- 1).prepareResizeLife();
                                }

                                break;

                            case "UNUTAR":
                                player.updateTask = Player.UpdateTask.NO_UPDATE;
                                break;


                            case "CENTAR":

                                player.upgradeVelocity(true);
                                scoreBar.updateScoreBarW(true);
                                player.updateTask = Player.UpdateTask.NO_UPDATE;
                                scoreLine.updateScoreLinePozY(rndWallPozY);
                                enableScoreLine = true;

                                if (scoreBar.getScoreWidth() == 2.5f){
                                    coins.activateAnim();
                                    scoreLbl.updateScore();

                                    /*
                                    if (scoreLbl.getScore() > 0 && scoreLbl.getScore() % 1 == 0){
                                        if (boxWall.get(0).getGrayBoxHeight() > boxWall.get(0).getMingGrayBoxHeight()){
                                            boxWall.get(0).updateGrayBoxHeight();
                                        }
                                    }
                                    */
                                }
                                break;

                            default:
                                break;
                        }

                        timeDelay = System.nanoTime();
                        enableTimeDelay = true;

                    }
                    break;
            }
        }
        return true;
    }


    private boolean enableTimeDelay = false;
    private boolean enableCounter = true;

    private boolean enableExplosion = false;

    public void update(){

        if (backgroundTransition.status()){
            backgroundTransition.update();
        }else{
            if (enableCounter){

                counter.update();
                if (!counter.timerActive()){
                    enableCounter = false;
                    enableTouchRespond = true;
                }
            }else{
                if (!gameEnd.btnStatus()){
                    player.update();
                }
            }
        }

        // update pozicije oblaka
        for (int i = 0; i < clouds.size(); i++){
            clouds.get(i).update();
        }

        for (int i = 0; i< boxWall.size(); i++){
            boxWall.get(i).update();
        }

        scoreBar.update();

        for (int i = 0; i< lifes.size(); i++){
            lifes.get(i).update();
        }

        coins.update();

        //  TIME DELAY 0.5 SEC
        if (enableTimeDelay){
            delayGame(500);
        }

        // stvori bombu
        if (enableBomb){
            createObstacle();
        }

        // update bombu
        if (drawBomb){
            obstacles.update();
            // provjera kolizije igrca i bombe
            if (obstacles.checkColision(player.getPozY())){
                //System.out.println("KOLIZIJA");
                obstacleCollision();
            }
        }

        // animacija explozije
        if (enableExplosion){
            bombExplosion.update();
        }

        // DUGMICI !!
        if (gameEnd.btnStatus()){
            gameEnd.update();
        }

        // update fps
        fpsLbl.setFps(thread.averageFPS);
    }

    private void delayGame(float time){
        drawBomb = false;

        //drawBomb = false;
        long elapsed = (System.nanoTime() - timeDelay) / 1000000;

        //System.out.println("asdasdasd");
        if (elapsed > time){
            enableScoreLine = false;
            enableTouchRespond = true;
            enableTimeDelay = false;
            enableExplosion = false;


            // provjera stvaranje bombe
            checkIfbombEnabled();

            // postavjanje igrača i zida na novoj poziciji
            playerNewPosition();

            if (statusPoz == null)  statusPoz = "";

            switch (statusPoz) {

                case "IZVAN":
                    if (lifes.size() > 0){
                        removeLife();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    // provjera ako treba stvoriti bombu
    private void checkIfbombEnabled(){
        enableBomb = obstacles.showobstacle();
        if (enableBomb){
            bombUp = obstacles.bombPozition();
        }else{
            drawBomb = false;
        }
    }

    // hvatanje rnd pozicija te postavljanej objekata na njima
    private void playerNewPosition(){
        //rndPlayerPozX = rndPlayerWallPoz.rndPlayerPozX();
        rndWallPozY = gameLogic.rndWallPozY();

        // postavljanje igrča
        player.newPozX();

        // postavljanje zida
        for (int i = 0; i < boxWall.size(); i++){
            boxWall.get(i).newWallPozX(i, rndWallPozY, player.getRndPozX());
        }
    }

    // stvori bombu te prati pozicije igrača
    private void createObstacle(){
        // ako je true igrač ide prema dole
        if (bombUp){
            //System.out.println("Gore");
            if (player.getMoveingDirection()){
                if (player.getPozY() > Constants.SCREEN_HEIGHT / 2){
                    enableBomb = false;
                    drawBomb = true;
                    obstacles.prepareAnim(true, player.getRndPozX() * Constants.objectFrameWidth);
                }
            }
        }else{
            //System.out.println("Dole");
            if (!player.getMoveingDirection()){
                if (player.getPozY() < Constants.SCREEN_HEIGHT / 2){
                    enableBomb = false;
                    drawBomb = true;
                    obstacles.prepareAnim(false, player.getRndPozX() * Constants.objectFrameWidth);
                }
            }
        }
    }

    // detekcija kolizije igrača i bombe
    private void obstacleCollision(){
        // aktiviraj bombu
        // eksplozija
        bombExplosion.setExplosion((int)player.getPozX(),(int)player.getPozY());

        enableExplosion = true;

        player.prepareResize();
        scoreBar.updateScoreBarW(false);

        if (lifes.size() > 0){
            lifes.get(lifes.size()- 1).prepareResizeLife();
        }

        statusPoz = "IZVAN";

        timeDelay = System.nanoTime();
        enableTimeDelay = true;
        enableTouchRespond = false;
    }

    // oduzmi zivot
    private void removeLife(){

        int index = lifes.size() - 1;
        lifes.get(index).updateCanvas = false;
        lifes.get(index).recycleLife();
        lifes.remove(index);

        // igrač ostao bez života
        if (lifes.size() == 0){
            gameEnd.playerDead(scoreLbl.getScore());
        }
    }


    private boolean drawBomb = false;
    private boolean enableScoreLine = false;

    private boolean drawCanvas = true;

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (canvas != null && drawCanvas){
            // pozadina
            background.draw(canvas);


            // zid
            for (int i = 0; i< boxWall.size(); i++){
                boxWall.get(i).draw(canvas);
            }

            // igrač
            player.draw(canvas);

            // scoreLine
            if (enableScoreLine){
                scoreLine.draw(canvas);
            }

            // bomba
            if (drawBomb){
                //System.out.println("Crtaj");
                obstacles.draw(canvas);
            }

            // TOP LAYER
            // Score Bar - progress
            scoreBar.draw(canvas);
            // lifes
            for (int i = 0; i< lifes.size(); i++){
                lifes.get(i).draw(canvas);
            }

            // stanje coina
            scoreLbl.draw(canvas);

            // level igrre
            level.draw(canvas);


            // coini
            coins.draw(canvas);

            if (enableExplosion){
                bombExplosion.draw(canvas);
            }

            // oblaci
            for (int i = 0; i < clouds.size(); i++){
                clouds.get(i).draw(canvas);
            }

            //  counter
            if (enableCounter) {
                counter.draw(canvas);
            }

            //if (gameEnd.btnStatus()){
                gameEnd.draw(canvas);
            //}


            if (backgroundTransition.status()) {
                backgroundTransition.draw(canvas);
            }

            // ispis FPS
            fpsLbl.draw(canvas);
        }
    }

    // ako korisnik pretisne botun za nazad
    public void pauseGame(){
        if (!gameEnd.btnStatus()){
            gameEnd.showGameEndBtns(true);
        }
    }

    public void onPause(){
        //super.onPause();
        drawCanvas = false;

        System.out.println("OnPause");

        background.backgroundRecyle();
        bombExplosion.recycle();

        for (int i = 0; i < clouds.size(); i++){
            clouds.get(i).recycle();
        }

        obstacles.recycle();
        scoreLine.recycle();

        for (int i = 0; i< boxWall.size(); i++){
            boxWall.get(i).recycle();
        }

        coins.recycle();

        for (int i = 0; i< lifes.size(); i++){
            lifes.get(i).recycle();
        }

        scoreBar.recycle();

        gameEnd.recycle();
        player.recycle();

    }

}
