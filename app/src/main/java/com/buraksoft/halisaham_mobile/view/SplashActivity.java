package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private EventViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        viewModel.getCities();
        new Handler().postDelayed(this::observe, 3000);
    }

    private void observe(){
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        viewModel.getError().observe(this,error -> {
            if (!error){
                findViewById(R.id.progressbar).setVisibility(View.GONE);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }else if (error){
                findViewById(R.id.progressbar).setVisibility(View.GONE);
                new AlertDialog.Builder(this)
                        .setTitle("UYARI")
                        .setMessage("Servise Bağlanılamadı !")
                        .setCancelable(Boolean.FALSE)
                        .setNeutralButton("TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }
}