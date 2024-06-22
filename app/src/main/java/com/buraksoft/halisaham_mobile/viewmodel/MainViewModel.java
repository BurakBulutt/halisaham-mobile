package com.buraksoft.halisaham_mobile.viewmodel;

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
import com.buraksoft.halisaham_mobile.service.request.EventSearchRequest;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final EventServiceAPI eventServiceAPI = new EventServiceAPI();
    private final CityServiceAPI cityServiceAPI = new CityServiceAPI();
    private final AreaServiceAPI areaService = new AreaServiceAPI();

    MutableLiveData<List<EventModel>> eventData = new MutableLiveData<>();
    MutableLiveData<List<CityModel>> cityData = new MutableLiveData<>();
    MutableLiveData<List<AreaModel>> areaData = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();
    MutableLiveData<Boolean> error = new MutableLiveData<>();

    public void getCities(){
        disposable.add(cityServiceAPI.getAllCities()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<DataResponse<CityModel>>>() {
                    @Override
                    public void onSuccess(Respond<DataResponse<CityModel>> dataResponseRespond) {
                        if (dataResponseRespond.getMeta().getCode() == 200){
                            cityData.setValue(dataResponseRespond.getData().getItems());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                    }
                }));
    }

    public void getEvents(EventSearchRequest request){
        loading.setValue(Boolean.TRUE);
        if (request.getAreaId() == null){
            disposable.add(eventServiceAPI.getByCityAndDistrictAndStreet(request)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Respond<DataResponse<EventModel>>>() {
                        @Override
                        public void onNext(Respond<DataResponse<EventModel>> dataResponseRespond) {
                            if (dataResponseRespond.getMeta().getCode() == 200){
                                loading.postValue(Boolean.FALSE);
                                eventData.setValue(dataResponseRespond.getData().getItems());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            loading.postValue(Boolean.FALSE);
                            error.postValue(Boolean.TRUE);
                        }

                        @Override
                        public void onComplete() {
                            loading.postValue(Boolean.FALSE);
                            error.postValue(Boolean.FALSE);
                        }
                    }));
        }else {
            disposable.add(eventServiceAPI.getByCityAndDistrictAndStreetAndArea(request)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Respond<DataResponse<EventModel>>>() {
                        @Override
                        public void onNext(Respond<DataResponse<EventModel>> dataResponseRespond) {
                            if (dataResponseRespond.getMeta().getCode() == 200){
                                loading.postValue(Boolean.FALSE);
                                eventData.setValue(dataResponseRespond.getData().getItems());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            loading.postValue(Boolean.FALSE);
                            error.postValue(Boolean.TRUE);
                        }

                        @Override
                        public void onComplete() {
                            loading.postValue(Boolean.FALSE);
                            error.postValue(Boolean.FALSE);
                        }
                    }));
        }
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

    public LiveData<List<EventModel>> getEventData(){
        return eventData;
    }

    public void clearEventData() {
        eventData.setValue(null);
    }

    public LiveData<List<AreaModel>> getAreaData(){
        return areaData;
    }

    public LiveData<List<CityModel>> getCityData(){
        return cityData;
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }

    public LiveData<Boolean> getError(){
        return error;
    }
}
