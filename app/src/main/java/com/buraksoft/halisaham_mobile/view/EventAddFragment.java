package com.buraksoft.halisaham_mobile.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.AutoCompleteTextView;
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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class EventAddFragment extends Fragment {
    private final EventRequest request = new EventRequest();
    private FragmentEventAddBinding binding;
    private EventViewModel viewModel;
    private Integer peopleCount = 1;
    private boolean spinnerLock = true;
    private boolean isFragmentFirstLoad = true;

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
        binding.backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.dateText.setInputType(android.text.InputType.TYPE_NULL);
        binding.timeText.setInputType(android.text.InputType.TYPE_NULL);

        binding.timeText.setOnClickListener(this::timeListener);
        binding.dateText.setOnClickListener(this::dateListener);
        binding.button.setOnClickListener(this::save);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFragmentFirstLoad){
            binding.citySpinner.setText("");
            binding.districtSpinner.setText("");
            binding.streetSpinner.setText("");
            binding.areaSpinner.setText("");
            binding.imageView.setImageResource(R.drawable._60_f_215154625_hjg9qkfwh9cu6lctuc8tiuv6jqsi0c5x);
            initSpinners();
        }
    }

    public void save(View view)  {
        request.setTitle(binding.titleText.getText().toString());
        request.setDescription(binding.descriptionText.getText().toString());

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String dateTime = binding.dateText.getText().toString() + " " + binding.timeText.getText().toString();
            Date date = format.parse(dateTime);
            request.setExpirationDate(date.getTime());
        } catch (ParseException e) {
            request.setExpirationDate(null);
        }

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
                String formatMonth = String.format("%02d",month + 1);
                binding.dateText.setText(dayOfMonth + "." + formatMonth + "." + year);
            }
        }, year, month, day);

        dialog.getDatePicker().setMinDate(c.getTimeInMillis());

        dialog.show();
    }

    private void timeListener(View view) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view12, hourOfDay, minute1) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute1);
            binding.timeText.setText(time);
        }, hour, minute, true);

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

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_item, cityNames);

        binding.citySpinner.setAdapter(cityAdapter);

        binding.citySpinner.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                binding.districtSpinner.setText("");
                binding.streetSpinner.setText("");
                binding.areaSpinner.setText("");
                setSpinnerEnabled(binding.districtSpinner, false);
                setSpinnerEnabled(binding.streetSpinner, false);
                setSpinnerEnabled(binding.areaSpinner, false);
            } else {
                setSpinnerEnabled(binding.districtSpinner, true);
                request.setCityId(cityModelList.get(position - 1).getId());
                initDistrict(cityModelList.get(position - 1));
            }
        });


        final List<Integer> peopleCount = new ArrayList<>();

        for (int i = 1; i <= 22; i++) {
            peopleCount.add(i);
        }

        ArrayAdapter<Integer> countAdapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_item, peopleCount);

        binding.countSpinner.setAdapter(countAdapter);

        binding.countSpinner.setOnItemClickListener((parent, view, position, id) -> {
            setPeopleCount(peopleCount.get(position));
            request.setMaxPeople(getPeopleCount());
        });

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_item, districtNames);

        binding.districtSpinner.setAdapter(adapter);

        binding.districtSpinner.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                binding.streetSpinner.setText("");
                setSpinnerEnabled(binding.streetSpinner, false);
                setSpinnerEnabled(binding.areaSpinner, false);
            } else {
                setSpinnerEnabled(binding.streetSpinner, true);
                request.setDistrictId(districtModelList.get(position - 1).getId());
                initStreet(districtModelList.get(position - 1));
            }
        });
    }

    public void initStreet(DistrictModel districtModel) {
        List<StreetModel> streetModelList = districtModel.getStreets();

        List<String> streetNames = streetModelList.stream()
                .map(StreetModel::getName)
                .collect(Collectors.toList());
        streetNames.add(0, "-");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_item, streetNames);

        binding.streetSpinner.setAdapter(adapter);

        binding.streetSpinner.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                binding.areaSpinner.setText("");
                setSpinnerEnabled(binding.areaSpinner, false);
            } else {
                Optional<StreetModel> streetModel = streetModelList.stream()
                        .filter(streetModel1 -> streetModel1.getName().equals(streetNames.get(position)))
                        .findFirst();

                if (!streetModel.isPresent()) {
                    Toast.makeText(requireContext(), "MAHALLE BULUNAMADI", Toast.LENGTH_LONG).show();
                    binding.areaSpinner.setText("");
                    setSpinnerEnabled(binding.areaSpinner, false);
                    return;
                }
                setSpinnerEnabled(binding.areaSpinner, true);
                request.setStreetId(streetModel.get().getId());
                spinnerLock = true;
                viewModel.getAreaByDistrictAndStreet(streetModel.get().getDistrictId(), streetModel.get().getId());
            }
        });
    }

    private void initAreaSpinner() {
        List<AreaModel> areaModelList = viewModel.getAreaData().getValue();

        List<String> areaNames = areaModelList.stream().map(AreaModel::getName).collect(Collectors.toList());
        areaNames.add(0, "-");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_item, areaNames);

        binding.areaSpinner.setAdapter(adapter);

        binding.areaSpinner.setOnItemClickListener((parent, view, position, id) -> {
            if (position != 0) {
                Optional<AreaModel> areaModel = areaModelList.stream()
                        .filter(areaModel1 -> areaModel1.getName().equals(areaNames.get(position)))
                        .findFirst();

                areaModel.ifPresent(model -> {
                    request.setAreaId(model.getId());
                    if (areaModel.get().getPhoto() != null){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(areaModel.get().getPhoto(), 0, areaModel.get().getPhoto().length);
                        binding.imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    private void setSpinnerEnabled(AutoCompleteTextView spinner, boolean isEnabled) {
        spinner.setEnabled(isEnabled);
        spinner.setFocusable(isEnabled);
        spinner.setFocusableInTouchMode(isEnabled);
        spinner.setClickable(isEnabled);
        spinner.setAlpha(isEnabled ? 1.0f : 0.5f); // Görsel olarak devre dışı bırakıldığını göstermek için opaklığı ayarlayabilirsiniz.
    }


    public void observeDatas() {
        viewModel.getCityData().observe(getViewLifecycleOwner(), cityModels -> {
            if (cityModels != null) {
                initSpinners();
                isFragmentFirstLoad = false;
            }
        });

        viewModel.getAreaData().observe(getViewLifecycleOwner(), areaModels -> {
            if (areaModels != null && spinnerLock) {
                initAreaSpinner();
                spinnerLock = false;
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                //TODO
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(),loading -> {
            if (loading){
                binding.progressbar.setVisibility(View.VISIBLE);
            }else {
                binding.progressbar.setVisibility(View.GONE);
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