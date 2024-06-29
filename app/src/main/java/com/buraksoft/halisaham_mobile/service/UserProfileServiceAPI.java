package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;
import com.buraksoft.halisaham_mobile.service.request.UserProfileBulkRequest;
import com.buraksoft.halisaham_mobile.service.request.UserProfileRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileServiceAPI {
    private static final String BASE_URL = "http://192.168.1.36:8090/user-profiles/";

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
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.getUserProfile(token,jwt);
    }

    public Single<Respond<UserProfileModel>> updateUserProfile(String id, UserProfileRequest request){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.updateProfile(id,request,jwt);
    }

    public Single<Respond<DataResponse<UserProfileModel>>> getProfilesBulk(UserProfileBulkRequest request){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.getBulkProfiles(request,jwt);
    }

}
