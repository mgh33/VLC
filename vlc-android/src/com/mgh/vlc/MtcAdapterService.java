package com.mgh.vlc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.vlc.BuildConfig;
import org.videolan.vlc.PlaybackService;
import org.videolan.vlc.VLCApplication;
import org.videolan.vlc.util.VLCInstance;

import java.util.ArrayList;
import java.util.List;

public class MtcAdapterService extends Service {

    private final static String TAG = "mgh-VLC/MtcAdapter";


    //region mtcReceiver

    protected BroadcastReceiver mtcReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //Log.v(TAG, "receive");
            if (service == null) return;

            String action = intent.getAction();

            if (action.equals("com.microntek.bootcheck")) {
                String pkname = intent.getStringExtra("class");
                Log.v(TAG, "receive bootcheck: " + pkname);
                if (!pkname.equals(BuildConfig.APPLICATION_ID) && !pkname.equals("phonecallin") && !pkname.equals("phonecallout")) {
                    closeApp();
                }

            } else if (action.equals("com.microntek.playmusic")) {
                try {
                    Log.v(TAG, "receive pause");
                    service.pause();
                } catch (Exception e) {
                }
            } else if (action.equals("com.microntek.lastmusic")) {
                try {
                    Log.v(TAG, "receive prevp");
                    service.previous();
                } catch (Exception e2) {
                }
            } else if (action.equals("com.microntek.nextmusic")) {
                try {
                    Log.v(TAG, "receive next");
                    service.next();
                } catch (Exception e3) {
                }
            } else if (action.equals("com.microntek.stopmusic")) {
                try {
                    Log.v(TAG, "receive stop");
                    service.stopPlayback();
                } catch (Exception e4) {
                }
            } else if (action.equals("com.microntek.musicstartapp")) {
                //MusicServer.this.SendWidgetInitinfo();
            }else if (action.equals("com.microntek.irkeyDown")) {
                int keyCode = intent.getIntExtra("keyCode", 0);

                Log.v(TAG, "receive key " + keyCode);
                //region keyCodes

                switch (keyCode) {
                    case 3:
                        if (service.isPlaying())
                            service.pause();
                        else
                            service.play();
                        return;
                    case 6:
                    case 22:
                    case 45:
                    case 61:
                        service.previous();
                        return;
                    case 13:
                        service.stopPlayback();
                        return;
                    case 14:
                    case 24:
                    case 46:
                    case 62:
                        service.next();
                        return;
                    case 17:
                        service.setRepeatType(PlaybackService.REPEAT_ALL);
                        return;
//                    case VorbisIdentificationHeader.FIELD_FRAMING_FLAG_POS /*29*/:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 1;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 30:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 2;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 31:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 3;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 32:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 4;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 33:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 5;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH /*34*/:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 6;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 35:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 7;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case LameFrame.LAME_HEADER_BUFFER_SIZE /*36*/:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 8;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 37:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 9;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 38:
//                        MusicActivity.this.keynum = (MusicActivity.this.keynum * 10) + 0;
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 39:
//                        if (MusicActivity.this.keynum != 0) {
//                            MusicActivity.this.keynum = 0;
//                        }
//                        MusicActivity.this.handler.removeMessages(6);
//                        MusicActivity.this.handler.sendEmptyMessageDelayed(6, 1000);
//                        return;
//                    case 58:
//                        MusicActivity.this.handler.removeMessages(3);
//                        Message msg = new Message();
//                        msg.what = 3;
//                        msg.arg1 = 1;
//                        MusicActivity.this.handler.sendMessageDelayed(msg, 300);
//                        return;
//                    case 59:
//                        MusicActivity.this.handler.removeMessages(3);
//                        Message msg1 = new Message();
//                        msg1.what = 3;
//                        msg1.arg1 = 2;
//                        MusicActivity.this.handler.sendMessageDelayed(msg1, 300);
//                        return;
                    default:
                        return;
                }
                //endregion
            }
        }
    };

    //endregion

    private PlaybackService service;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.v(TAG, "service bound!");
            service = PlaybackService.getService(iBinder);

            service.addCallback(new PlaybackService.Callback() {
                @Override
                public void update() {
                    Log.v(TAG, "callback update");
                    updateKLD(-1);
                }

                @Override
                public void updateProgress() {
                    Log.v(TAG, "callback updateProgress");
                    updateKLD(service.getTime());
                }

                @Override
                public void onMediaEvent(Media.Event event) {
                    Log.v(TAG, "callback media.event " + event);
                }

                @Override
                public void onMediaPlayerEvent(MediaPlayer.Event event) {
                    Log.v(TAG, "callback mediaplayer.event " + event);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "service disconnected!");
            service = null;
            closeApp();
        }

    };

    private List<Activity> activities = new ArrayList<>();

    Application.ActivityLifecycleCallbacks callback = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.v(TAG, "new activity: " + activity.getLocalClassName());
            activities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
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

/*    private Handler currentPlaybackUpdateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            updateKLD(service.getTime());

            // check every second for changes
            this.sendEmptyMessageDelayed(0, 1000);

            msg.recycle();
        }
    };
*/

    private void updateKLD(long pos){
        Intent intent = new Intent("com.mgh.mghlibs.MghService.SEND_KLD");
        intent.putExtra("com.mgh.mghlibs.MghService.SEND_STR", "VLC");

        if (service != null) {
            if (service.isPlaying() || service.hasMedia()) {
                intent.putExtra("com.mgh.mghlibs.MghService.SEND_TIME", pos);
            }
        }

        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            if (service == null)
                bindService(new Intent(this, PlaybackService.class), mServiceConnection, BIND_AUTO_CREATE);
        } catch (Throwable e){
            Log.e(TAG, "error on binding to PlayBackService", e);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.microntek.playmusic");
        filter.addAction("com.microntek.nextmusic");
        filter.addAction("com.microntek.lastmusic");
        filter.addAction("com.microntek.stopmusic");
        filter.addAction("com.microntek.musicstartapp");
        filter.addAction("com.microntek.irkeyDown");
        filter.addAction("com.microntek.irkeyUp");
        filter.addAction("com.microntek.bootcheck");
        registerReceiver(this.mtcReceiver, filter);

        VLCApplication inst = (VLCApplication) VLCApplication.getAppContext();
        inst.registerActivityLifecycleCallbacks(callback);

        updateKLD(-1);

        //currentPlaybackUpdateHandler.sendEmptyMessageDelayed(0,1000);
    }

    private void closeApp(){
        if (service != null)
            service.stop();

        Log.v(TAG, "closing app");
        VLCApplication inst = (VLCApplication) VLCApplication.getAppContext();
        inst.unregisterActivityLifecycleCallbacks(callback);

        for (Activity act : activities){

            try{
                act.finish();
            }catch (Throwable e){
                Log.w(TAG, "error on closing vlc", e);
            }

        }

        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "destroy");
        if (service != null)
            unbindService(mServiceConnection);

        unregisterReceiver(mtcReceiver);
    }
}
