package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

/**
 * Created by MicheleMaccini on 29/03/2015.
 */
public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    static Intent serviceIntentListen = null;

    private boolean addDaySveglia = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.putExtra(BaseActivity.PARAM_ADDDAY_SVEGLIA, addDaySveglia);

                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);

        serviceIntentListen = new Intent(this, ServiceListen.class);

        Context context = this.getBaseContext();
        context.startService(serviceIntentListen);

        Bundle p = getIntent().getExtras();
        if (p != null) {
            addDaySveglia = p.getBoolean(BaseActivity.PARAM_ADDDAY_SVEGLIA, false);

        }
    }
}
