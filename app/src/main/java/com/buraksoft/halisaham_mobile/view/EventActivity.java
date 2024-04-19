package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityEventBinding;

public class EventActivity extends AppCompatActivity {
    private ActivityEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}