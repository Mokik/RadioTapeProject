package com.mrpaloma.radiotape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MicheleMaccini on 08/02/2015.
 */
public class PalinsestoAll {

    public static List<Giorno> ITEMS = new ArrayList<Giorno>();
    public static Map<String, Giorno> ITEM_MAP = new HashMap<String, Giorno>();

    @SuppressWarnings("unused")
    public static void addItem(Giorno item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Giorno {
        public String id;
        public int ordinamento;
        public String giorno;
        public String oraInizio;
        public String titolo;
        public String descrizione;
        public String nomeImmagine;

        public Giorno(int ordinamento, String giorno, String oraInizio, String titolo, String descrizione, String nomeImmagine) {
            this.id = giorno + oraInizio;
            this.ordinamento = ordinamento;
            this.giorno = giorno;
            this.oraInizio = oraInizio;
            this.titolo = titolo;
            this.descrizione = descrizione;
            this.nomeImmagine = nomeImmagine;
        }

        public String getId() { return id; }
        public String toString() { return titolo; }
        public String getGiorno() { return giorno; }
        public String getOraInizio() { return oraInizio; }
        public String getTitolo() { return titolo; }
        public String getDescrizione() { return descrizione; }
        public String getImage() { return nomeImmagine; }
    }

}
