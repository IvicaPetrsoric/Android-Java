package com.example.ivica.ClumsyBox.AppVariables;


public class GlobalneVariable{

    private static GlobalneVariable sdInstance = null;
    private int ostvareniBodovi;
    private int screenHeight;
    private int screenWidth;
    private int gameViewHeight;
    private int odabraniIgrac;
    private String imeSpremljenogIgraca;
    private String textNewLocalHighScore;
    private String textReklamaGameView;
    private String vrijemeTrenutnogIgraca;
    private boolean izMainMenu;
    private boolean obavljenoSpremanjeULocalScore;
    private boolean obavljenoSpremanjeNaWeb;
    private boolean localScoreActive;
    private boolean povratakZaMain;
    private boolean stanjeMuzike;
    private boolean stanjeSFXmuzike;
    private boolean reklameIskljucene = false;

    public static GlobalneVariable getInstance(){
        if(sdInstance == null){
            sdInstance = new GlobalneVariable();
        }
        return sdInstance;
    }

    public void set_ostvareniBodovi(int bodovi){
        this.ostvareniBodovi = bodovi;
    }

    public void set_screenH(int screenH){
        this.screenHeight = screenH;
    }

    public void set_screenW(int screenW){
        this.screenWidth = screenW;
    }

    public void set_gameViewH(int gameViewH){
        this.gameViewHeight = gameViewH;
    }

    public void set_odabraniIgrac(int odIgrac){
        this.odabraniIgrac = odIgrac;
    }

    public void set_imeSpremljenogIgraca(String Ime){
        this.imeSpremljenogIgraca = Ime;
    }

    public void set_textNewLocalScore(String textNewLocal){
        this.textNewLocalHighScore = textNewLocal;
    }

    public void set_textReklama(String reklama){
        this.textReklamaGameView = reklama;
    }

    public void set_vrijemeTrenutnogIgraca(String vrijeme){
        this.vrijemeTrenutnogIgraca = vrijeme;
    }

    public void set_izMainMenu(boolean IzMaina){
        this.izMainMenu = IzMaina;
    }

    public void set_povrtakUmain(boolean zaMain){
        this.povratakZaMain = zaMain;
    }

    public void set_obavljenoSpremanjeULocalScore (boolean SaveLocal){
        this.obavljenoSpremanjeULocalScore = SaveLocal;
    }

    public void set_obavljenoSpremanjeNaWeb(boolean SaveWeb){
        this.obavljenoSpremanjeNaWeb = SaveWeb;
    }

    public void set_localScoreActive(boolean localActive){
        this.localScoreActive = localActive;
    }

    public void set_stanjeMuzike(boolean stanjeMuzike){
        this.stanjeMuzike = stanjeMuzike;
    }

    public void set_stanjeSFXmuzike(boolean stanjeSFX){
        this.stanjeSFXmuzike = stanjeSFX;
    }

    public void set_noAds(boolean noAds){
        this.reklameIskljucene = noAds;
    }


    public int get_OstBodovi(){
        return this.ostvareniBodovi;
    }

    public int get_screenH(){
        return this.screenHeight;
    }

    public int get_screenW(){
        return this.screenWidth;
    }

    public int get_gameViewH(){
        return this.gameViewHeight;
    }

    public int get_odabraniIgrac(){
        return this.odabraniIgrac;
    }

    public String get_ImeSpremljenog(){
        return this.imeSpremljenogIgraca;
    }

    public String get_textNewLocalScore(){
        return this.textNewLocalHighScore;
    }

    public String get_textReklama(){
        return this.textReklamaGameView;
    }

    public String get_trenutnoVrijeme(){
        return this.vrijemeTrenutnogIgraca;
    }

    public boolean get_izMainMenu(){
        return this.izMainMenu;
    }

    public boolean get_povratakMain(){
        return this.povratakZaMain;
    }

    public boolean get_SaveLocal(){
        return this.obavljenoSpremanjeULocalScore;
    }

    public boolean get_SaveWeb(){
        return this.obavljenoSpremanjeNaWeb;
    }

    public boolean get_localScoreActive(){
        return this.localScoreActive;
    }

    public boolean get_stanjeMuzike(){
        return this.stanjeMuzike;
    }

    public boolean get_stanjeSFXmuzike(){
        return this.stanjeSFXmuzike;
    }

    public boolean get_noAds(){
        return reklameIskljucene;
    }

}