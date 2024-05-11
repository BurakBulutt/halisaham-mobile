package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityServiceAPI {
    private static final String BASE_URL = "http://10.0.2.2:8090/cities/";

    private CityService api;

    public CityServiceAPI() {
        initApi();
    }

    private void initApi() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CityService.class);
    }

    public Single<Respond<DataResponse<CityModel>>> getAllCities(){
        String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.getAll(jwt);
    }
}
