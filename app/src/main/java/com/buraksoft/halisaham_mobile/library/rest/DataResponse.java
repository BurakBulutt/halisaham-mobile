package com.buraksoft.halisaham_mobile.library.rest;

import java.util.ArrayList;
import java.util.List;

public class DataResponse<T> {
    private List<T> items = new ArrayList<>();

    public DataResponse() {
    }

    public DataResponse(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }
}
