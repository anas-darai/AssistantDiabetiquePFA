package com.anasdarai.assistant_diabtique.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.databinding.FragmentConseilsBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.objs.Conseil;

public class ConseilsFragment extends Fragment {
    private FragmentConseilsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConseilsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db_manager dbManager = new db_manager(root.getContext());

        binding.itemConseil.btnNext.setOnClickListener(view -> {
            final Conseil conseilRan = dbManager.getConseilRan();

            binding.itemConseil.txtConseilNb.setText(getString(R.string.tip) + conseilRan.id);
            binding.itemConseil.txtEmoji.setText(conseilRan.emoji);
            binding.itemConseil.txtTitle.setText(conseilRan.title);
            binding.itemConseil.txtDescr.setText(conseilRan.description);
        });
        binding.itemConseil.btnNext.performClick();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}