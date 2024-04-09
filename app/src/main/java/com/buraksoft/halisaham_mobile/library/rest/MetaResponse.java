package com.buraksoft.halisaham_mobile.library.rest;

import com.buraksoft.halisaham_mobile.library.enums.MessageCodes;

public class MetaResponse {
    private int code;
    private String message;

    public MetaResponse() {
    }

    public MetaResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MetaResponse of(int code, String message){
        return new MetaResponse(code,message);
    }

    public static MetaResponse success(){
        return new MetaResponse(MessageCodes.SUCCESS.getCode(),"Success");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
