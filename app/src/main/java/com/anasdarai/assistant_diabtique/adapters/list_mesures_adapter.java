package com.anasdarai.assistant_diabtique.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.Mesure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class list_mesures_adapter extends BaseAdapter {

    final ArrayList<Mesure> liste_taches;
    final SimpleDateFormat dateFormatHourMin;
    final SimpleDateFormat dateformatDb;
    final SimpleDateFormat dateformatyymmdd;

    public list_mesures_adapter(ArrayList<Mesure> liste_taches) {
        this.liste_taches = liste_taches;
        dateFormatHourMin = new SimpleDateFormat("HH:mm", Locale.FRENCH);
        dateformatDb = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH);
        dateformatyymmdd= new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
    }

    @Override
    public int getCount() {
        return liste_taches.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repas, null);

        final TextView txt_title=inflate.findViewById(R.id.txt_name);
        final TextView txt_time=inflate.findViewById(R.id.txt_time);
        final TextView txt_short_descr=inflate.findViewById(R.id.txt_short_descr);

        try {
            final Date date = dateformatDb.parse(liste_taches.get(i).time);
            txt_time.setText(  dateFormatHourMin.format(date));
            txt_title.setText(  dateformatyymmdd.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txt_short_descr.setText(liste_taches.get(i).glycemie+" mg/dl");



        return inflate;
    }

}
