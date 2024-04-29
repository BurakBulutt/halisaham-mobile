package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventServiceAPI {
    private static final String BASE_URL = "http://10.0.2.2:8090/events/";

    private EventService api;

    public EventServiceAPI() {
        initService();
    }

    private void initService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(EventService.class);
    }


    public Observable<Respond<DataResponse<EventModel>>> getUserEvents(){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.getUserEvents(jwt);
    }

    public Single<Respond<DataResponse<EventModel>>> getAll(){
        return api.getAll();
    }

    public Single<EventModel> createEvent(EventRequest request){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.createEvent(request,jwt);
    }

    public  Single<EventModel> joinEvent(String eventId){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.joinEvent(eventId,jwt);
    }

    public Single<EventModel> deleteUserOnEvent(String eventId, String userId){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.deleteUserOnEvent(eventId,userId,jwt);
    }

    public Single<Void> deleteEvent(String id){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return api.deleteEvent(id,jwt);
    }


}
