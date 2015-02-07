package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class BaseActivity extends ActionBarActivity {

    public static String CODE_LOG = "RadioTape";

    private Handler updateHandler = new Handler();
    protected ServiceListen srvListen = null;
    private boolean bindedListening = false;
    protected Activity oActivity = null;
    Intent serviceIntentListen = null;

    // controllo se ho la connessione dei dati
    public static Boolean getIsConnection(Activity oActivity) {
        return checkConnection(oActivity);
    }
    protected static Boolean checkConnection(Activity oActivity) {
        Boolean isInternetPresent = false;

        try {
            Context context = oActivity.getBaseContext();

            // Connection detector class
            ConnectionDetector cd = new ConnectionDetector(context);

            // Check if Internet present
            isInternetPresent = cd.isConnectionToInternet();
            cd = null;

        } catch (Exception e) {
            EasyTrackerCustom.AddException(oActivity, e, "BaseActivity - checkConnection");

        }

        return isInternetPresent;
    }

    public void StopListen() {
        if (srvListen != null) { srvListen.setStopThread(); }

        StopListenService();

        // disconnetto il servizio
        if ((mConnection != null) && (srvListen != null)) disconnectServiceListening();
        serviceIntentListen = null;
    }

    public void PauseListen() {
        if (srvListen != null) {
            srvListen.pausePlaying();
        }
    }

    public void PlayListen() {
        if (srvListen != null) {
            srvListen.startPlaying();
        }
    }

    protected void updateControl() {}

    @Override
    public void onStart() {
        super.onStart();

        EasyTrackerCustom.StartAtivity(this);

        if (updateHandler == null) updateHandler = new Handler();
        if (updateHandler != null) updateHandler.postDelayed(updateTimerThread, 200);

        oActivity = this;
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTrackerCustom.StopAtivity(this);

        if (updateHandler != null) updateHandler.removeCallbacks(updateTimerThread);
        updateHandler = null;
    }

    @Override
    protected void onPause() {
        try { super.onPause(); }
        catch(Exception ex) { EasyTrackerCustom.AddException(this, ex, "MainActivity - onPause"); }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            updateControl(); // override

            updateHandler.postDelayed(this, 900);
        }
    };

    public void StartListenService() {
        // avvio il servizio

        Context context = this.getBaseContext();
        //serviceIntentUpload.putExtra(ServiceUploading.PARAM_ACTION_UNIQUEROUTE, getUniqueCode());
        //serviceIntentUpload.putExtra(ServiceUploading.PARAM_ACTION_UPLOAD, true);
        //serviceIntentUpload.putExtra(ServiceUploading.PARAM_ACTION_CREATEFILEKML, false);
        context.startService(serviceIntentListen);

        connectServiceListening(); // collego il servizio per avere informazioni
    }

    public void StopListenService() {
        disconnectServiceListening(); // disconnetto il servizio

        // fermo il servizio
        Context context = this.getBaseContext();
        context.stopService(serviceIntentListen);
    }

    // controllo del servizio
    protected void connectServiceListening() {
        try {

            // serve per ricevere i messaggi dal service
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ServiceListen.NAME_MESSAGE_INTENT));
            bindService(serviceIntentListen, mConnection, Context.BIND_AUTO_CREATE);

            bindedListening = true;

        } catch (Exception e) {
            EasyTrackerCustom.AddException(this, e, "connectServiceUploading");

        }
    }
    protected void disconnectServiceListening() {
        try {
            if (bindedListening) if (mConnection != null) unbindService(mConnection);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

            bindedListening = false;

        } catch (Exception e) {
            EasyTrackerCustom.AddException(this, e, "disconnectServiceUploading");

        }
    }

    // serve per ricevere i messaggi che mi arrivano dal service
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean stopServiceDefault = false;
            Boolean startServiceDefault = false;
            Boolean playingMusicDefault = false;

            try {
                Boolean stopService = intent.getBooleanExtra(ServiceListen.NAME_MESSAGE_STOPSERVICE, stopServiceDefault);
                Boolean startService = intent.getBooleanExtra(ServiceListen.NAME_MESSAGE_STARTSERVICE, startServiceDefault);
                Boolean playingMusic = intent.getBooleanExtra(ServiceListen.NAME_MESSAGE_PLAYINGMUSIC, playingMusicDefault);

                if (!playingMusic) {
                    playingMusic = playingMusic;
                }

            } catch (Exception e) {
                EasyTrackerCustom.AddException((BaseActivity)context, e, "ricezione messagio servicelisten");

            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            // TODO Auto-generated method stub

            Log.d(MainActivity.CODE_LOG, "Connected service listen");

            srvListen = ((ServiceListen.LocalBinder) binder).getService();
            if (srvListen != null) {
                srvListen.setActivityLaunch(BaseActivity.this);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // TODO Auto-generated method stub
            // (viene invocato solo se crash il servizio o distrutto)

            Log.d(MainActivity.CODE_LOG, "Disconnected service listen");
        }

    };

}
