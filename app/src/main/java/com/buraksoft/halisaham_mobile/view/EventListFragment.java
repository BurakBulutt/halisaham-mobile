package com.buraksoft.halisaham_mobile.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventListBinding;
import com.buraksoft.halisaham_mobile.library.adapter.EventRecyclerAdapter;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventListFragment extends Fragment {
    private FragmentEventListBinding binding;
    private EventViewModel viewModel;

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
        getUserEvents();
        observeDatas();
        binding.eventRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.eventRecyclerView.setAdapter(new EventRecyclerAdapter(viewModel.getEventData().getValue()));
    }

    public void getUserEvents(){

    }

    public void observeDatas(){

    }

}