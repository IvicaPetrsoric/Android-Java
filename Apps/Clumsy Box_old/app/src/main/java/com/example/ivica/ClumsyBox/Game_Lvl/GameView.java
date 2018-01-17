package com.example.ivica.ClumsyBox.Game_Lvl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import com.example.ivica.ClumsyBox.AppVariables.GlobalneVariable;
import com.example.ivica.ClumsyBox.R;
import java.util.Random;


public class GameView extends View {

    //public static final String TAG = "com.example.ivica.smartclicker";

    GlobalneVariable globalVar = GlobalneVariable.getInstance();

    float cubePositionX, cubePositionY, cubeSpeedY, pozZlatnikaY, pozZlatSkrinjaY , novaPozicijaOblakaY,novaPozicijaOblakaY_2 ;
    float pozicijaRezX, faktorKorekcije, pozicijaOblaka,pozicijaOblaka_2 , randPozicijaOblakaY, randomPozicijaZida, pozScoredY, pozScoreLinije, pomakPozadine;
    float pozEkspY1,pozEkspY2,pozEkspY3, pozEkspX1,pozEkspX2, pozEkspX3, speedEksp, miciEfektEksp, pozicijaLocalScore;
    int dodatnaPozicijaX, odabraniOblak, odabraniOblak_2 , odabraniIgrac, stanjeBara, stanjeZivota, rezultat, brojOdbijanja;
    Bitmap igrac[] = new Bitmap[4];
    Bitmap oblaci[] = new Bitmap[4];
    Bitmap prepreka, okvirBara, unutarBara, zlatnici, scoredLinija, skrinja;
    Bitmap loading, crna_pozadina, pokretanjeIgre;
    Bitmap eksplozija1, bomba;
    Bitmap zivoti;
    Paint paint = new Paint();
    Paint paint_2 = new Paint();
    float cubeRadius;
    int d, d1, desetina_d,  brojZaPrikazati,screenWidth, screenHeigth, odabraniIgracSP;
    float gameViewHeigth;
    boolean novaKocka, kretanjeOblakaDesno,kretanjeOblakaDesno_2, omoguciEfektZlatnika, omoguciScored, omoguciScoredLiniju, omoguciLoading, punjenjeSkrinje;
    boolean omoguciEksplozijuIgraca, omoguciPoravnavanje, omoguciBombu, udarBombe, brojacPrijeIgre,scaleCubeRadius,omoguciNewLocalScore;
    public String odabraniSvijetIgre;
    String new_local_score_text;

