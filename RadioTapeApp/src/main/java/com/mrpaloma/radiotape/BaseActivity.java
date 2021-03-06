package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class BaseActivity extends ActionBarActivity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";

    private static final String PROPERTY_STOP_NOTIFICATION = "stopNotification";
    public static final String PROPERTY_HOUR_SVEGLIA = "hourSveglia";
    public static final String PROPERTY_MINUTE_SVEGLIA = "minuteSveglia";
    public static final String PROPERTY_ATTIVA_SVEGLIA = "attivaSveglia";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static String CODE_LOG = "RadioTape";
    public static String PARAM_ANDROIDCODE = "AndroidCode";
    public static String PARAM_ADDDAY_SVEGLIA = "AddDaySveglia";
    //public static String PARAM_SERVICE = "ServiceIntent";

    protected boolean startPlaying = false;

    protected ServiceListen srvListen = null;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            // TODO Auto-generated method stub

            Log.d(MainActivity.CODE_LOG, "Connected service listen");

            srvListen = ((ServiceListen.LocalBinder) binder).getService();
            if (srvListen != null) {
                srvListen.setActivityLaunch(BaseActivity.this);

                if (startPlaying) srvListen.startPlaying();
                startPlaying = false;

                if (getStopListenNotification()) srvListen.stopPlaying();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // TODO Auto-generated method stub
            // (viene invocato solo se crash il servizio o distrutto)

            Log.d(MainActivity.CODE_LOG, "Disconnected service listen");
        }

    };
    protected Activity oActivity = null;
    protected boolean playingMusic = false;

    // serve per ricevere i messaggi che mi arrivano dal service
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //Boolean stopService = intent.getBooleanExtra(ServiceListen.NAME_MESSAGE_STOPSERVICE, false);
                //Boolean startService = intent.getBooleanExtra(ServiceListen.NAME_MESSAGE_STARTSERVICE, false);
                playingMusic = intent.getBooleanExtra(ServiceListen.NAME_MESSAGE_PLAYINGMUSIC, false);

            } catch (Exception e) {
                EasyTrackerCustom.AddException((BaseActivity) context, e, "ricezione messagio servicelisten");

            }
        }
    };
    protected boolean stopListenNotification = false;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if (!stopListenNotification) {
                updateControl(); // override

                updateHandler.postDelayed(this, 900);

            } else {
                CloseApp();
            }
        }
    };
    protected boolean updatePalinsestoAll = true;
    protected GoogleCloudMessaging gcm;
    protected AtomicInteger msgId = new AtomicInteger();
    protected SharedPreferences prefs;
    protected Context context = null;
    protected String regid;
    protected String sCodiceAndroid;
    Intent serviceIntentListen = null;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = NumberSerialKey.SENDER_ID_CLOUD_MESSAGE;
    private Handler updateHandler = new Handler();
    private boolean bindedListening = false;

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private AsyncTask<Void, Void, Void> mRegisterAppTask;

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

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    protected static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public boolean getStopListenNotification() {
        stopListenNotification = getStopNotification(this.getBaseContext());
        return stopListenNotification;
    }

    public void StopListen() {
        if (srvListen != null) {
            srvListen.setStopThread();
        }

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

    /*public void PauseListenAAC() {
        if (srvListen != null) {
            srvListen.pausePlayingAAC();
        }
    }*/

   /* public void PlayListenAAC() {
        if (srvListen != null) {
            srvListen.startPlayingAAC();
        }
    }*/

    public void PlayListen() {
        if (srvListen != null) {
            srvListen.startPlaying();
        }
    }

    protected void updateControl() {
    }

    /*protected void StartActivitySveglia() {

        // Avvio la nuova Activity
        Intent detailIntent = new Intent(this, SvegliaActivity.class);
        //detailIntent.putExtra(PARAM_ANDROIDCODE, sCodiceAndroid);

        startActivity(detailIntent);
        finish();

    }*/

    protected void StartActivitySetting() {

        // Avvio la nuova Activity
        Intent detailIntent = new Intent(this, SettingActivity.class);
        detailIntent.putExtra(PARAM_ANDROIDCODE, sCodiceAndroid);

        startActivity(detailIntent);
        finish();

    }

    public void CloseApp() {
        //if (updateHandler != null) updateHandler.removeCallbacks(updateTimerThread);

        StopListen();

        finish();
        moveTaskToBack(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        EasyTrackerCustom.StartAtivity(this);

        if (updateHandler == null) updateHandler = new Handler();
        if (updateHandler != null) updateHandler.postDelayed(updateTimerThread, 200);

        stopListenNotification = getStopNotification(oActivity.getBaseContext());

        oActivity = this;
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTrackerCustom.StopAtivity(this);

        if (updateHandler != null) updateHandler.removeCallbacks(updateTimerThread);
        updateHandler = null;

        disconnectServiceListening();
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
        } catch (Exception ex) {
            EasyTrackerCustom.AddException(this, ex, "MainActivity - onPause");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void StartListenService() {
        Intent serviceInt = SplashActivity.serviceIntentListen;
        if (serviceInt != null) {
            startPlaying = true;
            SplashActivity.serviceIntentListen = null;

            serviceIntentListen = serviceInt;
            connectServiceListening(); // collego il servizio per avere informazioni

        } else {
            if (!stopListenNotification) {
                // avvio il servizio
                serviceIntentListen = new Intent(this, ServiceListen.class);

                Context context = this.getBaseContext();
                //serviceIntentListen.putExtra(ServiceListen.PARAM_STOP_MUSIC, getStopNotification(context));
                //serviceIntentUpload.putExtra(ServiceUploading.PARAM_ACTION_UPLOAD, true);
                //serviceIntentUpload.putExtra(ServiceUploading.PARAM_ACTION_CREATEFILEKML, false);
                if ((context != null) && (serviceIntentListen != null)) context.startService(serviceIntentListen);

                connectServiceListening(); // collego il servizio per avere informazioni
            }
        }
    }

    public void StopListenService() {
        disconnectServiceListening(); // disconnetto il servizio

		if (serviceIntentListen != null) {
			// fermo il servizio
			Context context = this.getBaseContext();
			if (context != null) context.stopService(serviceIntentListen);		
		}
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

    // serve per sapere quando applicazione chiusa dalle notifiche
    protected void setStopNotification(Context context, boolean stop) {
        final SharedPreferences prefs = getGCMPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PROPERTY_STOP_NOTIFICATION, stop);
        editor.commit();
    }

    protected boolean getStopNotification(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        return prefs.getBoolean(PROPERTY_STOP_NOTIFICATION, false);
    }

    protected void setSveglia(Context context, int hour, int minute) {
        final SharedPreferences prefs = getGCMPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PROPERTY_HOUR_SVEGLIA, hour);
        editor.putInt(PROPERTY_MINUTE_SVEGLIA, minute);
        editor.commit();
    }

    protected int getHourSveglia(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);

        return prefs.getInt(PROPERTY_HOUR_SVEGLIA, -1);
    }

    protected int getMinuteSveglia(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);

        return prefs.getInt(PROPERTY_MINUTE_SVEGLIA, -1);
    }

    protected void setSvegliaAttiva(Context context, boolean attiva) {
        final SharedPreferences prefs = getGCMPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PROPERTY_ATTIVA_SVEGLIA, attiva);
        editor.commit();
    }

    protected boolean getSvegliaAttiva(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        return prefs.getBoolean(PROPERTY_ATTIVA_SVEGLIA, false);
    }

    protected boolean getSvegliaImpostata(Context context) {
        return getSvegliaAttiva(context);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    protected boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(CODE_LOG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    protected String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(CODE_LOG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(CODE_LOG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    protected SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(BaseActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    protected void registerInBackground() {
        final long start = System.currentTimeMillis();

        mRegisterAppTask = new AsyncTask<Void, Void, Void>() {
            String msg = "";

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(oActivity.getBaseContext());
                    }

                    regid = gcm.register(SENDER_ID);
                    sCodiceAndroid = regid;

                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(oActivity.getBaseContext(), regid);


                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.

                    EasyTrackerCustom.AddException(oActivity, ex, "Chiamata Registrazione App Activity iniziale " + msg);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                long durationSave = System.currentTimeMillis() - start;
                EasyTrackerCustom.AddTimingRegisterApp(oActivity, durationSave);

            }

        };
        mRegisterAppTask.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        ServerUtilitiesPushNotification.register(context, sCodiceAndroid);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(CODE_LOG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

}
