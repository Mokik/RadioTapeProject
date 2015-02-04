package com.mrpaloma.radiotape;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MicheleMaccini on 04/02/2015.
 */
public class TabWrite extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oFragmentView = inflater.inflate(R.layout.tab_listen, container, false);


        return oFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
