package com.buraksoft.halisaham_mobile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.ActivityMainBinding;
import com.buraksoft.halisaham_mobile.databinding.LayoutMenuImagesBinding;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.model.DistrictModel;
import com.buraksoft.halisaham_mobile.model.StreetModel;
import com.buraksoft.halisaham_mobile.service.request.EventSearchRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LayoutMenuImagesBinding menuImagesBinding;
    private ProgressDialog progressDialog;
    private MainViewModel viewModel;
    private EventSearchRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        menuImagesBinding = binding.layoutMenuImages;
        setContentView(binding.getRoot());
        request = new EventSearchRequest();
        observeDatas();
        viewModel.getCities();
        intentActivies();
        binding.button.setOnClickListener(this::checkAuth);
    }

    public void checkAuth(View view){
        if (TokenContextHolder.getToken() == null){
            new AlertDialog.Builder(this)
                    .setMessage("Lütfen Giriş Yapınız")
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(MainActivity.this,AuthenticationActivity.class);
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

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityNames);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districtNames);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, streetNames);

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
                        Toast.makeText(getApplicationContext(), "MAHALLE BULUNAMADI", Toast.LENGTH_LONG).show();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, areaNames);

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
        viewModel.getCityData().observe(this,cityModelList -> {
            if (cityModelList != null){
                initSpinners();
            }
        });

        viewModel.getAreaData().observe(this,areaModelList -> {
            if (areaModelList != null){
                initAreaSpinner();
            }
        });

        viewModel.getLoading().observe(this,loading -> {
            if (loading){
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }else{
                if (progressDialog != null){
                    progressDialog.dismiss();
                }
            }
        });

        viewModel.getError().observe(this,error -> {

        });

        viewModel.getEventData().observe(this,eventModelList -> {
            if (eventModelList != null){

            }
        });
    }

    public void intentActivies(){
        menuImagesBinding.authImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menuImagesBinding.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}