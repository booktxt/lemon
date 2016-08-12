package com.lemon.enums;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public enum LemonType {
    DREAM("梦想"),
    TRIFLES("琐事"),
    LYRICISM("抒情");

    private String value;

    LemonType(){}

    LemonType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}