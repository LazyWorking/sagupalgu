package com.lazyworking.sagupalgu.user.domain;

public enum Gender {
    M("M","남성"),
    F("F","여성"),
    E("E","기타");

    private String code;
    private String value;

    Gender(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
