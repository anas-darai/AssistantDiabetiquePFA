package com.anasdarai.assistant_diabtique.fragments;

import static com.anasdarai.assistant_diabtique.notifications.PlanifierTachesService.planifierTache;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.anasdarai.assistant_diabtique.objs.Mesure;
import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.Repas;
import com.anasdarai.assistant_diabtique.objs.Tache;
import com.anasdarai.assistant_diabtique.databinding.FragmentHomeBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.adapters.list_taches_adapter;
import com.anasdarai.assistant_diabtique.objs.typeDiabete;
import com.anasdarai.assistant_diabtique.views.ViewCourbe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        ViewCourbe v_courbe=binding.layoutCourbe.vCourbe;


        ListView list_tachesEnCours=binding.listTachesEnCours;

        ListView list_tacheseTerminees=binding.listTachesTerminees;







        db_manager dbManager=new db_manager(root.getContext());
        final SharedPreferences taches_expPref = root.getContext().getSharedPreferences("tachesExp", Context.MODE_PRIVATE);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(root.getContext());

        binding.layoutCourbe.txtUsername.setText(sharedPreferences.getString("nom_prenom",null));


        final typeDiabete typediabete = typeDiabete.typeFromVal(Integer.parseInt(sharedPreferences.getString("type_biabete","1")));
        binding.layoutCourbe.txtTypeDiabete.setText(typediabete.getText());


        final ArrayList<Tache> arrayList_tachesEnCours = new ArrayList<>(dbManager.getEnCoursTaches());

        final ArrayList<Tache> arrayList_tachesTerminees = new ArrayList<>(dbManager.getFinishedTaches());


        final list_taches_adapter tachesEnCours_adapter = new list_taches_adapter(arrayList_tachesEnCours);
        final list_taches_adapter tachesTerminees_adapter = new list_taches_adapter(arrayList_tachesTerminees);

        tachesEnCours_adapter.setOnCheckBoxClick((compoundButton, b) -> {
            int i=(int)compoundButton.getTag();
            dbManager.changeTacheStatus(arrayList_tachesEnCours.get(i),true);

            final Tache removedTache = arrayList_tachesEnCours.remove(i);
            arrayList_tachesTerminees.add(removedTache);
            tachesEnCours_adapter.notifyDataSetChanged();
            tachesTerminees_adapter.notifyDataSetChanged();

        });

        tachesTerminees_adapter.setOnCheckBoxClick((compoundButton, b) -> {
            int i=(int)compoundButton.getTag();

            dbManager.changeTacheStatus(arrayList_tachesTerminees.get(i),false);

            final Tache removedTache = arrayList_tachesTerminees.remove(i);
            arrayList_tachesEnCours.add(removedTache);
            tachesEnCours_adapter.notifyDataSetChanged();
            tachesTerminees_adapter.notifyDataSetChanged();


        });
        list_tachesEnCours.setAdapter(tachesEnCours_adapter);
        list_tacheseTerminees.setAdapter(tachesTerminees_adapter);



        binding.layoutCourbe.txtMonth.setText(new SimpleDateFormat("MMM", Locale.FRENCH).format(System.currentTimeMillis()));
        binding.layoutCourbe.txtDay.setText(new SimpleDateFormat("dd", Locale.FRENCH).format(System.currentTimeMillis()));


        final SimpleDateFormat dateformatDialog = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.FRENCH);
        binding.fabAddMesure.setOnClickListener(view -> {
            binding.linearAdd.setVisibility(View.GONE);

            Calendar calendar = Calendar.getInstance();


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(root.getContext());

            View dialog_add_mesure = this.getLayoutInflater().inflate(R.layout.dialog_add_mesure, null);


            TextView txt_date=dialog_add_mesure.findViewById(R.id.txt_date);

            TextView edit_glucose_level=dialog_add_mesure.findViewById(R.id.edit_glucose_level);
            SwitchCompat switch_apres_repas=dialog_add_mesure.findViewById(R.id.switch_apres_repas);



            txt_date.setText(dateformatDialog.format(calendar.getTime().getTime()));
            txt_date.setOnClickListener(view1 -> {

                TimePickerDialog timePicker= new TimePickerDialog(getContext(),
                        (timep, hour, min) -> {
                            calendar.set(Calendar.HOUR_OF_DAY,hour);
                            calendar.set(Calendar.MINUTE,min);
                            txt_date.setText(dateformatDialog.format(calendar.getTime().getTime()));
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                );
                timePicker.show();
            });

            dialogBuilder.setView(dialog_add_mesure);
            dialogBuilder.setPositiveButton(R.string.add,(dialogInterface, i) -> {

                int valGly=Integer.parseInt(edit_glucose_level.getText().toString());

                final Mesure mesure = new Mesure(valGly,calendar);
                dbManager.insertMesureToDb(mesure);
                v_courbe.addPoint(mesure);



                if (taches_expPref.getBoolean("hyperglycemie",true)&&valGly>250){
                    final Tache tache = new Tache(getString(R.string.titre_hyper_task),
                            getString(R.string.short_desc_hyper_task),
                            "",null,0);
                    dbManager.insertTacheToDb(tache);
                    arrayList_tachesEnCours.add(tache);
                    tachesEnCours_adapter.notifyDataSetChanged();



                }else if(taches_expPref.getBoolean("hypoglycemie",true)&&valGly<=70){
                    final Tache tache = new Tache(getString(R.string.titre_hypo_task),
                            getString(R.string.short_desc_hypo_task),
                            "",null,0);
                    dbManager.insertTacheToDb(tache);
                    arrayList_tachesEnCours.add(tache);
                    tachesEnCours_adapter.notifyDataSetChanged();
                }


                if (switch_apres_repas.isChecked()){

                    int FSI= Integer.parseInt(sharedPreferences.getString("FSI","2"));

                    if (typediabete==typeDiabete.diabete_type_1&&valGly>140){
                        final Tache tache = new Tache(getString(R.string.title_blood_sugar_high),
                                String.format(getString(R.string.short_desc_blood_sugar_high),(float)(valGly-140)/(FSI*18)),
                                "",null,0);
                        dbManager.insertTacheToDb(tache);
                        arrayList_tachesEnCours.add(tache);
                        tachesEnCours_adapter.notifyDataSetChanged();
                    }

                }

            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

        binding.fabAddRepas.setOnClickListener(view -> {
            binding.linearAdd.setVisibility(View.GONE);

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(root.getContext());

            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add_repas, null);


            TextView txt_date=dialogView.findViewById(R.id.txt_date);

            EditText edit_nom_repas=dialogView.findViewById(R.id.edit_nom_repas);

            EditText edit_quantite_sucre=dialogView.findViewById(R.id.edit_quantite_sucre);



            txt_date.setText(dateformatDialog.format(date.getTime()));

            dialogBuilder.setView(dialogView);

            dialogBuilder.setPositiveButton(R.string.add,(dialogInterface, i) -> {
                Repas repas=new Repas(edit_nom_repas.getText().toString(),Integer.parseInt(edit_quantite_sucre.getText().toString()));
                dbManager.insertRepasToDb(repas);


                if (taches_expPref.getBoolean("glycemie_apres_repas",true)) {
                    final Tache tache = new Tache(getString(R.string.measure_after_meal),
                            String.format("%s(%s)",
                            edit_nom_repas.getText().toString(), repas.quantity), "",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime() + 2 * 60 * 60 * 1000), 0);
                    dbManager.insertTacheToDb(tache);
                    planifierTache(tache,getContext().getApplicationContext());
                }



            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });


        binding.fabAdd.setOnClickListener(view ->binding.linearAdd.setVisibility(binding.linearAdd.getVisibility()==View.GONE ?View.VISIBLE :View.GONE));


        v_courbe.setPoints(dbManager.getMesuresToday());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}