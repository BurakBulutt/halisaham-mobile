package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityAuthenticationBinding;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

public class AuthenticationActivity extends AppCompatActivity {
    private ActivityAuthenticationBinding binding;
    private NavController navController;
    private NavInflater inflater;
    private NavGraph graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavBar.setSelectedItemId(R.id.navigation_profile);
        binding.bottomNavBar.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_matches) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
                finish();
            }

            return true;
        });
        binding.floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this,R.id.authFragmentContainerView);
        inflater = navController.getNavInflater();
        graph = inflater.inflate(R.navigation.auth_graph);
        checkToken();
    }


    public void checkToken(){
        if (TokenContextHolder.getToken() != null){
            graph.setStartDestination(R.id.userProfileFragment);
        }
        navController.setGraph(graph);
    }

}