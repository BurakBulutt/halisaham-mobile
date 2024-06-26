package com.buraksoft.halisaham_mobile.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.buraksoft.halisaham_mobile.library.rest.DataResponse;
import com.buraksoft.halisaham_mobile.library.rest.Respond;
import com.buraksoft.halisaham_mobile.model.AreaModel;
import com.buraksoft.halisaham_mobile.model.ChatModel;
import com.buraksoft.halisaham_mobile.model.CityModel;
import com.buraksoft.halisaham_mobile.model.EventModel;
import com.buraksoft.halisaham_mobile.model.MessageModel;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;
import com.buraksoft.halisaham_mobile.service.AreaServiceAPI;
import com.buraksoft.halisaham_mobile.service.ChatServiceAPI;
import com.buraksoft.halisaham_mobile.service.CityServiceAPI;
import com.buraksoft.halisaham_mobile.service.EventServiceAPI;
import com.buraksoft.halisaham_mobile.service.MessageServiceAPI;
import com.buraksoft.halisaham_mobile.service.UserProfileServiceAPI;
import com.buraksoft.halisaham_mobile.service.request.EventRequest;
import com.buraksoft.halisaham_mobile.service.request.MessageRequest;
import com.buraksoft.halisaham_mobile.service.request.UserProfileBulkRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.google.gson.Gson;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class EventViewModel extends ViewModel {
    private final EventServiceAPI eventService = new EventServiceAPI();
    private final ChatServiceAPI chatService = new ChatServiceAPI();
    private final MessageServiceAPI messageService = new MessageServiceAPI();
    private final CityServiceAPI cityService = new CityServiceAPI();
    private final AreaServiceAPI areaService = new AreaServiceAPI();
    private final UserProfileServiceAPI userProfileServiceAPI = new UserProfileServiceAPI();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StompClient stompClient;
    private static final String WS_URL = "ws://10.0.2.2:8090/ws";

    MutableLiveData<List<UserProfileModel>> userProfileData = new MutableLiveData<>();
    MutableLiveData<Boolean> profileError = new MutableLiveData<>();
    MutableLiveData<List<EventModel>> eventData = new MutableLiveData<>();
    MutableLiveData<List<CityModel>> cityData = new MutableLiveData<>();
    MutableLiveData<List<AreaModel>> areaData = new MutableLiveData<>();
    MutableLiveData<EventModel> singleEventData = new MutableLiveData<>();
    MutableLiveData<Boolean> loading = new MutableLiveData<>();
    MutableLiveData<Boolean> error = new MutableLiveData<>();
    MutableLiveData<Boolean> authError = new MutableLiveData<>();
    MutableLiveData<Boolean> success = new MutableLiveData<>();
    MutableLiveData<Boolean> eventAuthorityView = new MutableLiveData<>();
    MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    MutableLiveData<String> removedUser = new MutableLiveData<>();
    MutableLiveData<List<MessageModel>> messages = new MutableLiveData<>();
    MutableLiveData<String> chatId = new MutableLiveData<>();

    public EventViewModel() {
        connectStomp();
    }

    private void connectStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL);
        disposable.add(stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d("Stomp", "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e("Stomp", "Error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d("Stomp", "Stomp connection closed");
                            break;
                    }
                }));
        stompClient.connect();
    }

    public void getUserEvents() {
        loading.setValue(Boolean.TRUE);

        disposable.add(
                eventService.getUserEvents()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Respond<DataResponse<EventModel>>>() {
                            @Override
                            public void onNext(Respond<DataResponse<EventModel>> eventModelRespond) {
                                if (eventModelRespond.getMeta().getCode() == 200) {
                                    eventData.setValue(eventModelRespond.getData().getItems());
                                    error.setValue(Boolean.FALSE); //TODO ERROR CASELERINDE META RESPONSELAR DINLENECEK! LIVEDATA
                                } else {
                                    error.setValue(Boolean.TRUE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (TokenContextHolder.getToken() == null) {
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

    public void getCities() {
        disposable.add(cityService.getAllCities()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<DataResponse<CityModel>>>() {
                    @Override
                    public void onSuccess(Respond<DataResponse<CityModel>> dataResponseRespond) {
                        if (dataResponseRespond.getMeta().getCode() == 200) {
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

    public void getAreaByDistrictAndStreet(String district, String street) {
        disposable.add(areaService.getByDistrictAndStreet(district, street)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<DataResponse<AreaModel>>>() {
                    @Override
                    public void onSuccess(Respond<DataResponse<AreaModel>> dataResponseRespond) {
                        if (dataResponseRespond.getMeta().getCode() == 200) {
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

    public void saveEvent(EventRequest request) {
        loading.setValue(Boolean.TRUE);
        error.postValue(Boolean.FALSE);
        disposable.add(eventService.createEvent(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<EventModel>>() {
                    @Override
                    public void onSuccess(Respond<EventModel> eventModelRespond) {
                        if (eventModelRespond.getMeta().getCode() == 200) {
                            singleEventData.setValue(eventModelRespond.getData());
                            loading.postValue(Boolean.TRUE);
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

    public void joinEvent(String eventId) {
        loading.setValue(Boolean.TRUE);
        error.postValue(Boolean.FALSE);
        disposable.add(eventService.joinEvent(eventId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<EventModel>>() {
                    @Override
                    public void onSuccess(Respond<EventModel> eventModelRespond) {
                        if (eventModelRespond.getMeta().getCode() == 200) {
                            loading.postValue(Boolean.TRUE);
                            singleEventData.postValue(eventModelRespond.getData());
                        }else if (eventModelRespond.getMeta().getCode() == 500){
                            error.postValue(Boolean.TRUE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.postValue(Boolean.FALSE);
                        error.postValue(Boolean.TRUE);
                    }
                }));
    }

    public void exitOnEvent(String id) {
        loading.postValue(Boolean.TRUE);
        error.postValue(Boolean.FALSE);
        disposable.add(eventService.exitEvent(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<Void>>() {
                    @Override
                    public void onSuccess(Respond<Void> voidRespond) {
                        loading.postValue(Boolean.FALSE);
                        if (voidRespond.getMeta().getCode() == 200) {
                            updateSuccess.postValue(Boolean.TRUE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.postValue(Boolean.FALSE);
                        updateSuccess.postValue(Boolean.FALSE);
                        error.postValue(Boolean.TRUE);
                    }
                }));
    }

    public void getUserProfileIdIn(List<String> userIds) {
        UserProfileBulkRequest request = new UserProfileBulkRequest(userIds);
        loading.postValue(Boolean.TRUE);
        profileError.postValue(Boolean.FALSE);
        disposable.add(userProfileServiceAPI.getProfilesBulk(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<DataResponse<UserProfileModel>>>() {
                    @Override
                    public void onSuccess(Respond<DataResponse<UserProfileModel>> dataResponseRespond) {
                        loading.postValue(Boolean.FALSE);
                        if (dataResponseRespond.getMeta().getCode() == 200) {
                            userProfileData.setValue(dataResponseRespond.getData().getItems());
                            profileError.postValue(Boolean.FALSE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.postValue(Boolean.FALSE);
                        profileError.postValue(Boolean.TRUE);
                    }
                }));
    }

    ;

    public void getEventAuthority(String id) {
        disposable.add(eventService.getEventAuthorityView(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<Boolean>>() {
                    @Override
                    public void onSuccess(Respond<Boolean> booleanRespond) {
                        if (booleanRespond.getMeta().getCode() == 200) {
                            error.postValue(Boolean.FALSE);
                            eventAuthorityView.postValue(booleanRespond.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                    }
                }));
    }

    public void updateEvent(EventRequest request, String id) {
        loading.setValue(Boolean.TRUE);
        disposable.add(eventService.updateEvent(request, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<EventModel>>() {
                    @Override
                    public void onSuccess(Respond<EventModel> eventModelRespond) {
                        loading.postValue(Boolean.FALSE);
                        singleEventData.setValue(eventModelRespond.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                        loading.postValue(Boolean.FALSE);
                    }
                }));
    }

    public void deleteUserOnEvent(String eventId, String userId) {
        disposable.add(eventService.deleteUserOnEvent(eventId, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<Void>>() {
                    @Override
                    public void onSuccess(Respond<Void> voidRespond) {
                        if (voidRespond.getMeta().getCode() == 200) {
                            updateSuccess.postValue(Boolean.TRUE);
                            removedUser.postValue(userId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateSuccess.postValue(Boolean.FALSE);
                    }
                }));
    }

    public void getEventChat(String eventId) {
        error.setValue(Boolean.FALSE);
        disposable.add(chatService.getByEventId(eventId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<ChatModel>>() {
                    @Override
                    public void onSuccess(Respond<ChatModel> chatModelRespond) {
                        if (chatModelRespond.getMeta().getCode() == 200) {
                            error.postValue(Boolean.FALSE);
                            chatId.postValue(chatModelRespond.getData().getId());
                            getMessagesChat(chatModelRespond.getData().getId());
                            subscribeToChat();
                        } else {
                            error.postValue(Boolean.TRUE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                    }
                }));
    }

    public void getMessagesChat(String chatId) {
        error.setValue(Boolean.FALSE);
        disposable.add(messageService.getByChatId(chatId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Respond<DataResponse<MessageModel>>>() {
                    @Override
                    public void onNext(Respond<DataResponse<MessageModel>> dataResponseRespond) {
                        if (dataResponseRespond.getMeta().getCode() == 200) {
                            messages.postValue(dataResponseRespond.getData().getItems());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(Boolean.TRUE);
                    }

                    @Override
                    public void onComplete() {
                        //TODO
                    }
                }));
    }

    public void sendMessage(MessageRequest request) {
        success.setValue(Boolean.FALSE);
        disposable.add(messageService.createMessage(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Respond<MessageModel>>() {
                    @Override
                    public void onSuccess(Respond<MessageModel> messageModelRespond) {
                        Log.e("BASARILI", "mesaj gitti");
                        //    stompClient.send("/app/sendMessage", toJson(request)).subscribe();
                        success.postValue(Boolean.TRUE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("BASARISIZ", "mesaj gitmedi");
                    }
                }));
    }

    private void subscribeToChat() {
        disposable.add(stompClient.topic("/topic/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    MessageModel newMessage = fromJson(topicMessage.getPayload(), MessageModel.class);
                    List<MessageModel> currentMessages = messages.getValue();
                    if (currentMessages != null) {
                        currentMessages.add(newMessage);
                        messages.postValue(currentMessages);
                    }
                }, throwable -> Log.e("Stomp", "Error on subscribe topic", throwable)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }

    private String toJson(Object object) {
        return new Gson().toJson(object);
    }

    private <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public void setSingleDataDefault(){
        singleEventData.postValue(null);
    }

    public LiveData<String> getChatId() {
        return chatId;
    }

    public LiveData<List<MessageModel>> getMessages() {
        return messages;
    }

    public LiveData<String> getRemovedUser() {
        return removedUser;
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public LiveData<Boolean> getEventAuthortityView() {
        return eventAuthorityView;
    }

    public LiveData<Boolean> getProfileError() {
        return profileError;
    }

    public LiveData<List<UserProfileModel>> getUserProfileData() {
        return userProfileData;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }

    public LiveData<List<EventModel>> getEventData() {
        return eventData;
    }

    public LiveData<EventModel> getSingleEventData() {
        return singleEventData;
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
