package com.mrpaloma.radiotape;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;

import java.util.Calendar;

/**
 * Created by MicheleMaccini on 04/04/2015.
 */
public class AlarmManagerHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Intent detailIntent = new Intent(this, SplashActivity.class);
        //detailIntent.putExtra(PARAM_ANDROIDCODE, sCodiceAndroid);
        //startActivity(detailIntent);

        //Intent serviceIntentListen = new Intent(this, ServiceListen.class);

        //startActivity(intent);

        // controllo se la sveglia è attiva
        SharedPreferences prefs = context.getSharedPreferences(BaseActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        boolean active = prefs.getBoolean(BaseActivity.PROPERTY_ATTIVA_SVEGLIA, false);

        if (active) {
            active = true;

            // controllo se il servizio è già in esecuzione
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (ServiceListen.class.getName().equals(service.service.getClassName())) {
                    active = false;
                }
            }

            if (active) {
                active = true;

                Calendar now = Calendar.getInstance();

                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);

                int hourSveglia = prefs.getInt(BaseActivity.PROPERTY_HOUR_SVEGLIA, -1);
                int minuteSveglia = prefs.getInt(BaseActivity.PROPERTY_MINUTE_SVEGLIA, -1);

                active = ((hour == hourSveglia) && (minute == minuteSveglia));

                if (active) {
                    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RadioTape");
                    wl.acquire();

                    Intent service = new Intent(context, SplashActivity.class);
                    service.putExtra(BaseActivity.PARAM_ADDDAY_SVEGLIA, true);
                    service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(service);
                }
            }
        }

        //Intent serviceIntentListen = new Intent(context, ServiceListen.class);
        //context.startService(serviceIntentListen);

/*
        DBHelper mDbh = new DBHelper(context, null, null, 1);
        mDb = mDbh.getWritableDatabase();
        mDb.setLockingEnabled(true);
        mDba = new DBAdapter(context);
        mDba.open();

        Cursor cr = mDb.query("mReminderEntry", null, null, null, null, null, null);
        if (cr.equals(null)) {
            System.out.println("No Data Found");

        } else {
            Date d = new Date();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String today = null;
            if (day == 2) {
                today = "Monday";
            } else if (day == 3) {
                today = "Tuesday";
            } else if (day == 4) {
                today = "Wednesday";
            } else if (day == 5) {
                today = "Thursday";
            } else if (day == 6) {
                today = "Friday";
            } else if (day == 7) {
                today = "Saturday";
            } else if (day == 1) {
                today = "Sunday";
            }

            int system_hour = d.getHours();
            int system_minute = d.getMinutes();
            cr.moveToFirst();
            for (int i = 0; i < cr.getCount(); i++) {
                if (cr.getString(3).equals(system_hour + ":" + system_minute)
                        && cr.getString(1).equals("Daily")) {
                    Intent scheduledIntent = new Intent(context, MyScheduledActivity.class);
                    scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(scheduledIntent);

                    break;

                } else if (cr.getString(3).equals(
                        system_hour + ":" + system_minute)
                        && cr.getString(1).equals(today)) {

                    Intent scheduledIntent = new Intent(context, MyScheduledActivity.class);
                    scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(scheduledIntent);

                    break;
                } else {
                    System.out.println("No Matching");
                }
                cr.moveToNext();
            }
        }
        cr.close();
        mDba.close();
* */
    }

}
