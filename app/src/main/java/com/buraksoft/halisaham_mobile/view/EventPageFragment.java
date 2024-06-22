package com.buraksoft.halisaham_mobile.view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventPageBinding;
import com.buraksoft.halisaham_mobile.library.adapter.UserRecylerAdapter;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.model.UserModel;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventPageFragment extends Fragment {
    private FragmentEventPageBinding binding;
    private EventModel eventModel;
    private UserRecylerAdapter adapter;
    private EventViewModel viewModel;
    private Boolean buttonLock;


    public EventPageFragment() {

    }

    public static EventPageFragment newInstance() {
        EventPageFragment fragment = new EventPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventModel = EventPageFragmentArgs.fromBundle(getArguments()).getEventModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventPageBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        if (eventModel != null){
            initView(eventModel);
        }
        binding.updateButton.setOnClickListener(this::updateEvent);
        binding.editTitleButton.setOnClickListener(this::openText);
        observeDatas();
    }

    private void openText(View view) {

        if (buttonLock.equals(Boolean.TRUE)){
            buttonLock = Boolean.FALSE;
            resetViews();
        }else {
            binding.titleText.setFocusable(true);
            binding.eventDescription.setFocusable(true);
            binding.titleText.setFocusableInTouchMode(true);
            binding.eventDescription.setFocusableInTouchMode(true);
            binding.updateButton.setVisibility(View.VISIBLE);
            buttonLock = Boolean.TRUE;
        }
    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateEvent(View view){
        EventRequest request = new EventRequest();
        request.setTitle(binding.titleText.getText().toString());
        request.setDescription(binding.eventDescription.getText().toString());
        viewModel.updateEvent(toRequest(request,eventModel),eventModel.getId());
    }

    private void resetViews(){
        closeKeyboard();
        binding.eventDescription.setText(eventModel.getDescription());
        binding.titleText.setText(eventModel.getTitle());
        binding.titleText.setFocusableInTouchMode(false);
        binding.eventDescription.setFocusableInTouchMode(false);
        binding.titleText.setFocusable(false);
        binding.eventDescription.setFocusable(false);
        binding.updateButton.setVisibility(View.GONE);
    }

    private void initView(EventModel eventModel){
        buttonLock = Boolean.FALSE;
        binding.titleText.setFocusable(false);
        binding.eventDescription.setFocusable(false);
        binding.emptyView.setVisibility(View.GONE);
        binding.editDescriptionButton.setVisibility(View.GONE);
        binding.editTitleButton.setVisibility(View.GONE);
        binding.updateButton.setVisibility(View.GONE);
        binding.titleText.setText(eventModel.getTitle());
        binding.eventDescription.setText(eventModel.getDescription());
        binding.areaName.setText(eventModel.getArea().getName());
        if (eventModel.getArea().getPhoto() != null){
            initAreaPhoto();
        }
        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UserRecylerAdapter(eventModel.getUsers(),eventModel);
        adapter.setOnItemClickListener(new UserRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String eventId, UserModel userModel) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("ÇIKAR")
                        .setMessage(userModel.getName() + " " + userModel.getSurname() + " etkinlikten çıkarılıyor.")
                        .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.deleteUserOnEvent(eventId,userModel.getId());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        binding.userRecyclerView.setAdapter(adapter);
        getUserPhotos(eventModel);
        binding.backButton.setOnClickListener(v-> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        binding.exitButton.setOnClickListener(this::getDialog);
    }

    private void getUserPhotos(EventModel eventModel){
        List<String> userIds = eventModel.getUsers().stream().map(UserModel::getId).collect(Collectors.toList());
        viewModel.getUserProfileIdIn(userIds);
        viewModel.getEventAuthority(eventModel.getId());
    }

    private void getDialog(View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle("AYRIL")
                .setMessage("Maçtan çıkmak istediğinize emin misin ? ")
                .setCancelable(Boolean.FALSE)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitOnEvent();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void exitOnEvent(){
        viewModel.exitOnEvent(eventModel.getId());
    }

    private void initAreaPhoto(){
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(eventModel.getArea().getPhoto(),0,eventModel.getArea().getPhoto().length);
            binding.imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            Log.e("SAHA RESIMI YUKLENEMEDİ !", Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    private void observeDatas(){
        viewModel.getSuccess().observe(getViewLifecycleOwner(),success -> {
            if(success){
                binding.progressbar.setVisibility(View.GONE);
                NavController navController = Navigation.findNavController(requireView());
                NavDirections action = EventPageFragmentDirections.actionEventPageFragmentToEventListFragment();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.action_eventPageFragment_to_eventListFragment, true)
                        .build();
                navController.navigate(action,navOptions);
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(),loading -> {
            if (loading){
                binding.progressbar.setVisibility(View.VISIBLE);
            }else {
                binding.progressbar.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                Toast.makeText(requireContext(),"BİLİNMEYEN HATA",Toast.LENGTH_LONG).show();
                requireActivity().finish();
            }
        });

        viewModel.getUserProfileData().observe(getViewLifecycleOwner(),userProfileModelList -> {
            if (userProfileModelList != null){
                adapter.updateData(eventModel.getUsers(),userProfileModelList);
            }
        });

        viewModel.getEventAuthortityView().observe(getViewLifecycleOwner(),bool -> {
            if (bool != null){
                adapter.updateView(bool);
            }
            if (Boolean.TRUE.equals(bool)){
                binding.editDescriptionButton.setVisibility(View.VISIBLE);
                binding.editTitleButton.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.INVISIBLE);
            }
        });
        viewModel.getSingleEventData().observe(getViewLifecycleOwner(),updatedEvent -> {
            if (updatedEvent != null){
                eventModel = updatedEvent;
                NavController navController = Navigation.findNavController(requireView());
                EventPageFragmentDirections.ActionEventPageFragmentToEventChatFragment action = EventPageFragmentDirections.actionEventPageFragmentToEventChatFragment(eventModel);
                NavOptions options = new NavOptions.Builder()
                        .setPopUpTo(R.id.eventChatFragment,true)
                        .build();
                navController.navigate(action,options);
            }
        });
        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(),success -> {
            if (!success){
                new AlertDialog.Builder(requireContext())
                        .setTitle("UYARI")
                        .setMessage("İşlem Başarısız.")
                        .setCancelable(Boolean.FALSE)
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        viewModel.getRemovedUser().observe(getViewLifecycleOwner(),removed -> {
            if (removed != null){
                eventModel.getUsers().stream()
                        .filter(user -> user.getId().equals(removed))
                        .findFirst()
                        .ifPresent(userModel -> {
                            eventModel.getUsers().remove(userModel);
                            adapter = (UserRecylerAdapter) binding.userRecyclerView.getAdapter();
                            Objects.requireNonNull(adapter).updateData(eventModel.getUsers());
                        });
            }
        });
    }

    private EventRequest toRequest(EventRequest request,EventModel model){
        request.setAreaId(model.getArea().getId());
        request.setCityId(model.getCityId());
        request.setDistrictId(model.getDistrictId());
        request.setStreetId(model.getStreetId());
        request.setExpirationDate(model.getExpirationDate());
        request.setMaxPeople(model.getMaxPeople());
        return request;
    }
}