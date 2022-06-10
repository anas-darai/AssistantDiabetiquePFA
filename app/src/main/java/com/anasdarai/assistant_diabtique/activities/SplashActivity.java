package com.anasdarai.assistant_diabtique.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.databinding.ActivitySplashBinding;
import com.anasdarai.assistant_diabtique.fragments.ReglagesFragment;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        com.anasdarai.assistant_diabtique.databinding.ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.getBoolean("first_open",true)){
            final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        final ReglagesFragment fragment = new ReglagesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.cont, fragment, "1")
                .disallowAddToBackStack()
                .commit();



        binding.button.setOnClickListener(v->{
            sharedPreferences.edit().putBoolean("first_open",false).commit();
            final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}