package com.anasdarai.assistant_diabtique.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.Tache;
import com.anasdarai.assistant_diabtique.objs.TacheExp;
import com.anasdarai.assistant_diabtique.databinding.FragmentGtachesBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.adapters.list_g_taches_exp_adapter;
import com.anasdarai.assistant_diabtique.adapters.list_g_taches_per_adapter;

import java.util.ArrayList;

public class GTachesFragment extends Fragment {

    private FragmentGtachesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGtachesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db_manager dbManager=new db_manager(root.getContext());

        final ArrayList<Tache> allTachesPermanents = dbManager.getAllTachesPermanents();
        final list_g_taches_per_adapter list_g_taches_adapter = new list_g_taches_per_adapter(allTachesPermanents);
        binding.listTaches.setAdapter(list_g_taches_adapter);


        binding.fabAddPtache.setOnClickListener(v->{
            final AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
            final View inflate = getLayoutInflater().inflate(R.layout.dialog_add_per_tache, null);



            EditText edit_title_tache=inflate.findViewById(R.id.edit_title_tache);
            EditText edit_short_description=inflate.findViewById(R.id.edit_short_description);
            EditText edit_description=inflate.findViewById(R.id.edit_description);
            EditText edit_nb_jours=inflate.findViewById(R.id.edit_nb_jours);


            SwitchCompat  switch_every_day=inflate.findViewById(R.id.switch_every_day);
            EditText edit_hour=inflate.findViewById(R.id.edit_hour);
            EditText edit_min=inflate.findViewById(R.id.edit_min);
            View linear_time=inflate.findViewById(R.id.linear_time);

            View linear_every_nday=inflate.findViewById(R.id.linear_every_nday);

            builder.setTitle(R.string.add_task_per);

            switch_every_day.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    linear_time.setVisibility(View.VISIBLE);
                    linear_every_nday.setVisibility(View.GONE);
                }else{
                    linear_time.setVisibility(View.GONE);
                    linear_every_nday.setVisibility(View.VISIBLE);
                }
            });
            builder.setPositiveButton(R.string.add, (dialogInterface, i) -> {
                final Tache tache = new Tache();
                tache.permanent=1;
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
                dbManager.insertTacheToDb(tache);
                allTachesPermanents.add(tache);
                list_g_taches_adapter.notifyDataSetChanged();
            });

            builder.setView(inflate);
            builder.create().show();

        });


        final SharedPreferences taches_expPref = root.getContext().getSharedPreferences("tachesExp", Context.MODE_PRIVATE);

        final ArrayList<TacheExp> allTachesExp = new ArrayList<>();
        allTachesExp.add(new TacheExp(getString(R.string.hyper),"hyperglycemie"));
        allTachesExp.add(new TacheExp(getString(R.string.hypo),"hypoglycemie"));

        allTachesExp.add(new TacheExp(getString(R.string.measure_after_meal),"glycemie_apres_repas"));

        final list_g_taches_exp_adapter list_g_taches_exp_adapter = new list_g_taches_exp_adapter(allTachesExp, taches_expPref);
        binding.listTachesExp.setAdapter(list_g_taches_exp_adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}