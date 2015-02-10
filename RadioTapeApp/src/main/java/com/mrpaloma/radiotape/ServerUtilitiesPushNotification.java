package com.mrpaloma.radiotape;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by MicheleMaccini on 10/02/2015.
 */
public class ServerUtilitiesPushNotification {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    static final String SERVER_URL = "http://www.mrpaloma.com/radiotape/register.asp";

    public static void register(final Context context, final String regId) {
        String serverUrl = SERVER_URL;

        String sEmulator = "";
        if (Build.BRAND.equalsIgnoreCase("generic")) { sEmulator = "I am an emulator"; }

        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId); // parametro che viene inviato post alla pagine che lo registra nel database
        params.put("note", sEmulator);

        Log.v(BaseActivity.CODE_LOG, "register regId " + regId + " - " + sEmulator);

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                post(serverUrl, params);

                return;
            } catch (IOException e) {
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
                backoff *= 2;
            }
        }
    }

    public static void unregister(final Context context, final String regId) {
        String serverUrl = SERVER_URL + "?unregister=1";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);

        Log.v("unregister regId",regId+"");

        try {
            post(serverUrl, params);

        } catch (IOException e) {
        }
    }

    private static void post(String endpoint, Map<String, String> params) throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Errore " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
