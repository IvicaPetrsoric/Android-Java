package com.example.ivica.intenti;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MojIntentService extends IntentService {// intent service


    //   RADI SVOJ THREAD AUTOMATSKI

    private static final String TAG ="com.example.ivica.intenti";


    public MojIntentService() {
        super("MojIntentService");
    }   // house keeper

    @Override
    protected void onHandleIntent(Intent intent) {
        // ovo je sto service radi
        Log.i(TAG, "Servis je poceo");
    }
}
