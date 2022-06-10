package com.anasdarai.assistant_diabtique.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.databinding.FragmentExporterBinding;
import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.objs.Mesure;
import com.anasdarai.assistant_diabtique.objs.formatExport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ExporterFragment extends Fragment {

    private FragmentExporterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExporterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db_manager dbManager=new db_manager(root.getContext());

        final ArrayList<Mesure> allMesures = dbManager.getAllMesures();

        final formatExport[] formatExports = formatExport.values();
        String[] names= new String[formatExports.length];
        for (int i = 0; i < formatExports.length; i++) {
            names[i]=formatExports[i].name;
        }

        ArrayAdapter ad
                = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,names
                );

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        final Spinner spinnerFormat = binding.spinnerFormat;
        spinnerFormat.setAdapter(ad);

        binding.btnExport.setOnClickListener(v->{
            int i =spinnerFormat.getSelectedItemPosition();
            final String exportString= generateExportString(formatExports[i], allMesures);
            final File exportFile = new File(getContext().getExternalCacheDir(), "export." + formatExports[i].extention);

            exportFile.setReadable(true,false);

            try(final FileWriter fileWriter = new FileWriter(exportFile)){
                fileWriter.write(exportString);
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(Build.VERSION.SDK_INT>=24){
                try{
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                }catch(Exception ignored){
                }
            }

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            sharingIntent.setType("text/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));

            startActivity(Intent.createChooser(sharingIntent,getString(R.string.share_file)));

        });

        return root;
    }

    String generateExportString( final formatExport formatExp,ArrayList<Mesure> mesures){
        StringBuilder res=new StringBuilder();
        switch (formatExp){
            case XML:
                res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

                res.append( "<Mesures>\n");
                for (Mesure mesure:mesures)
                    res.append(mesure.exportString(formatExp));
                res.append("</Mesures>\n");


                break;
        }

        return res.toString();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}