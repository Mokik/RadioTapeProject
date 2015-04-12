package com.mrpaloma.radiotape;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MicheleMaccini on 04/04/2015.
 */
public class AlarmManagerHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
/*
        DBHelper mDbh = new DBHelper(context, null, null, 1);
        mDb = mDbh.getWritableDatabase();
        mDb.setLockingEnabled(true);
        mDba = new DBAdapter(context);
        mDba.open();
        Cursor cr = mDb.query("mReminderEntry", null, null, null, null,
                null, null);
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