    public GameView(Context context){
        super(context);

        screenWidth = globalVar.get_screenW();
        screenHeigth = globalVar.get_screenH();
        brojZaPrikazati = 2;
        omoguciBombu = false;
        udarBombe = false;
        brojacPrijeIgre = true;
        new_local_score_text = globalVar.get_textNewLocalScore();
        brojOdbijanja = 0;
        odabraniIgrac = 0;
        cubePositionX = 0;
        cubePositionY = 0;
        stanjeZivota = 3;
        pozZlatnikaY = 2f;
        pozZlatSkrinjaY = 0.4f;
        odabraniOblak = 0;
        odabraniOblak_2 = 0;
        pozicijaOblaka = -2;
        pozicijaOblaka_2 = -2;
        cubeSpeedY = 0;
        stanjeBara  = 0;
        rezultat = 0;
        novaPozicijaOblakaY = 0;
        novaPozicijaOblakaY_2 = 0;
        pomakPozadine = - screenWidth;
        faktorKorekcije = 8.5f;
        pozScoredY = 0.5f;
        //eksplozija
        pozEkspY1 = pozEkspY2 = pozEkspY3 = 0;
        miciEfektEksp = 0.5f;
        speedEksp = 1f;
        pozicijaLocalScore = 0;
        kretanjeOblakaDesno_2 = true;
        kretanjeOblakaDesno = true;
        omoguciScored = false;
        omoguciEfektZlatnika = false;
        novaKocka = true;
        omoguciScoredLiniju = false;
        omoguciLoading = false;
        punjenjeSkrinje = true;
        omoguciEksplozijuIgraca = false;
        omoguciPoravnavanje = true;
        omoguciNewLocalScore = false;
        // world 2
        scaleCubeRadius = false;

        //  pokretanjeIgre
        pokretanjeIgre = BitmapFactory.decodeResource(getResources(), R.drawable.pokretanje_igre);
        odabraniIgracSP = globalVar.get_odabraniIgrac();

        //  igraci
        switch (odabraniIgracSP){
            case 0:
                igrac[0] = BitmapFactory.decodeResource(getResources(), R.drawable.kutija_igrac_c);
                break;

            case 1:
                igrac[1] = BitmapFactory.decodeResource(getResources(), R.drawable.kutija_igrac_p);
                break;
            case 2:
                igrac[2] = BitmapFactory.decodeResource(getResources(), R.drawable.kutija_igrac_z);
                break;

            case 3:
                igrac[3] = BitmapFactory.decodeResource(getResources(), R.drawable.kokica_1);
                break;
        }

        //  zizvoti
        zivoti = BitmapFactory.decodeResource(getResources(),R.drawable.zivoti);
        // prepreka
        prepreka = BitmapFactory.decodeResource(getResources(), R.drawable.prepreka);
        // point bar
        okvirBara = BitmapFactory.decodeResource(getResources(), R.drawable.score_bar);
        unutarBara = BitmapFactory.decodeResource(getResources(),R.drawable.unutar_bara);
        //  zlatnici
        zlatnici = BitmapFactory.decodeResource(getResources(),R.drawable.coin);
        // oblak
        oblaci[0] = BitmapFactory.decodeResource(getResources(), R.drawable.oblak_1);
        oblaci[1] = BitmapFactory.decodeResource(getResources(), R.drawable.oblak_2);
        oblaci[2] = BitmapFactory.decodeResource(getResources(), R.drawable.oblak_3);
        oblaci[3] = BitmapFactory.decodeResource(getResources(), R.drawable.oblak_4);
        // scored
        scoredLinija = BitmapFactory.decodeResource(getResources(), R.drawable.scored_linija);
        //  loading
        loading = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        crna_pozadina = BitmapFactory.decodeResource(getResources(), R.drawable.pozadina_crna);
        //  eksplozija
        eksplozija1 = BitmapFactory.decodeResource(getResources(),R.drawable.eksplozija);
        //  bomba
        bomba = BitmapFactory.decodeResource(getResources(),R.drawable.bomba);
    }

    public void setWorldPlayng(String world){
        odabraniSvijetIgre = world;
    }

    public void setCubeRadius(float radius){
        this.cubeRadius = radius;
        d = Math.round(radius);
        desetina_d = d/10;
        d1 = d;

        pokretanjeIgre  = Bitmap.createScaledBitmap(pokretanjeIgre, d * 2  , d *  2, false);
        okvirBara = Bitmap.createScaledBitmap(okvirBara, d * 3  , d, false);
        oblaci[0] = Bitmap.createScaledBitmap(oblaci[0],2 * d, 2 * d,false);
        oblaci[1] = Bitmap.createScaledBitmap(oblaci[1],2 * d, 2 * d,false);
        oblaci[2] = Bitmap.createScaledBitmap(oblaci[2],2 * d, 2 * d,false);
        oblaci[3] = Bitmap.createScaledBitmap(oblaci[3],2 * d, 2 * d,false);
        zlatnici = Bitmap.createScaledBitmap(zlatnici, d, d, false);
        zivoti = Bitmap.createScaledBitmap(zivoti, d, d,false);
        bomba = Bitmap.createScaledBitmap(bomba,d *2,d *2,false);
        loading = Bitmap.createScaledBitmap(loading,3 * d,3 * d ,false);
        crna_pozadina = Bitmap.createScaledBitmap(crna_pozadina,screenWidth, screenHeigth, false);
    }

