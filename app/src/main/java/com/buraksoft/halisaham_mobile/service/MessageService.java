package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.MessageModel;
import com.buraksoft.halisaham_mobile.service.request.MessageRequest;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    String AUTHORIZATION = "Authorization";

    @GET("find-by-chat-id/{chatId}")
    Observable<Respond<DataResponse<MessageModel>>> getMessagesByChatId(@Path("chatId") String chatId,@Header(AUTHORIZATION) String jwt);

    @POST("save")
    Single<Respond<MessageModel>> createMessage(@Body MessageRequest request,@Header(AUTHORIZATION) String jwt);
}
