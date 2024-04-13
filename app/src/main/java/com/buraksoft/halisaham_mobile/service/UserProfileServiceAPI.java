package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileServiceAPI {
    private static final String BASE_URL = "http://10.0.2.2:8090/user-profiles/";

    private UserProfileService api;

    public UserProfileServiceAPI() {
        initApi();
    }

    private void initApi() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UserProfileService.class);
    }

    public Single<Respond<UserProfileModel>> getUserProfile(String token){
        return api.getUserProfile(token);
    }

    //TODO PROFILE DUZENLEME ISTEGI
}
