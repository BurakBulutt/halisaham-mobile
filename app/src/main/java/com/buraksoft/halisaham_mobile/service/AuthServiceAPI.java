package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.TokenModel;
import com.buraksoft.halisaham_mobile.service.request.LoginRequest;
import com.buraksoft.halisaham_mobile.service.request.RegisterRequest;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthServiceAPI {
    private static final String BASE_URL = "http://192.168.1.39:8090/auth/";

    private AuthService api;

    public AuthServiceAPI() {
        initRetrofit();
    }

    private void initRetrofit() {
    /*    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder()
                                .header("Authorization", "Your Token"); // Buraya tokeninizi ekleyin

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
     */
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AuthService.class);
    }


    public Single<Respond<TokenModel>> register(RegisterRequest request){
        return api.register(request);
    }

    public Single<Respond<TokenModel>> login(LoginRequest request){
        return api.login(request);
    }
}
