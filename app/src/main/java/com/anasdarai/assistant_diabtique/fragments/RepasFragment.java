package com.anasdarai.assistant_diabtique.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anasdarai.assistant_diabtique.objs.Repas;
import com.anasdarai.assistant_diabtique.databinding.FragmentRepasBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.adapters.list_repas_adapter;

import java.util.ArrayList;

public class RepasFragment extends Fragment {

    private FragmentRepasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRepasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db_manager dbManager=new db_manager(root.getContext());

        final ArrayList<Repas> repasToday = dbManager.getRepasToday();

        binding.listRepas.setAdapter(new list_repas_adapter(repasToday));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}