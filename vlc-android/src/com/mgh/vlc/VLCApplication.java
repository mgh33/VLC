package com.mgh.vlc;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.videolan.vlc.BuildConfig;
import org.videolan.vlc.PlaybackService;

import java.util.ArrayList;
import java.util.List;

public class VLCApplication extends org.videolan.vlc.VLCApplication {

    private final String TAG="mgh-vlc/Application";

    private List<Activity> activities = new ArrayList<>();

    Application.ActivityLifecycleCallbacks lifecycleCallback = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.v(TAG, "new activity: " + activity.getLocalClassName());
            activities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            init();
        }

        @Override
        public void onActivityResumed(Activity activity) {
            //init();
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.v(TAG, "remove activity: " + activity.getLocalClassName());
            activities.remove(activity);
        }
    };




    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(lifecycleCallback);

        //init();


    }

    private void init(){

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

        // automatically start playback
        Intent intent = new Intent(this, PlaybackService.class);
        intent.setAction(PlaybackService.ACTION_REMOTE_PLAY);
        startService(intent);

    }


    void closeActivities(){

        Log.v(TAG, "closing app");

        unregisterActivityLifecycleCallbacks(lifecycleCallback);

        for (Activity act : activities){

            try{
                act.finish();
            }catch (Throwable e){
                Log.w(TAG, "error on closing vlc", e);
            }

        }


    }


}
