package com.anasdarai.assistant_diabtique.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anasdarai.assistant_diabtique.adapters.list_taches_stat_adapter;
import com.anasdarai.assistant_diabtique.databinding.FragmentStatistiquesBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.objs.TacheStat;

import java.util.ArrayList;

public class StatistiquesFragment extends Fragment {

    private FragmentStatistiquesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatistiquesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db_manager dbManager=new db_manager(root.getContext());

        int lastNday=2;

        final ArrayList<TacheStat> getAllSttaTaches = dbManager.getAllStatTaches(lastNday);

        binding.listRepas.setAdapter(new list_taches_stat_adapter(getAllSttaTaches));

        binding.txtAvgMDay.setText(String.format("%.02f",dbManager.avg_nb_mesures_day(lastNday)));
        binding.txtAvgNbHyperDay.setText(String.format("%.02f",dbManager.avg_nb_hyper_day(lastNday)));
        binding.txtAvgNbHypoDay.setText(String.format("%.02f",dbManager.avg_nb_hypo_day(lastNday)));

        binding.txtAvgRepasDay.setText(String.format("%.02f",dbManager.avg_nb_repas_day(lastNday)));
        binding.txtAvgQsucreRepasDay.setText(String.format("%dg",(int)dbManager.avg_nb_qsucre_repas_day(lastNday)));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}