package com.buraksoft.halisaham_mobile.library.rest;

public class Respond<T> {
    private MetaResponse meta;
    private T data;

    public Respond() {
    }

    public Respond(MetaResponse meta, T data) {
        this.meta = meta;
        this.data = data;
    }

    public Respond(T data) {
        this.data = data;
        this.meta = MetaResponse.success();
    }

    public Respond(MetaResponse meta){
        this.meta = meta;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public T getData() {
        return data;
    }
}
