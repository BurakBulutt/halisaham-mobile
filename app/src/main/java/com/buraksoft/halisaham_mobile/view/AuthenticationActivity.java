package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityAuthenticationBinding;
import com.buraksoft.halisaham_mobile.databinding.LayoutMenuImagesBinding;

public class AuthenticationActivity extends AppCompatActivity {
    private ActivityAuthenticationBinding binding;
    private LayoutMenuImagesBinding menuImagesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        menuImagesBinding = binding.layoutMenuImages;
        setContentView(binding.getRoot());
        intentActivies();
    }

    public void intentActivies(){
        menuImagesBinding.homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*menuImagesBinding.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Intent intent = new Intent(AuthenticationActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });

         */

    }

}