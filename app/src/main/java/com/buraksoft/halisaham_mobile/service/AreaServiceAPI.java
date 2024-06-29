package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AreaServiceAPI {
    private static final String BASE_URL = "http://192.168.1.36:8090/areas/";
    private AreaService api;

    public AreaServiceAPI() {
        initApi();
    }

    public void initApi(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AreaService.class);
    }

    public Single<Respond<DataResponse<AreaModel>>> getByDistrictAndStreet(String district, String street){
        return api.findByDistrictAndStreet(district,street);
    }
}
