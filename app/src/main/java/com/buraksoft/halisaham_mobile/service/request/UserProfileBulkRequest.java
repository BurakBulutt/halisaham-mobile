package com.buraksoft.halisaham_mobile.service.request;


import java.util.List;

public class UserProfileBulkRequest {
    private final List<String> userIds;

    public UserProfileBulkRequest(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
