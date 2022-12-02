package com.lazyworking.sagupalgu.user.exception;

public class AccessTokenExpiredException extends RuntimeException{
    private String refreshToken;
    public AccessTokenExpiredException(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
