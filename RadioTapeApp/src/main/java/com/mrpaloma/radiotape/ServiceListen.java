package com.mrpaloma.radiotape;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.IOException;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class ServiceListen extends Service {

    public static String NAME_MESSAGE_INTENT = "MessageServiceListen";

    public static String NAME_MESSAGE_STOPSERVICE = "StopService";
    public static String NAME_MESSAGE_STARTSERVICE = "StartService";
    public static String NAME_MESSAGE_PLAYINGMUSIC = "PlayingMusic";

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_listen_started;
    public static int ONGOING_NOTIFICATION_ID = 2;

    private ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    private NotificationManager mNM;

    private BaseActivity oActivity = null;
    public void setActivityLaunch(BaseActivity activity) {oActivity = activity;}

    private Boolean loopWork = false;
    public void setStopThread() {loopWork = false;}

    public static String PARAM_PALINSESTO_NOW = "1";
    public static String PARAM_PALINSESTO_ALL = "";

    protected Palinsesto palinsestoToday = new Palinsesto();
    public Palinsesto getPalinsestoToday() {return palinsestoToday;}

    protected Palinsesto palinsestoAll = new Palinsesto();
    public Palinsesto getPalinsestoAll() {return palinsestoAll;}

    public void invokeWSPalinsesto(String sParam){
        final String sParamWs = sParam;

        AsyncTask<Void, Void, Void> mSendEmailTask  = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                PropertyInfo[] piParam = new PropertyInfo[1];

                piParam[0] = new PropertyInfo();
                piParam[0].setName("sParam");
                piParam[0].setValue(sParamWs);
                piParam[0].setType(String.class);

                SoapObject response = UtilsFunction.CallWebService(oActivity, "GetPalinsesto", piParam);
                SoapObject resultWs = (SoapObject) response.getProperty(1);

                for(int i=0; i<resultWs.getPropertyCount(); i++) {
                    SoapObject giorno = (SoapObject) resultWs.getProperty(i);

                    int iOrdinamento = i;

                    String sGiorno = giorno.getPropertyAsString(3);

                    String sOraInizio = giorno.getPropertyAsString(2).toString();
                    String sTitolo = giorno.getPropertyAsString(0).toString();
                    String sDescrizione = giorno.getPropertyAsString(1).toString();

                    String sImage = giorno.getPropertyAsString(4).toString();

                    Palinsesto.Giorno toDayPalinsesto = new Palinsesto.Giorno(iOrdinamento, sGiorno, sOraInizio, sTitolo, sDescrizione, sImage);
                    if (sParamWs.equals(PARAM_PALINSESTO_NOW))palinsestoToday.addItem(toDayPalinsesto);
                    if (sParamWs.equals(PARAM_PALINSESTO_ALL))palinsestoAll.addItem(toDayPalinsesto);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {}

        }.execute(null, null, null);
    }

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        ServiceListen getService() {
            return ServiceListen.this;
        }
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceListen", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void onDestroy() {

        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        try {

            // Display a notification about us starting.  We put an icon in the status bar.
            CharSequence text = getText(R.string.local_service_listen_started);
            showNotification(text);

            // inizializzo player
            if (player == null) initializeMediaPlayer();

            // avvio player
            if ((player!=null) && (!player.isPlaying())) startPlaying();

        } catch (Exception e) {
            EasyTrackerCustom.AddException(null, e, EasyTrackerCustom.TRACK_SERVICELISTEN);

        }

        return Service.START_NOT_STICKY;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(MainActivity.CODE_LOG, "Avviato loop");

            synchronized (this) {
                loopWork = true;

                try {

                    // invio messaggio per indicare che il servizio è partito
                    SendMessageStartService();

                    int timeSleep = 3000;
                    int callWsPalinsesto = 0;
                    boolean allPalinsesto = true;

                    // controllo se ho tutti i parametri valorizzati
                    try { while ((oActivity == null)) { Thread.sleep(100); } } catch (Exception e) { Log.d(MainActivity.CODE_LOG, "Listen wait param service " + e.getMessage()); }

                    // cycle
                    while (loopWork) {
                        SendMessagePlayingMusic();

                        // controllo se devo chiamare il palinsesto
                        if ((callWsPalinsesto == 0) && (oActivity != null)) {
                            if (BaseActivity.getIsConnection(oActivity)) {
                                if (palinsestoToday != null) palinsestoToday.ITEMS.clear();
                                invokeWSPalinsesto(PARAM_PALINSESTO_NOW);
                            }
                            callWsPalinsesto++;

                        } else {
                            callWsPalinsesto++;
                            if (callWsPalinsesto > 10) callWsPalinsesto = 0;
                        }

                        // controllo tutto il palinsesto
                        if (allPalinsesto)                       {
                            if (palinsestoAll != null) palinsestoAll.ITEMS.clear();

                            invokeWSPalinsesto(PARAM_PALINSESTO_ALL);
                            allPalinsesto = false;
                        }

                        Thread.sleep(timeSleep);
                    }

                    stopPlaying();

                } catch (Exception e) {
                    EasyTrackerCustom.AddException(oActivity, e, EasyTrackerCustom.TRACK_ACTION_LOOPSERVICELISTEN);

                    Log.d(MainActivity.CODE_LOG, "Listen service " + e.getMessage());

                } finally {

                    // invio messaggio per indicare che il servizio ha finito
                    SendMessageStopService();

                }
            }

            Log.d(MainActivity.CODE_LOG, "Terminato loop");
        }
    }

    protected void SendMessageStopService() {
        Intent intent = new Intent(NAME_MESSAGE_INTENT);
        intent.putExtra(NAME_MESSAGE_STOPSERVICE, true);
        intent.putExtra(NAME_MESSAGE_STARTSERVICE, false);
        intent.putExtra(NAME_MESSAGE_PLAYINGMUSIC, false);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    protected void SendMessageStartService() {
        Intent intent = new Intent(NAME_MESSAGE_INTENT);
        intent.putExtra(NAME_MESSAGE_STOPSERVICE, false);
        intent.putExtra(NAME_MESSAGE_STARTSERVICE, true);
        intent.putExtra(NAME_MESSAGE_PLAYINGMUSIC, false);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    protected void SendMessagePlayingMusic() {
        boolean playing = false;
        if (player != null) playing = player.isPlaying();

        Intent intent = new Intent(NAME_MESSAGE_INTENT);
        intent.putExtra(NAME_MESSAGE_STOPSERVICE, false);
        intent.putExtra(NAME_MESSAGE_STARTSERVICE, false);
        intent.putExtra(NAME_MESSAGE_PLAYINGMUSIC, playing);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(CharSequence text) {

        // In this sample, we'll use the same text for the ticker and the expanded notification
        //text = getText(R.string.local_service_upload_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.app_name), text, contentIntent);

        // Send the notification.
        //mNM.notify(NOTIFICATION, notification);

        // icona nel tab "in corso" e non solo nelle notifiche
        startForeground(ONGOING_NOTIFICATION_ID, notification);

    }

    // Visualizza testo
    private void setTextNotification(String testoView) {
        Context oContext = oActivity.getBaseContext();
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) oContext.getSystemService(ns);

        Notification notification = new Notification(R.drawable.ic_launcher, testoView, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.app_name), testoView, contentIntent);

        // Send the notification.
        //mNM.notify(NOTIFICATION, notification);

        // icona nel tab "in corso" e non solo nelle notifiche
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private MediaPlayer player;
    private void initializeMediaPlayer() {

        try {
            player = new MediaPlayer();
            player.setDataSource(getResources().getString(R.string.urlRadio));

        } catch (Exception e) {
            EasyTrackerCustom.AddException(null, e, EasyTrackerCustom.TRACK_SERVICELISTEN_INITIALIZEPLAYER);
        }

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    public void startPlaying() {
        if (player == null) return;

        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }
    }

    public void pausePlaying() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
        }
    }

}
