package com.anasdarai.assistant_diabtique.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anasdarai.assistant_diabtique.databinding.FragmentMesuresBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.adapters.list_mesures_adapter;
import com.anasdarai.assistant_diabtique.objs.Mesure;

import java.util.ArrayList;

public class MesuresFragment extends Fragment {

    private FragmentMesuresBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMesuresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db_manager dbManager=new db_manager(root.getContext());

        final ArrayList<Mesure> allMesure = dbManager.getAllMesures();

        binding.listMesures.setAdapter(new list_mesures_adapter(allMesure));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}