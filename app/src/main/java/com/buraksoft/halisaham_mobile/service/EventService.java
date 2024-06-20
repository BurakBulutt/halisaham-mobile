package com.buraksoft.halisaham_mobile.service;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;
import com.buraksoft.halisaham_mobile.service.request.EventSearchRequest;

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

    @GET("find-all-param")
    Observable<Respond<DataResponse<EventModel>>> getByCityAndDistrictAndStreetAndArea(@Query("cityId") String cityId,@Query("districtId") String districtId,@Query("streetId") String streetId,@Query("areaId") String areaId, @Header(AUTHORIZATION) String jwt);

    @GET("find-without-area")
    Observable<Respond<DataResponse<EventModel>>> getByCityAndDistrictAndStreet(@Query("cityId") String cityId,@Query("districtId") String districtId,@Query("streetId") String streetId, @Header(AUTHORIZATION) String jwt);

    @GET("")
    Single<Respond<DataResponse<EventModel>>> getAll();

    @POST("save")
    Single<Respond<EventModel>> createEvent(@Body EventRequest request, @Header(AUTHORIZATION) String jwt);

    @PUT("update/{id}")
    Single<Respond<EventModel>> updateEvent(@Body EventRequest request,@Path("id") String id,@Header(AUTHORIZATION) String jwt);

    @POST("join-event")
    Single<Respond<EventModel>> joinEvent(@Query("eventId") String eventId, @Header(AUTHORIZATION) String jwt);

    @PUT("delete-user-event/{eventId}")
    Single<Respond<Void>> deleteUserOnEvent(@Path("eventId") String eventId, @Query("userId") String userId, @Header(AUTHORIZATION) String jwt);

    @DELETE("exit-event/{id}")
    Single<Respond<Void>> exitEvent(@Path("id") String id, @Header(AUTHORIZATION) String jwt);

    @GET("check-event-authority/{id}")
    Single<Respond<Boolean>> getEventAuthorityView(@Path("id") String id,@Header(AUTHORIZATION) String jwt);
}
