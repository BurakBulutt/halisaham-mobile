package com.buraksoft.halisaham_mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.TokenModel;
import com.buraksoft.halisaham_mobile.service.AuthServiceAPI;
import com.buraksoft.halisaham_mobile.service.request.LoginRequest;
import com.buraksoft.halisaham_mobile.service.request.RegisterRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AuthenticationViewModel extends ViewModel {
    private AuthServiceAPI authServiceAPI = new AuthServiceAPI();
    private CompositeDisposable disposable = new CompositeDisposable();

    MutableLiveData<TokenModel> tokenData = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();
    MutableLiveData<Boolean> error = new MutableLiveData<>();


    public AuthenticationViewModel() {
    }


    public void register(RegisterRequest request){
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
                            }

                            @Override
                            public void onError(Throwable e) {
                                error.postValue(Boolean.TRUE);
                                loading.postValue(Boolean.FALSE);
                            }
                        })
        );
    }

    public void login(LoginRequest request){
        loading.setValue(Boolean.TRUE);
        disposable.add(
                authServiceAPI.login(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Respond<TokenModel>>() {
                            @Override
                            public void onSuccess(Respond<TokenModel> tokenModelRespond) {
                                if (tokenModelRespond.getMeta().getCode() == 200){
                                    tokenData.postValue(tokenModelRespond.getData());
                                    error.postValue(Boolean.FALSE);
                                    loading.postValue(Boolean.FALSE);
                                }else{
                                    loading.postValue(Boolean.FALSE);
                                    error.postValue(Boolean.TRUE);
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                error.postValue(Boolean.TRUE);
                                loading.postValue(Boolean.FALSE);
                            }
                        })
        );
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }
    public LiveData<Boolean> getError(){
        return error;
    }
    public LiveData<TokenModel> getTokenData(){
        return tokenData;
    }


}

