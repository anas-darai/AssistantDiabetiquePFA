package com.anasdarai.assistant_diabtique.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.TacheStat;

import java.util.ArrayList;

public class list_taches_stat_adapter extends BaseAdapter {

    final ArrayList<TacheStat> liste_taches;

    public list_taches_stat_adapter(ArrayList<TacheStat> liste_taches) {
        this.liste_taches = liste_taches;
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

        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tache_stat, null);

        final TextView txt_title=inflate.findViewById(R.id.txt_name);
        final TextView txt_time=inflate.findViewById(R.id.txt_time);

        int PS=liste_taches.get(i).PS;
            txt_time.setText(  PS+"%");

        txt_title.setText(  liste_taches.get(i).title);

        txt_time.setBackgroundColor(Color.rgb((int)((1-PS)*2.31),(int)(PS*1.74),96));

        return inflate;
    }

}
