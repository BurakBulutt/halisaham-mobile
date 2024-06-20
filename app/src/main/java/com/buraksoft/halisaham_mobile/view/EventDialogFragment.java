package com.buraksoft.halisaham_mobile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventDialogBinding;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

public class EventDialogFragment extends DialogFragment {
    private FragmentEventDialogBinding binding;
    private EventModel eventModel;
    private EventViewModel viewModel;

    public EventDialogFragment() {
        // Required empty public constructor
    }

    public static EventDialogFragment newInstance() {
        EventDialogFragment fragment = new EventDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            eventModel = EventDialogFragmentArgs.fromBundle(getArguments()).getEventModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        if (eventModel != null){
            byte[] photoBytes = eventModel.getArea().getPhoto();
            if (photoBytes != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes,0,eventModel.getArea().getPhoto().length);
                binding.eventImage.setImageBitmap(bitmap);
            }
            binding.eventTitle.setText(eventModel.getTitle());
            binding.eventDescription.setText(eventModel.getDescription());
            binding.eventPersonCountText.setText(eventModel.getUsers().size() + "/" + eventModel.getMaxPeople());
            binding.eventArea.setText(eventModel.getArea().getName());
            binding.joinEvent.setOnClickListener(this::joinEvent);
            observeDatas();
        }else {
            alertDialog();
        }

    }

    private void alertDialog(){
        new AlertDialog.Builder(requireContext())
                .setMessage("Bir Hata Meydana Geldi")
                .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(requireActivity(), MainActivity.class);
                        requireActivity().startActivity(i);
                        requireActivity().finish();;
                    }
                })
                .setCancelable(Boolean.FALSE)
                .show();
    }


    private void joinEvent(View view) {
        viewModel.joinEvent(eventModel.getId());
    }

    public void observeDatas(){
        viewModel.getSingleEventData().observe(getViewLifecycleOwner(),eventModel -> {
            if (eventModel != null){
                Intent i = new Intent(requireContext(), EventActivity.class);
                requireActivity().startActivity(i);
                requireActivity().finish();
            }
        });
    }
}