package com.buraksoft.halisaham_mobile.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.databinding.FragmentLoginBinding;
import com.buraksoft.halisaham_mobile.service.request.LoginRequest;
import com.buraksoft.halisaham_mobile.viewmodel.AuthenticationViewModel;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthenticationViewModel viewModel;
    private ProgressDialog progressDialog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        binding.loginButton.setOnClickListener(this::login);
        binding.registerButtonText.setOnClickListener(this::navRegister);
        observeDatas();

    }

    private void navRegister(View view) {
        NavController navController = Navigation.findNavController(requireView());
        NavDirections navigateRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
        navController.navigate(navigateRegister);
    }

    private void login(View view){
        LoginRequest request = new LoginRequest();
        request.setEmail(binding.emailText.getText().toString());
        request.setPassword(binding.passwordText.getText().toString());

        viewModel.login(request);
    }

    private void observeDatas(){
        viewModel.getLoading().observe(getViewLifecycleOwner(),isLoading -> {
            if (isLoading){
                progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }else{
                if (progressDialog != null){
                    progressDialog.dismiss();
                }
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                Toast.makeText(requireContext(),"EMAIL VEYA SIFRE HATALI",Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getTokenData().observe(getViewLifecycleOwner(),tokenModel -> {
            if (tokenModel != null){
                Intent i = new Intent(requireContext(), MainActivity.class);
                requireContext().startActivity(i);
            }
        });
    }
}