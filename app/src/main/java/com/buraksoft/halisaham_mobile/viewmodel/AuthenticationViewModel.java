package com.buraksoft.halisaham_mobile.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.TokenModel;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;
import com.buraksoft.halisaham_mobile.service.AuthServiceAPI;
import com.buraksoft.halisaham_mobile.service.UserProfileServiceAPI;
import com.buraksoft.halisaham_mobile.service.request.LoginRequest;
import com.buraksoft.halisaham_mobile.service.request.RegisterRequest;
import com.buraksoft.halisaham_mobile.service.request.UserProfileRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AuthenticationViewModel extends ViewModel {
    private final AuthServiceAPI authServiceAPI = new AuthServiceAPI();
    private final UserProfileServiceAPI userProfileServiceAPI = new UserProfileServiceAPI();
    private final CompositeDisposable disposable = new CompositeDisposable();

    MutableLiveData<TokenModel> tokenData = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();
    MutableLiveData<Boolean> error = new MutableLiveData<>();
    MutableLiveData<UserProfileModel> userProfile = new MutableLiveData<>();


    public AuthenticationViewModel() {
    }

    public void register(RegisterRequest request) {
        loading.setValue(Boolean.TRUE);
        disposable.add(
                authServiceAPI.register(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Respond<TokenModel>>() {
                            @Override
                            public void onSuccess(Respond<TokenModel> tokenModelRespond) {
                                tokenData.postValue(tokenModelRespond.getData());
                                loading.postValue(Boolean.FALSE);
                                error.postValue(Boolean.FALSE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                error.postValue(Boolean.TRUE);
                                loading.postValue(Boolean.FALSE);
                            }
                        })
        );
    }

    public void login(LoginRequest request) {
        loading.setValue(Boolean.TRUE);
        disposable.add(
                authServiceAPI.login(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Respond<TokenModel>>() {
                            @Override
                            public void onSuccess(Respond<TokenModel> tokenModelRespond) {
                                if (tokenModelRespond.getMeta().getCode() == 200) {
                                    tokenData.postValue(tokenModelRespond.getData());
                                    TokenContextHolder.setToken(tokenModelRespond.getData().getToken());
                                    error.postValue(Boolean.FALSE);
                                    loading.postValue(Boolean.FALSE);
                                } else {
                                    loading.postValue(Boolean.FALSE);
                                    error.postValue(Boolean.TRUE);
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                loading.postValue(Boolean.FALSE);
                                error.postValue(Boolean.TRUE);
                            }
                        })
        );
    }

    public void setUserProfile(String token) {
        loading.setValue(Boolean.TRUE);
        disposable.add(
                userProfileServiceAPI.getUserProfile(token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Respond<UserProfileModel>>() {
                            @Override
                            public void onSuccess(Respond<UserProfileModel> userProfileModelRespond) {
                                if (userProfileModelRespond.getMeta().getCode() == 200) {
                                    userProfile.postValue(userProfileModelRespond.getData());
                                    loading.postValue(Boolean.FALSE);
                                    error.postValue(Boolean.FALSE);
                                } else {
                                    loading.postValue(Boolean.FALSE);
                                    error.postValue(Boolean.TRUE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                loading.postValue(Boolean.FALSE); //TODO Bütün onError() caseleri düzenlenecek.

                                if (e instanceof HttpException){
                                    HttpException httpException = (HttpException) e;
                                    int statusCode = httpException.code();

                                    if (statusCode == 403){
                                        error.postValue(Boolean.TRUE);
                                    }
                                }
                            }
                        })
        );
    }

    public void updateProfilePhoto(String id,UserProfileRequest request){
        loading.setValue(Boolean.TRUE);
        disposable.add(
                userProfileServiceAPI.updateUserProfile(id,request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Respond<UserProfileModel>>() {
                            @Override
                            public void onSuccess(Respond<UserProfileModel> userProfileModelRespond) {
                                if (userProfileModelRespond.getMeta().getCode() == 200){
                                    loading.postValue(Boolean.FALSE);
                                    error.postValue(Boolean.FALSE);
                                }else {
                                    loading.postValue(Boolean.FALSE);
                                    error.postValue(Boolean.TRUE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                loading.postValue(Boolean.FALSE);
                                error.postValue(Boolean.TRUE);
                            }
                        })
        );
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public LiveData<TokenModel> getTokenData() {
        return tokenData;
    }

    public LiveData<UserProfileModel> getUserProfile() {
        return userProfile;
    }
}

