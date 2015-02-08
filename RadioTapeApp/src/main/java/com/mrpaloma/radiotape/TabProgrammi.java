package com.mrpaloma.radiotape;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by MicheleMaccini on 07/02/2015.
 */
public class TabProgrammi extends Fragment {

    private ListView olistView = null;
    public ListView getListView(){ return olistView; }

    private Boolean loadPalinsesto = false;
    public void setLoadPalinsesto(Boolean load) {loadPalinsesto = load; }
    public Boolean getLoadPalinsesto() {return loadPalinsesto; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oFragmentView = inflater.inflate(R.layout.tab_programmi, container, false);

        olistView = (ListView)oFragmentView.findViewById(R.id.listPalinsesto);
        setLoadPalinsesto(true);

        return oFragmentView;
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
