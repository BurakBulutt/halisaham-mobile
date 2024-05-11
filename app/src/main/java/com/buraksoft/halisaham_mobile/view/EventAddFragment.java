package com.buraksoft.halisaham_mobile.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.databinding.FragmentEventAddBinding;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.model.DistrictModel;
import com.buraksoft.halisaham_mobile.model.StreetModel;
import com.buraksoft.halisaham_mobile.viewmodel.EventViewModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class EventAddFragment extends Fragment {
    private FragmentEventAddBinding binding;
    private EventViewModel viewModel;
    private MutableLiveData<byte[]> imageData = new MutableLiveData<>();

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

                    if (!streetModel.isPresent()){
                        Toast.makeText(requireContext(),"MAHALLE BULUNAMADI",Toast.LENGTH_LONG).show();
                        binding.areaSpinner.setSelection(0);
                        binding.areaSpinner.setEnabled(Boolean.FALSE);
                        return;
                    }
                    binding.areaSpinner.setEnabled(Boolean.TRUE);
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
                    //TODO RESIM SIFIRLA
                }else {
                    Optional<AreaModel> areaModel = areaModelList.stream()
                            .filter(areaModel1 -> areaModel1.getName().equals(areaNames.get(position)))
                            .findFirst();

                    if (!areaModel.isPresent()){

                    }


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

        getImageData().observe(getViewLifecycleOwner(), imageData -> {

        });
    }


    public LiveData<byte[]> getImageData() {
        return imageData;
    }

}