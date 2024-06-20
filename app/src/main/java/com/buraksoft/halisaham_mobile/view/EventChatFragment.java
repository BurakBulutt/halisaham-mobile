package com.buraksoft.halisaham_mobile.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventChatBinding;
import com.buraksoft.halisaham_mobile.model.EventModel;


public class EventChatFragment extends Fragment {
    private FragmentEventChatBinding binding;
    private EventModel eventModel;

    public EventChatFragment() {

    }

    public static EventChatFragment newInstance() {
        return new EventChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            eventModel = EventChatFragmentArgs.fromBundle(getArguments()).getEventModel();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventChatBinding.inflate(inflater,container,false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (eventModel !=null){
            binding.toolbarText.setText(eventModel.getTitle());
        }
        selectBottomNavVisibilty(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatToolbar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            EventChatFragmentDirections.ActionEventChatFragmentToEventPageFragment action =
                    EventChatFragmentDirections.actionEventChatFragmentToEventPageFragment(eventModel);
            navController.navigate(action);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        selectBottomNavVisibilty(View.VISIBLE);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        selectBottomNavVisibilty(View.VISIBLE);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void selectBottomNavVisibilty(int id){
        Activity activity = requireActivity();
        if (activity.findViewById(R.id.coordinatorLayout) != null){
            activity.findViewById(R.id.coordinatorLayout).setVisibility(id);
        }
    }

}