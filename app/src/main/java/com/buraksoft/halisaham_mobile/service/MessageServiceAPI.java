package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.MessageModel;
import com.buraksoft.halisaham_mobile.service.request.MessageRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageServiceAPI {
    private static final String BASE_URL = "http://192.168.1.39:8090/messages/";
    private MessageService service;

    public MessageServiceAPI() {
        initApi();
    }

    public void initApi(){
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MessageService.class);
    }

    public Observable<Respond<DataResponse<MessageModel>>> getByChatId(String chatId){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return service.getMessagesByChatId(chatId,jwt);
    }

    public Single<Respond<MessageModel>> createMessage(MessageRequest request){
        final String jwt = "Bearer " + TokenContextHolder.getToken();
        return service.createMessage(request,jwt);
    }
}
