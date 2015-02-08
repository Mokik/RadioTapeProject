package com.mrpaloma.radiotape;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MicheleMaccini on 07/02/2015.
 */
public class PalinsestoAdapter extends ArrayAdapter<PalinsestoAll.Giorno> {

    protected Activity oActivity = null;
    public void setActivity(Activity obj) {oActivity = obj;}

    List<PalinsestoAll.Giorno> objList = null;

    public PalinsestoAdapter(Context context, int textViewResourceId, List<PalinsestoAll.Giorno> objects) {
        super(context, textViewResourceId, objects);

        objList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_giornopalinsesto, null);

        try {
            PalinsestoAll.Giorno g = getItem(position);

            TextView titolo = (TextView)convertView.findViewById(R.id.lnTitoloProgramma);
            if (titolo != null) titolo.setText(g.getTitolo());

            TextView oraInizio = (TextView)convertView.findViewById(R.id.lnOraInizio);
            if (oraInizio != null) oraInizio.setText(" " + g.getOraInizio().toString().substring(0,5));

            TextView descrizione = (TextView)convertView.findViewById(R.id.lnDescrizione);
            if (descrizione != null) descrizione.setText(g.getDescrizione());

            TextView giorno = (TextView)convertView.findViewById(R.id.lnGiornoNome);
            String sGiornoWrite = g.getGiorno();
            if (giorno != null) giorno.setText("- " + sGiornoWrite + " -");

            LinearLayout lytGiorno = (LinearLayout)convertView.findViewById(R.id.lnGiorno);
            if (lytGiorno != null) {
                Boolean visible = false;
                if (objList != null) {
                    if (position > 0) {
                        PalinsestoAll.Giorno gp = getItem(position - 1);
                        visible = !gp.getGiorno().toString().equals(sGiornoWrite);
                    }
                }

                if ((position == 0) || (visible)) {
                    lytGiorno.setVisibility(View.VISIBLE);

                } else {
                    lytGiorno.setVisibility(View.GONE);

                }
            }

        }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "PalinsestoAdapter - getView"); }

        return convertView;
    }

}
