package com.lemon.enums;

import com.lemon.framework.enumwrapper.Options;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public enum ContentType implements Options{
    DREAM("想做的事"),
    TRIFLES("琐碎的事"),
    LYRICISM("抒情的话");

    private String remark;

    ContentType(){}

    ContentType(String remark){
        this.remark = remark;
    }

    @Override
    public String getName() {
        return remark;
    }

    public String getValue() {
        return this.name();
    }

    public String getRemark() {
        return remark;
    }
}
