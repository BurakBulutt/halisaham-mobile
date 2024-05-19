package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.AreaModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface AreaService {
    @GET("find-district-and-street")
    Single<Respond<DataResponse<AreaModel>>> findByDistrictAndStreet(@Query("districtId") String districtId, @Query("streetId") String streetId);
}
