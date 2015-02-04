package com.mrpaloma.radiotape;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionDetector {
    private Context context ;

    public ConnectionDetector(Context context){
        this.context = context ;
    }

    public boolean isConnectionToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivity != null )
        {
            try {

                NetworkInfo [] info = connectivity.getAllNetworkInfo();

                if(info != null)
                    for(int i = 0 ; i < info.length ;i++)
                        if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true ;
                        }

            } catch (Exception ex ) { Log.d(BaseActivity.CODE_LOG, "Connection " + ex.getMessage());}
        }

        return false ;
    }
}

