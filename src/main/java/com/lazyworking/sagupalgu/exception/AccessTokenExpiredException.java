package com.lazyworking.sagupalgu.exception;

public class AccessTokenExpiredException extends RuntimeException{

    public AccessTokenExpiredException() {
        super("AccessToken is Expired");
    }
}
