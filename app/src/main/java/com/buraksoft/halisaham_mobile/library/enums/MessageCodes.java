package com.buraksoft.halisaham_mobile.library.enums;

public enum MessageCodes {
    SUCCESS(200,"general.success"),

    FAIL(500,"general.fail"),

    BAD_REQUEST(400,"general.badRequest"),

    NOT_FOUND(404,"general.notFound"),

    UNAUTHORIZED(401,"general.unauthorized"),

    ENTITY_NOT_FOUND(404,"general.entityNotFound");


    private String message;

    private int code;

    MessageCodes(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
