package com.buraksoft.halisaham_mobile.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventListBinding;
import com.buraksoft.halisaham_mobile.library.adapter.EventRecyclerAdapter;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListFragment extends Fragment {
    private FragmentEventListBinding binding;
    private EventViewModel viewModel;
    private EventRecyclerAdapter adapterView;

    public EventListFragment() {

    }

    public static EventListFragment newInstance() {
        return new EventListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        binding.eventRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterView = new EventRecyclerAdapter(new ArrayList<>());
        binding.eventRecyclerView.setAdapter(adapterView);
        observeDatas();
        getUserEvents();
        binding.addButton.setOnClickListener(this::navAddEvent);
        adapterView.setOnItemClickListener(this::itemListener);
        binding.reloadButton.setOnClickListener(this::reloadFragment);
    }

    private void reloadFragment(View view) {
        NavController navController = Navigation.findNavController(requireView());
        NavOptions options = new NavOptions.Builder()
                .setPopUpTo(R.id.eventListFragment,true)
                .build();
        navController.navigate(R.id.eventListFragment,getArguments(),options);
    }

    private void itemListener(EventModel eventModel) {
        NavController navController = Navigation.findNavController(requireView());
        EventListFragmentDirections.ActionEventListFragmentToEventChatFragment action = EventListFragmentDirections.actionEventListFragmentToEventChatFragment(eventModel);
        navController.navigate(action);
    }

    private void navAddEvent(View view) {
        NavController navController = Navigation.findNavController(requireView());
        NavDirections navigateAddEvent = EventListFragmentDirections.actionEventListFragmentToEventAddFragment();
        navController.navigate(navigateAddEvent);
    }

    public void getUserEvents(){
        viewModel.getUserEvents();
    }

    public void observeDatas(){
        viewModel.getLoading().observe(getViewLifecycleOwner(),isLoading -> {
            if (isLoading){
                binding.progressbar.setVisibility(View.VISIBLE);
            }else{
                binding.progressbar.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                Toast.makeText(requireContext(),"EVENTLER GETIRILEMEDI",Toast.LENGTH_LONG).show();
                Intent i = new Intent(requireContext(),MainActivity.class);
                requireActivity().startActivity(i);
            }
        });

        viewModel.getEventData().observe(getViewLifecycleOwner(),eventModels -> {
            if (eventModels != null){
               adapterView = (EventRecyclerAdapter) binding.eventRecyclerView.getAdapter();

               if (adapterView != null){
                   adapterView.updateData(eventModels);
               }
            }
        });

        viewModel.getAuthError().observe(getViewLifecycleOwner(), authError -> {
            if (authError){
                new AlertDialog.Builder(requireContext())
                        .setMessage("Lütfen Giriş Yapınız")
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(requireContext(),AuthenticationActivity.class);
                                requireActivity().startActivity(i);
                                requireActivity().finish();
                            }
                        })
                        .setCancelable(Boolean.FALSE)
                        .show();
            }
        });
    }

}