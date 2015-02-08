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

    public static SoapPrimitive CallWebService2(Activity oActivity, String nameMethod, PropertyInfo[] pi) {
        String SOAP_ACTION1 = "http://tempuri.org/" + nameMethod;
        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME1 = nameMethod;
        String URL = oActivity.getApplicationContext().getResources().getString(R.string.UrlWsServiceWebPage);

        SoapPrimitive response = null;

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

            int timeout = 1200000;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, timeout);
            androidHttpTransport.call(SOAP_ACTION1, envelope);

            SoapSerializationEnvelope ResponseWs = envelope;
            response = (SoapPrimitive) ResponseWs.getResponse();

        } catch (Exception ex) {
            EasyTrackerCustom.AddException(oActivity, ex, "CallWebService - " + nameMethod);

        }

        return response;
    }

    public static boolean SendActionWebPage(Activity oActivity, String sParam) {
        boolean ret = false;

        try {
            PropertyInfo[] piParam = new PropertyInfo[1];
            piParam [0] = new PropertyInfo();
            piParam [0].setName("action");
            piParam [0].setValue(sParam);
            piParam [0].setType(String.class);

            // chiamo web service per effettuare l'operazione
            SoapPrimitive response = UtilsFunction.CallWebService2(oActivity, "SendActionWebPage", piParam);
            if (response.toString().equals("Ok")) {
                ret = true;
            }

        } catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "UtilsFunction - SendActionWebPage"); }

        return ret;
    }

}
