package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class BaseActivity extends ActionBarActivity {

    public static String CODE_LOG = "RadioTape";

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

    @Override
    public void onStart() {
        EasyTrackerCustom.StartAtivity(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EasyTrackerCustom.StopAtivity(this);
        super.onStop();
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

}
