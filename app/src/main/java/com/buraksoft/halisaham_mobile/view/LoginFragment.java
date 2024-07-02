package com.buraksoft.halisaham_mobile.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.AuthenticationViewModel;


public class LoginFragment extends Fragment {
    private static final int AUTH_ERROR = 1051;
    private static final int VERIFY_ERROR = 1052;
    private FragmentLoginBinding binding;
    private AuthenticationViewModel viewModel;

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

    private void alertCreator(Integer code){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext())
                .setCancelable(Boolean.FALSE);

        switch (code){
            case 1051 :
                alertDialog
                        .setTitle("UYARI")
                        .setMessage("E-posta veya parola hatalı lütfen bilgilerinizi kontrol edin")
                        .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TokenContextHolder.setToken(null);
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case 1052 :
                alertDialog
                        .setTitle("UYARI")
                        .setMessage("Hesabınız henüz onaylanmamıştır lütfen e postanıza gönderilen bağlantı ile hesabınızı onaylayın.")
                        .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TokenContextHolder.setToken(null);
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    private void observeDatas(){
        viewModel.getLoading().observe(getViewLifecycleOwner(),isLoading -> {
            if (isLoading){
                binding.progressbar.setVisibility(View.VISIBLE);
            }else{
                binding.progressbar.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                Toast.makeText(requireContext(),"BAĞLANTI HATASI",Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getTokenData().observe(getViewLifecycleOwner(),tokenModel -> {
            if (tokenModel != null){
                Intent i = new Intent(requireContext(), MainActivity.class);
                requireActivity().startActivity(i);
                requireActivity().finish();
            }
        });

        viewModel.getAuthError().observe(getViewLifecycleOwner(),authError -> {
            if (authError){
                alertCreator(AUTH_ERROR);
                viewModel.clearAuthError();
            }
        });

        viewModel.getVerifyError().observe(getViewLifecycleOwner(),verifyError -> {
            if (verifyError){
                alertCreator(VERIFY_ERROR);
                viewModel.clearVerifyError();
            }
        });
    }
}