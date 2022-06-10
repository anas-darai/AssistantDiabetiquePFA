package com.anasdarai.assistant_diabtique.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.objs.Tache;

import java.util.ArrayList;

public class list_g_taches_per_adapter extends BaseAdapter {

    final ArrayList<Tache> liste_taches;

    public list_g_taches_per_adapter(ArrayList<Tache> liste_taches) {
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

        if(view==null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gtache_per, null);

            final ImageButton ibtn_delete=view.findViewById(R.id.ibtn_delete);
            final ImageButton ibtn_edit=view.findViewById(R.id.ibtn_edit);


            View finalView = view;
            ibtn_delete.setOnClickListener(v -> {



                int indiceTache=(int) finalView.getTag();
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Supprimer la tâche suivante");
                builder.setMessage(liste_taches.get(indiceTache).title);
                builder.setPositiveButton("Supprimer", (dialogInterface, i1) -> {
                    final db_manager dbManager = new db_manager(v.getContext());
                    dbManager.deleteTache(liste_taches.get(indiceTache));
                    liste_taches.remove(indiceTache);
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("Annuler",null);
                builder.create().show();
            });
            ibtn_edit.setOnClickListener(v->{
                int indiceTache=(int) finalView.getTag();
                final Tache tache = liste_taches.get(indiceTache);

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dialog_add_per_tache, null);



                EditText edit_title_tache=inflate.findViewById(R.id.edit_title_tache);
                EditText edit_short_description=inflate.findViewById(R.id.edit_short_description);
                EditText edit_description=inflate.findViewById(R.id.edit_description);
                EditText edit_nb_jours=inflate.findViewById(R.id.edit_nb_jours);


                SwitchCompat switch_every_day=inflate.findViewById(R.id.switch_every_day);
                EditText edit_hour=inflate.findViewById(R.id.edit_hour);
                EditText edit_min=inflate.findViewById(R.id.edit_min);
                View linear_time=inflate.findViewById(R.id.linear_time);

                View linear_every_nday=inflate.findViewById(R.id.linear_every_nday);

                builder.setTitle("Ajouter une tâche permanente");

                switch_every_day.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b){
                        linear_time.setVisibility(View.VISIBLE);
                        linear_every_nday.setVisibility(View.GONE);
                    }else{
                        linear_time.setVisibility(View.GONE);
                        linear_every_nday.setVisibility(View.VISIBLE);
                    }
                });
                builder.setPositiveButton("mettre à jour", (dialogInterface, in) -> {
                    tache.title=edit_title_tache.getText().toString();
                    tache.short_description=edit_short_description.getText().toString();
                    tache.description=edit_description.getText().toString();


                    if (switch_every_day.isChecked()){
                        int h=Integer.parseInt(edit_hour.getText().toString());
                        int m=Integer.parseInt(edit_min.getText().toString());
                        if (h==0&&m==0)
                            tache.time="";
                        else{
                            float f=(h+m/60f)/24f;
                            tache.time=String.valueOf(f);
                        }
                    }else{
                        tache.time=edit_nb_jours.getText().toString();
                    }

                    final db_manager dbManager = new db_manager(v.getContext());
                    dbManager.updateTache(tache);
                    notifyDataSetChanged();
                });

                builder.setView(inflate);


                edit_title_tache.setText(tache.title);
                edit_short_description.setText(tache.short_description);
                edit_description.setText(tache.description);

                String tm = tache.time;
                boolean checkedEveryDay=true;
                if (!tm.isEmpty()){
                    float f_tm = Float.parseFloat(tm);
                    if (1 > f_tm) {
                        int h= (int) (f_tm * 24);
                        int m= (int) ((f_tm * 24-h)*60);
                        edit_hour.setText(String.format("%02d",h));
                        edit_min.setText(String.format("%02d",m));

                    }else{
                        checkedEveryDay=false;
                        edit_nb_jours.setText(String.valueOf((int)f_tm));
                    }
                }
                switch_every_day.setChecked(checkedEveryDay);



                builder.create().show();
            });

        }
        view.setTag(i);
        final TextView txt_title=view.findViewById(R.id.txt_title);
        final TextView txt_short_descr=view.findViewById(R.id.txt_short_descr);


        txt_title.setText(liste_taches.get(i).title);




        String tm = liste_taches.get(i).time;

        if (tm.isEmpty()) {
            txt_short_descr.setText("Quotidienne");
        } else {
            float f_tm = Float.parseFloat(tm);
            if (1 > f_tm) {
                int h= (int) (f_tm * 24);
                int m= (int) ((f_tm * 24-h)*60);

                txt_short_descr.setText(String.format("Quotidienne à %02d:%02d",h,m));
            }else{
                txt_short_descr.setText(String.format("Chaque %d jours",(int)f_tm));
            }
        }





        return view;
    }


}
