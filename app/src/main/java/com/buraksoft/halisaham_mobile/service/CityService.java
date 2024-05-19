package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.CityModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CityService {
    String AUTHORIZATION = "Authorization";
    @GET("get-all")
    Single<Respond<DataResponse<CityModel>>> getAll();
}
