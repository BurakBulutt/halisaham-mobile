package com.buraksoft.halisaham_mobile.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavBar.getMenu().getItem(0).setCheckable(Boolean.FALSE);
        binding.bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile) {
                    Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.navigation_matches) {
                    Intent intent = new Intent(MainActivity.this, EventActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

}