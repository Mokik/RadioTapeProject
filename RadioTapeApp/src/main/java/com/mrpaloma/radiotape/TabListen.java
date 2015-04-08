package com.mrpaloma.radiotape;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class TabListen extends Fragment {

    private MainActivity oActivity = null;

    private View oFragmentView = null;
    public View getFragmentView() {return oFragmentView;}

    protected Button btnPause = null;
    public Button getBtnPause() {return btnPause;}

    protected Button btnPlay = null;
    public Button getBtnPlay() {return btnPlay;}

    protected ImageView imgPlay = null;
    public ImageView getImgPlay() {return imgPlay;}

    protected ImageView imgPause = null;
    public ImageView getImgPause() {return imgPause;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        oFragmentView = inflater.inflate(R.layout.tab_listen, container, false);
        oActivity = (MainActivity)getActivity();

        //btnPause = (Button) oFragmentView.findViewById(R.id.btnPause);
        //btnPlay = (Button) oFragmentView.findViewById(R.id.btnPlay);

        //new ImageLoadTask("http://www.mrpaloma.com/radiotape/pes.jpg", (ImageView)oFragmentView.findViewById(R.id.iconProgramma)).execute();

        /*if (btnPause != null) {
            btnPause.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) { pauseListen(); }

            });
        }

        if (btnPlay != null) {
            btnPlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) { playListen(); }

            });
        }*/

        /*ImageView imgR = (ImageView)oFragmentView.findViewById(R.id.iconRadioTapeListen);
        imgR.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(getResources().getString(R.string.urlRadioTape)));
                startActivity(intent);
            }
        });

        ImageView imgT = (ImageView)oFragmentView.findViewById(R.id.iconTwitterListen);
        imgT.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(getResources().getString(R.string.urlTwitter)));
                startActivity(intent);
            }
        });

        ImageView imgF = (ImageView)oFragmentView.findViewById(R.id.iconFacebookListen);
        imgF.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(getResources().getString(R.string.urlFacebook)));
                startActivity(intent);
            }
        });

        ImageView imgY = (ImageView)oFragmentView.findViewById(R.id.iconYoutubeListen);
        imgY.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(getResources().getString(R.string.urlYouTube)));
                startActivity(intent);
            }
        });*/

        //TextView smsNumber = (TextView)oFragmentView.findViewById(R.id.txtTelefono);
        //smsNumber.setOnClickListener(new View.OnClickListener(){
            //public void onClick(View v){
                //sendSms();
            //}
        //});

        //ImageView imgM = (ImageView)oFragmentView.findViewById(R.id.iconTelefonoListen);
        //imgM.setOnClickListener(new View.OnClickListener(){
            //public void onClick(View v){
                //sendSms();
            //}
        //});

        imgPlay = (ImageView)oFragmentView.findViewById(R.id.iconPlayListen);
        imgPlay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                playListen();
            }
        });

        imgPause = (ImageView)oFragmentView.findViewById(R.id.iconPauseListen);
        imgPause.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                pauseListen();
            }
        });

        ImageView imgS = (ImageView)oFragmentView.findViewById(R.id.iconShareListen);
        imgS.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                shareListen();
            }
        });

        return oFragmentView;
    }

    protected void shareListen() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Ascolta RadioTape. " + getResources().getString(R.string.urlRadioTape));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        EasyTrackerCustom.AddEvent(oActivity, EasyTrackerCustom.TRACK_EVENT, EasyTrackerCustom.TRACK_ACTION_SHARELISTEN, EasyTrackerCustom.TRACK_LABEL_SHARELISTEN, null);
    }

    protected void pauseListen() {
        try {
            if (oActivity != null) {
                oActivity.PauseListen();
                //oActivity.PauseListenAAC();

                //if (btnPlay != null) btnPlay.setVisibility(View.VISIBLE);
                if (imgPlay != null) imgPlay.setVisibility(View.VISIBLE);

                //if (btnPause != null) btnPause.setVisibility(View.GONE);
                if (imgPause != null) imgPause.setVisibility(View.GONE);

                EasyTrackerCustom.AddEvent(oActivity, EasyTrackerCustom.TRACK_EVENT, EasyTrackerCustom.TRACK_ACTION_PAUSE, EasyTrackerCustom.TRACK_LABEL_PAUSE, null);
            }

        }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "TabListen - Pause"); }
    }

    protected void playListen() {
        try {
            if (oActivity != null) {
                oActivity.PlayListen();
                //oActivity.PlayListenAAC();

                //if (btnPlay != null) btnPlay.setVisibility(View.GONE);
                if (imgPlay != null) imgPlay.setVisibility(View.GONE);

                //if (btnPause != null) btnPause.setVisibility(View.VISIBLE);
                if (imgPause != null) imgPause.setVisibility(View.VISIBLE);

                EasyTrackerCustom.AddEvent(oActivity, EasyTrackerCustom.TRACK_EVENT, EasyTrackerCustom.TRACK_ACTION_PLAY, EasyTrackerCustom.TRACK_LABEL_PLAY, null);
            }

        }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "TabListen - Play"); }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
