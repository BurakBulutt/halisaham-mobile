package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.TokenModel;
import com.buraksoft.halisaham_mobile.service.request.LoginRequest;
import com.buraksoft.halisaham_mobile.service.request.RegisterRequest;


import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("register")
    Single<Respond<TokenModel>> register(@Body RegisterRequest request);

    @POST("login")
    Single<Respond<TokenModel>> login(@Body LoginRequest request);

}
