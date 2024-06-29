package com.buraksoft.halisaham_mobile.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventChatBinding;
import com.buraksoft.halisaham_mobile.library.adapter.MessageAdapter;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.request.MessageRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;


public class EventChatFragment extends Fragment {
    private FragmentEventChatBinding binding;
    private EventModel eventModel;
    private EventViewModel viewModel;
    private MessageAdapter adapter;
    private boolean lock = false;

    public EventChatFragment() {

    }

    public static EventChatFragment newInstance() {
        return new EventChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventModel = EventChatFragmentArgs.fromBundle(getArguments()).getEventModel();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventChatBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (eventModel != null) {
            binding.toolbarText.setText(eventModel.getTitle());
        }
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        viewModel.getEventChat(eventModel.getId());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        binding.messageRecyclerView.setLayoutManager(linearLayoutManager);
        observeDatas();
        selectBottomNavVisibilty(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        binding.chatToolbar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            EventChatFragmentDirections.ActionEventChatFragmentToEventPageFragment action = EventChatFragmentDirections.actionEventChatFragmentToEventPageFragment(eventModel);
            NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.eventChatFragment, true)
                    .build();
            navController.navigate(action,options);
        });
        binding.sendButton.setOnClickListener(this::sendMessage);
        binding.messageRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom && binding.messageRecyclerView.getAdapter() != null) {
                binding.messageRecyclerView.postDelayed(() -> {
                    binding.messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }, 100);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void sendMessage(View view) {
        if (binding.messageText.getText().length() >= 1) {
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

    private void observeDatas() {
        viewModel.getMessages().removeObservers(getViewLifecycleOwner());
        viewModel.getSuccess().removeObservers(getViewLifecycleOwner());

        viewModel.getMessages().observe(getViewLifecycleOwner(), messageModels -> {
            if (messageModels != null && !messageModels.isEmpty()) {
                if (binding.messageRecyclerView.getAdapter() == null) {
                    if (adapter == null){
                        adapter = new MessageAdapter(requireContext(), messageModels, TokenContextHolder.getUserMail());
                    }
                    binding.messageRecyclerView.setAdapter(adapter);
                    lock = true;
                    binding.messageRecyclerView.scrollToPosition(messageModels.size()-1);
                } else {
                    adapter.updateData(messageModels);
                    binding.messageRecyclerView.scrollToPosition(adapter.getItemCount()-1);
                }
            }
        });

        viewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success && lock) {
                binding.messageText.setText("");

                binding.messageRecyclerView.post(() -> {
                    if (adapter.getItemCount() > 0) {
                        binding.messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                });
            }
        });
    }

    private void selectBottomNavVisibilty(int id) {
        Activity activity = requireActivity();
        if (activity.findViewById(R.id.coordinatorLayout) != null) {
            activity.findViewById(R.id.coordinatorLayout).setVisibility(id);
        }
    }

}