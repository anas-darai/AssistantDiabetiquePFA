package com.anasdarai.assistant_diabtique.fragments;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.typeDiabete;

public class ReglagesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        ListPreference itemList = findPreference("type_biabete");
        final Preference insuline = findPreference("insuline");


        itemList.setOnPreferenceChangeListener((preference, newValue) -> {
               int i=Integer.parseInt(newValue.toString());
            final typeDiabete type_diabete = typeDiabete.typeFromVal(i);
            insuline.setVisible(type_diabete==typeDiabete.diabete_type_1);
return true;
        });
    }

}