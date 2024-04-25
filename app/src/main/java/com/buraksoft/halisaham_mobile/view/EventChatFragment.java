package com.buraksoft.halisaham_mobile.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventChatBinding;


public class EventChatFragment extends Fragment {
    private FragmentEventChatBinding binding;

    public EventChatFragment() {

    }

    public static EventChatFragment newInstance() {
        return new EventChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventChatBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


}