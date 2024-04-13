package com.buraksoft.halisaham_mobile.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentUserProfileBinding;
import com.buraksoft.halisaham_mobile.viewmodel.AuthenticationViewModel;


public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    private AuthenticationViewModel viewModel;

    public UserProfileFragment() {

    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
    }
}