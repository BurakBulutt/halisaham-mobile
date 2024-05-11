package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityMainBinding;
import com.buraksoft.halisaham_mobile.databinding.LayoutMenuImagesBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LayoutMenuImagesBinding menuImagesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        menuImagesBinding = binding.layoutMenuImages;
        setContentView(binding.getRoot());
        intentActivies();
    }

    public void intentActivies(){
        menuImagesBinding.authImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menuImagesBinding.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}