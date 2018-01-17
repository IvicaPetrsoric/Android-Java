package com.example.ivica.clumsybox.GameView;

import android.content.Context;

import com.example.ivica.clumsybox.Data.Constants;

import java.util.Random;

/**
 * Created by Ivica on 17.4.2017..
 */

public class GameLogic extends GamePanel {

    private float playerPozY;
    private float wallPozY;
    private float playerFrame_20 = 0.5f;

    private float grayBoxPozY;
    private float grayBoxHeight;

    //  margine zida for rnd poz
    float maxWallPozY = (float) 0.65;
    float minWallPozY = (float) 0.3;

    Random rnd = new Random();

    float rndWallPozY;

    public GameLogic(Context context) {
        super(context);
    }


    public float rndWallPozY(){
        // odredivanje poz zida
        rndWallPozY = rnd.nextFloat() * (maxWallPozY - minWallPozY) + minWallPozY;

        return rndWallPozY;
    }

    public String  checkPlayerPoz(float playPozY, float wallBoxPozY, float grayBoxPozY, float grayBoxHeight){
        String returnStatus;
        this.playerPozY = playPozY;
        this.wallPozY = wallBoxPozY;
        this.grayBoxPozY = grayBoxPozY;
        this.grayBoxHeight = grayBoxHeight;

        // gornja strana zida te donja strana zida
        if ((playerPozY + Constants.objectFrameWidth < wallPozY) || (playerPozY > wallPozY + Constants.objectFrameWidth )) {
            returnStatus = "IZVAN";
        }
        /*
        else if ((playPozY + Constants.objectFrameWidth < grayBoxPozY) ||
                (playPozY > grayBoxPozY + (Constants.objectFrameWidth * grayBoxHeight / 2))){
            returnStatus = "UNUTAR";
        }
        */

        /*
        else if ((playerPozY + Constants.objectFrameWidth < (wallPozY + (playerFrame_20 * Constants.objectFrameWidth )))){

            returnStatus = "UNUTAR";
        }
        else if ((playerPozY  > (wallPozY + Constants.objectFrameWidth - (playerFrame_20 * Constants.objectFrameWidth )))){
            returnStatus = "UNUTAR";
        }
        */
        else{
            returnStatus = "CENTAR";
        }

        System.out.println("Status: " + returnStatus);

        return returnStatus;
    }
}
