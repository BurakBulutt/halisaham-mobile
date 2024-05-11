package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityEventBinding;
import com.buraksoft.halisaham_mobile.databinding.LayoutMenuImagesBinding;
import com.buraksoft.halisaham_mobile.library.adapter.EventRecyclerAdapter;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

import java.util.Objects;

public class EventActivity extends AppCompatActivity {
    private ActivityEventBinding binding;
    private LayoutMenuImagesBinding menuImagesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        menuImagesBinding = binding.layoutMenuImages;
        intentActivies();
        setContentView(binding.getRoot());
    }

    public void intentActivies(){
        menuImagesBinding.authImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menuImagesBinding.homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}