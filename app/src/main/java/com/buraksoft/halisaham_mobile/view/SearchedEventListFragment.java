package com.buraksoft.halisaham_mobile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentSearchedEventListBinding;
import com.buraksoft.halisaham_mobile.library.adapter.EventRecyclerAdapter;
import com.buraksoft.halisaham_mobile.library.adapter.SearchedEventRecyclerAdapter;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SearchedEventListFragment extends Fragment {
    private FragmentSearchedEventListBinding binding;
    private SearchedEventRecyclerAdapter adapter;
    private List<EventModel> eventModelList;

    public SearchedEventListFragment() {

    }

    public static SearchedEventListFragment newInstance() {
        return new SearchedEventListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            EventModel[] eventModels = SearchedEventListFragmentArgs.fromBundle(getArguments()).getEventModelList();
            eventModelList = Arrays.asList(eventModels);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchedEventListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (eventModelList.isEmpty()){
            emptyAlert();
        }
        adapter = new SearchedEventRecyclerAdapter(eventModelList);
        adapter.setOnItemClickListener(eventModel -> {
            NavController navController = Navigation.findNavController(requireView());
            SearchedEventListFragmentDirections.ActionSearchedEventListFragmentToEventDialogFragment action
                    = SearchedEventListFragmentDirections.actionSearchedEventListFragmentToEventDialogFragment(eventModel);
            navController.navigate(action);
        });
        binding.recyclerView.setAdapter(adapter);
    }

    public void emptyAlert(){
        new AlertDialog.Builder(requireContext())
                .setMessage("MAÇ BULUNAMADI")
                .setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavController navController = Navigation.findNavController(requireView());
                        navController.popBackStack();
                        navController.navigateUp();
                    }
                })
                .setCancelable(Boolean.FALSE)
                .show();
    }

}