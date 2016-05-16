package com.mgh.vlc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.videolan.vlc.BuildConfig;


public class StartActivity extends org.videolan.vlc.StartActivity {

    private final static String TAG = "mgh-VLC/StartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Intent it1 = new Intent("com.microntek.bootcheck");
            it1.putExtra("class", BuildConfig.APPLICATION_ID);
            sendBroadcast(it1);
        }catch(Throwable e){
            Log.e(TAG, "error on sending bootcheck", e);
        }

        try {
            Intent it1 = new Intent(this, MtcAdapterService.class);
            startService(it1);
        }catch(Throwable e){
            Log.e(TAG, "error on starting service", e);
        }

        super.onCreate(savedInstanceState);

    }


}
