package com.anasdarai.assistant_diabtique.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.Tache;

import java.util.ArrayList;

public class list_taches_adapter extends BaseAdapter {

    final ArrayList<Tache> liste_taches;

    public list_taches_adapter(ArrayList<Tache> liste_taches) {
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

        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tache, null);

        final  CheckBox checkBox=inflate.findViewById(R.id.checkbox);
        final TextView txt_title=inflate.findViewById(R.id.txt_title);
        final TextView txt_short_descr=inflate.findViewById(R.id.txt_short_descr);


        txt_title.setText(liste_taches.get(i).title);
        txt_short_descr.setText(liste_taches.get(i).short_description);

        checkBox.setChecked(liste_taches.get(i).isFinished);
        checkBox.setOnCheckedChangeListener(onCheckBoxClick);

        checkBox.setTag(i);


        return inflate;
    }

    private CompoundButton.OnCheckedChangeListener onCheckBoxClick;

    public void setOnCheckBoxClick(CompoundButton.OnCheckedChangeListener onCheckBoxClick) {
        this.onCheckBoxClick = onCheckBoxClick;
    }
}
