package com.buraksoft.halisaham_mobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentEventAddBinding;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.model.DistrictModel;
import com.buraksoft.halisaham_mobile.model.StreetModel;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class EventAddFragment extends Fragment {
    private final EventRequest request = new EventRequest();
    private FragmentEventAddBinding binding;
    private EventViewModel viewModel;
    private Integer peopleCount;

    public EventAddFragment() {
    }

    public static EventAddFragment newInstance() {
        return new EventAddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        getCities();
        observeDatas();
        binding.dateText.setOnClickListener(this::dateListener);
        binding.button.setOnClickListener(this::save);
    }

    public void save(View view) {
        request.setTitle(binding.titleText.getText().toString());
        request.setDescription(binding.descriptionText.getText().toString());
    //    request.setExpirationDate(binding.dateText.getText()); //TODO Date düzenlencek

        request.setExpirationDate(null);

        viewModel.saveEvent(request);
    }

    private void dateListener(View view) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.dateText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        dialog.show();
    }

    public void getCities() {
        viewModel.getCities();
    }

    public void initSpinners() {
        List<CityModel> cityModelList = viewModel.getCityData().getValue();

        List<String> cityNames = cityModelList.stream()
                .map(CityModel::getName)
                .collect(Collectors.toList());

        cityNames.add(0, "-");

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cityNames);

        binding.citySpinner.setAdapter(cityAdapter);
        binding.citySpinner.setDropDownVerticalOffset(100);

        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    binding.districtSpinner.setSelection(0);
                    binding.streetSpinner.setSelection(0);
                    binding.areaSpinner.setSelection(0);
                    binding.districtSpinner.setEnabled(Boolean.FALSE);
                    binding.streetSpinner.setEnabled(Boolean.FALSE);
                    binding.areaSpinner.setEnabled(Boolean.FALSE);
                } else {
                    binding.districtSpinner.setEnabled(Boolean.TRUE);
                    request.setCityId(cityModelList.get(position - 1).getId());
                    initDistrict(cityModelList.get(position - 1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.districtSpinner.setEnabled(Boolean.FALSE);
                binding.streetSpinner.setEnabled(Boolean.FALSE);
                binding.areaSpinner.setEnabled(Boolean.FALSE);
            }
        });

        final List<Integer> peopleCount = new ArrayList<>();

        for (int i = 1; i <= 22; i++) {
            peopleCount.add(i);
        }

        ArrayAdapter<Integer> countAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, peopleCount);

        binding.countSpinner.setAdapter(countAdapter);
        binding.countSpinner.setDropDownVerticalOffset(100);

        binding.countSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPeopleCount(peopleCount.get(position));
                request.setMaxPeople(getPeopleCount());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setPeopleCount(peopleCount.get(0));
                request.setMaxPeople(getPeopleCount());
            }
        });
    }

    public void initDistrict(CityModel cityModel) {
        List<DistrictModel> districtModelList = cityModel.getDistricts();

        List<String> districtNames = districtModelList.stream()
                .map(DistrictModel::getName)
                .collect(Collectors.toList());
        districtNames.add(0, "-");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, districtNames);

        binding.districtSpinner.setAdapter(adapter);
        binding.districtSpinner.setDropDownVerticalOffset(100);

        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    binding.streetSpinner.setSelection(0);
                    binding.streetSpinner.setEnabled(Boolean.FALSE);
                    binding.areaSpinner.setEnabled(Boolean.FALSE);
                } else {
                    binding.streetSpinner.setEnabled(Boolean.TRUE);
                    request.setDistrictId(districtModelList.get(position - 1).getId());
                    initStreet(districtModelList.get(position - 1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.streetSpinner.setEnabled(Boolean.FALSE);
                binding.areaSpinner.setEnabled(Boolean.FALSE);
            }
        });
    }

    public void initStreet(DistrictModel districtModel) {
        List<StreetModel> streetModelList = districtModel.getStreets();

        List<String> streetNames = streetModelList.stream()
                .map(StreetModel::getName)
                .collect(Collectors.toList());
        streetNames.add(0, "-");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, streetNames);

        binding.streetSpinner.setAdapter(adapter);
        binding.streetSpinner.setDropDownVerticalOffset(100);

        binding.streetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    binding.areaSpinner.setSelection(0);
                    binding.areaSpinner.setEnabled(Boolean.FALSE);
                } else {
                    Optional<StreetModel> streetModel = streetModelList.stream()
                            .filter(streetModel1 -> streetModel1.getName().equals(streetNames.get(position)))
                            .findFirst();

                    if (!streetModel.isPresent()) {
                        Toast.makeText(requireContext(), "MAHALLE BULUNAMADI", Toast.LENGTH_LONG).show();
                        binding.areaSpinner.setSelection(0);
                        binding.areaSpinner.setEnabled(Boolean.FALSE);
                        return;
                    }
                    binding.areaSpinner.setEnabled(Boolean.TRUE);
                    request.setStreetId(streetModel.get().getId());
                    viewModel.getAreaByDistrictAndStreet(streetModel.get().getDistrictId(), streetModel.get().getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.areaSpinner.setEnabled(Boolean.FALSE);
            }
        });
    }

    private void initAreaSpinner() {
        List<AreaModel> areaModelList = viewModel.getAreaData().getValue();

        List<String> areaNames = areaModelList.stream().map(AreaModel::getName).collect(Collectors.toList());
        areaNames.add(0, "-");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, areaNames);

        binding.areaSpinner.setAdapter(adapter);
        binding.streetSpinner.setDropDownVerticalOffset(100);

        binding.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    binding.imageView.setImageResource(R.drawable._60_f_215154625_hjg9qkfwh9cu6lctuc8tiuv6jqsi0c5x);
                } else {
                    Optional<AreaModel> areaModel = areaModelList.stream()
                            .filter(areaModel1 -> areaModel1.getName().equals(areaNames.get(position)))
                            .findFirst();

                    if (!areaModel.isPresent()) {
                        return;
                    }

                    request.setAreaId(areaModel.get().getId());
                    Bitmap bitmap = BitmapFactory.decodeByteArray(areaModel.get().getPhoto(), 0, areaModel.get().getPhoto().length);
                    binding.imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void observeDatas() {
        viewModel.getCityData().observe(getViewLifecycleOwner(), cityModels -> {
            if (cityModels != null) {
                initSpinners();
            }
        });

        viewModel.getAreaData().observe(getViewLifecycleOwner(), areaModels -> {
            if (areaModels != null) {
                initAreaSpinner();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                //TODO
            }
        });

        viewModel.getSingleEventData().observe(getViewLifecycleOwner(),eventModel -> {
            if (eventModel !=null){
                NavController navController = Navigation.findNavController(requireView());
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.action_eventAddFragment_to_eventListFragment, true)
                        .build();
                NavDirections navigate = EventAddFragmentDirections.actionEventAddFragmentToEventListFragment();
                navController.navigate(navigate,navOptions);
            }
        });

    }



    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }
}