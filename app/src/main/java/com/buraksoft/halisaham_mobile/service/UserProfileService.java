package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;
import com.buraksoft.halisaham_mobile.service.request.UserProfileRequest;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserProfileService {
    @GET("find-user")
    Single<Respond<UserProfileModel>> getUserProfile(@Query("token") String token);

    @PUT("{id}")
    Single<Respond<UserProfileModel>> updateProfile(@Path("id") String id, @Body UserProfileRequest request); //TODO request düzenlenecek

}
