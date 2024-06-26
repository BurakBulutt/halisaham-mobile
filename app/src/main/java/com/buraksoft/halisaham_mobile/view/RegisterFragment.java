package com.buraksoft.halisaham_mobile.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.buraksoft.halisaham_mobile.databinding.FragmentRegisterBinding;
import com.buraksoft.halisaham_mobile.service.request.RegisterRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.AuthenticationViewModel;


public class RegisterFragment extends Fragment {
    private static final int VERIFY_ERROR = 1052;

    private FragmentRegisterBinding binding;
    private AuthenticationViewModel viewModel;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        binding.registerButton.setOnClickListener(this::register);
        binding.loginText.setOnClickListener(this::navLogin);
        observeDatas();
    }

    private void navLogin(View view) {
        NavController navController = Navigation.findNavController(requireView());
        NavDirections navigateRegister = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
        navController.navigate(navigateRegister);
    }

    private void register(View view) {
        if (!binding.passwordText.getText().toString().equals(binding.passwordAgainText.getText().toString())){
            binding.informationText.setVisibility(View.VISIBLE);
            binding.informationText.setText("PAROLA ESLESMIYOR");
        }else{
            if (binding.checkBox.isChecked()){
                RegisterRequest request = new RegisterRequest();
                request.setName(binding.nameText.getText().toString());
                request.setSurname(binding.surnameText.getText().toString());
                request.setEmail(binding.emailText.getText().toString());
                request.setPassword(binding.passwordText.getText().toString());

                viewModel.register(request);

            }else{
                Toast.makeText(requireContext(),"Onay vermelisin",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void alertCreator(Integer code){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext())
                .setCancelable(Boolean.FALSE);

        switch (code){
            case 1051 :
                alertDialog
                        .setTitle("UYARI")
                        .setMessage("E-posta veya parola hatalı lütfen bilgilerinizi kontrol edin")
                        .setCancelable(Boolean.FALSE)
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
                        .setTitle("BİLGİ")
                        .setMessage("Kaydınız başarıyla oluşturuldu lütfen e postanıza gönderilen bağlantı ile hesabınızı onaylayın.")
                        .setCancelable(Boolean.FALSE)
                        .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TokenContextHolder.setToken(null);
                                requireActivity().getSupportFragmentManager().popBackStackImmediate();
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
                Toast.makeText(requireContext(),"KAYIT BASARISIZ",Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getTokenData().observe(getViewLifecycleOwner(),tokenModel -> {
            if (tokenModel != null){
                Intent i = new Intent(requireContext(), MainActivity.class);
                requireContext().startActivity(i);
            }
        });

        viewModel.getVerifyError().observe(getViewLifecycleOwner(),verify -> {
            if (verify){
                alertCreator(VERIFY_ERROR);
            }
        });
    }
}