    public void randIgrac(){
        if(scaleCubeRadius){
            scaleCubeRadius = false;
            d1 = d1 - desetina_d;
        }

        igrac[odabraniIgracSP] = Bitmap.createScaledBitmap(igrac[odabraniIgracSP], d, d1, false);
        prepreka = Bitmap.createScaledBitmap(prepreka, d, d1, false);
        scoredLinija = Bitmap.createScaledBitmap(scoredLinija,10 * d,d1 ,false);
        omoguciPoravnavanje = true;
    }

    public void setStartPosition(int x, int y){
        brojOdbijanja = 0;
        novaKocka = true;

        omoguciBombu = false;

        this.cubePositionX = x;
        this.cubePositionY = y;
        // random pozicija
        Random rand = new Random();
        dodatnaPozicijaX = rand.nextInt(8) +2 ;
        randomPozicijaZida();
    }

    public void setSpeed(float y){
        this.cubeSpeedY = y;
        gameViewHeigth = globalVar.get_gameViewH();
    }

    public void movePlayer(){
        if (!brojacPrijeIgre){

            cubePositionY = cubePositionY + cubeSpeedY;

            if (novaKocka){
                cubeSpeedY = Math.abs(cubeSpeedY) ;
                novaKocka = false;
            }
            // prema gore
            else if  (cubePositionY >= gameViewHeigth){
                cubeSpeedY = - cubeSpeedY;


            }
            //  prema dole
            else if (cubePositionY <= gameViewHeigth *0.15f){
                cubeSpeedY = Math.abs(cubeSpeedY);

            }

            if (cubePositionY >= gameViewHeigth * miciEfektEksp){
                omoguciEksplozijuIgraca = false;
            }

            if ((cubePositionY >= gameViewHeigth * 0.4f) && (!omoguciBombu)){
                omoguciBombu = true;

            }

            if ((cubePositionY < cubeRadius * 3.5f  ) && (omoguciBombu)) {
                omoguciBombu = false;
                dohtavitPozIgraca();
                udarBombe = true;
            }
        }

    }

    public void pomakeEfektaEksplozije(){
        pozEkspY1 = pozEkspY1 + speedEksp ;
        pozEkspY2 = pozEkspY2 - speedEksp;
        pozEkspX3 = pozEkspX3 + speedEksp;
        pozEkspX2 = pozEkspX2 - speedEksp;
    }

    public void postaviDuzinuScoreBara( boolean stanje){
        if (stanjeBara < 6 && (stanje)  ){
            stanjeBara++ ;
            unutarBara = Bitmap.createScaledBitmap(unutarBara, d * stanjeBara/2 ,d , false);

        }
        else if (!stanje){
            unutarBara = Bitmap.createScaledBitmap(unutarBara,  1 ,1 , false);
            stanjeBara = 0;
        }
        if (stanjeBara == 6){
            unutarBara = Bitmap.createScaledBitmap(unutarBara, d * 3 , d, false);
            omoguciEfektZlatnika = true;
            pozZlatnikaY = 2;
            rezultat++;
        }
    }

