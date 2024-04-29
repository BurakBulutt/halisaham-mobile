package com.buraksoft.halisaham_mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.EventServiceAPI;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EventViewModel extends ViewModel {
    private final EventServiceAPI eventService = new EventServiceAPI();
    private final CompositeDisposable disposable = new CompositeDisposable();

    MutableLiveData<List<EventModel>> eventData = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();
    MutableLiveData<Boolean> error = new MutableLiveData<>();

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
                                    error.setValue(Boolean.FALSE);
                                }else {
                                    error.setValue(Boolean.TRUE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                error.postValue(Boolean.TRUE);
                            }

                            @Override
                            public void onComplete() {
                                loading.postValue(Boolean.FALSE);
                                error.postValue(Boolean.TRUE);
                            }
                        })
        );

    }

    public LiveData<List<EventModel>> getEventData() {
        return eventData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

}
