package com.mrpaloma.radiotape;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by MicheleMaccini on 08/04/2015.
 */
public class TabSveglia extends Fragment {

    private MainActivity oActivity = null;
    private View oFragmentView = null;

    TextView txtHour = null;
    TextView txtMinuti = null;
    TextView txtAttiva = null;
    TextView txtDisattiva = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        oFragmentView = inflater.inflate(R.layout.tab_sveglia, container, false);
        oActivity = (MainActivity)getActivity();

        checkTextSveglia();

        txtHour = (TextView)oFragmentView.findViewById(R.id.lblOraSveglia);
        txtHour.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showTimePicker();
            }
        });

        txtMinuti = (TextView)oFragmentView.findViewById(R.id.lblMinutiSveglia);
        txtMinuti.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showTimePicker();
            }
        });

        txtAttiva = (TextView)oFragmentView.findViewById(R.id.lblAttiva);
        txtAttiva.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                oActivity.setSveglia(oActivity.getBaseContext(), Integer.parseInt(txtHour.getText().toString()), Integer.parseInt(txtMinuti.getText().toString()));
                oActivity.setAlarmClock(false);

                oActivity.setSvegliaAttiva(oActivity.getBaseContext(), true);
                checkTextSveglia();
            }
        });

        txtDisattiva = (TextView)oFragmentView.findViewById(R.id.lblDisattiva);
        txtDisattiva.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //oActivity.setSveglia(oActivity.getBaseContext(), -1, -1);
                oActivity.deleteAlarmClock();

                oActivity.setSvegliaAttiva(oActivity.getBaseContext(), false);
                checkTextSveglia();
            }
        });

        return oFragmentView;
    }

    protected void checkTextSveglia() {
        txtAttiva = (TextView)oFragmentView.findViewById(R.id.lblAttiva);
        txtDisattiva = (TextView)oFragmentView.findViewById(R.id.lblDisattiva);

        int hour = oActivity.getHourSveglia(oActivity.getBaseContext());
        int minute = oActivity.getMinuteSveglia(oActivity.getBaseContext());

        txtMinuti = (TextView)oFragmentView.findViewById(R.id.lblMinutiSveglia);
        txtHour = (TextView)oFragmentView.findViewById(R.id.lblOraSveglia);

        if (!(oActivity.getSvegliaImpostata(oActivity.getBaseContext()))) {
            txtAttiva.setVisibility(View.VISIBLE);
            txtDisattiva.setVisibility(View.GONE);

            //oActivity.setSveglia(oActivity.getBaseContext(), -1, -1);

        } else {
            txtAttiva.setVisibility(View.GONE);
            txtDisattiva.setVisibility(View.VISIBLE);

        }

        String sHour = hour + "";
        if (hour < 10) { sHour = "0" + hour; }

        String sMinute = minute + "";
        if (minute < 10) { sMinute = "0" + minute; }

        if (hour >= 0) txtHour.setText(sHour);
        if (minute >= 0)txtMinuti.setText(sMinute);
    }

    protected void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(oActivity, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hour = selectedHour + "";
                if (selectedHour < 10) { hour = "0" + selectedHour; }

                String minute = selectedMinute + "";
                if (selectedMinute < 10) { minute = "0" + selectedMinute; }

                txtHour.setText(hour);
                txtMinuti.setText(minute);

                oActivity.setSveglia(oActivity.getBaseContext(), selectedHour, selectedMinute);
                if (oActivity.getSvegliaImpostata(oActivity.getBaseContext())) {
                    oActivity.setSveglia(oActivity.getBaseContext(), Integer.parseInt(txtHour.getText().toString()), Integer.parseInt(txtMinuti.getText().toString()));
                    oActivity.setAlarmClock(false);
                }

            }

        }, hour, minute, true);//Yes 24 hour time

        mTimePicker.setTitle(getResources().getString(R.string.selezionaOra));
        mTimePicker.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
