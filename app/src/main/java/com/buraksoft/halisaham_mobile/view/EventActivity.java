package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityEventBinding;


public class EventActivity extends AppCompatActivity {
    private ActivityEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavBar.setSelectedItemId(R.id.navigation_matches);
        binding.bottomNavBar.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}