    public String  provjeraPozicijeIgraca(){
        omoguciBombu = false;

        if  (cubePositionY < ( gameViewHeigth * randomPozicijaZida)){
            dohtavitPozIgraca();
            return "VAN ZIDA";
        }
        //  gornja granica zida  30%
        else if (cubePositionY   < ( gameViewHeigth * randomPozicijaZida + ( d1 * 0.3f)) && ( (cubePositionY ) >  gameViewHeigth * randomPozicijaZida )  ){
            if (odabraniSvijetIgre.equals("2")){
                scaleCubeRadius = true;
            }
            return "UNUTAR";
        }
        //  donja granica zida  30%
        else if (cubePositionY  -  d1  > ( gameViewHeigth * randomPozicijaZida +  d1 - ( d1 * 0.3f)) && ( (cubePositionY -  d1 ) <  gameViewHeigth * randomPozicijaZida + d1)  ){
            if (odabraniSvijetIgre.equals("2")){
                scaleCubeRadius = true;
            }
            return "UNUTAR";
        }

        else if  ((cubePositionY <= ( gameViewHeigth * randomPozicijaZida +  d1)) ||   (cubePositionY +  d1 < ( gameViewHeigth * randomPozicijaZida + (3 * d1)))){
            if (omoguciPoravnavanje) {
                omoguciPoravnavanje = false;
                pozScoredY = 0.5f;
                omoguciScored = true;
                pozScoreLinije = gameViewHeigth * randomPozicijaZida;
                omoguciScoredLiniju = true;
                return "PORAVNATI";
            }
            return "";
        }

        else if ((cubePositionY > ( gameViewHeigth * randomPozicijaZida +  d1))){
            dohtavitPozIgraca();
            return "VAN ZIDA";
        }

        else{
            return "";
        }
    }

    public void loadanjeVelikeSkrinje(){
        skrinja = BitmapFactory.decodeResource(getResources(),R.drawable.skrinja);
        skrinja = Bitmap.createScaledBitmap(skrinja, 4 * d,3*  d, false);
    }

    public void ocistiMemoriju(){
        oblaci[0].recycle();
        oblaci[1].recycle();
        oblaci[2].recycle();
        oblaci[3].recycle();
        zivoti.recycle();
        igrac[odabraniIgracSP].recycle();
        prepreka.recycle();
        okvirBara.recycle();
        unutarBara.recycle();
        scoredLinija.recycle();
        loading.recycle();
        eksplozija1.recycle();
        bomba.recycle();
        zlatnici.recycle();
        skrinja.recycle();
    }

    public float provjeraVelicineRezultata(){
        switch(rezultat){

            case 10:
                faktorKorekcije = 8.3f;
                break;

            case 100:
                faktorKorekcije = 8.1f;
                break;

            case 1000:
                faktorKorekcije = 7.85f;
                break;

            case 10000:
                faktorKorekcije = 7.55f;
                break;

            case 100000:
                faktorKorekcije = 7.3f;
                break;

            case 1000000:
                faktorKorekcije = 7.05f ;
                break;

            case 10000000:
                faktorKorekcije = 6.85f ;
                break;
        }

        return  d * faktorKorekcije;
    }

    public void efektSkupljenogZlatnika(){
        pozZlatnikaY = pozZlatnikaY - 0.1f;
        if (pozZlatnikaY <= 1){
            omoguciEfektZlatnika = false;
        }
    }

    public void efektPozadine(){
        pomakPozadine = pomakPozadine + 20;
        if (pomakPozadine >0 ){
            pomakPozadine = 0;
        }
    }

    public void efektPunjenjaSkrinje(){
        pozZlatSkrinjaY = pozZlatSkrinjaY +0.0025f;
        if (pozZlatSkrinjaY > 0.48){
            punjenjeSkrinje = false;
        }
    }

    public  float[] kretanjeOblaka(){

        if (pozicijaOblaka >= 10 ) {
            kretanjeOblakaDesno = false;
            novaPozicijaOblakaY =  randomPozicijaOblaka();
            randomOblak();
        }
        else if (pozicijaOblaka <=-2) {
            kretanjeOblakaDesno = true;
            novaPozicijaOblakaY = randomPozicijaOblaka();
            randomOblak();
        }

        if (kretanjeOblakaDesno)  pozicijaOblaka =  pozicijaOblaka + 0.017f;
        else  pozicijaOblaka =  pozicijaOblaka - 0.017f;

        return new  float[] {pozicijaOblaka ,novaPozicijaOblakaY};
    }

