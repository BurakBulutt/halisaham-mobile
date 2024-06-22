package com.buraksoft.halisaham_mobile.utils;

public final class TokenContextHolder {
    private static ThreadLocal<String> token = new InheritableThreadLocal<>();
    private static ThreadLocal<String> userMail = new InheritableThreadLocal<>();

    public static void setToken(String token){
        TokenContextHolder.token.set(token);
    }

    public static String getToken(){
        return token.get();
    }

    public static void setUserMail(String userMail){
        TokenContextHolder.userMail.set(userMail);
    }

    public static String getUserMail(){
        return userMail.get();
    }

    public static void clear(){
        token.remove();
        userMail.remove();
    }
}
