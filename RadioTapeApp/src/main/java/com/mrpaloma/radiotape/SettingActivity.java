package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

/**
 * Created by MicheleMaccini on 08/02/2015.
 */
public class SettingActivity extends ActionBarActivity {

    private CheckBox chkNotifiche = null;

    private Boolean bSendNotification = false;
    private Boolean isCheckedNotifiche = false;
    private String sCodiceAndroid = "";
    private String sTipoApp = "";

    private AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sTipoApp = getString(R.string.tipo_app);

        Bundle p = getIntent().getExtras();
        if (sCodiceAndroid.equals("")) sCodiceAndroid = p.getString(BaseActivity.PARAM_ANDROIDCODE);
        if (sCodiceAndroid.equals("")) Toast.makeText(this.getBaseContext(), getResources().getString(R.string.txtErroreInternoRiprovare), Toast.LENGTH_LONG).show();

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        chkNotifiche = (CheckBox) findViewById(R.id.chkNotifiche);
        chkNotifiche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedNotifiche = isChecked;
            }
        });

        chkNotifiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSetting(isCheckedNotifiche);
            }
        });

        chkNotifiche.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            StartActivityMain();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void StartActivityMain() {

        // Avvio la nuova Activity
        Intent detailIntent = new Intent(this, MainActivity.class);
        //detailIntent.putExtra(PARAM_UNIQUECODE, "");

        startActivity(detailIntent);
        finish();

    }

    @Override
    public void onStart() {
        super.onStart();

        GetSetting();

        EasyTrackerCustom.StartAtivity(this);
        EasyTrackerCustom.AddScreen(this, "Setting");
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTrackerCustom.StopAtivity(this);
    }

    protected void GetSetting() {
        final Activity oActivity = this;
        final long start = System.currentTimeMillis();

        mRegisterTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                PropertyInfo[] piParam = new PropertyInfo[2];

                piParam[0] = new PropertyInfo();
                piParam[0].setName("sCodiceAndroid");
                piParam[0].setValue(sCodiceAndroid);
                piParam[0].setType(Integer.class);

                piParam[1] = new PropertyInfo();
                piParam[1].setName("sTipoApp");
                piParam[1].setValue(sTipoApp);
                piParam[1].setType(String.class);

                try {

                    // chiamo il web service
                    SoapObject response = UtilsFunction.CallWebService(oActivity, "GetSetting", piParam);
                    String sNotifiche = response.getPropertyAsString("SendNotifiche").toLowerCase();

                    bSendNotification = sNotifiche.equals("true");

                } catch (Exception ex) {
                    EasyTrackerCustom.AddException(oActivity, ex, "Chiamata Web Service Setting");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mRegisterTask = null;

                if (chkNotifiche != null) chkNotifiche.setChecked(bSendNotification);
                if (chkNotifiche != null) chkNotifiche.setVisibility(View.VISIBLE);

                long durationSave = System.currentTimeMillis() - start;
                EasyTrackerCustom.AddTimingGetSetting(oActivity, durationSave);
            }

        };
        mRegisterTask.execute(null, null, null);
    }

    protected void SetSetting(Boolean bSendNotificationParam) {
        final Activity oActivity = this;
        final boolean bSendNotification = bSendNotificationParam;
        final long start = System.currentTimeMillis();

        chkNotifiche.setVisibility(View.GONE);

        mRegisterTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                PropertyInfo[] piParam = new PropertyInfo[3];

                piParam[0] = new PropertyInfo();
                piParam[0].setName("sCodiceAndroid");
                piParam[0].setValue(sCodiceAndroid);
                piParam[0].setType(Integer.class);

                piParam[1] = new PropertyInfo();
                piParam[1].setName("sTipoApp");
                piParam[1].setValue(sTipoApp);
                piParam[1].setType(String.class);

                piParam[2] = new PropertyInfo();
                piParam[2].setName("bSendNotification");
                piParam[2].setValue(bSendNotification);
                piParam[2].setType(boolean.class);

                try {

                    // chiamo il web service
                    SoapObject response = UtilsFunction.CallWebService(oActivity, "SetSendNotification", piParam);

                } catch (Exception ex) {
                    EasyTrackerCustom.AddException(oActivity, ex, "Chiamata Web Service Setting");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mRegisterTask = null;

                chkNotifiche.setVisibility(View.VISIBLE);

                long durationSave = System.currentTimeMillis() - start;
                EasyTrackerCustom.AddTimingSetSetting(oActivity, durationSave);
            }

        };
        mRegisterTask.execute(null, null, null);
    }

}