    public  float[] kretanjeOblaka_2(){

        if (pozicijaOblaka_2 >= 10 ) {
            kretanjeOblakaDesno_2 = false;
            novaPozicijaOblakaY_2 =  randomPozicijaOblaka();
            randomOblak_2();
        }
        else if (pozicijaOblaka_2 <=-2) {
            kretanjeOblakaDesno_2 = true;
            novaPozicijaOblakaY_2 = randomPozicijaOblaka();
            randomOblak_2();
        }

        if (kretanjeOblakaDesno_2)  pozicijaOblaka_2 =  pozicijaOblaka_2 + 0.009f;
        else  pozicijaOblaka_2 =  pozicijaOblaka_2 - 0.009f;

        return new  float[] {pozicijaOblaka_2 ,novaPozicijaOblakaY_2};
    }


    public void randomPozicijaZida(){
        Random rand = new Random();
        randomPozicijaZida = rand.nextFloat();
        if (randomPozicijaZida <=0.25f || randomPozicijaZida >=0.8f)  randomPozicijaZida();
    }

    public float randomPozicijaOblaka(){
        Random rand = new Random();
        randPozicijaOblakaY = rand.nextFloat();
        if (randPozicijaOblakaY <= 0.1 || randPozicijaOblakaY >= 0.9)  randomPozicijaOblaka();
        return randPozicijaOblakaY;
    }

    public void randomOblak(){
        Random rand = new Random();
        odabraniOblak = rand.nextInt(4);
    }

    public void randomOblak_2(){
        Random rand = new Random();
        odabraniOblak_2 = rand.nextInt(2);
    }

    public void dohtavitPozIgraca(){

        postaviDuzinuScoreBara(false);

        float vrij1 = (cubePositionY - cubeRadius/2) ;    //Y
        float vrij2 = (dodatnaPozicijaX * cubePositionX - cubeRadius/2);//X

        pozEkspY3 = vrij1;
        pozEkspY1 = pozEkspY2  = vrij1;
        pozEkspX1 = vrij2;
        pozEkspX3  = pozEkspX2 = vrij2;

        omoguciEksplozijuIgraca = true;
        stanjeZivota--;
    }

    public float novaPozHighLocal(float vrijednost){
        pozicijaLocalScore = pozicijaLocalScore + 0.75f;
        vrijednost = vrijednost - pozicijaLocalScore;
        return vrijednost;
    }

