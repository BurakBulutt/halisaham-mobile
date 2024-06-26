package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.ChatModel;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatServiceAPI {
    private static final String BASE_URL = "http://192.168.18.1:8090/chats/";
    private ChatService service;

    public ChatServiceAPI() {
        initApi();
    }

    public void initApi(){
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ChatService.class);
    }

    public Single<Respond<ChatModel>> getByEventId(String eventId){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return service.getChatByEventId(eventId,jwt);
    }


}
