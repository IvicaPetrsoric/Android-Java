package com.example.ivica.intenti;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG ="com.example.ivica.intenti";

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // tu pocinje service
        Log.i(TAG,"onStartCommand pozvan");
        // MORAMO NAPRAVITI SVOJ THREAD
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<5; i++){
                    long futureTime = System.currentTimeMillis()+5000;
                    while (System.currentTimeMillis() < futureTime){
                        synchronized (this) {
                                try {
                                    wait(futureTime - System.currentTimeMillis());
                                    Log.i(TAG, "Servis radi nesto");
                                } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        };

        Thread mojThread = new Thread(r);
        mojThread.start();
        return Service.START_STICKY;    // ako service se nekako unisti zbog android OS onda se service restarta

    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"On destroy method");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