    @Override
    protected void onDraw (Canvas canvas){
        super.onDraw(canvas);

        if (brojacPrijeIgre){
            canvas.drawBitmap(pokretanjeIgre,d*4, gameViewHeigth * 0.4f,null);
        }

        else if(!omoguciLoading) {

           // STVORI ZID DESNO
           for (int i = 0; i < dodatnaPozicijaX - 1; i++) {
               canvas.drawBitmap(prepreka, cubeRadius * i, gameViewHeigth * randomPozicijaZida, null);
           }
           //  LIJEVO
           for (int i = dodatnaPozicijaX; i + 1 <= 10; i++) {
               canvas.drawBitmap(prepreka, cubeRadius * i, gameViewHeigth * randomPozicijaZida, null);
           }
           //   bomba
           if (omoguciBombu){
               canvas.drawBitmap(bomba,dodatnaPozicijaX * cubePositionX - cubeRadius *1.5f,   cubeRadius,null);
           }
           //  IGRAC
           canvas.drawBitmap(igrac[odabraniIgracSP], dodatnaPozicijaX * cubePositionX - cubeRadius, cubePositionY -  d1, null); // pozicija nula
           // oblaci 1
           float pozicije_1[] = kretanjeOblaka();    // poz_X,poz_Y
           canvas.drawBitmap(oblaci[odabraniOblak], d * pozicije_1[0], gameViewHeigth * pozicije_1[1], null);
           // oblaci 2
           float pozicije_2[] = kretanjeOblaka_2();    // poz_X,poz_Y
           canvas.drawBitmap(oblaci[odabraniOblak_2], d * pozicije_2[0], gameViewHeigth * pozicije_2[1], null);
           // scored
           if (omoguciScoredLiniju) {
               canvas.drawBitmap(scoredLinija, 0, pozScoreLinije, null);
           }
           //  progress bar
           canvas.drawBitmap(unutarBara, d / 10, d / 10, null);
           canvas.drawBitmap(okvirBara, d / 10, d / 10, null);
           // broj zlatnika
           paint.setColor(Color.WHITE);
           paint.setTextSize(d / 2);
           paint.setFakeBoldText(true);
           pozicijaRezX = provjeraVelicineRezultata();
           canvas.drawText("" + rezultat, pozicijaRezX, d * 0.7f, paint);

           //  zlatnici
           canvas.drawBitmap(zlatnici, d * 9, 0, null);
           if (omoguciEfektZlatnika) {
               // skupljen zlatnik
               efektSkupljenogZlatnika();
               canvas.drawBitmap(zlatnici, d * 9, d * pozZlatnikaY, null);
           }
           //  zivoti
           if (stanjeZivota > 0) {
               if (stanjeZivota == 3) {
                   canvas.drawBitmap(zivoti, d / 10 + d * 3.25f, d / 10 , null);
                   canvas.drawBitmap(zivoti, d / 10 + d * 4.25f, d / 10 , null);
                   canvas.drawBitmap(zivoti, d / 10 + d * 5.25f, d / 10 , null);
               } else if (stanjeZivota == 2) {
                   canvas.drawBitmap(zivoti, d / 10 + d * 3.25f, d / 10 , null);
                   canvas.drawBitmap(zivoti, d / 10 + d * 4.25f, d / 10 , null);
               } else{
                   canvas.drawBitmap(zivoti, d / 10 + d * 3.25f, d / 10 , null);
               }
           }

           if (omoguciEksplozijuIgraca) {
               // eksplozija
               //  +y
               canvas.drawBitmap(eksplozija1, pozEkspX1, pozEkspY1, null);
               //   -y
               canvas.drawBitmap(eksplozija1, pozEkspX1, pozEkspY2, null);
               //   +x
               canvas.drawBitmap(eksplozija1, pozEkspX2, pozEkspY3, null);
               //   -x
               canvas.drawBitmap(eksplozija1, pozEkspX3, pozEkspY3, null);
               //   X|Y
               canvas.drawBitmap(eksplozija1, pozEkspX2, pozEkspY2, null);
               //   -X|Y
               canvas.drawBitmap(eksplozija1, pozEkspX3, pozEkspY2, null);
               //   -X|-Y
               canvas.drawBitmap(eksplozija1, pozEkspX2, pozEkspY1, null);
               //   X|-Y
               canvas.drawBitmap(eksplozija1, pozEkspX3, pozEkspY1, null);
           }

            if (omoguciNewLocalScore){

                paint_2.setARGB(255, 50, 255, 50);
                paint_2.setFakeBoldText(true);

                float testTextSize = 48f;

                paint_2.setTextSize(testTextSize);
                Rect bounds = new Rect();
                paint_2.getTextBounds(new_local_score_text, 0, new_local_score_text.length(), bounds);

                float desiredTextSize = testTextSize * screenWidth * 0.5f / bounds.width();
                paint_2.setTextSize(desiredTextSize);

                canvas.drawText(new_local_score_text, d * 2.5f, novaPozHighLocal(gameViewHeigth * 0.4f), paint_2);

            }
        }

        else {
           //   efekt skrinje na kraju
           if (punjenjeSkrinje){
               if (rezultat >0){
                   efektPunjenjaSkrinje();
                   canvas.drawBitmap(zlatnici,d * 5 - d/3 ,gameViewHeigth  * pozZlatSkrinjaY,null);
                   canvas.drawBitmap(skrinja,d * 3 + d/2, gameViewHeigth * 0.5f,null);
               }
               else{
                   punjenjeSkrinje = false;
               }
           }
           // efekt zatvaranja ekrana
           else{
               efektPozadine();
               canvas.drawBitmap(crna_pozadina, pomakPozadine,0 , null);
           }
       }
    }
}
