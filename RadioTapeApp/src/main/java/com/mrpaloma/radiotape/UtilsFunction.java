package com.mrpaloma.radiotape;

import android.app.Activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by MicheleMaccini on 07/02/2015.
 */
public class UtilsFunction {

    public static SoapObject CallWebService(Activity oActivity, String nameMethod, PropertyInfo[] pi) {
        String SOAP_ACTION1 = "http://tempuri.org/" + nameMethod;
        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME1 = nameMethod;
        String URL = oActivity.getApplicationContext().getResources().getString(R.string.UrlWsService);

        SoapObject response = null;

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            if ((pi != null) && (pi.length > 0)) {

                for(int x = 0; x < pi.length; x = x+1) {
                    request.addProperty(pi[x]);
                }

            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION1, envelope);

            SoapSerializationEnvelope ResponseWs = envelope;
            response = (SoapObject) ResponseWs.getResponse();

        } catch (Exception ex) {
            EasyTrackerCustom.AddException(oActivity, ex, "CallWebService - " + nameMethod);

        }

        return response;
    }

}
