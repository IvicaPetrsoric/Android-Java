package com.example.ivica.receiverbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class eceiveBroadcast extends BroadcastReceiver {
    public eceiveBroadcast() {
    }
    // new - other-broadcast, manifest pogledati!
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Broadcast je primljen! ",Toast.LENGTH_LONG).show();
    }



}
