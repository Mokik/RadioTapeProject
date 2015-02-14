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
public class TabWrite extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oFragmentView = inflater.inflate(R.layout.tab_write, container, false);

        final MainActivity oActivity = (MainActivity)getActivity();
        final View oFragmentViewTh = oFragmentView;

        Button btnSendPosition = (Button) oFragmentView.findViewById(R.id.btnInviaMessage);
        if (btnSendPosition != null) {
            btnSendPosition.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {

                        final EditText edtMessage = (EditText)oFragmentViewTh.findViewById(R.id.edtMessage);
                        if (edtMessage == null) return;

                        AsyncTask<Void, Void, Void> mSendEmailTask  = new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... arg0) {
                                String message = edtMessage.getText().toString();
                                if ((message.equals("")) || (message.length() <= 3)) return null;

                                String param = "p=4&message=" + message;

                                UtilsFunction utlsFunction = new UtilsFunction();
                                utlsFunction.SendActionWebPage(oActivity, param);

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {}

                        }.execute(null, null, null);

                        Toast.makeText(oActivity.getBaseContext(), getResources().getString(R.string.txtInvioAvvenutoConSuccesso), Toast.LENGTH_LONG).show();
                        edtMessage.setText("");

                    }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "TabSendPosition - onCreateView - Send message"); }
                }

            }); }

        return oFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
