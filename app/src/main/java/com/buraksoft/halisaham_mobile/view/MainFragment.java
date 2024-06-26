package com.buraksoft.halisaham_mobile.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentMainBinding;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.model.DistrictModel;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.model.StreetModel;
import com.buraksoft.halisaham_mobile.service.request.EventSearchRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.MainViewModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MainViewModel viewModel;
    private EventSearchRequest request;
    private boolean isFragmentFirstLoad = true;
    private boolean spinnerLock = false;


    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        request = new EventSearchRequest();
        observeDatas();
        viewModel.getCities();
        binding.button.setOnClickListener(this::checkAuth);
    }

    public void checkAuth(View view){
        if (TokenContextHolder.getToken() == null){
            new AlertDialog.Builder(requireContext())
                    .setMessage("Lütfen Giriş Yapınız")
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(requireContext(),AuthenticationActivity.class);
                            startActivity(i);
                        }
                    })
                    .setCancelable(Boolean.FALSE)
                    .show();
        }else {
            viewModel.getEvents(request);
        }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, streetNames);

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

                areaModel.ifPresent(model -> request.setAreaId(model.getId()));
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

    @Override
    public void onResume() {
        super.onResume();
        if (!isFragmentFirstLoad){
            binding.citySpinner.setText("");
            binding.districtSpinner.setText("");
            binding.streetSpinner.setText("");
            binding.areaSpinner.setText("");
            initSpinners();
        }
    }

    private void updateRequestWithSpinnerValues() {
        String selectedCity = binding.citySpinner.getText().toString();
        if (!selectedCity.equals("-")) {
            List<CityModel> cityModelList = viewModel.getCityData().getValue();
            Optional<CityModel> cityModel = cityModelList.stream()
                    .filter(city -> city.getName().equals(selectedCity))
                    .findFirst();
            cityModel.ifPresent(city -> {
                request.setCityId(city.getId());
                updateDistrictRequest(city);
            });
        }

        String selectedDistrict = binding.districtSpinner.getText().toString();
        if (!selectedDistrict.equals("-")) {
            List<DistrictModel> districtModelList = viewModel.getCityData().getValue().stream()
                    .filter(city -> city.getId().equals(request.getCityId()))
                    .findFirst()
                    .map(CityModel::getDistricts)
                    .orElse(null);
            if (districtModelList != null) {
                Optional<DistrictModel> districtModel = districtModelList.stream()
                        .filter(district -> district.getName().equals(selectedDistrict))
                        .findFirst();
                districtModel.ifPresent(district -> {
                    request.setDistrictId(district.getId());
                    updateStreetRequest(district);
                });
            }
        }

        String selectedStreet = binding.streetSpinner.getText().toString();
        if (!selectedStreet.equals("-")) {
            List<StreetModel> streetModelList = viewModel.getCityData().getValue().stream()
                    .filter(city -> city.getId().equals(request.getCityId()))
                    .findFirst()
                    .map(CityModel::getDistricts)
                    .orElse(null).stream()
                    .filter(district -> district.getId().equals(request.getDistrictId()))
                    .findFirst()
                    .map(DistrictModel::getStreets)
                    .orElse(null);
            if (streetModelList != null) {
                Optional<StreetModel> streetModel = streetModelList.stream()
                        .filter(street -> street.getName().equals(selectedStreet))
                        .findFirst();
                streetModel.ifPresent(street -> {
                    request.setStreetId(street.getId());
                    updateAreaRequest(street);
                });
            }
        }

        String selectedArea = binding.areaSpinner.getText().toString();
        if (!selectedArea.equals("-")) {
            List<AreaModel> areaModelList = viewModel.getAreaData().getValue();
            Optional<AreaModel> areaModel = areaModelList.stream()
                    .filter(area -> area.getName().equals(selectedArea))
                    .findFirst();
            areaModel.ifPresent(area -> request.setAreaId(area.getId()));
        }
    }

    private void updateDistrictRequest(CityModel cityModel) {
        String selectedDistrict = binding.districtSpinner.getText().toString();
        if (!selectedDistrict.equals("-")) {
            List<DistrictModel> districtModelList = cityModel.getDistricts();
            Optional<DistrictModel> districtModel = districtModelList.stream()
                    .filter(district -> district.getName().equals(selectedDistrict))
                    .findFirst();
            districtModel.ifPresent(district -> {
                request.setDistrictId(district.getId());
                updateStreetRequest(district);
            });
        }
    }

    private void updateStreetRequest(DistrictModel districtModel) {
        String selectedStreet = binding.streetSpinner.getText().toString();
        if (!selectedStreet.equals("-")) {
            List<StreetModel> streetModelList = districtModel.getStreets();
            Optional<StreetModel> streetModel = streetModelList.stream()
                    .filter(street -> street.getName().equals(selectedStreet))
                    .findFirst();
            streetModel.ifPresent(street -> {
                request.setStreetId(street.getId());
                updateAreaRequest(street);
            });
        }
    }

    private void updateAreaRequest(StreetModel streetModel) {
        String selectedArea = binding.areaSpinner.getText().toString();
        if (!selectedArea.equals("-")) {
            List<AreaModel> areaModelList = viewModel.getAreaData().getValue();
            Optional<AreaModel> areaModel = areaModelList.stream()
                    .filter(area -> area.getName().equals(selectedArea))
                    .findFirst();
            areaModel.ifPresent(area -> request.setAreaId(area.getId()));
        }
    }

    public void observeDatas(){
        viewModel.getCityData().observe(getViewLifecycleOwner(),cityModelList -> {
            if (cityModelList != null){
                initSpinners();
                isFragmentFirstLoad = false;
            }
        });

        viewModel.getAreaData().observe(getViewLifecycleOwner(),areaModelList -> {
            if (areaModelList != null && spinnerLock){
                initAreaSpinner();
                spinnerLock = false;
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(),loading -> {
            if (loading){
                binding.progressbar.setVisibility(View.VISIBLE);
            }else{
                binding.progressbar.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                new AlertDialog.Builder(requireContext())
                        .setTitle("UYARI")
                        .setMessage("Servis Devre Dışı")
                        .setCancelable(Boolean.FALSE)
                        .setNeutralButton("TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requireActivity().finish();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        viewModel.getEventData().observe(getViewLifecycleOwner(),eventModelList -> {
            if (eventModelList != null){
                EventModel[] array = eventModelList.toArray(new EventModel[0]);
                NavController navController = Navigation.findNavController(requireView());
                MainFragmentDirections.ActionMainFragmentToSearchedEventListFragment action =
                        MainFragmentDirections.actionMainFragmentToSearchedEventListFragment(array);
                navController.navigate(action);
                viewModel.clearEventData();
            }
        });
    }
}