package com.buraksoft.halisaham_mobile.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventChatBinding;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.request.MessageRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;


public class EventChatFragment extends Fragment {
    private FragmentEventChatBinding binding;
    private EventModel eventModel;
    private EventViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
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
        viewModel.getEventChat(eventModel.getId());
        observeDatas();
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
        binding.sendButton.setOnClickListener(this::sendMessage);
    }

    private void sendMessage(View view) {
        if (binding.messageText.getText().length() >= 1){
            MessageRequest request = new MessageRequest();
            request.setMessage(binding.messageText.getText().toString());
            request.setChatId(viewModel.getChatId().getValue());
            request.setUser(TokenContextHolder.getUserMail());

            viewModel.sendMessage(request);
        }
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

    private void observeDatas(){
        viewModel.getMessages().observe(getViewLifecycleOwner(),messageModels -> {
            if (messageModels != null){
                Toast.makeText(requireContext(),"Mesajlar Geliyo",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectBottomNavVisibilty(int id){
        Activity activity = requireActivity();
        if (activity.findViewById(R.id.coordinatorLayout) != null){
            activity.findViewById(R.id.coordinatorLayout).setVisibility(id);
        }
    }

}