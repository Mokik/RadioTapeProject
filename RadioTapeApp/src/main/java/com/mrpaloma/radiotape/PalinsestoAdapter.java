package com.mrpaloma.radiotape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MicheleMaccini on 07/02/2015.
 */
public class PalinsestoAdapter extends ArrayAdapter<Palinsesto.Giorno> {

    public PalinsestoAdapter(Context context, int textViewResourceId, List<Palinsesto.Giorno> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_giornopalinsesto, null);

        Palinsesto.Giorno g = getItem(position);

        TextView titolo = (TextView)convertView.findViewById(R.id.lnTitoloProgramma);
        titolo.setText(g.getTitolo());

        /*TextView titolo2 = (TextView)convertView.findViewById(R.id.lnTitoloSecondo);
        ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);

        DummyContent.DummyItem c = getItem(position);

        titolo2.setText(Html.fromHtml(c.getSecondoTitolo()));*/


        return convertView;
    }

}
