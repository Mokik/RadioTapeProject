package com.mrpaloma.radiotape;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;

/**
 * Created by MicheleMaccini on 04/02/14.
 */
public  class  EasyTrackerCustom {

    public static String TRACK_EVENT = "RecordingRoutes";

    public static String TRACK_TIMING_REGISTERAPP = "RegistrApp";

    public static String TRACK_ACTION_LOOPSERVICELISTEN = "LoopServiceListen";
    public static String TRACK_ACTION_PAUSE = "Pause";
    public static String TRACK_ACTION_PLAY = "Play";

    public static String TRACK_EVENT_CONNESSIONEDATI = "ErroreConnessioneDati";
    public static String TRACK_EVENT_REGISTERAPP = "RegisterApp";
    public static String TRACK_EVENT_GETSETTING = "GetSetting";
    public static String TRACK_EVENT_SETSETTING = "SetSetting";

    public static String TRACK_TIMING_GETSETTING = "GetSetting";
    public static String TRACK_TIMING_SETSETTING = "SetSetting";

    public static String TRACK_LABEL_PAUSE = "Button";
    public static String TRACK_LABEL_PLAY = "Button";

    public static String TRACK_SERVICELISTEN = "ServiceListen";
    public static String TRACK_SERVICELISTEN_INITIALIZEPLAYER = "InitializePlayer";
    public static String TRACK_SERVICELISTEN_INITIALIZEPLAYERAAC = "InitializePlayerAAC";

    public static String DEBUGSI = "debug";

    public static void StartAtivity(Activity activity) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) {

        } else {
            if (activity != null) EasyTracker.getInstance(activity).activityStart(activity);  // Add this method.
        }
    }

    public static void StopAtivity(Activity activity) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) {

        } else {
            if (activity != null) EasyTracker.getInstance(activity).activityStop(activity);  // Add this method.
        }
    }

    public static void AddEvent(Activity activity, String event, String action, String label, Long value)  {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) { } else {

            if (activity != null) {
                // May return null if a EasyTracker has not yet been initialized with a
                // property ID.
                EasyTracker easyTracker = EasyTracker.getInstance(activity);

                // MapBuilder.createEvent().build() returns a Map of event fields and values
                // that are set and sent with the hit.
                easyTracker.send(MapBuilder
                                .createEvent(event,     // Event category (required)
                                        action,  // Event action (required)
                                        label,   // Event label
                                        value)            // Event value
                                .build()
                );
            }

        }
    }

    public static void AddScreen(Activity activity, String nameScreen) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) { } else {

            if (activity != null) {
                // May return null if EasyTracker has not yet been initialized with a property
                // ID.
                EasyTracker easyTracker = EasyTracker.getInstance(activity);

                // This screen name value will remain set on the tracker and sent with
                // hits until it is set to a new value or to null.
                easyTracker.set(Fields.SCREEN_NAME, nameScreen);

                easyTracker.send(MapBuilder
                                .createAppView()
                                .build()
                );
            }

        }
    }

    public static void AddTiming(Activity activity, String resources, long loadTime, String name, String label) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) { } else {

            if (activity != null) {
                // May return null if EasyTracker has not been initialized with a property
                // ID.
                EasyTracker easyTracker = EasyTracker.getInstance(activity);

                easyTracker.send(MapBuilder
                                .createTiming(resources,    // Timing category (required)
                                        loadTime,           // Timing interval in milliseconds (required)
                                        name,               // Timing name
                                        label)              // Timing label
                                .build()
                );
            }

        }
    }

    public static void AddTimingRegisterApp(Activity activity, long recordTime) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) { } else {
            EasyTrackerCustom.AddTiming(activity, TRACK_EVENT_REGISTERAPP, recordTime, TRACK_TIMING_REGISTERAPP, TRACK_TIMING_REGISTERAPP);
        }
    }

    public static void AddException(Activity activity, Exception e, String nameThread) {
        String err = ((e == null) || (e.getMessage() == null)) ? "Errore non specificato" : e.getMessage();

        if ((BuildConfig.BUILD_TYPE == DEBUGSI) && (false)) { } else {
            if (activity != null) {

                // May return null if EasyTracker has not yet been initialized with a
                // property ID.
                EasyTracker easyTracker = EasyTracker.getInstance(activity);

                // StandardExceptionParser is provided to help get meaningful Exception descriptions.
                easyTracker.send(MapBuilder
                                .createException(new StandardExceptionParser(activity, null)              // Context and optional collection of package names
                                                // to be used in reporting the exception.
                                                .getDescription(nameThread + " (" + err + ")",     // The name of the thread on which the exception occurred.
                                                        e),                             // The exception.
                                        false)                          // False indicates a fatal exception
                                .build()
                );
            }
        }

        Log.d(MainActivity.CODE_LOG, nameThread + " - " + err );
    }

    public static void AddTimingGetSetting(Activity activity, long recordTime) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) { } else {
            EasyTrackerCustom.AddTiming(activity, TRACK_EVENT_GETSETTING, recordTime, TRACK_TIMING_GETSETTING, TRACK_TIMING_GETSETTING);
        }
    }

    public static void AddTimingSetSetting(Activity activity, long recordTime) {
        if (BuildConfig.BUILD_TYPE == DEBUGSI) { } else {
            EasyTrackerCustom.AddTiming(activity, TRACK_EVENT_SETSETTING, recordTime, TRACK_TIMING_SETSETTING, TRACK_TIMING_SETSETTING);
        }
    }

}
