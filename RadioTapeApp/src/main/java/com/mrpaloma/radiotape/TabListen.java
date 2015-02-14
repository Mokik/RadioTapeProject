package com.mrpaloma.radiotape;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class TabListen extends Fragment {

    private View oFragmentView = null;
    public View getFragmentView() {return oFragmentView;}

    protected Button btnPause = null;
    public Button getBtnPause() {return btnPause;}

    protected Button btnPlay = null;
    public Button getBtnPlay() {return btnPlay;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        oFragmentView = inflater.inflate(R.layout.tab_listen, container, false);

        final MainActivity oActivity = (MainActivity)getActivity();

        btnPause = (Button) oFragmentView.findViewById(R.id.btnPause);
        btnPlay = (Button) oFragmentView.findViewById(R.id.btnPlay);

        if (btnPause != null) {
            btnPause.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        if (oActivity != null) {
                            oActivity.PauseListen();

                            if (btnPlay != null) btnPlay.setVisibility(View.VISIBLE);
                            if (btnPause != null) btnPause.setVisibility(View.GONE);

                            EasyTrackerCustom.AddEvent(oActivity, EasyTrackerCustom.TRACK_EVENT, EasyTrackerCustom.TRACK_ACTION_PAUSE, EasyTrackerCustom.TRACK_LABEL_PAUSE, null);
                        }

                    }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "TabListen - onCreateView - Pause"); }
                }

            });
        }

        if (btnPlay != null) {
            btnPlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        if (oActivity != null) {
                            oActivity.PlayListen();

                            if (btnPlay != null) btnPlay.setVisibility(View.GONE);
                            if (btnPause != null) btnPause.setVisibility(View.VISIBLE);

                            EasyTrackerCustom.AddEvent(oActivity, EasyTrackerCustom.TRACK_EVENT, EasyTrackerCustom.TRACK_ACTION_PLAY, EasyTrackerCustom.TRACK_LABEL_PLAY, null);
                        }

                    }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "TabListen - onCreateView - Play"); }
                }

            });
        }


        return oFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
