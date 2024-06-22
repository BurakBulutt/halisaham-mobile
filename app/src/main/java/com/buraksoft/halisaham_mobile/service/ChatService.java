package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.ChatModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ChatService {
    String AUTHORIZATION = "Authorization";

    @GET("find-by-event/{id}")
    Single<Respond<ChatModel>> getChatByEventId(@Path("id") String eventId,@Header(AUTHORIZATION) String jwt);

}
