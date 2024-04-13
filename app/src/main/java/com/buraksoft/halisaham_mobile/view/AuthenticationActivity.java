package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityAuthenticationBinding;
import com.buraksoft.halisaham_mobile.databinding.LayoutMenuImagesBinding;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.AuthenticationViewModel;

public class AuthenticationActivity extends AppCompatActivity {
    private ActivityAuthenticationBinding binding;
    private LayoutMenuImagesBinding menuImagesBinding;
    private NavController navController;
    private NavInflater inflater;
    private NavGraph graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        menuImagesBinding = binding.layoutMenuImages;
        intentActivies();

    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this,R.id.authFragmentContainerView);
        inflater = navController.getNavInflater();
        graph = inflater.inflate(R.navigation.auth_graph);
        checkToken();
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

    public void checkToken(){
        if (TokenContextHolder.getToken() != null){
            graph.setStartDestination(R.id.userProfileFragment);
        }
        navController.setGraph(graph);
    }

}