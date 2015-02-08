package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by MicheleMaccini on 08/02/2015.
 */
public class SettingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        EasyTrackerCustom.StartAtivity(this);
        EasyTrackerCustom.AddScreen(this, "Setting");
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTrackerCustom.StopAtivity(this);
    }

}
