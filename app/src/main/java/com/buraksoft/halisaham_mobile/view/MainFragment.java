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
    private ProgressDialog progressDialog;
    private MainViewModel viewModel;
    private EventSearchRequest request;


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
                    request.setCityId(cityModelList.get(position-1).getId());
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
                    request.setDistrictId(districtModelList.get(position-1).getId());
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

                } else {
                    Optional<AreaModel> areaModel = areaModelList.stream()
                            .filter(areaModel1 -> areaModel1.getName().equals(areaNames.get(position)))
                            .findFirst();

                    if (!areaModel.isPresent()) {
                        return;
                    }
                    request.setAreaId(areaModel.get().getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
    }

    public void observeDatas(){
        viewModel.getCityData().observe(getViewLifecycleOwner(),cityModelList -> {
            if (cityModelList != null){
                initSpinners();
            }
        });

        viewModel.getAreaData().observe(getViewLifecycleOwner(),areaModelList -> {
            if (areaModelList != null){
                initAreaSpinner();
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(),loading -> {
            if (loading){
                progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }else{
                if (progressDialog != null){
                    progressDialog.dismiss();
                }
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