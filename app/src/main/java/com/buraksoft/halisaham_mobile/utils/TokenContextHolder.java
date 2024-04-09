package com.buraksoft.halisaham_mobile.utils;

public final class TokenContextHolder {
    private static ThreadLocal<String> token = new InheritableThreadLocal<>();

    public static void setToken(String token){
        TokenContextHolder.token.set(token);
    }

    public static String getToken(){
        return token.get();
    }

    public static void clear(){
        token.remove();
    }
}
