package com.mrpaloma.radiotape;

import android.app.Activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    public static String GetShoutCastServer(String endpoint) {
        //String endpoint = "http://s25.myradiostream.com:5976/admin.cgi?mode=viewxml&page=1&sid=1";
        String returnTitle = "";

        try {
 /*           String url = "http://s25.myradiostream.com:5976/admin.cgi";
            String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
            String param1 = "viewxml";
            String param2 = "1";

            String query = String.format("mode=%s&page=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
            endpoint = url + "?" + query;

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 100000);
            HttpConnectionParams.setSoTimeout(httpParameters, 100000+120000);

            DefaultHttpClient hc = new DefaultHttpClient();
            HttpPost postMethod = new HttpPost(endpoint);
            postMethod.setParams(httpParameters);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username","admin"));
            nameValuePairs.add(new BasicNameValuePair("password","delehawu9646"));
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = hc.execute(postMethod);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                InputStream inStream = entity.getContent();
                //result = Utility.convertStreamToString(inStream);
                //Log.i("---------------- Result", result);
            }*/

            /*String result;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(endpoint);
            request.addHeader("username","admin");
            request.addHeader("pass","delehawu9646");
            ResponseHandler<String> handler = new BasicResponseHandler();

            try {
                result = httpclient.execute(request, handler);

            } catch (ClientProtocolException e) {
                result=e.toString();
            }
            httpclient.getConnectionManager().shutdown();*/

            Authenticator authenticator = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return (new PasswordAuthentication("admin", NumberSerialKey.PASSWORD_ADMIN_STREAM.toCharArray()));
                }
            };
            Authenticator.setDefault(authenticator);

            URL obj = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            //String authorization = "admin:delehawu9646";
            //byte[] encodedBytes;
            //encodedBytes = Base64.encode(authorization.getBytes(), 0);
            //authorization = "Basic " + encodedBytes;
            //con.setRequestProperty("Authorization", authorization);

            int responseCode = con.getResponseCode();
            if (responseCode == 200)
            {
                //BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //String inputLine;
                //StringBuffer response = new StringBuffer();

                String xmlRecords = convertStreamToString(con.getInputStream());

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlRecords));

                Document doc = db.parse(is);
                NodeList nodes = doc.getElementsByTagName("SHOUTCASTSERVER");

                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);

                    NodeList name = element.getElementsByTagName("SONGTITLE");
                    Element line = (Element) name.item(0);

                    String title = getCharacterDataFromElement(line);
                    returnTitle = title;
                }

                // iterate the employees
                /*for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);

                    NodeList name = element.getElementsByTagName("name");
                    Element line = (Element) name.item(0);
                    System.out.println("Name: " + getCharacterDataFromElement(line));

                    NodeList title = element.getElementsByTagName("title");
                    line = (Element) title.item(0);
                    System.out.println("Title: " + getCharacterDataFromElement(line));
                } */


                //while ((inputLine = in.readLine()) != null) {
                //    response.append(inputLine);
                //}
                //in.close();
            }

        } catch(Exception ex) {
            //EasyTrackerCustom.AddException(oActivity, ex, EasyTrackerCustom.TRACK_ACTION_LOOPSERVICELISTEN);

            Log.d(MainActivity.CODE_LOG, "GetShoutCastServer " + ex.getMessage());
        }

        return returnTitle;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    public static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
