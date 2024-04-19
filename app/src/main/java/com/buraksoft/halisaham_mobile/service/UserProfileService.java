package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;
import com.buraksoft.halisaham_mobile.service.request.UserProfileRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserProfileService {
    String AUTHORIZATION = "Authorization";

    @GET("find-user")
    Single<Respond<UserProfileModel>> getUserProfile(@Query("token") String token,@Header(AUTHORIZATION) String jwt);

    @PUT("{id}")
    Single<Respond<UserProfileModel>> updateProfile(@Path("id") String id, @Body UserProfileRequest request,@Header(AUTHORIZATION) String jwt);

}
