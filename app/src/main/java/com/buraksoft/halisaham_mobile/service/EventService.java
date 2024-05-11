package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {
    String AUTHORIZATION = "Authorization";

    @GET("user-events")
    Observable<Respond<DataResponse<EventModel>>> getUserEvents(@Header(AUTHORIZATION) String jwt);

    @GET("")
    Single<Respond<DataResponse<EventModel>>> getAll();

    @POST("save")
    Single<Respond<EventModel>> createEvent(@Body EventRequest request,@Header(AUTHORIZATION) String jwt);

    @POST
    Single<EventModel> joinEvent(@Query("eventId") String eventId,@Header(AUTHORIZATION) String jwt);

    @PUT
    Single<EventModel> deleteUserOnEvent(@Path("eventId") String eventId,@Query("userId") String userId,@Header(AUTHORIZATION) String jwt);

    @DELETE
    Single<Void> deleteEvent(@Path("id") String id,@Header(AUTHORIZATION) String jwt);
}
