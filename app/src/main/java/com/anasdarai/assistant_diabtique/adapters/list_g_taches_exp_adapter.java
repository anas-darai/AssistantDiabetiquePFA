package com.anasdarai.assistant_diabtique.adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.SwitchCompat;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.TacheExp;

import java.util.ArrayList;

public class list_g_taches_exp_adapter extends BaseAdapter {

    final ArrayList<TacheExp> liste_taches;
    final SharedPreferences taches_expPref;
    public list_g_taches_exp_adapter(ArrayList<TacheExp> liste_taches, final SharedPreferences taches_expPref) {
        this.liste_taches = liste_taches;
        this.taches_expPref=taches_expPref;
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

        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gtache_exp, null);

        final SwitchCompat switch_tache=inflate.findViewById(R.id.switch_tache);

        switch_tache.setText(liste_taches.get(i).title);
        switch_tache.setChecked(taches_expPref.getBoolean(liste_taches.get(i).tag,false));

        switch_tache.setOnCheckedChangeListener((compoundButton, b) -> taches_expPref.edit().putBoolean((String) compoundButton.getTag(),b).commit());

        switch_tache.setTag(liste_taches.get(i).tag);
        return inflate;
    }

}
