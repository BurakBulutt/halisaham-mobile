package com.buraksoft.halisaham_mobile.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.AreaServiceAPI;
import com.buraksoft.halisaham_mobile.service.CityServiceAPI;
import com.buraksoft.halisaham_mobile.service.EventServiceAPI;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EventViewModel extends ViewModel {
    private final EventServiceAPI eventService = new EventServiceAPI();
    private final CityServiceAPI cityService = new CityServiceAPI();
    private final AreaServiceAPI areaService = new AreaServiceAPI();
    private final CompositeDisposable disposable = new CompositeDisposable();

    MutableLiveData<List<EventModel>> eventData = new MutableLiveData<>();
    MutableLiveData<List<CityModel>> cityData = new MutableLiveData<>();
    MutableLiveData<List<AreaModel>> areaData = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();
    MutableLiveData<Boolean> error = new MutableLiveData<>();
    MutableLiveData<Boolean> authError = new MutableLiveData<>();

    public void getUserEvents(){
        loading.setValue(Boolean.TRUE);

        disposable.add(
                eventService.getUserEvents()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Respond<DataResponse<EventModel>>>() {
                            @Override
                            public void onNext(Respond<DataResponse<EventModel>> eventModelRespond) {
                                if (eventModelRespond.getMeta().getCode() == 200){
                                    eventData.setValue(eventModelRespond.getData().getItems());
                                    error.setValue(Boolean.FALSE); //TODO ERROR CASELERINDE META RESPONSELAR DINLENECEK! LIVEDATA
                                }else {
                                    error.setValue(Boolean.TRUE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (TokenContextHolder.getToken() == null){
                                    authError.setValue(Boolean.TRUE);
                                    loading.postValue(Boolean.FALSE);
                                    return;
                                }
                                error.postValue(Boolean.TRUE);
                            }

                            @Override
                            public void onComplete() {
                                loading.postValue(Boolean.FALSE);
                                error.postValue(Boolean.FALSE);
                            }
                        })
        );
    }

    public void getCities(){
        disposable.add(cityService.getAllCities()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<DataResponse<CityModel>>>() {
                    @Override
                    public void onSuccess(Respond<DataResponse<CityModel>> dataResponseRespond) {
                        if (dataResponseRespond.getMeta().getCode() == 200){
                            error.postValue(Boolean.FALSE);
                            cityData.setValue(dataResponseRespond.getData().getItems());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                    }
                }));
    }

    public void getAreaByDistrictAndStreet(String district,String street){
        disposable.add(areaService.getByDistrictAndStreet(district,street)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<DataResponse<AreaModel>>>() {
                    @Override
                    public void onSuccess(Respond<DataResponse<AreaModel>> dataResponseRespond) {
                        if (dataResponseRespond.getMeta().getCode() == 200){
                            areaData.postValue(dataResponseRespond.getData().getItems());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                    }
                })

        );
    }

    public void saveEvent(EventRequest request){
        loading.setValue(Boolean.TRUE);
        disposable.add(eventService.createEvent(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<EventModel>>() {
                    @Override
                    public void onSuccess(Respond<EventModel> eventModelRespond) {
                        if (eventModelRespond.getMeta().getCode() == 200){
                            loading.postValue(Boolean.FALSE);
                            error.postValue(Boolean.FALSE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.postValue(Boolean.FALSE);
                        error.postValue(Boolean.TRUE);
                    }


                }));
    }

    public LiveData<List<EventModel>> getEventData() {
        return eventData;
    }

    public LiveData<List<AreaModel>> getAreaData() {
        return areaData;
    }

    public LiveData<List<CityModel>> getCityData() {
        return cityData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public LiveData<Boolean> getAuthError() {
        return authError;
    }

}
